package net.tinyio;

import net.tinyio.nativesockets.net_h;
import net.tinyio.nativesockets.pollfd;
import net.tinyio.nativesockets.sockaddr_in;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Test {

    private static final MemorySegment ERRNO_LOCATION = net_h.__errno_location();
    private static final long MAX_FDS = 64;

    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            final int fd = net_h.socket(net_h.PF_INET(), net_h.SOCK_STREAM(), 0);
            int port = 4048;
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
            System.out.println(String.format("listing on port %d", port));
            final MemorySegment buffer = arena.allocate(1024);
            final MemorySegment pollFds = arena.allocate(pollfd.$LAYOUT().byteSize() * MAX_FDS);
            setPollFd(0, pollFds, fd, (short) (net_h.POLLIN() | net_h.POLLPRI()));
            int count = 1;
            for(;;) {
                final int pollResult = net_h.poll(pollFds, count, 0);
                if (pollResult == 0) {
                    Thread.yield();
                    continue;
                }
                if (pollResult < 0) {
                    on_error(String.format("poll error %d", getErrno()));
                    continue;
                }
                final short serverEvents = pollREventsAt(0, pollFds);
                assert pollfd.revents$get(pollFds, 0) == serverEvents;
                if (0 != (serverEvents & net_h.POLLIN())) {
                    MemorySegment client_addr = arena.allocate(sockaddr_in.$LAYOUT());
                    MemorySegment addr_len = arena.allocate(ValueLayout.JAVA_INT, (int) client_addr.byteSize());
                    int client_fd = net_h.accept(fd, client_addr, addr_len);
                    if (client_fd < 0) {
                        on_error("Could not establish new connection\n");
                    }
                    else {
                        System.out.println(String.format("client connected fd=%d", client_fd));
                    }
                    int index = registerPollInterest(pollFds, client_fd, (short) (net_h.POLLIN() | net_h.POLLPRI()));
                    assert index >= 0;
                    count++;
                }
                for (int i = 1; i < MAX_FDS; i++) {
                    final int f = pollFdAt(i, pollFds);
                    if (f == 0) continue;
                    final short revents = pollREventsAt(i, pollFds);
                    if (0 != (revents & net_h.POLLIN())) {
                        final long read = net_h.recv(f, buffer, buffer.byteSize(), 0);
                        if (read > 0) {
                            net_h.send(f, buffer, read, 0);
                        }
                        else if (read == 0) {
                            setPollFd(i, pollFds, 0, (short) 0);
                            count--;
                            on_error("recv read 0 ");
                        }
                        else  {
                            final int errno = getErrno();
                            if (errno != net_h.EWOULDBLOCK()) {
                                setPollFd(i, pollFds, 0, (short) 0);
                                count--;
                                on_error("recv read error ");
                            }
                        }
                    }
                }
            }
        }
    }

    private static int getErrno() {
        final int errno = ERRNO_LOCATION.get(ValueLayout.JAVA_INT, 0);
        return errno;
    }

    private static int registerPollInterest(MemorySegment pollFds, int fd, short events) {
        for (int i = 0; i < MAX_FDS; i++) {
            final int f = pollFdAt(i, pollFds);
            if (f == 0) { //free slot
                setPollFd(i, pollFds, fd, events);
                return i;
            }
        }
        return -1;
    }

    public static int pollFdAt(int index, MemorySegment pollFds) {
        final long offset = pollfd.sizeof() * index;
        return pollFds.get(ValueLayout.JAVA_INT, offset);
    }


    private static short pollREventsAt(int index, MemorySegment pollFds) {
        final long offset = pollfd.sizeof() * index;
        final long fieldOffset = ValueLayout.JAVA_INT.byteSize() + ValueLayout.JAVA_SHORT.byteSize() + offset;
        return pollFds.get(ValueLayout.JAVA_SHORT,  fieldOffset);
    }

    public static void setPollFd(int index, MemorySegment pollFds, int fd, short events) {
        final long offset = pollfd.sizeof() * index;
        pollFds.set(ValueLayout.JAVA_INT, offset, fd);
        pollFds.set(ValueLayout.JAVA_SHORT, offset + ValueLayout.JAVA_INT.byteSize(), events);
    }



    private static void doEcho(int client_fd, MemorySegment buffer) {
        for(;;) {
            final long read = net_h.recv(client_fd, buffer, buffer.byteSize(), 0);
            if (read < 0) {
                on_error(String.format("read failed on fd %d", client_fd));
                break;
            } else if (read == 0) {
                break;
            }
            long err = net_h.send(client_fd, buffer, read, 0);
            if (err < 0) {
                on_error(String.format("send error %d ", client_fd));
                break;
            }
        }
    }

    private static void on_error(String s) {
        System.err.println(s);
    }

    private static MemorySegment createSockInAddr(Arena arena, short port, int ip) {
        MemorySegment sockaddr = arena.allocate(sockaddr_in.$LAYOUT());
        sockaddr_in.sin_port$set(sockaddr, net_h.htons(port));
        sockaddr_in.sin_family$set(sockaddr, (short) net_h.AF_INET());
        MemorySegment in_addr = sockaddr_in.sin_addr$slice(sockaddr);
        sockaddr_in.in_addr.s_addr$set(in_addr, ip);
        return sockaddr;
    }

}
