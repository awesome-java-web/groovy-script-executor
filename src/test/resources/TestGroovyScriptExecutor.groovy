class TestGroovyScriptExecutor {

    def testInvokeMethodNoArgs = { ->
        return Integer.MAX_VALUE
    }

    def testInvokeMethodWithArgs = { int x ->
        return Math.pow(x, 2) * 10
    }

    def testInvokeMethodWithTwoArgs = { int x, int y ->
        return Math.pow(x, y) - 1
    }

}