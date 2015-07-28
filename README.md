# NoArgsConstructor
Lombok like NoArgsConstructor for Scala using Macros

If you are wondering where is this required and why??
### This is intended for using scala case clasess with libraries like hibernate which demand no args constructor for populating objects via reflection 

ex: https://github.com/selvakn/ScalaHibernateSpringBootSample

[![Build Status](https://snap-ci.com/selvakn/NoArgsConstructor/branch/master/build_image)](https://snap-ci.com/selvakn/NoArgsConstructor/branch/master)

Usage:
------

```scala
@NoArgsConstructor
case class Person(firstName: String, lastName: String)
```
and

```scala
val person = new Person()
```

and Add `org.scalamacros:paradise` to the compiler plugin using [sbt](https://github.com/scalamacros/sbt-example-paradise) or [gradle](https://github.com/selvakn/ScalaHibernateSpringBootSample/blob/master/build.gradle)
