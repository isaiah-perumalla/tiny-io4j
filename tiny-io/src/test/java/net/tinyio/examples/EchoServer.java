package net.tinyio.examples;

import net.tinyio.TcpListener;
import net.tinyio.Utils;
import net.tinyio.posix.DefaultMux;
import net.tinyio.posix.IoMux;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.net.InetSocketAddress;
import java.rmi.server.UID;

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
            if (bytesRead > 0) {
                echoBack(context, readSegment, bytesRead);
            }
            else if (bytesRead < 0) {
                //error
                logError(context);
            }
        }

        private void echoBack(EchoClientContext context, MemorySegment segment, long bytesRead) {
            long sendCount = write(context, segment, (int) bytesRead);
            if (sendCount < 0) {
                logError(context);
            }
            else if (sendCount < bytesRead) {
                logger.info("sent buffer is full, [bytes={},sent={}] [{}:{}]",
                        bytesRead,
                        sendCount,
                        Utils.ipAddressToString(context.ipAddress()),
                        context.port());
            }
            context.echoCount += sendCount;
        }

        @Override
        protected void onDisconnected(EchoClientContext context) {
            logger.info("disconnected from [{}:{}], echoedBytes={}",
                    Utils.ipAddressToString(context.ipAddress()),
                    context.port(),
                    context.echoCount);
        }
    }

    private static void logError(EchoClientContext context) {
        int errorNo = context.lastErrorNo();
        logger.error("send error {}, [{}:{}]", errorNo,
                Utils.ipAddressToString(context.ipAddress()),
                context.port());
    }

    private static class EchoClientContext extends TcpListener.ClientContext{
        long echoCount = 0;
    }
}
