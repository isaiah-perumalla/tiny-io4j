// Generated by jextract

package net.isaiahp.tinyio.nativesockets;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$0 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$0() {}
    static final FunctionDescriptor const$0 = FunctionDescriptor.ofVoid(
        RuntimeHelper.POINTER
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "perror",
        constants$0.const$0
    );
    static final StructLayout const$2 = MemoryLayout.structLayout(
        RuntimeHelper.POINTER.withName("msg_name"),
        JAVA_INT.withName("msg_namelen"),
        MemoryLayout.paddingLayout(4),
        RuntimeHelper.POINTER.withName("msg_iov"),
        JAVA_LONG.withName("msg_iovlen"),
        RuntimeHelper.POINTER.withName("msg_control"),
        JAVA_LONG.withName("msg_controllen"),
        JAVA_INT.withName("msg_flags"),
        MemoryLayout.paddingLayout(4)
    ).withName("msghdr");
    static final VarHandle const$3 = constants$0.const$2.varHandle(PathElement.groupElement("msg_name"));
    static final VarHandle const$4 = constants$0.const$2.varHandle(PathElement.groupElement("msg_namelen"));
    static final VarHandle const$5 = constants$0.const$2.varHandle(PathElement.groupElement("msg_iov"));
}


