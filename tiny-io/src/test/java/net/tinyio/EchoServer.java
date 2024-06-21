package net.tinyio;

import net.tinyio.posix.DefaultMux;
import net.tinyio.posix.IoMux;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.net.InetSocketAddress;

public class EchoServer {
    private static final Logger logger = LogManager.getLogger(EchoServer.class);

    enum Type {Server, Client}
    public static void main(String[] args) {

        try (Arena arena = Arena.ofConfined()) {
            InetSocketAddress socketAddress = new InetSocketAddress(4048);

            DefaultMux mux = new DefaultMux();
            mux.open();
            Server server = new Server(4048, mux);
            server.open();
            do {
                int count = mux.poll(1);
                if (count == 0) {
                    Thread.yield();
                }
            }while (true);
        }
    }

    private static class Server extends TcpListener<EchoClientContext> {
        private final Arena arena;
        private MemorySegment readSegment;

        public Server(int port, IoMux mux) {
            super(port, mux);
            this.arena = Arena.ofConfined();
            readSegment = arena.allocate(1024);
        }

        protected EchoClientContext createClientContext() {
            return new EchoClientContext();
        }
        @Override
        protected void onAccepted(EchoClientContext tcpChannel) {
            logger.info("client connection accepted [{}:{}]", Utils.ipAddressToString(tcpChannel.ipAddress()),
                    tcpChannel.port());
        }

        @Override
        protected void availableToWrite(EchoClientContext context) {
            super.availableToWrite(context);
        }

        @Override
        protected void availableToRead(EchoClientContext context) {
            long bytesRead = read(context, readSegment);
            System.out.println("read bytes " + bytesRead);
            logger.info("read bytes {} from [{}:{}]", bytesRead,
                    Utils.ipAddressToString(context.ipAddress()),
                    context.port());

        }

        @Override
        protected void onDisconnected(EchoClientContext context) {
            logger.info("disconnected from [{}:{}]", Utils.ipAddressToString(context.ipAddress()), context.port());
        }
    }

    private static class EchoClientContext extends TcpListener.ClientContext{
    }
}
