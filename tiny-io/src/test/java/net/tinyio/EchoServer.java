package net.tinyio;

import java.lang.foreign.Arena;
import java.net.Inet4Address;
import java.net.InetSocketAddress;

public class EchoServer {
    enum Type {Server, Client}
    public static void main(String[] args) {

        try (Arena arena = Arena.ofConfined()) {
            InetSocketAddress socketAddress = new InetSocketAddress(4048);

            TcpListener tcpListener = TcpListener.bindAndListen(socketAddress);
            PollSelector<Type> selector = new PollSelector(128);
            selector.register(Type.Server, tcpListener.fd(), PollSelector.READ);

        }
    }
}
