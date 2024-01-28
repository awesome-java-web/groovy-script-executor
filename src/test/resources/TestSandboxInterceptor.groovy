class TestSandboxInterceptor {

    def testSystemGC = {
        System.gc()
    }

    def testSystemExit = {
        System.exit(0)
    }

    def testSystemRunFinalization = {
        System.runFinalization()
    }

    def testRuntimeClass = {
        Runtime.getRuntime().gc()
    }

}
