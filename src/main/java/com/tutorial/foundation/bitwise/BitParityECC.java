package com.tutorial.foundation.bitwise;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;

/**
 * BitParityECC — Parity applied, with 2-D parity & Hamming(7,4) ECC,
 * plus a fast block parity via 64-bit folding. Designed to bolt onto your
 * BitTricksAdvanced work.
 *
 * BUILD & RUN (JDK 17+):
 *   javac com/tutorial/core/bitwise/BitParityECC.java && \
 *   java com.tutorial.core.bitwise.BitParityECC
 */
public class BitParityECC {

    // ---------------------------------------------------------------------
    // UTIL: binary view helpers
    // ---------------------------------------------------------------------
    static String b8(int x){ String s=Integer.toBinaryString(x & 0xFF); return "00000000".substring(s.length())+s; }
    static String b7(int x){ String s=Integer.toBinaryString(x & 0x7F); return "0000000".substring(s.length())+s; }

    // =====================================================================
    // 1) 2-D PARITY (row + column parity)
    //
    // Idea: For a byte matrix data[R][C], compute:
    //   - rowParity[r] = parity of all bits in data[r][*] (even parity)
    //   - colParity[c] = parity of all bits in data[*][c] (even parity)
    // On read/check: recompute parity; the single row with mismatch and single
    // column with mismatch pinpoints a flipped bit at (r,c).
    //
    // This detects & LOCATES any single-bit error (one per matrix).
    // (Multiple errors are detectable but not necessarily correctable.)
    // =====================================================================

    /** Compute row parity (even parity) for a byte matrix. */
    static int[] computeRowParity(byte[][] data){
        int R = data.length, C = data[0].length;
        int[] rowP = new int[R];
        for (int r=0; r<R; r++){
            int p = 0;
            for (int c=0; c<C; c++) p ^= (Integer.bitCount(data[r][c] & 0xFF) & 1);
            rowP[r] = p; // 0=even, 1=odd
        }
        return rowP;
    }

    /** Compute column parity (even parity) for a byte matrix. */
    static int[] computeColParity(byte[][] data){
        int R = data.length, C = data[0].length;
        int[] colP = new int[C];
        for (int c=0; c<C; c++){
            int p = 0;
            for (int r=0; r<R; r++) p ^= (Integer.bitCount(data[r][c] & 0xFF) & 1);
            colP[c] = p;
        }
        return colP;
    }

    /** Flip a single bit in data[r][c] at bit index k (0..7). */
    static void flipBit(byte[][] data, int r, int c, int k){
        data[r][c] ^= (1 << k);
    }

    /**
     * Given original parity (rowP0/colP0) and current data, find the unique
     * row/column with mismatch. Returns {row, col} or {-1,-1} if none.
     * If exactly one row & one column mismatch, that’s your error location.
     */
    static int[] locateSingleBitError(byte[][] data, int[] rowP0, int[] colP0){
        int[] rowP = computeRowParity(data);
        int[] colP = computeColParity(data);
        int badR = -1, badC = -1;

        for (int r=0; r<rowP.length; r++) if (rowP[r] != rowP0[r]) { badR = r; break; }
        for (int c=0; c<colP.length; c++) if (colP[c] != colP0[c]) { badC = c; break; }
        return new int[]{badR, badC};
    }

    static void demo2DParity(){
        System.out.println("=== 2-D PARITY (row+col) ===");
        // Build a small 3x4 byte matrix
        byte[][] data = {
                { 3,  7,  8, 12},
                {15,  0,  1,  2},
                { 9, 10, 11,  4}
        };
        int[] rowP = computeRowParity(data);
        int[] colP = computeColParity(data);
        System.out.println("row parity = " + Arrays.toString(rowP));
        System.out.println("col parity = " + Arrays.toString(colP));

        // Flip one bit (simulate single-bit error)
        flipBit(data, 1, 2, 0); // flip LSB at row=1, col=2
        System.out.println("Corrupted cell[1][2] now = " + b8(data[1][2]));

        // Locate it
        int[] pos = locateSingleBitError(data, rowP, colP);
        System.out.println("Located error at row=" + pos[0] + ", col=" + pos[1]);
        // Correct by flipping that bit back if you track which bit inside the byte;
        // 2-D parity pins the BYTE cell; to pinpoint the bit within the byte, you’d
        // carry per-bit-plane parity (8 planes) or use a stronger code like Hamming.
    }

    // =====================================================================
    // 2) HAMMING (7,4) — Single-bit error correction, double-bit detection.
    //
    // Layout (1-based bit positions):
    //   Positions: 1 2 3 4 5 6 7
    //   Parity:    p1 p2 d1 p4 d2 d3 d4
    //   p1 covers bits {1,3,5,7}
    //   p2 covers bits {2,3,6,7}
    //   p4 covers bits {4,5,6,7}
    //
    // Encode: set p1,p2,p4 so that each of their covered sets has EVEN parity.
    // Decode: recompute syndrome s = (p1',p2',p4') — nonzero s points to bad bit.
    // =====================================================================

    /** Get bit at 1-based position pos from low 7 bits of code. */
    static int getBit7(int code, int pos){ return (code >>> (pos-1)) & 1; }
    /** Set bit at 1-based position pos to v (0/1) in code’s low 7 bits. */
    static int setBit7(int code, int pos, int v){
        int mask = 1 << (pos-1);
        return (v==1) ? (code | mask) : (code & ~mask);
    }

    /**
     * Encode a 4-bit nibble (d1..d4 in bits [0..3]) into Hamming(7,4).
     * Args: nibble in [0..15], bits b0..b3 correspond to d1..d4.
     * Returns: 7-bit code in low bits of int.
     */
    static int hamming74Encode(int nibble){
        int d1 = (nibble >>> 0) & 1;
        int d2 = (nibble >>> 1) & 1;
        int d3 = (nibble >>> 2) & 1;
        int d4 = (nibble >>> 3) & 1;
        int code = 0;
        code = setBit7(code, 3, d1);
        code = setBit7(code, 5, d2);
        code = setBit7(code, 6, d3);
        code = setBit7(code, 7, d4);

        int p1 = (getBit7(code,1) ^ getBit7(code,3) ^ getBit7(code,5) ^ getBit7(code,7)) & 1; // but bit1 not set yet
        int p2 = (getBit7(code,2) ^ getBit7(code,3) ^ getBit7(code,6) ^ getBit7(code,7)) & 1;
        int p4 = (getBit7(code,4) ^ getBit7(code,5) ^ getBit7(code,6) ^ getBit7(code,7)) & 1;

        // We want EVEN parity → parity of set must be 0 ⇒ set pi to make set even.
        // Since bits at 1,2,4 are themselves parity bits, recompute cleanly:
        p1 = (d1 ^ d2 ^ d4) & 1;           // covers 1,3,5,7 → actual data there: d1 (pos3), d2(pos5), d4(pos7)
        p2 = (d1 ^ d3 ^ d4) & 1;           // covers 2,3,6,7 → d1(pos3), d3(pos6), d4(pos7)
        p4 = (d2 ^ d3 ^ d4) & 1;           // covers 4,5,6,7 → d2(pos5), d3(pos6), d4(pos7)

        code = setBit7(code, 1, p1);
        code = setBit7(code, 2, p2);
        code = setBit7(code, 4, p4);
        return code & 0x7F;
    }

    /**
     * Decode/correct Hamming(7,4).
     * Returns {correctedCode7, dataNibble(0..15), corrected(0/1), syndrome(0..7)}.
     * corrected=1 if a single-bit flip was detected and fixed.
     */
    static int[] hamming74Decode(int code7){
        code7 &= 0x7F;
        int p1 = getBit7(code7,1);
        int p2 = getBit7(code7,2);
        int p4 = getBit7(code7,4);
        int d1 = getBit7(code7,3);
        int d2 = getBit7(code7,5);
        int d3 = getBit7(code7,6);
        int d4 = getBit7(code7,7);

        // Recompute parity (even). Syndrome bits s1,s2,s4 indicate which position is wrong.
        int s1 = (p1 ^ d1 ^ d2 ^ d4) & 1;          // covers 1,3,5,7
        int s2 = (p2 ^ d1 ^ d3 ^ d4) & 1;          // covers 2,3,6,7
        int s4 = (p4 ^ d2 ^ d3 ^ d4) & 1;          // covers 4,5,6,7
        int syndrome = (s4 << 2) | (s2 << 1) | s1; // 3-bit index (1..7) if single-bit error

        int corrected = 0;
        if (syndrome != 0){
            corrected = 1;
            code7 ^= (1 << (syndrome - 1)); // flip the bad bit
            // refresh data bits after correction
            d1 = getBit7(code7,3);
            d2 = getBit7(code7,5);
            d3 = getBit7(code7,6);
            d4 = getBit7(code7,7);
        }

        int nibble = (d4 << 3) | (d3 << 2) | (d2 << 1) | d1;
        return new int[]{code7, nibble, corrected, syndrome};
    }

    static void demoHamming74(){
        System.out.println("\n=== HAMMING (7,4) ECC ===");
        int nibble = 0b1011; // data: d4..d1 = 1 0 1 1
        int enc = hamming74Encode(nibble);
        System.out.println("nibble=" + Integer.toBinaryString(nibble) + " -> code7=" + b7(enc));

        // Flip one bit (simulate single-bit error)
        int corrupted = enc ^ (1 << 5); // flip position 6 (0-based index 5)
        System.out.println("corrupted code7=" + b7(corrupted));

        int[] dec = hamming74Decode(corrupted);
        System.out.println("decoded   code7=" + b7(dec[0])
                + " data=" + Integer.toBinaryString(dec[1])
                + " corrected=" + dec[2] + " syndrome=" + dec[3]);
    }

    // =====================================================================
    // 3) FAST BLOCK PARITY (64-bit fold) — “SIMD-ish” by chunking.
    //
    // Strategy: XOR-reduce the byte array in 64-bit chunks, then take popcount&1.
    // This is essentially parity across the WHOLE block (even/odd # of 1-bits).
    // For very large buffers, this is far faster than bit-by-bit loops.
    // =====================================================================

    /** Return 0 for even parity, 1 for odd parity across all bits of data[]. */
    static int parityBlock(byte[] data){
        // Align on 8-byte boundaries and XOR-reduce
        int n = data.length;
        int i = 0;

        long acc64 = 0L;

        // Process as many full 8-byte chunks as possible using LongBuffer
        int fullBytes = (n / 8) * 8;
        LongBuffer lb = ByteBuffer.wrap(data, 0, fullBytes).asLongBuffer();
        while (lb.hasRemaining()) acc64 ^= lb.get();

        // Tail bytes
        long tail = 0L;
        for (i = fullBytes; i < n; i++){
            tail ^= (long)(data[i] & 0xFFL) << ((i - fullBytes) * 8);
        }
        acc64 ^= tail;

        // Parity of acc64 (popcount % 2)
        return (Long.bitCount(acc64) & 1);
    }

    static void demoBlockParity(){
        System.out.println("\n=== BLOCK PARITY (64-bit fold) ===");
        byte[] big = new byte[33];
        for (int i=0;i<big.length;i++) big[i] = (byte) i; // sample data
        int p0 = parityBlock(big);
        System.out.println("parity(big) = " + p0);
        // Flip a random bit to change parity
        big[10] ^= 0x01;
        int p1 = parityBlock(big);
        System.out.println("parity(big') = " + p1 + " (changed)");
    }

    // =====================================================================
    // main — run demos
    // =====================================================================
    public static void main(String[] args) {
        demo2DParity();
        demoHamming74();
        demoBlockParity();
    }
}
