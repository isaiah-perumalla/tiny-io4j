package net.tinyio;

public interface IoMux {
    void addInterest(long serverSocketHandle, Op op);

    enum Op {
        Read,Write
    }
    long registerFd(int fd, Handler ioHandler);

    interface Handler {
        void onRead();
        void onWrite();
    }
}
