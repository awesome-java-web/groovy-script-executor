# Groovy Script Executor
Groovy Script Executor 帮助你轻松实现在 Java 项目里动态解析并执行 Groovy 代码。它轻量化，体积小，方便快速集成和使用，一行代码即可实现调用，支持动态传入 Groovy 方法名称和参数，随意调用并得到返回结果。它内部已经处理了```GroovyObject```对象的缓存问题，避免频繁使用```GroovyClassLoader```加载脚本生成对象时出现内存泄漏。缓存框架支持```Guava```和```Caffeine```，你也可以实现内部接口拓展自己想用的缓存框架。

# 快速开始
以下介绍假定你正在使用 Maven 构建系统并且正在使用 Java 8 或者更高的 Java 版本。
- 第一步：添加依赖
```
<dependency>
    <groupId>io.github.awesome-java-web</groupId>
    <artifactId>groovy-script-executor</artifactId>
    <version>0.1.0</version>
</dependency>
<!-- 本地缓存框架，具体根据使用情况，想用哪个框架就引入对应的依赖，如果不想自定义缓存框架的配置，默认框架请使用caffeine -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>${caffeine.version}</version>
</dependency>
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>${guava.version}</version>
</dependency>
```
- 第二步：请参考以下示例代码
``` java
import com.github.awesome.scripting.groovy.cache.LocalCacheManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GroovyScriptExecutorSample {

    // 声明GroovyScriptExecutor对象
    private final GroovyScriptExecutor groovyScriptExecutor;

    public GroovyScriptExecutorSample() {
        // 初始化本地缓存管理器，并指定要使用的缓存框架和缓存配置，这里使用默认缓存框架和配置(caffeine)
        LocalCacheManager localCacheManager = LocalCacheManager.newBuilder().useDefaultCache();
        // 初始化GroovyScriptExecutor对象，使用上面创建好的本地缓存管理器
        groovyScriptExecutor = GroovyScriptExecutor.newBuilder().withCacheManager(localCacheManager);
    }

    public static void main(String[] args) throws IOException {
        GroovyScriptExecutorSample sample = new GroovyScriptExecutorSample();
        // 解析测试用的Groovy代码，具体代码参见如下链接
        // https://github.com/awesome-java-web/groovy-script-executor/blob/main/src/test/resources/TestGroovyScriptExecutor.groovy
        Path path = Paths.get("src/test/resources/TestGroovyScriptExecutor.groovy");
        byte[] bytes = Files.readAllBytes(path);
        final String classScript = new String(bytes);
        // 调用不带参数的Groovy方法
        Object result = sample.groovyScriptExecutor.execute(classScript, "testInvokeMethodNoArgs");
        System.out.println(result); // 这里将会输出2147483647
        // 调用带单个参数的Groovy方法
        result = sample.groovyScriptExecutor.execute(classScript, "testInvokeMethodWithArgs", 10240);
        System.out.println(result); // 这里将会输出1048576000
        // 调用带多个参数的Groovy方法
        result = sample.groovyScriptExecutor.execute(classScript, "testInvokeMethodWithTwoArgs", 2, 31);
        System.out.println(result); // 这里将会输出2147483647
    }

}
```
