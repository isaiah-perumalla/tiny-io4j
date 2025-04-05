// Generated by jextract

package net.isaiahp.tinyio.nativesockets;

import java.lang.invoke.MethodHandle;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
public class net_h  {

    public static final OfByte C_CHAR = JAVA_BYTE;
    public static final OfShort C_SHORT = JAVA_SHORT;
    public static final OfInt C_INT = JAVA_INT;
    public static final OfLong C_LONG = JAVA_LONG;
    public static final OfLong C_LONG_LONG = JAVA_LONG;
    public static final OfFloat C_FLOAT = JAVA_FLOAT;
    public static final OfDouble C_DOUBLE = JAVA_DOUBLE;
    public static final AddressLayout C_POINTER = RuntimeHelper.POINTER;
    /**
     * {@snippet :
     * #define PF_INET 2
     * }
     */
    public static int PF_INET() {
        return (int)2L;
    }
    /**
     * {@snippet :
     * #define SOL_SOCKET 1
     * }
     */
    public static int SOL_SOCKET() {
        return (int)1L;
    }
    /**
     * {@snippet :
     * #define SO_REUSEADDR 2
     * }
     */
    public static int SO_REUSEADDR() {
        return (int)2L;
    }
    /**
     * {@snippet :
     * #define SO_SNDBUF 7
     * }
     */
    public static int SO_SNDBUF() {
        return (int)7L;
    }
    /**
     * {@snippet :
     * #define SO_RCVBUF 8
     * }
     */
    public static int SO_RCVBUF() {
        return (int)8L;
    }
    /**
     * {@snippet :
     * #define EAGAIN 11
     * }
     */
    public static int EAGAIN() {
        return (int)11L;
    }
    /**
     * {@snippet :
     * #define POLLIN 1
     * }
     */
    public static int POLLIN() {
        return (int)1L;
    }
    /**
     * {@snippet :
     * #define POLLPRI 2
     * }
     */
    public static int POLLPRI() {
        return (int)2L;
    }
    /**
     * {@snippet :
     * #define POLLOUT 4
     * }
     */
    public static int POLLOUT() {
        return (int)4L;
    }
    /**
     * {@snippet :
     * #define POLLERR 8
     * }
     */
    public static int POLLERR() {
        return (int)8L;
    }
    /**
     * {@snippet :
     * #define POLLNVAL 32
     * }
     */
    public static int POLLNVAL() {
        return (int)32L;
    }
    /**
     * {@snippet :
     * #define O_NONBLOCK 2048
     * }
     */
    public static int O_NONBLOCK() {
        return (int)2048L;
    }
    /**
     * {@snippet :
     * #define F_GETFL 3
     * }
     */
    public static int F_GETFL() {
        return (int)3L;
    }
    /**
     * {@snippet :
     * #define F_SETFL 4
     * }
     */
    public static int F_SETFL() {
        return (int)4L;
    }
    public static MethodHandle perror$MH() {
        return RuntimeHelper.requireNonNull(constants$0.const$1,"perror");
    }
    /**
     * {@snippet :
     * void perror(char* __s);
     * }
     */
    public static void perror(MemorySegment __s) {
        var mh$ = perror$MH();
        try {
            mh$.invokeExact(__s);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    /**
     * {@snippet :
     * enum __socket_type.SOCK_STREAM = 1;
     * }
     */
    public static int SOCK_STREAM() {
        return (int)1L;
    }
    public static MethodHandle socket$MH() {
        return RuntimeHelper.requireNonNull(constants$1.const$5,"socket");
    }
    /**
     * {@snippet :
     * int socket(int __domain, int __type, int __protocol);
     * }
     */
    public static int socket(int __domain, int __type, int __protocol) {
        var mh$ = socket$MH();
        try {
            return (int)mh$.invokeExact(__domain, __type, __protocol);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle bind$MH() {
        return RuntimeHelper.requireNonNull(constants$2.const$1,"bind");
    }
    /**
     * {@snippet :
     * int bind(int __fd, struct sockaddr* __addr, socklen_t __len);
     * }
     */
    public static int bind(int __fd, MemorySegment __addr, int __len) {
        var mh$ = bind$MH();
        try {
            return (int)mh$.invokeExact(__fd, __addr, __len);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle connect$MH() {
        return RuntimeHelper.requireNonNull(constants$2.const$2,"connect");
    }
    /**
     * {@snippet :
     * int connect(int __fd, struct sockaddr* __addr, socklen_t __len);
     * }
     */
    public static int connect(int __fd, MemorySegment __addr, int __len) {
        var mh$ = connect$MH();
        try {
            return (int)mh$.invokeExact(__fd, __addr, __len);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle send$MH() {
        return RuntimeHelper.requireNonNull(constants$2.const$4,"send");
    }
    /**
     * {@snippet :
     * ssize_t send(int __fd, void* __buf, size_t __n, int __flags);
     * }
     */
    public static long send(int __fd, MemorySegment __buf, long __n, int __flags) {
        var mh$ = send$MH();
        try {
            return (long)mh$.invokeExact(__fd, __buf, __n, __flags);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle recv$MH() {
        return RuntimeHelper.requireNonNull(constants$2.const$5,"recv");
    }
    /**
     * {@snippet :
     * ssize_t recv(int __fd, void* __buf, size_t __n, int __flags);
     * }
     */
    public static long recv(int __fd, MemorySegment __buf, long __n, int __flags) {
        var mh$ = recv$MH();
        try {
            return (long)mh$.invokeExact(__fd, __buf, __n, __flags);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle setsockopt$MH() {
        return RuntimeHelper.requireNonNull(constants$3.const$1,"setsockopt");
    }
    /**
     * {@snippet :
     * int setsockopt(int __fd, int __level, int __optname, void* __optval, socklen_t __optlen);
     * }
     */
    public static int setsockopt(int __fd, int __level, int __optname, MemorySegment __optval, int __optlen) {
        var mh$ = setsockopt$MH();
        try {
            return (int)mh$.invokeExact(__fd, __level, __optname, __optval, __optlen);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle listen$MH() {
        return RuntimeHelper.requireNonNull(constants$3.const$3,"listen");
    }
    /**
     * {@snippet :
     * int listen(int __fd, int __n);
     * }
     */
    public static int listen(int __fd, int __n) {
        var mh$ = listen$MH();
        try {
            return (int)mh$.invokeExact(__fd, __n);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle accept$MH() {
        return RuntimeHelper.requireNonNull(constants$3.const$5,"accept");
    }
    /**
     * {@snippet :
     * int accept(int __fd, struct sockaddr* __addr, socklen_t* __addr_len);
     * }
     */
    public static int accept(int __fd, MemorySegment __addr, MemorySegment __addr_len) {
        var mh$ = accept$MH();
        try {
            return (int)mh$.invokeExact(__fd, __addr, __addr_len);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle shutdown$MH() {
        return RuntimeHelper.requireNonNull(constants$4.const$0,"shutdown");
    }
    /**
     * {@snippet :
     * int shutdown(int __fd, int __how);
     * }
     */
    public static int shutdown(int __fd, int __how) {
        var mh$ = shutdown$MH();
        try {
            return (int)mh$.invokeExact(__fd, __how);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle __errno_location$MH() {
        return RuntimeHelper.requireNonNull(constants$4.const$2,"__errno_location");
    }
    /**
     * {@snippet :
     * int* __errno_location();
     * }
     */
    public static MemorySegment __errno_location() {
        var mh$ = __errno_location$MH();
        try {
            return (MemorySegment)mh$.invokeExact();
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle ntohl$MH() {
        return RuntimeHelper.requireNonNull(constants$5.const$3,"ntohl");
    }
    /**
     * {@snippet :
     * uint32_t ntohl(uint32_t __netlong);
     * }
     */
    public static int ntohl(int __netlong) {
        var mh$ = ntohl$MH();
        try {
            return (int)mh$.invokeExact(__netlong);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle ntohs$MH() {
        return RuntimeHelper.requireNonNull(constants$5.const$5,"ntohs");
    }
    /**
     * {@snippet :
     * uint16_t ntohs(uint16_t __netshort);
     * }
     */
    public static short ntohs(short __netshort) {
        var mh$ = ntohs$MH();
        try {
            return (short)mh$.invokeExact(__netshort);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle htonl$MH() {
        return RuntimeHelper.requireNonNull(constants$6.const$0,"htonl");
    }
    /**
     * {@snippet :
     * uint32_t htonl(uint32_t __hostlong);
     * }
     */
    public static int htonl(int __hostlong) {
        var mh$ = htonl$MH();
        try {
            return (int)mh$.invokeExact(__hostlong);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle htons$MH() {
        return RuntimeHelper.requireNonNull(constants$6.const$1,"htons");
    }
    /**
     * {@snippet :
     * uint16_t htons(uint16_t __hostshort);
     * }
     */
    public static short htons(short __hostshort) {
        var mh$ = htons$MH();
        try {
            return (short)mh$.invokeExact(__hostshort);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle poll$MH() {
        return RuntimeHelper.requireNonNull(constants$7.const$1,"poll");
    }
    /**
     * {@snippet :
     * int poll(struct pollfd* __fds, nfds_t __nfds, int __timeout);
     * }
     */
    public static int poll(MemorySegment __fds, long __nfds, int __timeout) {
        var mh$ = poll$MH();
        try {
            return (int)mh$.invokeExact(__fds, __nfds, __timeout);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle fcntl$MH() {
        return RuntimeHelper.requireNonNull(constants$7.const$2,"fcntl");
    }
    /**
     * {@snippet :
     * int fcntl(int __fd, int __cmd,...);
     * }
     */
    public static int fcntl(int __fd, int __cmd, Object... x2) {
        var mh$ = fcntl$MH();
        try {
            return (int)mh$.invokeExact(__fd, __cmd, x2);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle close$MH() {
        return RuntimeHelper.requireNonNull(constants$7.const$3,"close");
    }
    /**
     * {@snippet :
     * int close(int __fd);
     * }
     */
    public static int close(int __fd) {
        var mh$ = close$MH();
        try {
            return (int)mh$.invokeExact(__fd);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle bzero$MH() {
        return RuntimeHelper.requireNonNull(constants$7.const$5,"bzero");
    }
    /**
     * {@snippet :
     * void bzero(void* __s, size_t __n);
     * }
     */
    public static void bzero(MemorySegment __s, long __n) {
        var mh$ = bzero$MH();
        try {
            mh$.invokeExact(__s, __n);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle inet_addr$MH() {
        return RuntimeHelper.requireNonNull(constants$8.const$1,"inet_addr");
    }
    /**
     * {@snippet :
     * in_addr_t inet_addr(char* __cp);
     * }
     */
    public static int inet_addr(MemorySegment __cp) {
        var mh$ = inet_addr$MH();
        try {
            return (int)mh$.invokeExact(__cp);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle inet_ntoa$MH() {
        return RuntimeHelper.requireNonNull(constants$8.const$3,"inet_ntoa");
    }
    /**
     * {@snippet :
     * char* inet_ntoa(struct in_addr __in);
     * }
     */
    public static MemorySegment inet_ntoa(MemorySegment __in) {
        var mh$ = inet_ntoa$MH();
        try {
            return (MemorySegment)mh$.invokeExact(__in);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle inet_ntop$MH() {
        return RuntimeHelper.requireNonNull(constants$8.const$5,"inet_ntop");
    }
    /**
     * {@snippet :
     * char* inet_ntop(int __af, void* __cp, char* __buf, socklen_t __len);
     * }
     */
    public static MemorySegment inet_ntop(int __af, MemorySegment __cp, MemorySegment __buf, int __len) {
        var mh$ = inet_ntop$MH();
        try {
            return (MemorySegment)mh$.invokeExact(__af, __cp, __buf, __len);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    /**
     * {@snippet :
     * #define AF_INET 2
     * }
     */
    public static int AF_INET() {
        return (int)2L;
    }
    /**
     * {@snippet :
     * #define EWOULDBLOCK 11
     * }
     */
    public static int EWOULDBLOCK() {
        return (int)11L;
    }
    /**
     * {@snippet :
     * #define INADDR_ANY 0
     * }
     */
    public static int INADDR_ANY() {
        return (int)0L;
    }
}


