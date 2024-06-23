package net.tinyio;

import net.tinyio.nativesockets.net_h;
import net.tinyio.nativesockets.sockaddr_in;
import net.tinyio.posix.IoMux;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static net.tinyio.Utils.createSockInAddr;

public class TcpListener<T extends TcpListener.ClientContext> {
    private static final Logger logger = LogManager.getLogger(TcpListener.class);
    private final StringBuilder sb = new StringBuilder();

    private final int port;
    private final IoMux mux;
    private Arena arena;
    private IoMux.Handler ioHandler = new IoHandler();
    private long serverSocketHandle;
    private int fd;
    private long nextClientId;
    private MemorySegment sendBufferValue;


    public TcpListener(int port, IoMux mux) {
        this.port = port;
        this.mux = mux;
    }

    public void open() {
        arena = Arena.ofConfined();
        sendBufferValue = arena.allocate(ValueLayout.JAVA_INT, 1024);
        final int fd = net_h.socket(net_h.PF_INET(), net_h.SOCK_STREAM(), 0);
        MemorySegment sockInAddr = createSockInAddr(arena, (short) port, net_h.INADDR_ANY());
        MemorySegment optVal = arena.allocate(ValueLayout.JAVA_INT, 1);
        net_h.setsockopt(fd, net_h.SOL_SOCKET(), net_h.SO_REUSEADDR(), optVal, (int) optVal.byteSize());
        int err = net_h.bind(fd, sockInAddr, (int) sockInAddr.byteSize());
        if (err < 0) {
            throw new IllegalStateException("could not bind ");
        }
        Utils.setNonBlocking(fd);
        this.fd = fd;
        err = net_h.listen(fd, 128);
        if (err < 0) {
            throw new IllegalStateException("Could not listen on socket\n");
        }
        serverSocketHandle = mux.registerFd(fd, ioHandler);
        mux.addInterest(serverSocketHandle, IoMux.Op.Read);
        logger.info("tcp server listening [{}:{}]", Utils.ipAddressToString(getIpAdd(sockInAddr)), port);
        onOpened();
    }

    protected void onOpened() {

    }

    protected void onAccepted(T clientContext) {

    }

    public void close() {
        if (arena != null) {
            arena.close();
        }
        //shutdown socket
    }


    private class IoHandler implements IoMux.Handler {
        Arena clientArena = Arena.ofConfined();
        MemorySegment client_addr = clientArena.allocate(sockaddr_in.$LAYOUT());
        MemorySegment addr_len = clientArena.allocate(ValueLayout.JAVA_INT, (int) client_addr.byteSize());

        @Override
        public void onRead() {
            net_h.bzero(client_addr, client_addr.byteSize());
            int clientFd = net_h.accept(fd, client_addr, addr_len);
            if (clientFd < 0 ) {
                //error
                int errNo = net_h.__errno_location().get(ValueLayout.JAVA_INT, 0);
                logger.error("accept error {} fd={}", errNo, clientFd);
            }
            else {
                net_h.setsockopt(clientFd, net_h.SOL_SOCKET(), net_h.SO_SNDBUF(), sendBufferValue, (int) sendBufferValue.byteSize());
                final T context = createClientContext();
                final long clientHandle = mux.registerFd(clientFd, new ClientIoHandler(context));
                final int port = 0xFFFF & net_h.ntohs(sockaddr_in.sin_port$get(client_addr));
                final int ipAdd = getIpAdd(client_addr);
                context.init(ipAdd, port, clientFd, clientHandle, nextClientId++);

                mux.addInterest(clientHandle, IoMux.Op.Read);
                onAccepted(context);
            }
        }

        @Override
        public void onWrite() {
            throw new IllegalStateException("should not get write event for socket listener");
        }
    }

    private static int getIpAdd(MemorySegment client_addr) {
        MemorySegment sinAddr = sockaddr_in.sin_addr$slice(client_addr);
        final int ipAdd = sockaddr_in.in_addr.s_addr$get(sinAddr); // in network byte order
        return ipAdd;
    }


    protected T createClientContext() {
        return (T) new ClientContext();
    }

    private class ClientIoHandler implements IoMux.Handler {
        private final T context;

        public ClientIoHandler(T context) {
            this.context = context;
        }

        @Override
        public void onRead() {
            TcpListener.this.availableToRead(context);
        }

        @Override
        public void onWrite() {
            TcpListener.this.availableToWrite(context);
        }
    }

    protected void availableToWrite(T context) {

    }

    protected long write(T context, MemorySegment segment, int length) {
        long count = net_h.send(context.fd, segment, length, 0);
        if (count < 0) { //error
            int errNo = net_h.__errno_location().getAtIndex(ValueLayout.JAVA_INT, 0);
            context.setLastError(errNo);
            handleDisconneted(context);
        }
        else if (count < length) { //send buffer
            logger.info("adding write interest {} fd={}", context.ioHandle, context.fd);
            mux.addInterest(context.ioHandle, IoMux.Op.Write);
            context.setSendBufferFull();
        }
        return count;
    }



    protected long read(T context, MemorySegment segment) {
        if (segment.byteSize() == 0) {
            return 0;
        }

        int flags = 0;
        long read = net_h.recv(context.fd, segment, segment.byteSize(), flags);
        if (read == 0) { //eof
            handleDisconneted(context);
        }
        else if (read < 0) {
             //error
            int errNo = net_h.__errno_location().get(ValueLayout.JAVA_INT, 0);
            context.setLastError(errNo);
        }
        return read;
    }

    private void handleDisconneted(T context) {
        logger.info("disconnecting errno={}, [{}:{}]", context.lastErrorNo(),
                Utils.ipAddressToString(context.ipAddress()), context.port());
        mux.deRegister(context.ioHandle);
        net_h.close(context.fd);
        context.ioHandle = -1;
        onDisconnected(context);
    }

    protected void onDisconnected(T context) {

    }

    protected void availableToRead(T context) {

    }


    public static class ClientContext {
        protected int fd;
        protected  long ioHandle;
        protected long id;
        private int port;
        private int ipAdd;
        private int lastError;

        public void init(int ipAdd, int port, int clientFd, long clientHandle, long id) {
            fd = clientFd;
            this.id = id;
            this.port = port;
            this.ioHandle = clientHandle;
            this.ipAdd = ipAdd;

        }



        public int port() {
            return port;
        }
        public int ipAddress() {
            return this.ipAdd;
        }

        public void setLastError(int errNo) {
            this.lastError = errNo;
        }

        public void setSendBufferFull() {

        }

        public int lastErrorNo() {
            return lastError;
        }
    }
}
