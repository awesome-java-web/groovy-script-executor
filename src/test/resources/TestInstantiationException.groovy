/**
 * {@link InstantiationException} will be thrown when you try to create an instance of a class using the {@code newInstance()} method
 * in {@link Class} class, but the specified class object cannot be instantiated because it is an interface or is an abstract class.
 * we use this feature for unit test coverage.
 */
interface TestInstantiationException {
    void test()
}
