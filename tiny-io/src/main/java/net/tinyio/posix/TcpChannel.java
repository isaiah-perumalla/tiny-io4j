package net.tinyio.posix;

import java.lang.foreign.Arena;

public class TcpChannel {
    private final int clientFd;

    public TcpChannel(Arena clientArena, int clientFd) {
        this.clientFd = clientFd;
    }
}
