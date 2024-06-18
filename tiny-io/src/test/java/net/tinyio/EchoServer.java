package net.tinyio;

import net.tinyio.posix.PollSelector;

import java.lang.foreign.Arena;
import java.net.InetSocketAddress;

public class EchoServer {
    enum Type {Server, Client}
    public static void main(String[] args) {

        try (Arena arena = Arena.ofConfined()) {
            InetSocketAddress socketAddress = new InetSocketAddress(4048);

            IoMux mux = null;
            TcpListener tcpListener = new TcpListener(4048, mux);
            PollSelector selector = new PollSelector(128);


        }
    }
}
