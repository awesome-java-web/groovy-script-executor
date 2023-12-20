package com.github.awesome.scripting.groovy.security;

public enum DefaultShouldInterceptKeywords {

    SYSTEM_GC("System.gc()", "normally we should not call the System.gc() method manually in the code"),
    SYSTEM_EXIT("System.exit()", "System.exit() method will terminate the currently running JVM, we think it's dangerous"),

    ;

    private final String keyword;

    private final String explanation;

    DefaultShouldInterceptKeywords(String keyword, String explanation) {
        this.keyword = keyword;
        this.explanation = explanation;
    }

    public static DefaultShouldInterceptKeywords fromKeyword(String keyword) {
        DefaultShouldInterceptKeywords[] keywords = DefaultShouldInterceptKeywords.values();
        for (DefaultShouldInterceptKeywords kw : keywords) {
            if (kw.getKeyword().equals(keyword)) {
                return kw;
            }
        }
        return null;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getExplanation() {
        return explanation;
    }

}
