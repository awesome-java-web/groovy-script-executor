package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.awesome.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

/**
 * 对 Groovy 脚本编译器做一些定制，添加沙箱执行环境，注册安全拦截器。
 *
 * @author codeboyzhou
 * @since 0.2.0
 */
public class GroovyScriptCompiler {

    /**
     * 编译器配置对象
     */
    private final CompilerConfiguration configuration;

    /**
     * 创建默认的 Groovy 编译器对象，在默认情况下，沙箱执行环境和安全拦截器已经注册。
     *
     * @return 当前类的实例对象
     */
    public static GroovyScriptCompiler asDefault() {
        return new GroovyScriptCompiler();
    }

    /**
     * 构造方法完成初始化工作
     */
    public GroovyScriptCompiler() {
        this.configuration = new CompilerConfiguration();
        this.addCompilationCustomizer(new SandboxTransformer());
        this.registerSecurityInterceptor(new SystemClassInterceptor());
        this.registerSecurityInterceptor(new RuntimeClassInterceptor());
    }

    /**
     * 添加编译器自定义配置
     *
     * @param customizer 编译器自定义配置器
     * @see SandboxTransformer
     */
    public void addCompilationCustomizer(CompilationCustomizer customizer) {
        this.configuration.addCompilationCustomizers(customizer);
    }

    /**
     * 注册安全拦截器，使用这个方法可以在外部扩展添加自己的安全拦截器，例如：限制访问其它系统类或方法。
     * <p>
     * 在 {@link GroovyScriptExecutor} 开始执行之前，会将你想要加载的拦截器全部生效。
     *
     * @param interceptor 安全拦截器
     * @see SystemClassInterceptor
     * @see RuntimeClassInterceptor
     */
    public void registerSecurityInterceptor(GroovyInterceptor interceptor) {
        interceptor.register();
    }

    /**
     * 获取编译器配置对象
     *
     * @return 编译器配置对象
     */
    public CompilerConfiguration getConfiguration() {
        return this.configuration;
    }

}
