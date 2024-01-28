package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.awesome.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

public class GroovyScriptCompiler {

    private final CompilerConfiguration configuration;

    public static GroovyScriptCompiler asDefault() {
        return new GroovyScriptCompiler();
    }

    public GroovyScriptCompiler() {
        this.configuration = new CompilerConfiguration();
        this.addCompilationCustomizer(new SandboxTransformer());
        this.registerSecurityInterceptor(new SystemClassInterceptor());
        this.registerSecurityInterceptor(new RuntimeClassInterceptor());
    }

    public void addCompilationCustomizer(CompilationCustomizer customizer) {
        this.configuration.addCompilationCustomizers(customizer);
    }

    public void registerSecurityInterceptor(GroovyInterceptor interceptor) {
        interceptor.register();
    }

    public CompilerConfiguration getConfiguration() {
        return this.configuration;
    }

}
