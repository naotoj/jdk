/*
 * Copyright (c) 2022, 2025, Oracle and/or its affiliates. All rights reserved.
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
 *
 */

#include "stubGenerator_x86_64.hpp"

// Constants for libm trigonometric stubs

ATTRIBUTE_ALIGNED(8) static const juint _ONE[] = {
    0x00000000UL, 0x3ff00000UL
};
address StubGenerator::ONE = (address)_ONE;

ATTRIBUTE_ALIGNED(16) static const juint _ONEHALF[] = {
    0x00000000UL, 0x3fe00000UL, 0x00000000UL, 0x3fe00000UL
};
address StubGenerator::ONEHALF = (address)_ONEHALF;

ATTRIBUTE_ALIGNED(8) static const juint _SIGN_MASK[] = {
    0x00000000UL, 0x80000000UL
};
address StubGenerator::SIGN_MASK = (address)_SIGN_MASK;

ATTRIBUTE_ALIGNED(8) static const juint _TWO_POW_55[] = {
    0x00000000UL, 0x43600000UL
};
address StubGenerator::TWO_POW_55 = (address)_TWO_POW_55;

ATTRIBUTE_ALIGNED(8) static const juint _TWO_POW_M55[] = {
    0x00000000UL, 0x3c800000UL
};
address StubGenerator::TWO_POW_M55 = (address)_TWO_POW_M55;

ATTRIBUTE_ALIGNED(16) static const juint _SHIFTER[] = {
    0x00000000UL, 0x43380000UL, 0x00000000UL, 0x43380000UL
};
address StubGenerator::SHIFTER = (address)_SHIFTER;

ATTRIBUTE_ALIGNED(4) static const juint _ZERO[] = {
    0x00000000UL, 0x00000000UL
};
address StubGenerator::ZERO = (address)_ZERO;

ATTRIBUTE_ALIGNED(16) static const juint _SC_1[] = {
    0x55555555UL, 0xbfc55555UL, 0x00000000UL, 0xbfe00000UL
};
address StubGenerator::SC_1 = (address)_SC_1;

ATTRIBUTE_ALIGNED(16) static const juint _SC_2[] = {
    0x11111111UL, 0x3f811111UL, 0x55555555UL, 0x3fa55555UL
};
address StubGenerator::SC_2 = (address)_SC_2;

ATTRIBUTE_ALIGNED(16) static const juint _SC_3[] = {
    0x1a01a01aUL, 0xbf2a01a0UL, 0x16c16c17UL, 0xbf56c16cUL
};
address StubGenerator::SC_3 = (address)_SC_3;

ATTRIBUTE_ALIGNED(16) static const juint _SC_4[] = {
    0xa556c734UL, 0x3ec71de3UL, 0x1a01a01aUL, 0x3efa01a0UL
};
address StubGenerator::SC_4 = (address)_SC_4;

ATTRIBUTE_ALIGNED(8) static const juint _PI_4[] = {
    0x40000000UL, 0x3fe921fbUL, 0x18469899UL, 0x3e64442dUL
};
address StubGenerator::PI_4 = (address)_PI_4;

ATTRIBUTE_ALIGNED(8) static const juint _PI32INV[] = {
    0x6dc9c883UL, 0x40245f30UL
};
address StubGenerator::PI32INV = (address)_PI32INV;

ATTRIBUTE_ALIGNED(8) static const juint _NEG_ZERO[] = {
    0x00000000UL, 0x80000000UL
};
address StubGenerator::NEG_ZERO = (address)_NEG_ZERO;

ATTRIBUTE_ALIGNED(8) static const juint _P_1[] = {
    0x54400000UL, 0x3fb921fbUL
};
address StubGenerator::P_1 = (address)_P_1;

ATTRIBUTE_ALIGNED(16) static const juint _P_2[] = {
    0x1a600000UL, 0x3d90b461UL, 0x1a600000UL, 0x3d90b461UL
};
address StubGenerator::P_2 = (address)_P_2;

ATTRIBUTE_ALIGNED(8) static const juint _P_3[] = {
    0x2e037073UL, 0x3b63198aUL
};
address StubGenerator::P_3 = (address)_P_3;


ATTRIBUTE_ALIGNED(16) static const juint _PI_INV_TABLE[] = {
    0x00000000UL, 0x00000000UL, 0xa2f9836eUL, 0x4e441529UL, 0xfc2757d1UL,
    0xf534ddc0UL, 0xdb629599UL, 0x3c439041UL, 0xfe5163abUL, 0xdebbc561UL,
    0xb7246e3aUL, 0x424dd2e0UL, 0x06492eeaUL, 0x09d1921cUL, 0xfe1deb1cUL,
    0xb129a73eUL, 0xe88235f5UL, 0x2ebb4484UL, 0xe99c7026UL, 0xb45f7e41UL,
    0x3991d639UL, 0x835339f4UL, 0x9c845f8bUL, 0xbdf9283bUL, 0x1ff897ffUL,
    0xde05980fUL, 0xef2f118bUL, 0x5a0a6d1fUL, 0x6d367ecfUL, 0x27cb09b7UL,
    0x4f463f66UL, 0x9e5fea2dUL, 0x7527bac7UL, 0xebe5f17bUL, 0x3d0739f7UL,
    0x8a5292eaUL, 0x6bfb5fb1UL, 0x1f8d5d08UL, 0x56033046UL, 0xfc7b6babUL,
    0xf0cfbc21UL
};
address StubGenerator::PI_INV_TABLE = (address)_PI_INV_TABLE;


ATTRIBUTE_ALIGNED(16) static const juint _Ctable[] = {
    0x00000000UL, 0x00000000UL, 0x00000000UL, 0x00000000UL, 0x00000000UL,
    0x00000000UL, 0x00000000UL, 0x3ff00000UL, 0x176d6d31UL, 0xbf73b92eUL,
    0xbc29b42cUL, 0x3fb917a6UL, 0xe0000000UL, 0xbc3e2718UL, 0x00000000UL,
    0x3ff00000UL, 0x011469fbUL, 0xbf93ad06UL, 0x3c69a60bUL, 0x3fc8f8b8UL,
    0xc0000000UL, 0xbc626d19UL, 0x00000000UL, 0x3ff00000UL, 0x939d225aUL,
    0xbfa60beaUL, 0x2ed59f06UL, 0x3fd29406UL, 0xa0000000UL, 0xbc75d28dUL,
    0x00000000UL, 0x3ff00000UL, 0x866b95cfUL, 0xbfb37ca1UL, 0xa6aea963UL,
    0x3fd87de2UL, 0xe0000000UL, 0xbc672cedUL, 0x00000000UL, 0x3ff00000UL,
    0x73fa1279UL, 0xbfbe3a68UL, 0x3806f63bUL, 0x3fde2b5dUL, 0x20000000UL,
    0x3c5e0d89UL, 0x00000000UL, 0x3ff00000UL, 0x5bc57974UL, 0xbfc59267UL,
    0x39ae68c8UL, 0x3fe1c73bUL, 0x20000000UL, 0x3c8b25ddUL, 0x00000000UL,
    0x3ff00000UL, 0x53aba2fdUL, 0xbfcd0dfeUL, 0x25091dd6UL, 0x3fe44cf3UL,
    0x20000000UL, 0x3c68076aUL, 0x00000000UL, 0x3ff00000UL, 0x99fcef32UL,
    0x3fca8279UL, 0x667f3bcdUL, 0x3fe6a09eUL, 0x20000000UL, 0xbc8bdd34UL,
    0x00000000UL, 0x3fe00000UL, 0x94247758UL, 0x3fc133ccUL, 0x6b151741UL,
    0x3fe8bc80UL, 0x20000000UL, 0xbc82c5e1UL, 0x00000000UL, 0x3fe00000UL,
    0x9ae68c87UL, 0x3fac73b3UL, 0x290ea1a3UL, 0x3fea9b66UL, 0xe0000000UL,
    0x3c39f630UL, 0x00000000UL, 0x3fe00000UL, 0x7f909c4eUL, 0xbf9d4a2cUL,
    0xf180bdb1UL, 0x3fec38b2UL, 0x80000000UL, 0xbc76e0b1UL, 0x00000000UL,
    0x3fe00000UL, 0x65455a75UL, 0xbfbe0875UL, 0xcf328d46UL, 0x3fed906bUL,
    0x20000000UL, 0x3c7457e6UL, 0x00000000UL, 0x3fe00000UL, 0x76acf82dUL,
    0x3fa4a031UL, 0x56c62ddaUL, 0x3fee9f41UL, 0xe0000000UL, 0x3c8760b1UL,
    0x00000000UL, 0x3fd00000UL, 0x0e5967d5UL, 0xbfac1d1fUL, 0xcff75cb0UL,
    0x3fef6297UL, 0x20000000UL, 0x3c756217UL, 0x00000000UL, 0x3fd00000UL,
    0x0f592f50UL, 0xbf9ba165UL, 0xa3d12526UL, 0x3fefd88dUL, 0x40000000UL,
    0xbc887df6UL, 0x00000000UL, 0x3fc00000UL, 0x00000000UL, 0x00000000UL,
    0x00000000UL, 0x3ff00000UL, 0x00000000UL, 0x00000000UL, 0x00000000UL,
    0x00000000UL, 0x0f592f50UL, 0x3f9ba165UL, 0xa3d12526UL, 0x3fefd88dUL,
    0x40000000UL, 0xbc887df6UL, 0x00000000UL, 0xbfc00000UL, 0x0e5967d5UL,
    0x3fac1d1fUL, 0xcff75cb0UL, 0x3fef6297UL, 0x20000000UL, 0x3c756217UL,
    0x00000000UL, 0xbfd00000UL, 0x76acf82dUL, 0xbfa4a031UL, 0x56c62ddaUL,
    0x3fee9f41UL, 0xe0000000UL, 0x3c8760b1UL, 0x00000000UL, 0xbfd00000UL,
    0x65455a75UL, 0x3fbe0875UL, 0xcf328d46UL, 0x3fed906bUL, 0x20000000UL,
    0x3c7457e6UL, 0x00000000UL, 0xbfe00000UL, 0x7f909c4eUL, 0x3f9d4a2cUL,
    0xf180bdb1UL, 0x3fec38b2UL, 0x80000000UL, 0xbc76e0b1UL, 0x00000000UL,
    0xbfe00000UL, 0x9ae68c87UL, 0xbfac73b3UL, 0x290ea1a3UL, 0x3fea9b66UL,
    0xe0000000UL, 0x3c39f630UL, 0x00000000UL, 0xbfe00000UL, 0x94247758UL,
    0xbfc133ccUL, 0x6b151741UL, 0x3fe8bc80UL, 0x20000000UL, 0xbc82c5e1UL,
    0x00000000UL, 0xbfe00000UL, 0x99fcef32UL, 0xbfca8279UL, 0x667f3bcdUL,
    0x3fe6a09eUL, 0x20000000UL, 0xbc8bdd34UL, 0x00000000UL, 0xbfe00000UL,
    0x53aba2fdUL, 0x3fcd0dfeUL, 0x25091dd6UL, 0x3fe44cf3UL, 0x20000000UL,
    0x3c68076aUL, 0x00000000UL, 0xbff00000UL, 0x5bc57974UL, 0x3fc59267UL,
    0x39ae68c8UL, 0x3fe1c73bUL, 0x20000000UL, 0x3c8b25ddUL, 0x00000000UL,
    0xbff00000UL, 0x73fa1279UL, 0x3fbe3a68UL, 0x3806f63bUL, 0x3fde2b5dUL,
    0x20000000UL, 0x3c5e0d89UL, 0x00000000UL, 0xbff00000UL, 0x866b95cfUL,
    0x3fb37ca1UL, 0xa6aea963UL, 0x3fd87de2UL, 0xe0000000UL, 0xbc672cedUL,
    0x00000000UL, 0xbff00000UL, 0x939d225aUL, 0x3fa60beaUL, 0x2ed59f06UL,
    0x3fd29406UL, 0xa0000000UL, 0xbc75d28dUL, 0x00000000UL, 0xbff00000UL,
    0x011469fbUL, 0x3f93ad06UL, 0x3c69a60bUL, 0x3fc8f8b8UL, 0xc0000000UL,
    0xbc626d19UL, 0x00000000UL, 0xbff00000UL, 0x176d6d31UL, 0x3f73b92eUL,
    0xbc29b42cUL, 0x3fb917a6UL, 0xe0000000UL, 0xbc3e2718UL, 0x00000000UL,
    0xbff00000UL, 0x00000000UL, 0x00000000UL, 0x00000000UL, 0x00000000UL,
    0x00000000UL, 0x00000000UL, 0x00000000UL, 0xbff00000UL, 0x176d6d31UL,
    0x3f73b92eUL, 0xbc29b42cUL, 0xbfb917a6UL, 0xe0000000UL, 0x3c3e2718UL,
    0x00000000UL, 0xbff00000UL, 0x011469fbUL, 0x3f93ad06UL, 0x3c69a60bUL,
    0xbfc8f8b8UL, 0xc0000000UL, 0x3c626d19UL, 0x00000000UL, 0xbff00000UL,
    0x939d225aUL, 0x3fa60beaUL, 0x2ed59f06UL, 0xbfd29406UL, 0xa0000000UL,
    0x3c75d28dUL, 0x00000000UL, 0xbff00000UL, 0x866b95cfUL, 0x3fb37ca1UL,
    0xa6aea963UL, 0xbfd87de2UL, 0xe0000000UL, 0x3c672cedUL, 0x00000000UL,
    0xbff00000UL, 0x73fa1279UL, 0x3fbe3a68UL, 0x3806f63bUL, 0xbfde2b5dUL,
    0x20000000UL, 0xbc5e0d89UL, 0x00000000UL, 0xbff00000UL, 0x5bc57974UL,
    0x3fc59267UL, 0x39ae68c8UL, 0xbfe1c73bUL, 0x20000000UL, 0xbc8b25ddUL,
    0x00000000UL, 0xbff00000UL, 0x53aba2fdUL, 0x3fcd0dfeUL, 0x25091dd6UL,
    0xbfe44cf3UL, 0x20000000UL, 0xbc68076aUL, 0x00000000UL, 0xbff00000UL,
    0x99fcef32UL, 0xbfca8279UL, 0x667f3bcdUL, 0xbfe6a09eUL, 0x20000000UL,
    0x3c8bdd34UL, 0x00000000UL, 0xbfe00000UL, 0x94247758UL, 0xbfc133ccUL,
    0x6b151741UL, 0xbfe8bc80UL, 0x20000000UL, 0x3c82c5e1UL, 0x00000000UL,
    0xbfe00000UL, 0x9ae68c87UL, 0xbfac73b3UL, 0x290ea1a3UL, 0xbfea9b66UL,
    0xe0000000UL, 0xbc39f630UL, 0x00000000UL, 0xbfe00000UL, 0x7f909c4eUL,
    0x3f9d4a2cUL, 0xf180bdb1UL, 0xbfec38b2UL, 0x80000000UL, 0x3c76e0b1UL,
    0x00000000UL, 0xbfe00000UL, 0x65455a75UL, 0x3fbe0875UL, 0xcf328d46UL,
    0xbfed906bUL, 0x20000000UL, 0xbc7457e6UL, 0x00000000UL, 0xbfe00000UL,
    0x76acf82dUL, 0xbfa4a031UL, 0x56c62ddaUL, 0xbfee9f41UL, 0xe0000000UL,
    0xbc8760b1UL, 0x00000000UL, 0xbfd00000UL, 0x0e5967d5UL, 0x3fac1d1fUL,
    0xcff75cb0UL, 0xbfef6297UL, 0x20000000UL, 0xbc756217UL, 0x00000000UL,
    0xbfd00000UL, 0x0f592f50UL, 0x3f9ba165UL, 0xa3d12526UL, 0xbfefd88dUL,
    0x40000000UL, 0x3c887df6UL, 0x00000000UL, 0xbfc00000UL, 0x00000000UL,
    0x00000000UL, 0x00000000UL, 0xbff00000UL, 0x00000000UL, 0x00000000UL,
    0x00000000UL, 0x00000000UL, 0x0f592f50UL, 0xbf9ba165UL, 0xa3d12526UL,
    0xbfefd88dUL, 0x40000000UL, 0x3c887df6UL, 0x00000000UL, 0x3fc00000UL,
    0x0e5967d5UL, 0xbfac1d1fUL, 0xcff75cb0UL, 0xbfef6297UL, 0x20000000UL,
    0xbc756217UL, 0x00000000UL, 0x3fd00000UL, 0x76acf82dUL, 0x3fa4a031UL,
    0x56c62ddaUL, 0xbfee9f41UL, 0xe0000000UL, 0xbc8760b1UL, 0x00000000UL,
    0x3fd00000UL, 0x65455a75UL, 0xbfbe0875UL, 0xcf328d46UL, 0xbfed906bUL,
    0x20000000UL, 0xbc7457e6UL, 0x00000000UL, 0x3fe00000UL, 0x7f909c4eUL,
    0xbf9d4a2cUL, 0xf180bdb1UL, 0xbfec38b2UL, 0x80000000UL, 0x3c76e0b1UL,
    0x00000000UL, 0x3fe00000UL, 0x9ae68c87UL, 0x3fac73b3UL, 0x290ea1a3UL,
    0xbfea9b66UL, 0xe0000000UL, 0xbc39f630UL, 0x00000000UL, 0x3fe00000UL,
    0x94247758UL, 0x3fc133ccUL, 0x6b151741UL, 0xbfe8bc80UL, 0x20000000UL,
    0x3c82c5e1UL, 0x00000000UL, 0x3fe00000UL, 0x99fcef32UL, 0x3fca8279UL,
    0x667f3bcdUL, 0xbfe6a09eUL, 0x20000000UL, 0x3c8bdd34UL, 0x00000000UL,
    0x3fe00000UL, 0x53aba2fdUL, 0xbfcd0dfeUL, 0x25091dd6UL, 0xbfe44cf3UL,
    0x20000000UL, 0xbc68076aUL, 0x00000000UL, 0x3ff00000UL, 0x5bc57974UL,
    0xbfc59267UL, 0x39ae68c8UL, 0xbfe1c73bUL, 0x20000000UL, 0xbc8b25ddUL,
    0x00000000UL, 0x3ff00000UL, 0x73fa1279UL, 0xbfbe3a68UL, 0x3806f63bUL,
    0xbfde2b5dUL, 0x20000000UL, 0xbc5e0d89UL, 0x00000000UL, 0x3ff00000UL,
    0x866b95cfUL, 0xbfb37ca1UL, 0xa6aea963UL, 0xbfd87de2UL, 0xe0000000UL,
    0x3c672cedUL, 0x00000000UL, 0x3ff00000UL, 0x939d225aUL, 0xbfa60beaUL,
    0x2ed59f06UL, 0xbfd29406UL, 0xa0000000UL, 0x3c75d28dUL, 0x00000000UL,
    0x3ff00000UL, 0x011469fbUL, 0xbf93ad06UL, 0x3c69a60bUL, 0xbfc8f8b8UL,
    0xc0000000UL, 0x3c626d19UL, 0x00000000UL, 0x3ff00000UL, 0x176d6d31UL,
    0xbf73b92eUL, 0xbc29b42cUL, 0xbfb917a6UL, 0xe0000000UL, 0x3c3e2718UL,
    0x00000000UL, 0x3ff00000UL
};
address StubGenerator::Ctable = (address)_Ctable;

