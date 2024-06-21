package net.tinyio.posix;
import net.tinyio.nativesockets.net_h;
import net.tinyio.nativesockets.pollfd;
import org.agrona.collections.Int2IntHashMap;
import org.agrona.collections.IntArrayList;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class PollSelector {
    public static final int READ = net_h.POLLIN() | net_h.POLLPRI();
    public static final int WRITE = net_h.POLLOUT();
    private final int maxFds;
    private final IoReadyNotifier selectorHandler;
    private final Int2IntHashMap fdToIndexMap;
    private final IntEntryPool intEntryPool = new IntEntryPool();
    private MemorySegment pollFds;
    private Arena arena;

    public PollSelector(int maxFds, IoReadyNotifier selectorHandler) {
        this.maxFds = maxFds;
        this.fdToIndexMap = new Int2IntHashMap(maxFds, 0.75f, -1);
        this.selectorHandler = selectorHandler;
    }

    public void open() {
        arena = Arena.ofConfined();
        pollFds = arena.allocate(pollfd.$LAYOUT().byteSize() * maxFds);
    }

    public void add(int fd, IoMux.Op op) {
        final short newEvents = toFlags(op);
        int entry = fdToIndexMap.get(fd);
        if (entry == -1) {
            entry = intEntryPool.next();
            fdToIndexMap.put(fd, entry);
        }
        final short currentEvents = getPollEventsAt(entry);
        setPollFd(entry, fd, (short) (currentEvents | newEvents));
    }

    private short toFlags(IoMux.Op op) {
        switch (op) {
            case Read -> {
                return (short) READ;
            }
            case Write -> {
                return (short) WRITE;
            }
        }
        return 0;
    }

    public void remove(int fd, IoMux.Op op) {
        final int entry = fdToIndexMap.get(fd);
        if (entry != -1) {
            final short removeEvents = toFlags(op);
            final short currentEvents = getPollEventsAt(entry);
            final short events = (short) (currentEvents & ~removeEvents);
            if (events != 0) {
                setPollFd(entry, fd, events);
            }
            else {
                removeFd(fd, entry);
            }
        }
    }

    public boolean remove(int fd) {
        final int entry = fdToIndexMap.get(fd);
        if (entry != -1) {
            removeFd(fd, entry);
            return true;
        }
        else {
            return false;
        }
    }
    private void removeFd(int fd, int entry) {
        fdToIndexMap.remove(fd);
        intEntryPool.free(entry);
        setPollFd(entry, -1, (short) 0);
    }

    private short getPollEventsAt(int index) {
        final long offset = pollfd.sizeof() * index;
        return pollFds.get(ValueLayout.JAVA_SHORT, offset + ValueLayout.JAVA_INT.byteSize());
    }
    private void setPollFd(int index, int fd, short events) {
        final long offset = pollfd.sizeof() * index;
        pollFds.set(ValueLayout.JAVA_INT, offset, fd);
        pollFds.set(ValueLayout.JAVA_SHORT, offset + ValueLayout.JAVA_INT.byteSize(), events);
    }

    public int poll(int timeout) {
        int maxEntry = intEntryPool.maxEntry();
        int poll = net_h.poll(pollFds, maxEntry, timeout);
        if (poll > 0) {
            for (int entry = 0; entry < maxEntry; entry++) {
                final int f = pollFdAt(entry, pollFds);
                if (f == 0) continue;
                final short revents = pollREventsAt(entry, pollFds);
                if (0 != (revents & READ)) {
                    selectorHandler.onRead(f);
                }
                if (0 != (revents & WRITE)) {
                    selectorHandler.onWrite(f);
                }
            }
        }
        return poll;
    }

    private static short pollREventsAt(int index, MemorySegment pollFds) {
        final long offset = pollfd.sizeof() * index;
        final long fieldOffset = ValueLayout.JAVA_INT.byteSize() + ValueLayout.JAVA_SHORT.byteSize() + offset;
        return pollFds.get(ValueLayout.JAVA_SHORT,  fieldOffset);
    }
    private static int pollFdAt(int index, MemorySegment pollFds) {
        final long offset = pollfd.sizeof() * index;
        return pollFds.get(ValueLayout.JAVA_INT, offset);
    }
    private static class IntEntryPool {
        private final IntArrayList freeList = new IntArrayList( 64, -1);
        private int next = 0;
        int maxEntry() {
            return next;
        }
        int next() {
            if (freeList.size() == 0) {
                final int entry = next;
                next++;
                return entry;
            }
            else {
                final int index = freeList.size() - 1;
                final int entry = freeList.removeAt(index);
                return entry;
            }
        }

        void free(int entry) {
            assert entry >=0 && entry < next;
            freeList.addInt(entry);
        }
    }
}
