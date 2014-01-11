name := "akka-dbms-layer"

version := "0.1"

organization := "DisBrain srl"

scalaVersion := "2.10.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
   "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3",
   "com.typesafe.akka" % "akka-slf4j_2.10" % "2.2.3",
   "com.typesafe.atmos" % "trace-akka-2.2.1_2.10" % "1.3.1",
   "ch.qos.logback" % "logback-classic" % "1.0.7",
   "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
   "c3p0" % "c3p0" % "0.9.1.2",
   "mysql" % "mysql-connector-java" % "5.1.6",
   "junit" % "junit" % "4.11" % "test",
   "com.novocode" % "junit-interface" % "0.10" % "test",
   "com.google.protobuf" % "protobuf-java" % "2.5.0" )

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")

compileOrder := CompileOrder.JavaThenScala

javacOptions ++= Seq( "-Xlint" )

javaOptions ++= Seq( "-javaagent:lib/weaver/aspectjweaver.jar",
                     "-Dorg.aspectj.tracing.factory=default",
                     "-Djava.library.path=lib/sigar/",
                     "-classpath" )
