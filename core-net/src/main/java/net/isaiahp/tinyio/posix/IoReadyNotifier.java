package net.isaiahp.tinyio.posix;

public interface IoReadyNotifier {
    void onRead(int fd);

    void onWrite(int fd);
}
