/*
 * Copyright (c) 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8363972
 * @summary Unit tests for lenient parsing
 * @modules jdk.localedata
 * @run junit LenientParsingTest
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LenientParsingTest {
    private static final Locale FINNISH = Locale.of("fi", "FI");
    private static final String PATTERN = "+#;-#";
    private static final DecimalFormatSymbols DFS =
        new DecimalFormatSymbols(Locale.ROOT);

    // "parseLenient" data from CLDR v47. These data are subject to change
    private static Stream<String> minusSigns() {
        return Stream.of(
            "－",    // U+FF0D Fullwidth Hyphen-Minus
            "﹣",    // U+FE63 Small Hyphen-Minus
            "‐",    // U+2010 Hyphen
            "‑",    // U+2011 Non-Breaking Hyphen
            "‒",    // U+2012 Figure Dash
            "–",    // U+2013 En Dash
            "−",    // U+2212 Minus Sign
            "⁻",    // U+207B Superscript Minus
            "₋",    // U+208B Subscript Minus
            "➖"     // U+2796 Heavy Minus Sign
        );
    }

    private static Stream<String> plusSigns() {
        return Stream.of(
            "＋",    // U+FF0B Fullwidth Plus Sign
            "﬩",    // U+FB29 Hebrew Letter Alternative Plus Sign
            "﹢",    // U+FE62 Small Plus Sign
            "⁺",    // U+207A Superscript Plus Sign
            "₊",    // U+208A Subscript Plus Sign
            "➕"     // U+2795 Heavy Plus Sign
        );
    }

    @Test
    void testFinnishMinusSign() {
        // originally reported in JDK-8189097
        // Should not throw a ParseException
        assertDoesNotThrow(() -> NumberFormat.getInstance(FINNISH).parse("-1,5"));
    }

    @Test
    void testFinnishMinusSignStrict() {
        // Should throw a ParseException
        var nf = NumberFormat.getInstance(FINNISH);
        nf.setStrict(true);
        assertThrows(ParseException.class, () -> nf.parse("-1,5"));
    }

    @ParameterizedTest
    @MethodSource("plusSigns")
    public void testLenientPlusSign(String sign) throws ParseException {
        var df = new DecimalFormat(PATTERN, DFS);
        df.setStrict(false);
        assertEquals(df.format(df.parse(sign + "1")), "+1");
    }

    @ParameterizedTest
    @MethodSource("minusSigns")
    public void testLenientMinusSign(String sign) throws ParseException {
        var df = new DecimalFormat(PATTERN, DFS);
        df.setStrict(false);
        assertEquals(df.format(df.parse(sign + "1")), "-1");
    }

    @ParameterizedTest
    @MethodSource("plusSigns")
    public void testStrictPlusSign(String sign) {
        assertThrows(ParseException.class, () -> {
            var df = new DecimalFormat(PATTERN, DFS);
            df.setStrict(true);
            df.parse(sign + "1");
        });
    }

    @ParameterizedTest
    @MethodSource("minusSigns")
    public void testStrictMinusSign(String sign) {
        assertThrows(ParseException.class, () -> {
            var df = new DecimalFormat(PATTERN, DFS);
            df.setStrict(true);
            df.parse(sign + "1");
        });
    }

    @ParameterizedTest
    @MethodSource("plusSigns")
    public void testDefaultPlusSign(String sign) throws ParseException {
        var df = new DecimalFormat(PATTERN, DFS);
        assertEquals(df.format(df.parse(sign + "1")), "+1");
    }

    @ParameterizedTest
    @MethodSource("minusSigns")
    public void testDefaultMinusSign(String sign) throws ParseException {
        var df = new DecimalFormat(PATTERN, DFS);
        assertEquals(df.format(df.parse(sign + "1")), "-1");
    }
}
