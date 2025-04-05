// Generated by jextract

package net.isaiahp.tinyio.nativesockets;

import java.lang.invoke.VarHandle;
import java.lang.foreign.*;

/**
 * {@snippet :
 * struct sockaddr_in {
 *     sa_family_t sin_family;
 *     in_port_t sin_port;
 *     struct in_addr sin_addr;
 *     unsigned char sin_zero[8];
 * };
 * }
 */
public class sockaddr_in {

    public static MemoryLayout $LAYOUT() {
        return constants$4.const$3;
    }
    public static VarHandle sin_family$VH() {
        return constants$4.const$4;
    }
    /**
     * Getter for field:
     * {@snippet :
     * sa_family_t sin_family;
     * }
     */
    public static short sin_family$get(MemorySegment seg) {
        return (short)constants$4.const$4.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * sa_family_t sin_family;
     * }
     */
    public static void sin_family$set(MemorySegment seg, short x) {
        constants$4.const$4.set(seg, x);
    }
    public static short sin_family$get(MemorySegment seg, long index) {
        return (short)constants$4.const$4.get(seg.asSlice(index*sizeof()));
    }
    public static void sin_family$set(MemorySegment seg, long index, short x) {
        constants$4.const$4.set(seg.asSlice(index*sizeof()), x);
    }
    public static VarHandle sin_port$VH() {
        return constants$4.const$5;
    }
    /**
     * Getter for field:
     * {@snippet :
     * in_port_t sin_port;
     * }
     */
    public static short sin_port$get(MemorySegment seg) {
        return (short)constants$4.const$5.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * in_port_t sin_port;
     * }
     */
    public static void sin_port$set(MemorySegment seg, short x) {
        constants$4.const$5.set(seg, x);
    }
    public static short sin_port$get(MemorySegment seg, long index) {
        return (short)constants$4.const$5.get(seg.asSlice(index*sizeof()));
    }
    public static void sin_port$set(MemorySegment seg, long index, short x) {
        constants$4.const$5.set(seg.asSlice(index*sizeof()), x);
    }
    /**
     * {@snippet :
     * struct in_addr {
     *     in_addr_t s_addr;
     * };
     * }
     */
    public static final class in_addr {

        // Suppresses default constructor, ensuring non-instantiability.
        private in_addr() {}
        public static MemoryLayout $LAYOUT() {
            return constants$5.const$0;
        }
        public static VarHandle s_addr$VH() {
            return constants$5.const$1;
        }
        /**
         * Getter for field:
         * {@snippet :
         * in_addr_t s_addr;
         * }
         */
        public static int s_addr$get(MemorySegment seg) {
            return (int)constants$5.const$1.get(seg);
        }
        /**
         * Setter for field:
         * {@snippet :
         * in_addr_t s_addr;
         * }
         */
        public static void s_addr$set(MemorySegment seg, int x) {
            constants$5.const$1.set(seg, x);
        }
        public static int s_addr$get(MemorySegment seg, long index) {
            return (int)constants$5.const$1.get(seg.asSlice(index*sizeof()));
        }
        public static void s_addr$set(MemorySegment seg, long index, int x) {
            constants$5.const$1.set(seg.asSlice(index*sizeof()), x);
        }
        public static long sizeof() { return $LAYOUT().byteSize(); }
        public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
        public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
            return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
        }
        public static MemorySegment ofAddress(MemorySegment addr, Arena arena) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, arena); }
    }

    public static MemorySegment sin_addr$slice(MemorySegment seg) {
        return seg.asSlice(4, 4);
    }
    public static MemorySegment sin_zero$slice(MemorySegment seg) {
        return seg.asSlice(8, 8);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, Arena arena) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, arena); }
}


