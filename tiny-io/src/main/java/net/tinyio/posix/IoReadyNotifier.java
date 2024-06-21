package net.tinyio.posix;

public interface IoReadyNotifier {
    void onRead(int fd);

    void onWrite(int fd);
}
