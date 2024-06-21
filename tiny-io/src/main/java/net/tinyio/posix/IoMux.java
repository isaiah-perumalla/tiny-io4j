package net.tinyio.posix;

public interface IoMux {
    void addInterest(long serverSocketHandle, Op op);

    int poll(int timeoutMillis);

    void deRegister(long ioHandle);

    enum Op {
        Read,Write
    }
    long registerFd(int fd, Handler ioHandler);

    interface Handler {
        void onRead();
        void onWrite();
    }
}
