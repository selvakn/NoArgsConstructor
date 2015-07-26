import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context


final class NoArgsConstructor extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro NoArgsConstructorImpl.add
}

final class NoArgsConstructorImpl(val c: Context) {

  import c.universe._

  def add(annottees: Tree*): Tree = annottees match {
    case Seq(cd: ClassDef) =>
      Block(List(adaptClass(cd)), q"()")
    case _ => ???
  }

  private def adaptClass(cd: ClassDef): Tree = {
    val q"${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$stats }" = cd
    val nstats = stats :+ DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(),
      Block(
        List(),
        Apply(Ident(termNames.CONSTRUCTOR), vp(0).map(t => q"""null.asInstanceOf[${t.tpt}]"""))
      ))
    q"""${m} class $name[..$tp] $cm(...$vp) extends { ..$early } with ..$parents { $self => ..$nstats }"""
  }
}
