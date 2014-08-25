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
    val q"${ m } class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$stats }" = cd
    val nstats = stats :+ q"def this() = this(1)" // Here things go wrong
    q"""${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$nstats }"""
  }

  def withWorkaround(annottees: Tree*): Tree = annottees match {
    case Seq(cd: ClassDef) =>
      Block(List(adaptClassWithWorkaround(cd)), q"()")
    case _ => ???
  }

  private def adaptClassWithWorkaround(cd: ClassDef): Tree = {
    val q"${ m } class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$stats }" = cd
    val nstats = stats :+ DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(), Block(List(), Apply(Ident(termNames.CONSTRUCTOR), List(Literal(Constant(1))))))
    q"""${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$nstats }"""
  }

}
