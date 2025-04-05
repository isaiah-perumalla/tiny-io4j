package net.isaiahp.tinyio;

import net.isaiahp.tinyio.nativesockets.net_h;
import net.isaiahp.tinyio.nativesockets.sockaddr_in;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class Utils {
    private static final StringBuilder sb = new StringBuilder();
    public static CharSequence ipAddressToString(int ipAdd) {
        sb.setLength(0);
        sb.append(ipAdd  & 0xFF).append(".")
                .append((ipAdd >> 8) & 0xFF).append(".")
                .append((ipAdd >> 16) & 0xFF).append(".")
                .append((ipAdd >> 24) & 0xFF);

        return sb;
    }

    public static void setNonBlocking(int fd) {

        final int flags = net_h.fcntl(fd, net_h.F_GETFL(), 0);
        final int err = net_h.fcntl(fd, net_h.F_SETFL(), flags|net_h.O_NONBLOCK());
        if (err < 0) {
            throw new IllegalStateException("unable to set fd to O_NONBLOCK");
        }
    }

    public static MemorySegment createSockInAddr(Arena arena, short port, int ip) {
        MemorySegment sockaddr = arena.allocate(sockaddr_in.$LAYOUT());
        sockaddr_in.sin_port$set(sockaddr, net_h.htons(port));
        sockaddr_in.sin_family$set(sockaddr, (short) net_h.AF_INET());
        MemorySegment in_addr = sockaddr_in.sin_addr$slice(sockaddr);
        sockaddr_in.in_addr.s_addr$set(in_addr, net_h.htonl(ip));
        return sockaddr;
    }
}
