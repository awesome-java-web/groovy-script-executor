package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.cache.LocalCacheManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 演示程序
 *
 * @author codeboyzhou
 * @since 0.2.0
 */
public class GroovyScriptExecutorSample {

    /**
     * 声明{@link GroovyScriptExecutor}对象
     */
    private GroovyScriptExecutor groovyScriptExecutor;

    /**
     * 使用默认的{@link GroovyScriptExecutor}对象，不定制{@link GroovyScriptCompiler}和{@link LocalCacheManager}
     */
    public void useDefaultGroovyScriptExecutor() {
        this.groovyScriptExecutor = GroovyScriptExecutor.newBuilder();
    }

    public void testExecuteNormalGroovyScript() throws IOException {
        // 解析测试用的Groovy代码，具体代码参见如下链接
        // https://github.com/awesome-java-web/groovy-script-executor/blob/main/src/test/resources/TestGroovyScriptExecutor.groovy
        Path path = Paths.get("src/test/resources/TestGroovyScriptExecutor.groovy");
        byte[] bytes = Files.readAllBytes(path);
        final String classScript = new String(bytes);

        // 调用不带参数的Groovy方法
        Object result = this.groovyScriptExecutor.execute(classScript, "testInvokeMethodNoArgs");
        System.out.println(result); // 这里将会输出1024

        // 调用带单个参数的Groovy方法
        result = this.groovyScriptExecutor.execute(classScript, "testInvokeMethodWithArgs", 1024);
        System.out.println(result); // 这里将会输出1025

        // 调用带多个参数的Groovy方法
        result = this.groovyScriptExecutor.execute(classScript, "testInvokeMethodWithTwoArgs", 1024, 1024);
        System.out.println(result); // 这里将会输出2048
    }

    public void testEvaluateNormalGroovyScript() {
        // 调用不带参数的Groovy方法
        String script = "def testEvaluateNoArgs() { return 1024 }";
        Object result = this.groovyScriptExecutor.evaluate(script, "testEvaluateNoArgs");
        System.out.println(result); // 这里将会输出1024

        // 调用带单个参数的Groovy方法
        script = "def testEvaluateWithArgs(int a) { return a + 1 }";
        result = this.groovyScriptExecutor.evaluate(script, "testEvaluateWithArgs", 1024);
        System.out.println(result); // 这里将会输出1025

        // 调用带多个参数的Groovy方法
        script = "def testEvaluateWithTwoArgs(int a, int b) { return a + b }";
        result = this.groovyScriptExecutor.evaluate(script, "testEvaluateWithTwoArgs", 1024, 1024);
        System.out.println(result); // 这里将会输出2048
    }

    public void testSecurityInterceptor() throws IOException {
        // 解析测试用的Groovy代码，具体代码参见如下链接
        // https://github.com/awesome-java-web/groovy-script-executor/blob/main/src/test/resources/TestSandboxInterceptor.groovy
        Path path = Paths.get("src/test/resources/TestSandboxInterceptor.groovy");
        byte[] bytes = Files.readAllBytes(path);
        final String classScript = new String(bytes);

        // 测试Groovy代码中包含System.gc()方法
        try {
            this.groovyScriptExecutor.execute(classScript, "testSystemGC");
        } catch (Exception e) {
            System.err.println(e.getMessage()); // 这里将会输出异常信息
        }

        // 测试Groovy代码中包含System.exit()方法
        try {
            this.groovyScriptExecutor.execute(classScript, "testSystemExit");
        } catch (Exception e) {
            System.err.println(e.getMessage()); // 这里将会输出异常信息
        }

        // 测试Groovy代码中包含System.runFinalization()方法
        try {
            this.groovyScriptExecutor.execute(classScript, "testSystemRunFinalization");
        } catch (Exception e) {
            System.err.println(e.getMessage()); // 这里将会输出异常信息
        }

        // 测试Groovy代码中包含Runtime类
        try {
            this.groovyScriptExecutor.execute(classScript, "testRuntimeClass");
        } catch (Exception e) {
            System.err.println(e.getMessage()); // 这里将会输出异常信息
        }
    }

    public void testLocalCacheStats() {
        // 打印缓存统计信息
        System.out.println(this.groovyScriptExecutor.getLocalCacheManager().stats());
    }

    public static void main(String[] args) throws IOException {
        GroovyScriptExecutorSample sample = new GroovyScriptExecutorSample();
        // 使用默认的GroovyScriptExecutor对象，不定制GroovyScriptCompiler和LocalCacheManager
        sample.useDefaultGroovyScriptExecutor();
        sample.testExecuteNormalGroovyScript();
        sample.testEvaluateNormalGroovyScript();
        sample.testSecurityInterceptor();
        sample.testLocalCacheStats();
    }

}