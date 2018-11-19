import sbt._

object Dependencies {

  import Versions._

  /**
    * logging related libaries
    */
  object Logging {
    val logback: ModuleID = "ch.qos.logback" % "logback-classic" % logbackVersion
    val logstashLogback: ModuleID = "net.logstash.logback" % "logstash-logback-encoder" % logstashLogbackVersion
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  }

  val loggingLib: Seq[ModuleID] = Seq(Logging.logback, Logging.logstashLogback, Logging.scalaLogging)

  /**
    * spark related libaries
    */
  object Spark {
    val sparkCassConnector = "datastax" % "spark-cassandra-connector" % "2.3.1-s_2.11"
    val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion excludeAll(ExclusionRule(name = "slf4j-log4j12"))
    val config = "com.typesafe" % "config" % sparkConfigVersion
    val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  }

  val sparkLib = Seq(Spark.sparkCassConnector, Spark.sparkCore, Spark.config, Spark.sparkSql)

  /** Module deps */
  val moduleDependencies: Seq[ModuleID] = loggingLib ++ sparkLib
}