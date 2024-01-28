package com.github.awesome.scripting.groovy.support;

import com.github.awesome.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.awesome.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

public class GroovyCompilerConfigurationSupport {

    private final CompilerConfiguration configuration;

    public static void initialize() {
        new GroovyCompilerConfigurationSupport();
    }

    public GroovyCompilerConfigurationSupport() {
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
