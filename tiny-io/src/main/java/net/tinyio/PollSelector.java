package net.tinyio;
import net.tinyio.nativesockets.net_h;
import net.tinyio.nativesockets.pollfd;

public class PollSelector<T> {
    public static final int READ = net_h.POLLIN() | net_h.POLLPRI();

    public PollSelector(int maxFds) {

    }

    public void register(T t, int fd, int flags) {

    }
}
