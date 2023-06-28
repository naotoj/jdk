/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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
package org.openjdk.bench.java.util;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.text.DateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/*
 * This benchmark tests caching of Locale objects works
 * correctly without the cache in underlying BaseLocale class
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 4, time = 2)
@Measurement(iterations = 4, time = 2)
@Fork(value = 5)
public class LocaleCache {
    @Benchmark
    public void testForLanguageTag() {
        Locale previous = null;
        for (int count = 100; count > 0; count--) {
            var l = Locale.forLanguageTag("foo");
            if (previous != null && previous != l) {
                throw new RuntimeException("Different Locale was created");
            }
            previous =  l;
        }
    }

    @Benchmark
    public void testLocaleOf() {
        Locale previous = null;
        for (int count = 100; count > 0; count--) {
            var l = Locale.of("foo");
            if (previous != null && previous != l) {
                throw new RuntimeException("Different Locale was created");
            }
            previous =  l;
        }
    }
}
