package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.awesome.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

public class GroovyScriptCompiler {

    private final CompilerConfiguration configuration;

    public static GroovyScriptCompiler initialize() {
        return new GroovyScriptCompiler();
    }

    public CompilerConfiguration getConfiguration() {
        return this.configuration;
    }

    public GroovyScriptCompiler() {
        this.configuration = new CompilerConfiguration();
        this.addCompilationCustomizers();
        this.registerSecurityInterceptor();
    }

    public void addCompilationCustomizers() {
        this.configuration.addCompilationCustomizers(new SandboxTransformer());
    }

    public void registerSecurityInterceptor() {
        new SystemClassInterceptor().register();
        new RuntimeClassInterceptor().register();
    }

}
