// Generated by jextract

package net.isaiahp.tinyio.nativesockets;

import java.lang.invoke.MethodHandle;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$7 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$7() {}
    static final FunctionDescriptor const$0 = FunctionDescriptor.of(JAVA_INT,
        RuntimeHelper.POINTER,
        JAVA_LONG,
        JAVA_INT
    );
    static final MethodHandle const$1 = RuntimeHelper.downcallHandle(
        "poll",
        constants$7.const$0
    );
    static final MethodHandle const$2 = RuntimeHelper.downcallHandleVariadic(
        "fcntl",
        constants$3.const$2
    );
    static final MethodHandle const$3 = RuntimeHelper.downcallHandle(
        "close",
        constants$5.const$2
    );
    static final FunctionDescriptor const$4 = FunctionDescriptor.ofVoid(
        RuntimeHelper.POINTER,
        JAVA_LONG
    );
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        "bzero",
        constants$7.const$4
    );
}


