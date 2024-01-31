<h1 align="center">
    <p>Groovy Script Executor</p>
    <img alt="Static Badge" src="https://img.shields.io/badge/license-MIT-red">
    <img alt="Static Badge" src="https://img.shields.io/badge/JDK-8+-blue">
    <img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.awesome-java-web/groovy-script-executor?color=blue">
    <img alt="GitHub last commit (by committer)" src="https://img.shields.io/github/last-commit/awesome-java-web/groovy-script-executor?color=blue">
    <img alt="GitHub Workflow Status (with event)" src="https://img.shields.io/github/actions/workflow/status/awesome-java-web/groovy-script-executor/maven.yml">
    <img alt="Codecov" src="https://img.shields.io/codecov/c/github/awesome-java-web/groovy-script-executor?color=brightgreen">
</h1>

Groovy Script Executor 帮助你轻松实现在 Java 项目里动态解析并执行 Groovy 代码。

它轻量化，体积小，方便快速集成和使用，一行代码即可实现调用，支持动态传入 Groovy 方法名称和参数。

它内部已经实现了`GroovyObject`对象缓存，避免频繁使用`GroovyClassLoader`加载脚本时出现内存泄漏问题。

对象缓存框架目前支持`Guava`和`Caffeine`，你也可以实现内部定义好的接口，扩展适合自己项目的缓存框架。

# 快速开始
以下介绍假定你正在使用 Maven 构建系统并且正在使用 Java 8 或者更高的 Java 版本。
- 第一步：添加依赖
```
<!---------------------- required ------------------------>
<dependency>
    <groupId>io.github.awesome-java-web</groupId>
    <artifactId>groovy-script-executor</artifactId>
    <version>0.2.0-SNAPSHOT</version>
</dependency>
<!----------------- at least one required -----------------
    本地缓存框架，具体根据使用情况，想用哪个框架就引入对应的依赖
    如果不想自定义缓存框架的配置，默认框架请引入 caffeine 依赖
---------------------------------------------------------->
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
- 第二步：请参考这段示例代码了解如何使用
[GroovyScriptExecutorSample](https://github.com/awesome-java-web/groovy-script-executor/blob/0.2.0/src/test/java/com/github/awesome/scripting/groovy/GroovyScriptExecutorSample.java)
