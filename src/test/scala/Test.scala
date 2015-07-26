// @ctor class Test(i: Int) // will fail with "called constructor's definition must precede calling constructor's definition"

import org.scalatest.FunSpec

@ctorWorkaround class TestWorkaround(i: Int, j: Long, k: String)

class ExampleSpec extends FunSpec {

  describe("test") {

    it("should work") {
      println( new TestWorkaround() )
    }
  }
}