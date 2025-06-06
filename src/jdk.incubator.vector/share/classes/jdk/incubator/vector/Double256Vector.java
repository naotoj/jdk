/*
 * Copyright (c) 2017, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package jdk.incubator.vector;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

import jdk.internal.vm.annotation.ForceInline;
import jdk.internal.vm.vector.VectorSupport;

import static jdk.internal.vm.vector.VectorSupport.*;

import static jdk.incubator.vector.VectorOperators.*;

// -- This file was mechanically generated: Do not edit! -- //

@SuppressWarnings("cast")  // warning: redundant cast
final class Double256Vector extends DoubleVector {
    static final DoubleSpecies VSPECIES =
        (DoubleSpecies) DoubleVector.SPECIES_256;

    static final VectorShape VSHAPE =
        VSPECIES.vectorShape();

    static final Class<Double256Vector> VCLASS = Double256Vector.class;

    static final int VSIZE = VSPECIES.vectorBitSize();

    static final int VLENGTH = VSPECIES.laneCount(); // used by the JVM

    static final Class<Double> ETYPE = double.class; // used by the JVM

    Double256Vector(double[] v) {
        super(v);
    }

    // For compatibility as Double256Vector::new,
    // stored into species.vectorFactory.
    Double256Vector(Object v) {
        this((double[]) v);
    }

    static final Double256Vector ZERO = new Double256Vector(new double[VLENGTH]);
    static final Double256Vector IOTA = new Double256Vector(VSPECIES.iotaArray());

    static {
        // Warm up a few species caches.
        // If we do this too much we will
        // get NPEs from bootstrap circularity.
        VSPECIES.dummyVector();
        VSPECIES.withLanes(LaneType.BYTE);
    }

    // Specialized extractors

    @ForceInline
    final @Override
    public DoubleSpecies vspecies() {
        // ISSUE:  This should probably be a @Stable
        // field inside AbstractVector, rather than
        // a megamorphic method.
        return VSPECIES;
    }

    @ForceInline
    @Override
    public final Class<Double> elementType() { return double.class; }

    @ForceInline
    @Override
    public final int elementSize() { return Double.SIZE; }

    @ForceInline
    @Override
    public final VectorShape shape() { return VSHAPE; }

    @ForceInline
    @Override
    public final int length() { return VLENGTH; }

    @ForceInline
    @Override
    public final int bitSize() { return VSIZE; }

    @ForceInline
    @Override
    public final int byteSize() { return VSIZE / Byte.SIZE; }

    /*package-private*/
    @ForceInline
    final @Override
    double[] vec() {
        return (double[])getPayload();
    }

    // Virtualized constructors

    @Override
    @ForceInline
    public final Double256Vector broadcast(double e) {
        return (Double256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Vector broadcast(long e) {
        return (Double256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    Double256Mask maskFromArray(boolean[] bits) {
        return new Double256Mask(bits);
    }

    @Override
    @ForceInline
    Double256Shuffle iotaShuffle() { return Double256Shuffle.IOTA; }

    @Override
    @ForceInline
    Double256Shuffle iotaShuffle(int start, int step, boolean wrap) {
        return (Double256Shuffle) iotaShuffleTemplate(start, step, wrap);
    }

    @Override
    @ForceInline
    Double256Shuffle shuffleFromArray(int[] indices, int i) { return new Double256Shuffle(indices, i); }

    @Override
    @ForceInline
    Double256Shuffle shuffleFromOp(IntUnaryOperator fn) { return new Double256Shuffle(fn); }

    // Make a vector of the same species but the given elements:
    @ForceInline
    final @Override
    Double256Vector vectorFactory(double[] vec) {
        return new Double256Vector(vec);
    }

    @ForceInline
    final @Override
    Byte256Vector asByteVectorRaw() {
        return (Byte256Vector) super.asByteVectorRawTemplate();  // specialize
    }

    @ForceInline
    final @Override
    AbstractVector<?> asVectorRaw(LaneType laneType) {
        return super.asVectorRawTemplate(laneType);  // specialize
    }

    // Unary operator

    @ForceInline
    final @Override
    Double256Vector uOp(FUnOp f) {
        return (Double256Vector) super.uOpTemplate(f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector uOp(VectorMask<Double> m, FUnOp f) {
        return (Double256Vector)
            super.uOpTemplate((Double256Mask)m, f);  // specialize
    }

    // Binary operator

    @ForceInline
    final @Override
    Double256Vector bOp(Vector<Double> v, FBinOp f) {
        return (Double256Vector) super.bOpTemplate((Double256Vector)v, f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector bOp(Vector<Double> v,
                     VectorMask<Double> m, FBinOp f) {
        return (Double256Vector)
            super.bOpTemplate((Double256Vector)v, (Double256Mask)m,
                              f);  // specialize
    }

    // Ternary operator

    @ForceInline
    final @Override
    Double256Vector tOp(Vector<Double> v1, Vector<Double> v2, FTriOp f) {
        return (Double256Vector)
            super.tOpTemplate((Double256Vector)v1, (Double256Vector)v2,
                              f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector tOp(Vector<Double> v1, Vector<Double> v2,
                     VectorMask<Double> m, FTriOp f) {
        return (Double256Vector)
            super.tOpTemplate((Double256Vector)v1, (Double256Vector)v2,
                              (Double256Mask)m, f);  // specialize
    }

    @ForceInline
    final @Override
    double rOp(double v, VectorMask<Double> m, FBinOp f) {
        return super.rOpTemplate(v, m, f);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> convertShape(VectorOperators.Conversion<Double,F> conv,
                           VectorSpecies<F> rsp, int part) {
        return super.convertShapeTemplate(conv, rsp, part);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> reinterpretShape(VectorSpecies<F> toSpecies, int part) {
        return super.reinterpretShapeTemplate(toSpecies, part);  // specialize
    }

    // Specialized algebraic operations:

    // The following definition forces a specialized version of this
    // crucial method into the v-table of this class.  A call to add()
    // will inline to a call to lanewise(ADD,), at which point the JIT
    // intrinsic will have the opcode of ADD, plus all the metadata
    // for this particular class, enabling it to generate precise
    // code.
    //
    // There is probably no benefit to the JIT to specialize the
    // masked or broadcast versions of the lanewise method.

    @Override
    @ForceInline
    public Double256Vector lanewise(Unary op) {
        return (Double256Vector) super.lanewiseTemplate(op);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Unary op, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Binary op, Vector<Double> v) {
        return (Double256Vector) super.lanewiseTemplate(op, v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Binary op, Vector<Double> v, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, v, (Double256Mask) m);  // specialize
    }


    /*package-private*/
    @Override
    @ForceInline
    public final
    Double256Vector
    lanewise(Ternary op, Vector<Double> v1, Vector<Double> v2) {
        return (Double256Vector) super.lanewiseTemplate(op, v1, v2);  // specialize
    }

    @Override
    @ForceInline
    public final
    Double256Vector
    lanewise(Ternary op, Vector<Double> v1, Vector<Double> v2, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, v1, v2, (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public final
    Double256Vector addIndex(int scale) {
        return (Double256Vector) super.addIndexTemplate(scale);  // specialize
    }

    // Type specific horizontal reductions

    @Override
    @ForceInline
    public final double reduceLanes(VectorOperators.Associative op) {
        return super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final double reduceLanes(VectorOperators.Associative op,
                                    VectorMask<Double> m) {
        return super.reduceLanesTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op) {
        return (long) super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op,
                                        VectorMask<Double> m) {
        return (long) super.reduceLanesTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialized
    }

    @Override
    @ForceInline
    final <F> VectorShuffle<F> bitsToShuffle(AbstractSpecies<F> dsp) {
        throw new AssertionError();
    }

    @Override
    @ForceInline
    public final Double256Shuffle toShuffle() {
        return (Double256Shuffle) toShuffle(vspecies(), false);
    }

    // Specialized unary testing

    @Override
    @ForceInline
    public final Double256Mask test(Test op) {
        return super.testTemplate(Double256Mask.class, op);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask test(Test op, VectorMask<Double> m) {
        return super.testTemplate(Double256Mask.class, op, (Double256Mask) m);  // specialize
    }

    // Specialized comparisons

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, Vector<Double> v) {
        return super.compareTemplate(Double256Mask.class, op, v);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, double s) {
        return super.compareTemplate(Double256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, long s) {
        return super.compareTemplate(Double256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, Vector<Double> v, VectorMask<Double> m) {
        return super.compareTemplate(Double256Mask.class, op, v, (Double256Mask) m);
    }


    @Override
    @ForceInline
    public Double256Vector blend(Vector<Double> v, VectorMask<Double> m) {
        return (Double256Vector)
            super.blendTemplate(Double256Mask.class,
                                (Double256Vector) v,
                                (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector slice(int origin, Vector<Double> v) {
        return (Double256Vector) super.sliceTemplate(origin, v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector slice(int origin) {
        return (Double256Vector) super.sliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin, Vector<Double> w, int part) {
        return (Double256Vector) super.unsliceTemplate(origin, w, part);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin, Vector<Double> w, int part, VectorMask<Double> m) {
        return (Double256Vector)
            super.unsliceTemplate(Double256Mask.class,
                                  origin, w, part,
                                  (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin) {
        return (Double256Vector) super.unsliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> s) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    (Double256Shuffle) s);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> shuffle,
                                  VectorMask<Double> m) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    Double256Mask.class,
                                    (Double256Shuffle) shuffle,
                                    (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> s,
                                  Vector<Double> v) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    (Double256Shuffle) s,
                                    (Double256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector compress(VectorMask<Double> m) {
        return (Double256Vector)
            super.compressTemplate(Double256Mask.class,
                                   (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector expand(VectorMask<Double> m) {
        return (Double256Vector)
            super.expandTemplate(Double256Mask.class,
                                   (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector selectFrom(Vector<Double> v) {
        return (Double256Vector)
            super.selectFromTemplate((Double256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector selectFrom(Vector<Double> v,
                                   VectorMask<Double> m) {
        return (Double256Vector)
            super.selectFromTemplate((Double256Vector) v,
                                     Double256Mask.class, (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector selectFrom(Vector<Double> v1,
                                   Vector<Double> v2) {
        return (Double256Vector)
            super.selectFromTemplate((Double256Vector) v1, (Double256Vector) v2);  // specialize
    }

    @ForceInline
    @Override
    public double lane(int i) {
        long bits;
        switch(i) {
            case 0: bits = laneHelper(0); break;
            case 1: bits = laneHelper(1); break;
            case 2: bits = laneHelper(2); break;
            case 3: bits = laneHelper(3); break;
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
        return Double.longBitsToDouble(bits);
    }

    @ForceInline
    public long laneHelper(int i) {
        return (long) VectorSupport.extract(
                     VCLASS, ETYPE, VLENGTH,
                     this, i,
                     (vec, ix) -> {
                     double[] vecarr = vec.vec();
                     return (long)Double.doubleToRawLongBits(vecarr[ix]);
                     });
    }

    @ForceInline
    @Override
    public Double256Vector withLane(int i, double e) {
        switch(i) {
            case 0: return withLaneHelper(0, e);
            case 1: return withLaneHelper(1, e);
            case 2: return withLaneHelper(2, e);
            case 3: return withLaneHelper(3, e);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    @ForceInline
    public Double256Vector withLaneHelper(int i, double e) {
        return VectorSupport.insert(
                                VCLASS, ETYPE, VLENGTH,
                                this, i, (long)Double.doubleToRawLongBits(e),
                                (v, ix, bits) -> {
                                    double[] res = v.vec().clone();
                                    res[ix] = Double.longBitsToDouble((long)bits);
                                    return v.vectorFactory(res);
                                });
    }

    // Mask

    static final class Double256Mask extends AbstractMask<Double> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Double> ETYPE = double.class; // used by the JVM

        Double256Mask(boolean[] bits) {
            this(bits, 0);
        }

        Double256Mask(boolean[] bits, int offset) {
            super(prepare(bits, offset));
        }

        Double256Mask(boolean val) {
            super(prepare(val));
        }

        private static boolean[] prepare(boolean[] bits, int offset) {
            boolean[] newBits = new boolean[VSPECIES.laneCount()];
            for (int i = 0; i < newBits.length; i++) {
                newBits[i] = bits[offset + i];
            }
            return newBits;
        }

        private static boolean[] prepare(boolean val) {
            boolean[] bits = new boolean[VSPECIES.laneCount()];
            Arrays.fill(bits, val);
            return bits;
        }

        @ForceInline
        final @Override
        public DoubleSpecies vspecies() {
            // ISSUE:  This should probably be a @Stable
            // field inside AbstractMask, rather than
            // a megamorphic method.
            return VSPECIES;
        }

        @ForceInline
        boolean[] getBits() {
            return (boolean[])getPayload();
        }

        @Override
        Double256Mask uOp(MUnOp f) {
            boolean[] res = new boolean[vspecies().laneCount()];
            boolean[] bits = getBits();
            for (int i = 0; i < res.length; i++) {
                res[i] = f.apply(i, bits[i]);
            }
            return new Double256Mask(res);
        }

        @Override
        Double256Mask bOp(VectorMask<Double> m, MBinOp f) {
            boolean[] res = new boolean[vspecies().laneCount()];
            boolean[] bits = getBits();
            boolean[] mbits = ((Double256Mask)m).getBits();
            for (int i = 0; i < res.length; i++) {
                res[i] = f.apply(i, bits[i], mbits[i]);
            }
            return new Double256Mask(res);
        }

        @ForceInline
        @Override
        public final
        Double256Vector toVector() {
            return (Double256Vector) super.toVectorTemplate();  // specialize
        }

        /**
         * Helper function for lane-wise mask conversions.
         * This function kicks in after intrinsic failure.
         */
        @ForceInline
        private final <E>
        VectorMask<E> defaultMaskCast(AbstractSpecies<E> dsp) {
            if (length() != dsp.laneCount())
                throw new IllegalArgumentException("VectorMask length and species length differ");
            boolean[] maskArray = toArray();
            return  dsp.maskFactory(maskArray).check(dsp);
        }

        @Override
        @ForceInline
        public <E> VectorMask<E> cast(VectorSpecies<E> dsp) {
            AbstractSpecies<E> species = (AbstractSpecies<E>) dsp;
            if (length() != species.laneCount())
                throw new IllegalArgumentException("VectorMask length and species length differ");

            return VectorSupport.convert(VectorSupport.VECTOR_OP_CAST,
                this.getClass(), ETYPE, VLENGTH,
                species.maskType(), species.elementType(), VLENGTH,
                this, species,
                (m, s) -> s.maskFactory(m.toArray()).check(s));
        }

        @Override
        @ForceInline
        /*package-private*/
        Double256Mask indexPartiallyInUpperRange(long offset, long limit) {
            return (Double256Mask) VectorSupport.indexPartiallyInUpperRange(
                Double256Mask.class, double.class, VLENGTH, offset, limit,
                (o, l) -> (Double256Mask) TRUE_MASK.indexPartiallyInRange(o, l));
        }

        // Unary operations

        @Override
        @ForceInline
        public Double256Mask not() {
            return xor(maskAll(true));
        }

        @Override
        @ForceInline
        public Double256Mask compress() {
            return (Double256Mask)VectorSupport.compressExpandOp(VectorSupport.VECTOR_OP_MASK_COMPRESS,
                Double256Vector.class, Double256Mask.class, ETYPE, VLENGTH, null, this,
                (v1, m1) -> VSPECIES.iota().compare(VectorOperators.LT, m1.trueCount()));
        }


        // Binary operations

        @Override
        @ForceInline
        public Double256Mask and(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_AND, Double256Mask.class, null, long.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a & b));
        }

        @Override
        @ForceInline
        public Double256Mask or(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_OR, Double256Mask.class, null, long.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a | b));
        }

        @Override
        @ForceInline
        public Double256Mask xor(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_XOR, Double256Mask.class, null, long.class, VLENGTH,
                                          this, m, null,
                                          (m1, m2, vm) -> m1.bOp(m2, (i, a, b) -> a ^ b));
        }

        // Mask Query operations

        @Override
        @ForceInline
        public int trueCount() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TRUECOUNT, Double256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> trueCountHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public int firstTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_FIRSTTRUE, Double256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> firstTrueHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public int lastTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_LASTTRUE, Double256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> lastTrueHelper(m.getBits()));
        }

        @Override
        @ForceInline
        public long toLong() {
            if (length() > Long.SIZE) {
                throw new UnsupportedOperationException("too many lanes for one long");
            }
            return VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TOLONG, Double256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> toLongHelper(m.getBits()));
        }

        // laneIsSet

        @Override
        @ForceInline
        public boolean laneIsSet(int i) {
            Objects.checkIndex(i, length());
            return VectorSupport.extract(Double256Mask.class, double.class, VLENGTH,
                                         this, i, (m, idx) -> (m.getBits()[idx] ? 1L : 0L)) == 1L;
        }

        // Reductions

        @Override
        @ForceInline
        public boolean anyTrue() {
            return VectorSupport.test(BT_ne, Double256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> anyTrueHelper(((Double256Mask)m).getBits()));
        }

        @Override
        @ForceInline
        public boolean allTrue() {
            return VectorSupport.test(BT_overflow, Double256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> allTrueHelper(((Double256Mask)m).getBits()));
        }

        @ForceInline
        /*package-private*/
        static Double256Mask maskAll(boolean bit) {
            return VectorSupport.fromBitsCoerced(Double256Mask.class, long.class, VLENGTH,
                                                 (bit ? -1 : 0), MODE_BROADCAST, null,
                                                 (v, __) -> (v != 0 ? TRUE_MASK : FALSE_MASK));
        }
        private static final Double256Mask  TRUE_MASK = new Double256Mask(true);
        private static final Double256Mask FALSE_MASK = new Double256Mask(false);

    }

    // Shuffle

    static final class Double256Shuffle extends AbstractShuffle<Double> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Long> ETYPE = long.class; // used by the JVM

        Double256Shuffle(long[] indices) {
            super(indices);
            assert(VLENGTH == indices.length);
            assert(indicesInRange(indices));
        }

        Double256Shuffle(int[] indices, int i) {
            this(prepare(indices, i));
        }

        Double256Shuffle(IntUnaryOperator fn) {
            this(prepare(fn));
        }

        long[] indices() {
            return (long[])getPayload();
        }

        @Override
        @ForceInline
        public DoubleSpecies vspecies() {
            return VSPECIES;
        }

        static {
            // There must be enough bits in the shuffle lanes to encode
            // VLENGTH valid indexes and VLENGTH exceptional ones.
            assert(VLENGTH < Long.MAX_VALUE);
            assert(Long.MIN_VALUE <= -VLENGTH);
        }
        static final Double256Shuffle IOTA = new Double256Shuffle(IDENTITY);

        @Override
        @ForceInline
        public Double256Vector toVector() {
            return (Double256Vector) toBitsVector().castShape(vspecies(), 0);
        }

        @Override
        @ForceInline
        Long256Vector toBitsVector() {
            return (Long256Vector) super.toBitsVectorTemplate();
        }

        @Override
        Long256Vector toBitsVector0() {
            return ((Long256Vector) vspecies().asIntegral().dummyVector()).vectorFactory(indices());
        }

        @Override
        @ForceInline
        public int laneSource(int i) {
            return (int)toBitsVector().lane(i);
        }

        @Override
        @ForceInline
        public void intoArray(int[] a, int offset) {
            switch (length()) {
                case 1 -> a[offset] = laneSource(0);
                case 2 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_64, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 4 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_128, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 8 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_256, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 16 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_512, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                default -> {
                    VectorIntrinsics.checkFromIndexSize(offset, length(), a.length);
                    for (int i = 0; i < length(); i++) {
                        a[offset + i] = laneSource(i);
                    }
                }
           }

        }

        @Override
        @ForceInline
        public void intoMemorySegment(MemorySegment ms, long offset, ByteOrder bo) {
            switch (length()) {
                case 1 -> ms.set(ValueLayout.OfInt.JAVA_INT_UNALIGNED, offset, laneSource(0));
                case 2 -> toBitsVector()
                       .convertShape(VectorOperators.L2I, IntVector.SPECIES_64, 0)
                       .reinterpretAsInts()
                       .intoMemorySegment(ms, offset, bo);
                case 4 -> toBitsVector()
                       .convertShape(VectorOperators.L2I, IntVector.SPECIES_128, 0)
                       .reinterpretAsInts()
                       .intoMemorySegment(ms, offset, bo);
                case 8 -> toBitsVector()
                       .convertShape(VectorOperators.L2I, IntVector.SPECIES_256, 0)
                       .reinterpretAsInts()
                       .intoMemorySegment(ms, offset, bo);
                case 16 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_512, 0)
                        .reinterpretAsInts()
                        .intoMemorySegment(ms, offset, bo);
                default -> {
                    VectorIntrinsics.checkFromIndexSize(offset, length(), ms.byteSize() / 4);
                    for (int i = 0; i < length(); i++) {
                        ms.setAtIndex(ValueLayout.JAVA_INT_UNALIGNED, offset + (i << 2), laneSource(i));
                    }
                }
            }
         }

        @Override
        @ForceInline
        public final Double256Mask laneIsValid() {
            return (Double256Mask) toBitsVector().compare(VectorOperators.GE, 0)
                    .cast(vspecies());
        }

        @ForceInline
        @Override
        public final Double256Shuffle rearrange(VectorShuffle<Double> shuffle) {
            Double256Shuffle concreteShuffle = (Double256Shuffle) shuffle;
            return (Double256Shuffle) toBitsVector().rearrange(concreteShuffle.cast(LongVector.SPECIES_256))
                    .toShuffle(vspecies(), false);
        }

        @ForceInline
        @Override
        public final Double256Shuffle wrapIndexes() {
            Long256Vector v = toBitsVector();
            if ((length() & (length() - 1)) == 0) {
                v = (Long256Vector) v.lanewise(VectorOperators.AND, length() - 1);
            } else {
                v = (Long256Vector) v.blend(v.lanewise(VectorOperators.ADD, length()),
                            v.compare(VectorOperators.LT, 0));
            }
            return (Double256Shuffle) v.toShuffle(vspecies(), false);
        }

        private static long[] prepare(int[] indices, int offset) {
            long[] a = new long[VLENGTH];
            for (int i = 0; i < VLENGTH; i++) {
                int si = indices[offset + i];
                si = partiallyWrapIndex(si, VLENGTH);
                a[i] = (long)si;
            }
            return a;
        }

        private static long[] prepare(IntUnaryOperator f) {
            long[] a = new long[VLENGTH];
            for (int i = 0; i < VLENGTH; i++) {
                int si = f.applyAsInt(i);
                si = partiallyWrapIndex(si, VLENGTH);
                a[i] = (long)si;
            }
            return a;
        }

        private static boolean indicesInRange(long[] indices) {
            int length = indices.length;
            for (long si : indices) {
                if (si >= (long)length || si < (long)(-length)) {
                    String msg = ("index "+si+"out of range ["+length+"] in "+
                                  java.util.Arrays.toString(indices));
                    throw new AssertionError(msg);
                }
            }
            return true;
        }
    }

    // ================================================

    // Specialized low-level memory operations.

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset) {
        return super.fromArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset, VectorMask<Double> m, int offsetInRange) {
        return super.fromArray0Template(Double256Mask.class, a, offset, (Double256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Double> m) {
        return super.fromArray0Template(Double256Mask.class, a, offset, indexMap, mapOffset, (Double256Mask) m);
    }



    @ForceInline
    @Override
    final
    DoubleVector fromMemorySegment0(MemorySegment ms, long offset) {
        return super.fromMemorySegment0Template(ms, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromMemorySegment0(MemorySegment ms, long offset, VectorMask<Double> m, int offsetInRange) {
        return super.fromMemorySegment0Template(Double256Mask.class, ms, offset, (Double256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset) {
        super.intoArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset, VectorMask<Double> m) {
        super.intoArray0Template(Double256Mask.class, a, offset, (Double256Mask) m);
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Double> m) {
        super.intoArray0Template(Double256Mask.class, a, offset, indexMap, mapOffset, (Double256Mask) m);
    }


    @ForceInline
    @Override
    final
    void intoMemorySegment0(MemorySegment ms, long offset, VectorMask<Double> m) {
        super.intoMemorySegment0Template(Double256Mask.class, ms, offset, (Double256Mask) m);
    }


    // End of specialized low-level memory operations.

    // ================================================

}

