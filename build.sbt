
lazy val root = (project in file("."))
.settings(
  name := "af-extractor",
  organization := "com.reali",
  scalaVersion := "2.12.12",
  version := "0.1.0-SNAPSHOT",

  libraryDependencies ++= Seq(
    "org.scalikejdbc" %% "scalikejdbc" % "3.3.0",
    "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.0",
    "org.postgresql" % "postgresql" % "42.2.5",
    "ch.qos.logback"  %  "logback-classic" % "1.2.3",
    "com.amazonaws" % "aws-java-sdk-s3" % "1.11.349",
    "ch.qos.logback.contrib" % "logback-json-classic" % "0.1.5",
    "software.amazon.awssdk" % "secretsmanager" % "2.5.69",
    "org.json4s" %% "json4s-jackson" % "3.2.11",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.18.1-play27",
    "org.reactivemongo" %% "reactivemongo-play-json" % "0.18.7-play27",
   "org.mongodb.scala" %% "mongo-scala-driver" % "2.3.0"
  )
)


