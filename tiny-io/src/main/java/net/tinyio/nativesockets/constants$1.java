// Generated by jextract

package net.tinyio.nativesockets;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$1 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$1() {}
    static final VarHandle const$0 = constants$0.const$2.varHandle(MemoryLayout.PathElement.groupElement("msg_iovlen"));
    static final VarHandle const$1 = constants$0.const$2.varHandle(MemoryLayout.PathElement.groupElement("msg_control"));
    static final VarHandle const$2 = constants$0.const$2.varHandle(MemoryLayout.PathElement.groupElement("msg_controllen"));
    static final VarHandle const$3 = constants$0.const$2.varHandle(MemoryLayout.PathElement.groupElement("msg_flags"));
    static final FunctionDescriptor const$4 = FunctionDescriptor.of(JAVA_INT,
        JAVA_INT,
        JAVA_INT,
        JAVA_INT
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "socket",
        constants$1.const$4
    );
}


