class TestGroovyScriptExecutor {

    def testInvokeMethodNoArgs = { ->
        return 1024
    }

    def testInvokeMethodWithArgs = { int a ->
        return a + 1
    }

    def testInvokeMethodWithTwoArgs = { int a, int b ->
        return a + b
    }

}