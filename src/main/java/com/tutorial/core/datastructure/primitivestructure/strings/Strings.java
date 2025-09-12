package com.tutorial.core.datastructure.primitivestructure.strings;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Java Primitive Structures: Strings — full tour (beginner → advanced).
 *
 * Key ideas:
 *  • Strings are immutable, pooled (interned), and value-based (equals/hashCode by content).
 *  • Many APIs operate on UTF-16 code units (char), not full Unicode code points.
 *  • For heavy edits, use StringBuilder; for structured joining, use StringJoiner / Collectors.joining.
 *  • Regex, formatting, and normalization add power (and footguns).
 *
 * Build & run (JDK 17+ recommended):
 *   javac com/tutorial/foundation/StringsTour.java && java com.tutorial.foundation.StringsTour
 */
public class Strings {

    // -------------------------------------------------------------------------
    // 1) Creation & string pool (interning by default for literals)
    //    WHEN TO USE:
    //      - Rely on equals() for value equality; use == ONLY to discuss identity/pool behavior.
    //      - .intern() can save memory for MANY duplicates, but costs CPU; measure before using.
    // -------------------------------------------------------------------------
    static void creationAndPooling() {
        System.out.println("=== CREATION & POOLING");

        java.lang.String a = "hello";              // literal → pooled
        java.lang.String b = "hello";              // same pooled object
        java.lang.String c = new java.lang.String("hello");  // new heap object (not pooled)
        java.lang.String d = c.intern();           // ask JVM to return pooled instance

        System.out.println("a==b? " + (a == b));   // true (same pool entry)
        System.out.println("a==c? " + (a == c));   // false
        System.out.println("a==d? " + (a == d));   // true after intern()
        System.out.println("a.equals(c)? " + a.equals(c)); // ALWAYS use equals() for content
    }

    // -------------------------------------------------------------------------
    // 2) Immutability & concatenation
    //    WHEN TO USE:
    //      - Prefer + for one-off small concatenations (compiler may optimize).
    //      - In loops or many appends, use StringBuilder to avoid many temp objects.
    // -------------------------------------------------------------------------
    static void immutabilityAndConcat() {
        System.out.println("\n=== IMMUTABILITY & CONCAT");

        String base = "world";
        String upper = base.toUpperCase();   // new string; base unchanged
        String concat = base + " peace";     // new string
        System.out.println("base=" + base + " | upper=" + upper + " | concat=" + concat);

        // Loop concatenation anti-pattern (creates many temporaries):
        int n = 5;
        String bad = "";
        for (int i = 0; i < n; i++) {
            bad += i; // Avoid in hot loops
        }
        System.out.println("bad loop concat = " + bad);

        // Preferred pattern:
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(i);
        System.out.println("builder concat  = " + sb);
    }

    // -------------------------------------------------------------------------
    // 3) Equality & hashing (value-based)
    //    WHEN TO USE:
    //      - Use equals() for content, never == (except to reason about pool identity).
    //      - Strings are excellent map keys due to immutability + cached hashCode.
    // -------------------------------------------------------------------------
    static void equalityAndHashing() {
        System.out.println("\n=== EQUALITY & HASHING");

        String s1 = "java";
        String s2 = "java";
        String s3 = new String("java");

        System.out.println("s1==s2? " + (s1 == s2));         // true (pooled)
        System.out.println("s1==s3? " + (s1 == s3));         // false
        System.out.println("s1.equals(s3)? " + s1.equals(s3)); // true

        Map<String, Integer> m = new HashMap<>();
        m.put("alpha", 1); m.put("beta", 2);
        System.out.println("hash lookup alpha→ " + m.get("alpha")); // fast & stable
    }

    // -------------------------------------------------------------------------
    // 4) Common operations
    //    WHEN TO USE:
    //      - Everyday slicing/searching. Note that charAt/substring work in UTF-16 code units.
    // -------------------------------------------------------------------------
    static void commonOps() {
        System.out.println("\n=== COMMON OPS");

        String text = "Ada Lovelace, First Programmer";
        System.out.println("length=" + text.length());
        System.out.println("charAt(4)=" + text.charAt(4));            // 'o'
        System.out.println("substring(0,3)=" + text.substring(0,3));  // "Ada"
        System.out.println("indexOf('First')=" + text.indexOf("First"));

        String[] parts = text.split(", ");  // regex-based split
        System.out.println("split -> " + Arrays.toString(parts));

        String joined = String.join(" | ", "red", "green", "blue");
        System.out.println("join  -> " + joined);
    }

    // -------------------------------------------------------------------------
    // 5) Unicode: code points vs char (surrogate pairs)
    //    BACKGROUND:
    //      - char is a 16-bit UTF-16 code unit. Some characters need TWO chars (surrogate pair).
    //      - Many String methods (charAt, length) report/code-unit info, not Unicode scalars.
    //    WHEN TO USE:
    //      - For emoji/astral symbols, iterate by code points to avoid splitting a surrogate pair.
    // -------------------------------------------------------------------------
    static void unicodeCodePoints() {
        System.out.println("\n=== UNICODE: CODE POINTS");

        String emoji = "😀";                   // U+1F600 (requires surrogate pair)
        System.out.println("length (char units) = " + emoji.length());             // 2
        System.out.println("codePointCount      = " + emoji.codePointCount(0, emoji.length())); // 1

        // Safe iteration by code point:
        System.out.print("code points hex      = ");
        emoji.codePoints().forEach(cp -> System.out.print(Integer.toHexString(cp).toUpperCase() + " "));
        System.out.println();

        // Pitfall: charAt(0) returns only the high surrogate, not the full symbol.
        char high = emoji.charAt(0);
        System.out.println("charAt(0) is surrogate? " + Character.isHighSurrogate(high));
    }

    // -------------------------------------------------------------------------
    // 6) Unicode normalization (NFC/NFD)
    //    BACKGROUND:
    //      - Some characters can be represented as one code point or as base+combining marks.
    //      - For reliable equality/searching, normalize both sides to same form (NFC common).
    //    WHEN TO USE:
    //      - Comparing text from different sources, filenames, user input, etc.
    // -------------------------------------------------------------------------
    static void unicodeNormalization() {
        System.out.println("\n=== UNICODE NORMALIZATION");

        String composed = "é";                       // U+00E9
        String decomposed = "e\u0301";               // U+0065 + U+0301 (combining acute)
        System.out.println("equals raw? " + composed.equals(decomposed)); // false

        String nfc1 = Normalizer.normalize(composed, Normalizer.Form.NFC);
        String nfc2 = Normalizer.normalize(decomposed, Normalizer.Form.NFC);
        System.out.println("equals NFC? " + nfc1.equals(nfc2));          // true
    }

    // -------------------------------------------------------------------------
    // 7) Regex: match, groups, replaceAll
    //    WHEN TO USE:
    //      - Pattern-based validation/extraction/transforms.
    //      - Use precompiled Pattern for repeated use in hot paths.
    // -------------------------------------------------------------------------
    static void regexBasics() {
        System.out.println("\n=== REGEX");

        String log = "user=ada id=42 email=ada@example.com";
        Pattern p = Pattern.compile("user=(\\w+)\\s+id=(\\d+)\\s+email=([^\\s]+)");
        Matcher m = p.matcher(log);
        if (m.find()) {
            System.out.println("user=" + m.group(1) + " id=" + m.group(2) + " email=" + m.group(3));
        }

        // replaceAll with regex capture groups:
        String masked = log.replaceAll("(email=)[^\\s]+", "$1***@***");
        System.out.println("masked -> " + masked);

        // split by non-word:
        System.out.println("split non-word -> " + Arrays.toString("a,b;c|d".split("\\W")));
    }

    // -------------------------------------------------------------------------
    // 8) Formatting & joining
    //    WHEN TO USE:
    //      - String.format for classic printf-style formatting.
    //      - StringJoiner / Collectors.joining for structured assembly.
    // -------------------------------------------------------------------------
    static void formattingAndJoining() {
        System.out.println("\n=== FORMATTING & JOINING");

        String f = String.format("pi ≈ %.3f, hex=0x%X", Math.PI, 3735928559L);
        System.out.println("format -> " + f);

        StringJoiner sj = new StringJoiner(", ", "[", "]");
        sj.add("alpha").add("beta").add("gamma");
        System.out.println("StringJoiner -> " + sj);

        List<Integer> nums = List.of(1,2,3,4);
        String csv = nums.stream().map(Object::toString).collect(Collectors.joining(","));
        System.out.println("Collectors.joining -> " + csv);
    }

    // -------------------------------------------------------------------------
    // 9) Performance patterns
    //    WHEN TO USE:
    //      - Use StringBuilder for many appends (single-thread).
    //      - Use StringBuffer for legacy/when you need synchronization (rare; prefer external locks).
    //      - For many equal strings, intern() may reduce memory; benchmark first.
    // -------------------------------------------------------------------------
    static void performancePatterns() {
        System.out.println("\n=== PERFORMANCE PATTERNS");

        // StringBuilder vs StringBuffer:
        StringBuilder sb = new StringBuilder();
        sb.append("fast").append('-').append("single-thread");
        System.out.println("StringBuilder -> " + sb);

        StringBuffer sync = new StringBuffer();
        sync.append("thread-safe").append('-').append("legacy");
        System.out.println("StringBuffer  -> " + sync);

        // NOTE: Since Java 9, Strings use "compact strings" internally:
        // - If all chars fit Latin-1, store bytes + coder flag; otherwise UTF-16.
        // - You don't control this, but it improves memory/cache behavior automatically.
    }

    // -------------------------------------------------------------------------
    // 10) Edge cases & utilities
    //     WHEN TO USE:
    //       - Defensive checks, safe substring boundaries, null handling patterns.
    // -------------------------------------------------------------------------
    static void edgeCasesAndUtilities() {
        System.out.println("\n=== EDGE CASES & UTILITIES");

        String maybeNull = null;
        // Safe compare: Objects.equals handles nulls
        System.out.println("Objects.equals(null, \"x\") = " + Objects.equals(maybeNull, "x"));

        // Bound-checked slicing helper
        String s = "abcdef";
        System.out.println("safeSlice(1,4) -> " + safeSlice(s, 1, 4));
        System.out.println("safeSlice(4,99) -> " + safeSlice(s, 4, 99)); // clamps to length
    }

    private static String safeSlice(String s, int start, int end) {
        if (s == null) return null;
        start = Math.max(0, Math.min(start, s.length()));
        end   = Math.max(start, Math.min(end, s.length()));
        return s.substring(start, end);
    }

    // -------------------------------------------------------------------------
    // main — run all demos
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        creationAndPooling();
        immutabilityAndConcat();
        equalityAndHashing();
        commonOps();
        unicodeCodePoints();
        unicodeNormalization();
        regexBasics();
        formattingAndJoining();
        performancePatterns();
        edgeCasesAndUtilities();
    }
}
