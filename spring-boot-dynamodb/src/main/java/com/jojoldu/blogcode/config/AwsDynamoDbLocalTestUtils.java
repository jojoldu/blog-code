package com.jojoldu.blogcode.config;

import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

/**
 * 공식 예제에서는 메이븐으로 설정하는 방법이 나와있으나, 그레이들 혹은 IDE에 실행하면 sqlite4java 기본 라이브러리를 사용할 수 없으므로 로컬 데이터베이스가 시작되지 않을 수 있다.
 * AwsDynamoDbLocal 의 기본 의존성에 sqlite4java가 포함되지만, 이를 IDE나 그레이들에서 가져오지 못하여 발생한 이슈이며, 아래 유틸 클래스는 의존성을 강제로 가져오는 코드이다.
 * 로컬 데이터베이스를 사용하기 전에 AwsDynamoDbLocalTestUtils.initSqLite() 만 호출하면 된다.
 * 속성이 이미 초기화 된 경우 호출은 초기화를 건너뛴다.
 * 원문: https://github.com/redskap/aws-dynamodb-java-example-local-testing#initialize-sqlite4java-for-testing
 *
 * (기존 코드에서는 Guava를 사용하지만, Guava를 사용하는 코드를 Java8 코드로 전환한 클래스)
 */

/**
 * Helper class for initializing AWS DynamoDB to run with sqlite4java for local testing.
 *
 * Copied from: https://github.com/redskap/aws-dynamodb-java-example-local-testing
 */
@NoArgsConstructor(access = PRIVATE)
public abstract class AwsDynamoDbLocalTestUtils {
    private static final String BASE_LIBRARY_NAME = "sqlite4java";

    /**
     * Sets the sqlite4java library path system parameter if it is not set already.
     */
    public static void initSqLite() {
        initSqLite(() -> {
            final List<String> classPath = getClassPathList(System.getProperty("java.class.path"), File.pathSeparator);

            return getLibPath(
                    System.getProperty("os.name"),
                    System.getProperty("java.runtime.name"),
                    System.getProperty("os.arch"),
                    classPath);
        });
    }

    /**
     * Sets the sqlite4java library path system parameter if it is not set already.
     *
     * @param libPathSupplier Calculates lib path for sqlite4java.
     */
    public static void initSqLite(Supplier<String> libPathSupplier) {
        if (System.getProperty("sqlite4java.library.path") == null) {
            System.setProperty("sqlite4java.library.path", libPathSupplier.get());
        }
    }

    /**
     * Calculates the possible Library Names for finding the proper sqlite4j native library and returns the directory with the most specific matching library.
     *
     * @param osName      The value of <code>"os.name"</code> system property (<code>System.getProperty("os.name")</code>).
     * @param runtimeName The value of <code>"java.runtime.name"</code> system property (<code>System.getProperty("java.runtime.name")</code>).
     * @param osArch      The value of <code>"os.arch"</code> system property (<code>System.getProperty("os.arch")</code>).
     * @param osArch      The classpath split into strings by path separator. Value of <code>"java.class.path"</code> system property
     *                    (<code>System.getProperty("os.arch")</code>) split by <code>File.pathSeparator</code>.
     * @return
     */
    public static String getLibPath(final String osName, final String runtimeName, final String osArch, final List<String> classPath) {
        final String os = getOs(osName, runtimeName);
        final List<String> libNames = getLibNames(os, getArch(os, osArch));

        for (final String libName : libNames) {
            for (final String classPathLib : classPath) {
                if (classPathLib.contains(libName)) {
                    return new File(classPathLib).getParent();
                }
            }
        }

        throw new IllegalStateException("SQLite library \"" + libNames + "\" is missing from classpath");
    }

    /**
     * Calculates the possible Library Names for finding the proper sqlite4java native library.
     *
     * Based on the internal calculation of the sqlite4java wrapper <a href="https://bitbucket
     * .org/almworks/sqlite4java/src/fa4bb0fe7319a5f1afe008284146ac83e027de60/java/com/almworks/sqlite4java/Internal
     * .java?at=master&fileviewer=file-view-default#Internal.java-160">Internal
     * class</a>.
     *
     * @param os   Operating System Name used by sqlite4java to get native library.
     * @param arch Operating System Architecture used by sqlite4java to get native library.
     * @return Possible Library Names used by sqlite4java to get native library.
     */
    public static List<String> getLibNames(final String os, final String arch) {
        List<String> result = new ArrayList<>();

        final String base = BASE_LIBRARY_NAME + "-" + os;

        result.add(base + "-" + arch);

        if (arch.equals("x86_64") || arch.equals("x64")) {
            result.add(base + "-amd64");
        } else if (arch.equals("x86")) {
            result.add(base + "-i386");
        } else if (arch.equals("i386")) {
            result.add(base + "-x86");
        } else if (arch.startsWith("arm") && arch.length() > 3) {
            if (arch.length() > 5 && arch.startsWith("armv") && Character.isDigit(arch.charAt(4))) {
                result.add(base + "-" + arch.substring(0, 5));
            }
            result.add(base + "-arm");
        }

        result.add(base);
        result.add(BASE_LIBRARY_NAME);

        return result;
    }

    /**
     * Calculates the Operating System Architecture for finding the proper sqlite4java native library.
     *
     * Based on the internal calculation of the sqlite4java wrapper <a href="https://bitbucket
     * .org/almworks/sqlite4java/src/fa4bb0fe7319a5f1afe008284146ac83e027de60/java/com/almworks/sqlite4java/Internal
     * .java?at=master&fileviewer=file-view-default#Internal.java-204">Internal
     * class</a>.
     *
     * @param osArch The value of <code>"os.arch"</code> system property (<code>System.getProperty("os.arch")</code>).
     * @param os     Operating System Name used by sqlite4java to get native library.
     * @return Operating System Architecture used by sqlite4java to get native library.
     */
    public static String getArch(final String os, final String osArch) {
        String result;

        if (osArch == null) {
            result = "x86";
        } else {
            final String loweCaseOsArch = osArch.toLowerCase(Locale.US);
            result = loweCaseOsArch;
            if ("win32".equals(os) && "amd64".equals(loweCaseOsArch)) {
                result = "x64";
            }
        }

        return result;
    }

    /**
     * Calculates the Operating System Name for finding the proper sqlite4java native library.
     *
     * Based on the internal calculation of the sqlite4java wrapper <a href="https://bitbucket
     * .org/almworks/sqlite4java/src/fa4bb0fe7319a5f1afe008284146ac83e027de60/java/com/almworks/sqlite4java/Internal
     * .java?at=master&fileviewer=file-view-default#Internal.java-219">Internal
     * class</a>.*
     *
     * @param osName      The value of <code>"os.name"</code> system property (<code>System.getProperty("os.name")</code>).
     * @param runtimeName The value of <code>"java.runtime.name"</code> system property (<code>System.getProperty("java.runtime.name")</code>).
     * @return Operating System Name used by sqlite4java to get native library.
     */
    public static String getOs(final String osName, final String runtimeName) {

        String result;
        if (osName == null) {
            result = "linux";
        } else {
            final String loweCaseOsName = osName.toLowerCase(Locale.US);
            if (loweCaseOsName.startsWith("mac") || loweCaseOsName.startsWith("darwin") || loweCaseOsName.startsWith("os x")) {
                result = "osx";
            } else if (loweCaseOsName.startsWith("windows")) {
                result = "win32";
            } else {
                if (runtimeName != null && runtimeName.toLowerCase(Locale.US).contains("android")) {
                    result = "android";
                } else {
                    result = "linux";
                }
            }
        }

        return result;
    }

    /**
     * Splits classpath string by path separator value.
     *
     * @param classPath     Value of <code>"java.class.path"</code> system property (<code>System.getProperty("os.arch")</code>).
     * @param pathSeparator Value of path separator (<code>File.pathSeparator</code>).
     * @return The list of each classpath elements.
     */
    public static List<String> getClassPathList(final String classPath, final String pathSeparator) {
        return Arrays.asList(classPath.split(pathSeparator));
    }
}
