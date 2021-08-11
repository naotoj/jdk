/*
 * Copyright (c) 2012, 2021, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6336885 7196799 7197573 7198834 8000245 8000615 8001440 8008577
 *      8010666 8013086 8013233 8013903 8015960 8028771 8054482 8062006
 *      8150432 8215913 8220227 8228465 8232871 8232860 8236495 8245241
 *      8246721 8248695 8257964 8261919
 * @summary tests for "java.locale.providers" system property
 * @library /test/lib
 * @build LocaleProviders
 *        providersrc.spi.src.tznp
 *        providersrc.spi.src.tznp8013086
 * @modules java.base/sun.util.locale
 *          java.base/sun.util.locale.provider
 * @run testng/othervm -Djdk.lang.Process.allowAmbiguousCommands=false LocaleProvidersRun
 */

import java.util.Locale;

import jdk.test.lib.JDKToolLauncher;
import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.Utils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.fail;

public class LocaleProvidersRun {
    private static Locale platDefLoc;
    private static String defLang;
    private static String defCtry;
    private static Locale platDefFormat;
    private static String defFmtLang;
    private static String defFmtCtry;
    private static boolean isWinOrMac;

    @BeforeTest
    public static void init() {
        //get the platform default locales
        platDefLoc = Locale.getDefault(Locale.Category.DISPLAY);
        defLang = platDefLoc.getLanguage();
        defCtry = platDefLoc.getCountry();
        System.out.println("DEFLANG = " + defLang);
        System.out.println("DEFCTRY = " + defCtry);

        platDefFormat = Locale.getDefault(Locale.Category.FORMAT);
        defFmtLang = platDefFormat.getLanguage();
        defFmtCtry = platDefFormat.getCountry();
        System.out.println("DEFFMTLANG = " + defFmtLang);
        System.out.println("DEFFMTCTRY = " + defFmtCtry);

        var osName = System.getProperty("os.name");
        isWinOrMac = osName.startsWith("Windows") || osName.startsWith("Mac");
    }

    @DataProvider
    public Object[][] testRunData() {
        return new Object[][]{
            //testing HOST is selected for the default locale,
            // if specified on Windows or MacOSX
            {"HOST,JRE", "adapterTest", isWinOrMac ? "HOST" : "JRE", defLang, defCtry},

            //testing HOST is NOT selected for the non-default locale, if specified
            //Try to find the locale JRE supports which is not the platform default
            // (HOST supports that one)
            {"HOST,JRE", "adapterTest", "JRE", "ga", "IE"},

            //testing SPI is NOT selected, as there is none.
            {"SPI,JRE", "adapterTest", "JRE", "en", "US"},
            {"SPI,COMPAT", "adapterTest", "JRE", "en", "US"},

            //testing the order, variant #1. This assumes en_GB DateFormat data are
            // available both in JRE & CLDR
            {"CLDR,JRE", "adapterTest", "CLDR", "en", "GB"},
            {"CLDR,COMPAT", "adapterTest", "CLDR", "en", "GB"},

            //testing the order, variant #2. This assumes en_GB DateFormat data are
            // available both in JRE & CLDR
            {"JRE,CLDR", "adapterTest", "JRE", "en", "GB"},
            {"COMPAT,CLDR", "adapterTest", "JRE", "en", "GB"},

            //testing the order, variant #3 for non-existent locale in JRE
            // assuming "haw" is not in JRE.
            {"JRE,CLDR", "adapterTest", "CLDR", "haw", ""},
            {"COMPAT,CLDR", "adapterTest", "CLDR", "haw", ""},

            //testing the order, variant #4 for the bug 7196799. CLDR's "zh" data
            // should be used in "zh_CN"
            {"CLDR", "adapterTest", "CLDR", "zh", "CN"},

            //testing FALLBACK provider. SPI and invalid one cases.
            {"SPI", "adapterTest", "FALLBACK", "en", "US"},
            {"FOO", "adapterTest", "CLDR", "en", "US"},
            {"BAR,SPI", "adapterTest", "FALLBACK", "en", "US"},

            //testing 7198834 fix.
            {"HOST", "bug7198834Test", "", "", ""},

            //testing 8000245 fix.
            {"JRE", "tzNameTest", "Europe/Moscow", "", ""},
            {"COMPAT", "tzNameTest", "Europe/Moscow", "", ""},

            //testing 8000615 fix.
            {"JRE", "tzNameTest", "America/Los_Angeles", "", ""},
            {"COMPAT", "tzNameTest", "America/Los_Angeles", "", ""},

            //testing 8001440 fix.
            {"CLDR", "bug8001440Test", "", "", ""},

            //testing 8013086 fix.
            {"JRE,SPI", "bug8013086Test", "ja", "JP", ""},
            {"COMPAT,SPI", "bug8013086Test", "ja", "JP", ""},

            //testing 8013903 fix. (Windows only)
            {"HOST,JRE", "bug8013903Test", "", "", ""},
            {"HOST", "bug8013903Test", "", "", ""},
            {"HOST,COMPAT", "bug8013903Test", "", "", ""},


            //testing 8228465 fix. (Windows only)
            {"HOST", "bug8228465Test", "", "", ""},

            //testing 8232871 fix. (macOS only)
            {"HOST", "bug8232871Test", "", "", ""},

            //testing 8232860 fix. (macOS/Windows only)
            {"HOST", "bug8232860Test", "", "", ""},

            //testing 8245241 fix.
            //jdk.lang.Process.allowAmbiguousCommands=false is needed for properly escaping
            //double quotes in the string argument.
            {"FOO", "bug8245241Test",
                    "Invalid locale provider adapter \"FOO\" ignored.", "", ""},

            //testing 8248695 fix.
            {"HOST", "bug8248695Test", "", "", ""},

            //testing 8257964 fix. (macOS/Windows only)
            {"HOST", "bug8257964Test", "", "", ""},
        };
    }

    @DataProvider
    public Object[][] testRunDataEn() {
        return new Object[][]{
            //testing 8010666 fix.
            {"HOST", "bug8010666Test", "", "", ""},
            //testing 8220227 fix. (Windows only)
            {"HOST", "bug8220227Test", "", "", ""},
        };
    }

    @DataProvider
    public Object[][] testRunDataZhCN() {
        return new Object[][]{
            //testing 8027289 fix, if the platform format default is zh_CN
            // this assumes Windows' currency symbol for zh_CN is \u00A5, the yen
            // (yuan) sign.
            {"JRE,HOST", "bug8027289Test", "FFE5", "", ""},
            {"COMPAT,HOST", "bug8027289Test", "FFE5", "", ""},
            {"HOST", "bug8027289Test", "00A5", "", ""},
        };
    }

    @Test (dataProvider = "testRunData")
    public void testRun(String prefList, String methodName,
            String param1, String param2, String param3) throws Throwable{
        JDKToolLauncher launcher = JDKToolLauncher.createUsingTestJDK("java");
        launcher.addToolArg("-ea")
                .addToolArg("-esa")
                .addToolArg("-cp")
                .addToolArg(Utils.TEST_CLASS_PATH)
                .addToolArg("-Djava.util.logging.config.class=LocaleProviders$LogConfig")
                .addToolArg("-Djava.locale.providers=" + prefList)
                .addToolArg("--add-exports=java.base/sun.util.locale.provider=ALL-UNNAMED")
                .addToolArg("LocaleProviders")
                .addToolArg(methodName)
                .addToolArg(param1)
                .addToolArg(param2)
                .addToolArg(param3);
        int exitCode = ProcessTools.executeCommand(launcher.getCommand())
                .getExitValue();
        if (exitCode != 0) {
            throw new RuntimeException("Unexpected exit code: " + exitCode);
        }
    }

    @Test (dataProvider = "testRunDataEn")
    public void testRunEn(String prefList, String methodName,
                        String param1, String param2, String param3) throws Throwable {
        // Only run if def locale is English
        if ("en".equals(defLang)) {
            testRun(prefList, methodName, param1, param2, param3);
        }
    }

    @Test (dataProvider = "testRunDataZhCN")
    public void testRunZhCN(String prefList, String methodName,
                          String param1, String param2, String param3) throws Throwable {
        // Only run if def locale is English
        if ("zh".equals(defLang) && "CN".equals(defCtry)) {
            testRun(prefList, methodName, param1, param2, param3);
        }
    }
}
