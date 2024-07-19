/*
 * Copyright (c) 2016, 2020, Oracle and/or its affiliates. All rights reserved.
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

package jdk.jfr.tool;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedObject;
import jdk.jfr.consumer.RecordingFile;
import jdk.test.lib.Asserts;
import jdk.test.lib.process.OutputAnalyzer;
import jsonp.JsonArray;
import jsonp.JsonException;
import jsonp.JsonNumber;
import jsonp.JsonObject;
import jsonp.JsonParser;
import jsonp.JsonPathBuilder;
import jsonp.JsonString;
import jsonp.JsonValue;

/**
 * @test
 * @key jfr
 * @summary Tests print --json
 * @requires vm.hasJFR
 *
 * @library /test/lib /test/jdk /json-parser.jar
 * @modules jdk.jfr
 *
 * @run main/othervm jdk.jfr.tool.TestPrintJSON
 */
public class TestPrintJSON {

    public static void main(String... args) throws Throwable {
        JsonPathBuilder jpb = new JsonPathBuilder();

        Path recordingFile = ExecuteHelper.createProfilingRecording().toAbsolutePath();

        OutputAnalyzer output = ExecuteHelper.jfr("print", "--json", "--stack-depth", "999", recordingFile.toString());
        String json = output.getStdout();

        JsonValue doc = JsonParser.parse(json);
        if (jpb.objectKey("recording").objectKey("events").build().apply(doc) instanceof JsonArray jsonEvents) {
//        if (doc instanceof JsonObject jo &&
//            jo.get("recording") instanceof JsonObject req &&
//                req.get("events") instanceof JsonArray jsonEvents) {

            List<RecordedEvent> events = RecordingFile.readAllEvents(recordingFile);
            Collections.sort(events, new EndTicksComparator());
            // Verify events are equal
            Iterator<RecordedEvent> it = events.iterator();
            for (JsonValue jsonEvent : jsonEvents.values()) {
                RecordedEvent recordedEvent = it.next();
                String typeName = recordedEvent.getEventType().getName();
                if (jpb.clear().objectKey("type").build().apply(jsonEvent) instanceof JsonString name) {
//                if (jsonEvent instanceof JsonObject o &&
//                    o.get("type") instanceof JsonString name) {
                    Asserts.assertEquals(typeName, name.value());
                } else {
                    throw new JsonException("types don't match");
                }
                assertEquals(jsonEvent, recordedEvent);
            }
            Asserts.assertFalse(events.size() != jsonEvents.values().size(), "Incorrect number of events");
        } else {
            throw new JsonException("types don't match");
        }
    }

    private static void assertEquals(JsonValue jsonValue, Object jfrObject) throws Exception {
System.out.printf("asserting %s, %s\n", jsonValue, jfrObject);

        var jpb = new JsonPathBuilder();

        // Check object
        if (jfrObject instanceof RecordedObject) {
//            if (jsonValue instanceof JsonObject jo &&
//                jo.get("values") instanceof JsonObject values) {
            if (jpb.objectKey("values").build().apply(jsonValue) instanceof JsonObject values) {
                RecordedObject recObject = (RecordedObject) jfrObject;
                Asserts.assertEquals(values.keys().size(), recObject.getFields().size());
                for (ValueDescriptor v : recObject.getFields()) {
                    String name = v.getName();
                    if (values.get(name) instanceof JsonString js) {
                        Object expectedValue = recObject.getValue(name);
                        if (v.getAnnotation(Timestamp.class) != null) {
                            // Make instant of OffsetDateTime
                            String text = js.value();
                            jsonValue = JsonString.fromString(OffsetDateTime.parse(text).toInstant().toString());
                            expectedValue = recObject.getInstant(name);
                        }
                        if (v.getAnnotation(Timespan.class) != null) {
                            expectedValue = recObject.getDuration(name);
                        }
                        assertEquals(jsonValue, expectedValue);
                        return;
                    } else {
                        throw new JsonException("types don't match");
                    }
                }
            } else {
                throw new JsonException("types don't match");
            }
        }
        // Check array
        if (jfrObject != null && jfrObject.getClass().isArray()) {
            Object[] jfrArray = (Object[]) jfrObject;
            if (jsonValue instanceof JsonArray ja) {
                for (int i = 0; i < jfrArray.length; i++) {
                    assertEquals(ja.get(i), jfrArray[i]);
                }
                return;
            } else {
                throw new JsonException("types don't match");
            }
        }

        if (jsonValue instanceof JsonNumber jn) {
            Number n = jn.value();
            String ns = jn.toString();
            // Double.NaN / Double.Inifinity is not supported by JSON format,
            // use null
            if (jfrObject instanceof Double) {
                double expected = ((Double) jfrObject);
                if (Double.isInfinite(expected) || Double.isNaN(expected)) {
                    Asserts.assertEquals("null", ns);
                    return;
                }
                double value = n.doubleValue();
                Asserts.assertEquals(expected, value);
                return;
            }
            // Float.NaN / Float.Inifinity is not supported by JSON format,
            // use null
            if (jfrObject instanceof Float) {
                float expected = ((Float) jfrObject);
                if (Float.isInfinite(expected) || Float.isNaN(expected)) {
                    Asserts.assertEquals("null", jn);
                    return;
                }
                float value = n.floatValue();
                Asserts.assertEquals(expected, value);
                return;
            }
            if (jfrObject instanceof Integer) {
                Integer expected = ((Integer) jfrObject);
                double value = n.doubleValue();
                Asserts.assertEquals(expected.doubleValue(), value);
                return;
            }
            if (jfrObject instanceof Long) {
                Long expected = ((Long) jfrObject);
                double value = n.doubleValue();
                Asserts.assertEquals(expected.doubleValue(), value);
                return;
            }

        }

        if (jsonValue instanceof JsonString js) {
            String jfrText = String.valueOf(jfrObject);
            Asserts.assertEquals(jfrText, js.value(), "Primitive values don't match. JSON = " + js.value());
        } else {
            throw new JsonException("types don't match");
        }
    }
}
