import scala.language.experimental.macros
import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly
import scala.annotation.implicitNotFound
import scala.reflect.macros.blackbox.Context


final class ctor extends StaticAnnotation {

  def macroTransform(annottees: Any*): Any = macro CtorImpl.withQQ

}

final class ctorWorkaround extends StaticAnnotation {

  def macroTransform(annottees: Any*): Any = macro CtorImpl.withWorkaround

}

final class CtorImpl(val c: Context) {

  import c.universe._

  def withQQ(annottees: Tree*): Tree = annottees match {
    case Seq(cd: ClassDef) =>
      Block(List(adaptClass(cd)), q"()")
    case _ => ???
  }

  private def adaptClass(cd: ClassDef): Tree = {
    val q"${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$stats }" = cd
    val nstats = stats :+ q"def this() = this(0)" // Here things go wrong
    q"""${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$nstats }"""
  }

  def withWorkaround(annottees: Tree*): Tree = annottees match {
    case Seq(cd: ClassDef) =>
      Block(List(adaptClassWithWorkaround(cd)), q"()")
    case _ => ???
  }

  private def adaptClassWithWorkaround(cd: ClassDef): Tree = {
    val q"${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$stats }" = cd
//    debugValDef(vp(0)(0))
//    debugValDef(vp(0)(1))
//    debugValDef(vp(0)(2))
    val res = vp(0).map(t => q"""null.asInstanceOf[${t.tpt}]""")
    debug(res)
    val nstats = stats :+ DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(),
      Block(
        List(),
        Apply(Ident(termNames.CONSTRUCTOR), res
//          List(
//            q"""null.asInstanceOf[Int]""",
//            Literal(Constant(0)),
//            Literal(Constant(""))
//          )
        )
      ))
    q"""${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$nstats }"""
  }

  private def debug(any: Any) = {
    println(any)
    if (any != null)
      println(any.getClass)
  }

  def debugValDef(t: ValDef): Unit = {
    println("==================================")

    //    debug(t)
//    debug(t.tpt.toString())
    val s = q"""return null.asInstanceOf[${t.tpt}]"""
    debug(s)
    debug(c.eval(c.Expr(s)))
//    debug(s)
//    debug(t.rhs)
//    debug(t.name)
//    debug(t.mods)
//    debug(t.children)

    //    val t = vp(0)(0).tpt
    //    debug(null.asInstanceOf[t])
  }
}
