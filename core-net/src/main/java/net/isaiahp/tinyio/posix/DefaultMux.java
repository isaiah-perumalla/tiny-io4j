package net.isaiahp.tinyio.posix;

import org.agrona.collections.Long2LongHashMap;
import org.agrona.collections.Long2ObjectHashMap;

public class DefaultMux implements IoMux {
    private final PollSelector pollSelector = new PollSelector(1024, new ReadyListener());
    //handles -> IoHandler
    private final Long2ObjectHashMap<Handler> handlers = new Long2ObjectHashMap<>(128, 0.75f);
    private final Long2LongHashMap handleToFds = new Long2LongHashMap(128, 0.75f, -1);
    private final Long2LongHashMap fdToHandle = new Long2LongHashMap(128, 0.75f, -1);
    private long nextHandle = 0;
    @Override
    public void addInterest(long handle, Op op) {
        assert handle != -1 : "invalid handle";
        int fd = (int) handleToFds.get(handle);
        if (fd == -1) {
            throw new IllegalStateException("invalid handle");
        }
        pollSelector.add(fd, op);
    }

    @Override
    public int poll(int timeoutMillis) {
        return pollSelector.poll(timeoutMillis);
    }

    @Override
    public void deRegister(long ioHandle) {
        Handler handler = handlers.remove(ioHandle);

        int fd = (int) handleToFds.remove(ioHandle);
        assert ioHandle == fdToHandle.remove(fd);
        pollSelector.remove(fd);

    }

    @Override
    public long registerFd(int fd, Handler ioHandler) {
        long handle = nextHandle++;
        handlers.put(handle, ioHandler);
        handleToFds.put(handle, fd);
        fdToHandle.put(fd, handle);
        assert fdToHandle.size() == handleToFds.size();
        return handle;
    }

    public void open() {
        pollSelector.open();
    }

    private class ReadyListener implements IoReadyNotifier {
        @Override
        public void onRead(int fd) {
            Handler ioHandler = getHandler(fd);
            ioHandler.onRead();
        }

        @Override
        public void onWrite(int fd) {
            Handler ioHandler = getHandler(fd);
            if (ioHandler != null) {
                ioHandler.onWrite();
            }
        }
    }

    private Handler getHandler(int fd) {
        long handle = fdToHandle.get(fd);
        if (handle == -1) {
            return null;
        }
        Handler ioHandler = handlers.get(handle);
        assert ioHandler != null;
        return ioHandler;
    }
}
