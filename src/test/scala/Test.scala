import org.scalatest.FunSpec

@NoArgsConstructor
case class TestKlass(integerProperty: Int, longProperty: Long, stringProperty: String)

class ExampleSpec extends FunSpec {

  describe("test") {

    it("should work") {
      val instance = new TestKlass()

      assert(instance.integerProperty === 0)
      assert(instance.longProperty === 0)
      assert(instance.stringProperty === null)
    }
  }
}