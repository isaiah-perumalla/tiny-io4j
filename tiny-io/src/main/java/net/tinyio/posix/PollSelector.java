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
    private final Int2IntHashMap fdToIndexMap;
    private final IntEntryPool intEntryPool = new IntEntryPool();
    private MemorySegment pollFds;
    private Arena arena;

    public PollSelector(int maxFds) {
        this.maxFds = maxFds;
        this.fdToIndexMap = new Int2IntHashMap(maxFds, 0.75f, -1);
    }

    public void open() {
        arena = Arena.ofConfined();
        pollFds = arena.allocate(pollfd.$LAYOUT().byteSize() * maxFds);
    }

    public void add(int fd, IoMux.Op op) {
        final int entry = intEntryPool.next();
        fdToIndexMap.put(fd, entry);
        if (op == IoMux.Op.Read) {
            setPollFd(entry, fd, (short) READ);
        }
        else if (op == IoMux.Op.Write) {
            setPollFd(entry, fd, (short) WRITE);
        }
    }

    public void remove(int fd, IoMux.Op op) {

    }
    private void setPollFd(int index, int fd, short events) {
        final long offset = pollfd.sizeof() * index;
        pollFds.set(ValueLayout.JAVA_INT, offset, fd);
        pollFds.set(ValueLayout.JAVA_SHORT, offset + ValueLayout.JAVA_INT.byteSize(), events);
    }

    private static class IntEntryPool {
        private final IntArrayList freeList = new IntArrayList( 64, -1);
        private int next = 0;

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
