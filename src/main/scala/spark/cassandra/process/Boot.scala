package spark.cassandra.process


import com.datastax.spark.connector._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory
import spark.cassandra.model.CassandraConnectionConfig

import scala.util.Try

/**
  * Spark Program to compute row counts
  * set cassandra detail in application.conf or pass as VM option -DCASSANDRA_URL
  * program requires input as a table name
  * it can run directly from IntelliJ or
  * from spark-submit with your local or remote spark cluster
  * Created by arun_sethia01 on 11/13/18.
  */
object CassandraRowCount extends App {

  val logger = LoggerFactory.getLogger(this.getClass.getName)

  //load application.conf file
  final implicit val config: Config = ConfigFactory.load()

  if (args.length < 1) {
    logger.warn("please provide the table name")
    sys.exit(1)
  }

  //read cassandra URL detail , user can provide either from application.conf
  //or via -DCASSANDRA_URL as JVM argument
  val cassandraUrl: Option[String] = Try {
    config.getString("cassandra.url")
  }.toOption

  val casUrl = cassandraUrl match {
    case Some(v) => v
    case _ => {
      logger.warn(s"Cassandra URL is missing It should be in following format: cassandra://<user>:<pwd>@<node0>:<port>/keyspace?dc=<dcname>&node=<nodename>&node=<nodename>")
      logger.warn("you can also pass the cassandra URL from the application.conf or via JVM options -DCASSANDRA_URL=")
      sys.exit(1)
    }
  }

  val Array(tablename) = args

  val cassConfig: CassandraConnectionConfig = CassandraConnectionConfig(config.getString("cassandra.url"))

  val conf = new SparkConf(true)
    .set("spark.cassandra.connection.host", cassConfig.hosts.mkString(","))
    .set("spark.cassandra.auth.username", cassConfig.user)
    .set("spark.cassandra.auth.password", cassConfig.password)

  val sc = new SparkContext("local[*]", s"table_row_count_${cassConfig.keyspace}_${tablename}", conf)

  val rdd = sc.cassandraTable(cassConfig.keyspace, tablename)

  logger.info(s"*************************** total row count: ${rdd.cassandraCount()} *****************")

}

