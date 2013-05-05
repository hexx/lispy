import sbt._,Keys._

object Build extends Build {
  lazy val baseSettings = Seq(
    scalaVersion := "2.10.1",
    organization := "com.github.hexx",
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-language:_")
  )

  lazy val lispy = Project(
    id = "lispy",
    base = file(".")
  ).settings(
    baseSettings ++ seq(
      name := "lispy",
      version := "0.0.1",
      libraryDependencies ++= Seq(
      ),
      initialCommands in console += Seq(
        "com.github.hexx.lispy._"
      ).map("import " + _ + "\n").mkString
    ) : _*
  )

  lazy val lispyExample = Project(
    id = "lispy-example",
    base = file("example")
  ).settings(
    baseSettings  ++ seq(
      initialCommands in console += Seq(
        "com.github.hexx.lispy._",
        "Example._"
      ).map("import " + _ + "\n").mkString,
      initialCommands in console += "def parse(s: String) = Parser.parseAll(Parser.exp, s).get"
    ) : _*
  ).dependsOn(lispy)
}
