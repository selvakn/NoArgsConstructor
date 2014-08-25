
@ctorWorkaround class TestWorkaround(i: Int) // OK

@ctor class Test(i: Int) // will fail with "called constructor's definition must precede calling constructor's definition"
