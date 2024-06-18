package net.tinyio;

import net.tinyio.nativesockets.net_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.channels.Channel;

import static net.tinyio.Utils.createSockInAddr;

public class TcpListener {

    private final int port;
    private final IoMux mux;
    private Arena arena;
    private IoMux.Handler ioHandler = new IoHandler();
    private long serverSocketHandle;


    public TcpListener(int port, IoMux mux) {
        this.port = port;
        this.mux = mux;
    }

    public void open() {
        arena = Arena.ofConfined();
        final int fd = net_h.socket(net_h.PF_INET(), net_h.SOCK_STREAM(), 0);
        MemorySegment sockInAddr = createSockInAddr(arena, (short) port, net_h.INADDR_ANY());
        MemorySegment optVal = arena.allocate(ValueLayout.JAVA_INT, 1);
        net_h.setsockopt(fd, net_h.SOL_SOCKET(), net_h.SO_REUSEADDR(), optVal, (int) optVal.byteSize());
        int err = net_h.bind(fd, sockInAddr, (int) sockInAddr.byteSize());
        if (err < 0) {
            throw new IllegalStateException("could not bind ");
        }
        Utils.setNonBlocking(fd);

        err = net_h.listen(fd, 128);
        if (err < 0) {
            throw new IllegalStateException("Could not listen on socket\n");
        }
        serverSocketHandle = mux.registerFd(fd, ioHandler);
        mux.addInterest(serverSocketHandle, IoMux.Op.Read);
    }


    public void close() {
        if (arena != null) {
            arena.close();
        }
        //shutdown socket
    }

    public Channel accept() {
        return null;
    }

    private class IoHandler implements IoMux.Handler {
        @Override
        public void onRead() {

        }

        @Override
        public void onWrite() {

        }
    }
}
