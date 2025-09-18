package com.tutorial.foundation.bitwise;

import java.util.Arrays;

/**
 * BitTricksAdvanced — parity (with practical uses), swap bits, reverse bits,
 * closest int with same set-bit count, and shift-based multiply/divide.
 *
 * WHAT’S NEW (parity usages beyond basics):
 *  - Even-parity bit on bytes (pack/check; simulate single-bit errors)
 *  - XOR “checksum” over a packet (common interview pattern)
 *  - Rolling parity for streams (update-in-O(1))
 *  - Prefix parity for subarray queries (parity of any range in O(1))
 *
 * BUILD & RUN (JDK 17+):
 *   javac com/tutorial/core/bitwise/BitTricksAdvanced.java && \
 *   java com.tutorial.core.bitwise.BitTricksAdvanced
 */
public class BitParity {

    // =========================================================================
    // 1) PARITY — “is the number of 1-bits odd (1) or even (0)?”
    //    Several strategies (standard interview canon).
    // =========================================================================

    /** Naive parity: count bits then mod 2 (int x). */
    static int parityNaive(int x) {
        int count = 0;
        while (x != 0) {
            count += (x & 1);
            x >>>= 1;
        }
        return count & 1; // 1 if odd, 0 if even
    }

    /** Brian Kernighan parity: clear lowest set bit each loop; O(#set bits). */
    static int parityBK(int x) {
        int p = 0;
        while (x != 0) {
            p ^= 1;       // flip parity each time we remove a 1-bit
            x &= (x - 1); // drop lowest set bit
        }
        return p;
    }

    /** XOR folding parity for 32-bit: O(log word-size). */
    static int parityFold32(int x) {
        x ^= x >>> 16;
        x ^= x >>> 8;
        x ^= x >>> 4;
        x ^= x >>> 2;
        x ^= x >>> 1;
        return x & 1;
    }

    // 64-bit variants
    static int parityNaive64(long x) {
        int p = 0;
        while (x != 0) {
            p ^= (x & 1L);
            x >>>= 1;
        }
        return p;
    }
    static int parityBK64(long x) {
        int p = 0;
        while (x != 0) {
            p ^= 1;
            x &= (x - 1);
        }
        return p;
    }
    static int parityFold64(long x) {
        x ^= x >>> 32;
        x ^= x >>> 16;
        x ^= x >>> 8;
        x ^= x >>> 4;
        x ^= x >>> 2;
        x ^= x >>> 1;
        return (int)(x & 1L);
    }

    /** Byte parity lookup (useful in tight loops). */
    static final byte[] PARITY_TABLE_256 = buildParityTable256();
    private static byte[] buildParityTable256() {
        byte[] t = new byte[256];
        for (int i = 0; i < 256; i++) t[i] = (byte) (Integer.bitCount(i) & 1);
        return t;
    }
    static int parityLookup32(int x) {
        return PARITY_TABLE_256[(x >>> 24) & 0xFF]
                ^ PARITY_TABLE_256[(x >>> 16) & 0xFF]
                ^ PARITY_TABLE_256[(x >>>  8) & 0xFF]
                ^ PARITY_TABLE_256[(x       ) & 0xFF];
    }

    // =========================================================================
    // 1A) PARITY — PRACTICAL USES
    // =========================================================================

    /**
     * Attach an even-parity bit to a 7-bit payload.
     * We encode data in bits[0..6], and write the parity bit into bit7 (MSB).
     * Returns: byte where total 1s across all 8 bits is EVEN.
     * Args: data7 must be in [0, 127] (7-bit).
     */
    static byte packWithEvenParity(byte data7) {
        if ((data7 & 0x80) != 0) throw new IllegalArgumentException("data7 must be 7-bit (0..127)");
        int p = Integer.bitCount(data7) & 1; // 1 if odd, 0 if even
        int parityBit = (p == 1) ? 1 : 0;    // if odd, set MSB to 1 to make overall even
        return (byte) (data7 | (parityBit << 7));
    }

    /**
     * Check an even-parity byte (bit7 is parity). Returns true if parity is valid.
     * We expect total 1s across all 8 bits to be EVEN.
     */
    static boolean checkEvenParity(byte bWithParity) {
        return (Integer.bitCount(bWithParity & 0xFF) & 1) == 0;
    }

    /**
     * Extract 7-bit payload from an even-parity byte (does not validate).
     * Use checkEvenParity first if needed. Clears bit7.
     */
    static byte extractPayload7(byte bWithParity) {
        return (byte) (bWithParity & 0x7F);
    }

    /** Simulate single-bit error by flipping bit k (0..7). */
    static byte flipBit(byte b, int k) {
        return (byte) (b ^ (1 << k));
    }

    /**
     * XOR-style “checksum” for a packet: XOR all bytes.
     * Property: detects any single-bit error and many odd-number-of-bit flips.
     * (Classic interview exercise; parity generalizes from 1 bit to 8-bit XOR.)
     */
    static byte xorChecksum(byte[] data) {
        byte acc = 0;
        for (byte d : data) acc ^= d;
        return acc;
    }

    /**
     * Rolling parity over a stream: update in O(1) per element.
     * Maintain a single bit (0=even,1=odd) for “how many 1-bits seen so far”.
     */
    static int rollingParityUpdate(int currentParity /*0/1*/, byte next) {
        return currentParity ^ (Integer.bitCount(next & 0xFF) & 1);
    }

    /**
     * Prefix parity for subarray queries: parity[i] = parity of data[0..i-1].
     * Then parity of any range [L,R) is parity[R] XOR parity[L].
     */
    static int[] buildPrefixParity(byte[] data) {
        int[] pref = new int[data.length + 1];
        for (int i = 0; i < data.length; i++) {
            pref[i + 1] = pref[i] ^ (Integer.bitCount(data[i] & 0xFF) & 1);
        }
        return pref;
    }
    static int rangeParity(int[] pref, int L, int R) { // parity of data[L..R-1]
        return pref[R] ^ pref[L];
    }

    // =========================================================================
    // 2) SWAP BITS — swap two bit positions if they differ.
    // =========================================================================
    static int swapBits(int x, int i, int j) {
        int bi = (x >>> i) & 1;
        int bj = (x >>> j) & 1;
        if (bi != bj) x ^= (1 << i) | (1 << j); // toggle both
        return x;
    }

    // =========================================================================
    // 3) REVERSE BITS — loop, mask-hack, table (32-bit)
    // =========================================================================
    static int reverseBitsLoop32(int x) {
        for (int i = 0, j = 31; i < j; i++, j--) x = swapBits(x, i, j);
        return x;
    }
    static int reverseBitsMask32(int x) {
        x = (x >>> 1)  & 0x55555555 | (x & 0x55555555) << 1;
        x = (x >>> 2)  & 0x33333333 | (x & 0x33333333) << 2;
        x = (x >>> 4)  & 0x0F0F0F0F | (x & 0x0F0F0F0F) << 4;
        x = (x >>> 8)  & 0x00FF00FF | (x & 0x00FF00FF) << 8;
        x = (x >>> 16) | (x << 16);
        return x;
    }
    static final int[] REVERSE_BYTE = buildReverseByteTable();
    private static int[] buildReverseByteTable() {
        int[] t = new int[256];
        for (int b = 0; b < 256; b++) {
            int x = b;
            x = (x >>> 1) & 0x55 | (x & 0x55) << 1;
            x = (x >>> 2) & 0x33 | (x & 0x33) << 2;
            x = (x >>> 4) & 0x0F | (x & 0x0F) << 4;
            t[b] = x;
        }
        return t;
    }
    static int reverseBitsTable32(int x) {
        return  (REVERSE_BYTE[(x       ) & 0xFF] << 24)
                | (REVERSE_BYTE[(x >>> 8) & 0xFF] << 16)
                | (REVERSE_BYTE[(x >>>16) & 0xFF] <<  8)
                |  REVERSE_BYTE[(x >>>24) & 0xFF];
    }

    // =========================================================================
    // 4) CLOSEST INT WITH SAME BIT COUNT — swap lowest adjacent differing bits.
    // =========================================================================
    static int closestIntSameBitCount(int x) {
        for (int i = 0; i < 31; i++) {
            int bi = (x >>> i) & 1, bj = (x >>> (i + 1)) & 1;
            if (bi != bj) return x ^ ((1 << i) | (1 << (i + 1)));
        }
        throw new IllegalArgumentException("No neighbor with same popcount (all 0s or all 1s).");
    }

    // =========================================================================
    // 5) MULTIPLY & DIVIDE USING SHIFTS (integers)
    // =========================================================================
    static int multiplyShiftAdd(int a, int b) {
        if (a < 0 || b < 0) throw new IllegalArgumentException("Use non-negative for this demo");
        int res = 0;
        while (b != 0) {
            if ((b & 1) != 0) res += a;
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }
    static int divideShiftSubtract(int dividend, int divisor) {
        if (dividend < 0 || divisor <= 0) throw new IllegalArgumentException("dividend>=0, divisor>0");
        int q = 0, p = 31;
        long d = (long) divisor << p;
        long r = dividend;
        while (p >= 0) {
            if (r >= d) { r -= d; q |= (1 << p); }
            d >>>= 1; p--;
        }
        return q;
    }

    // =========================================================================
    // DEMOS
    // =========================================================================
    static void demoParityBasics() {
        System.out.println("=== PARITY (basics)");
        int[] xs = {0, 1, 0b1011, 0x7FFFFFFF, 0xFFFFFFFF};
        for (int x : xs) {
            System.out.printf("x=%s%n", Integer.toBinaryString(x));
            System.out.printf("  naive   : %d%n", parityNaive(x));
            System.out.printf("  BK      : %d%n", parityBK(x));
            System.out.printf("  fold32  : %d%n", parityFold32(x));
            System.out.printf("  lookup  : %d%n", parityLookup32(x));
        }
        long y = 0xF0F0_F0F0_0000_0001L;
        System.out.printf("long y=%s | fold64=%d%n", Long.toBinaryString(y), parityFold64(y));
    }

    static void demoParityUses() {
        System.out.println("\n=== PARITY — practical uses");

        // A) Even-parity byte (bit7 holds parity)
        byte payload = 0b010_1010; // 7-bit data (example)
        byte framed = packWithEvenParity(payload);
        System.out.printf("payload(7b)=%8s | framed=%8s | valid=%s%n",
                to8(payload), to8(framed), checkEvenParity(framed));

        // Simulate single-bit error — parity catches it
        byte corrupted = flipBit(framed, 2); // flip some bit
        System.out.printf("corrupted   =%8s | valid=%s%n", to8(corrupted), checkEvenParity(corrupted));

        // B) XOR checksum over a packet
        byte[] packet = {1, 2, 3, 4, 5};
        byte checksum = xorChecksum(packet);
        System.out.printf("packet=%s | checksum=%8s%n", Arrays.toString(packet), to8(checksum));
        // Flip a bit in packet[2] and see checksum mismatch (in real systems you'd recompute & compare)
        packet[2] ^= 0x01;
        byte checksum2 = xorChecksum(packet);
        System.out.printf("packet'=%s | checksum'=%8s (changed)%n", Arrays.toString(packet), to8(checksum2));

        // C) Rolling parity (streaming)
        int roll = 0;
        for (byte b : new byte[]{3, 7, 8, 12}) {
            roll = rollingParityUpdate(roll, b);
        }
        System.out.println("rolling parity after [3,7,8,12] = " + roll);

        // D) Prefix parity for subarray queries
        byte[] data = {3, 7, 8, 12, 15}; // any bytes
        int[] pref = buildPrefixParity(data);
        // parity of data[1..4) = indices 1,2,3
        System.out.println("range parity [1,4) = " + rangeParity(pref, 1, 4));
    }

    static void demoSwapReverseClosestMulDiv() {
        System.out.println("\n=== SWAP / REVERSE / CLOSEST / MUL-DIV");

        int x = 0b101001; // 41
        System.out.println("x           : " + Integer.toBinaryString(x));
        System.out.println("swap(0,5)   : " + Integer.toBinaryString(swapBits(x, 0, 5)));

        int revSrc = 0b00000010_00000000_00000000_00000001;
        System.out.println("reverse loop: " + Integer.toBinaryString(reverseBitsLoop32(revSrc)));
        System.out.println("reverse mask: " + Integer.toBinaryString(reverseBitsMask32(revSrc)));
        System.out.println("reverse tbl : " + Integer.toBinaryString(reverseBitsTable32(revSrc)));

        int c0 = 0b00101100; // popcount=3
        int c1 = closestIntSameBitCount(c0);
        System.out.println("closest same popcount: " + Integer.toBinaryString(c0) + " -> " + Integer.toBinaryString(c1));

        int a = 23, b = 19;
        int prod = multiplyShiftAdd(a, b);
        System.out.printf("%d * %d = %d (check=%d)%n", a, b, prod, a * b);

        int dividend = 1234, divisor = 13;
        int q = divideShiftSubtract(dividend, divisor);
        System.out.printf("%d / %d = %d (check=%d)%n", dividend, divisor, q, dividend / divisor);
    }

    // Helpers
    static String to8(byte b) {
        String s = Integer.toBinaryString(b & 0xFF);
        return "00000000".substring(s.length()) + s;
    }

    // =========================================================================
    // main — run everything
    // =========================================================================
    public static void main(String[] args) {
        demoParityBasics();
        demoParityUses();
        demoSwapReverseClosestMulDiv();
    }
}
