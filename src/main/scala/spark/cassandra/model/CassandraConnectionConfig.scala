package spark.cassandra.model

import java.net.URI

/**
  * This is model class to hold cassandra connection configuration
  * Created by Arun Sethia on 11/18/18.
  */
case class CassandraConnectionConfig(hosts: List[String], port: Int, user: String,
                                     password: String, keyspace: String)


object CassandraConnectionConfig {
  /**
    * create CassandraConnectionConfig object from given cassandra configuration
    *
    * @param cassandraUrl
    * @return
    */
  def apply(cassandraUrl: String) = createCassandraConfig(cassandraUrl)

  /**
    * default cassandra DB port
    */
  private final val DEFAULT_PORT = 9042

  /**
    * provided cassandra URL build cassandra db config object
    * cassandra://<user>:<pwd>@<node>:<port>/<schema_name>?dc=<dcname>&node=<node1>&node=<node2>&node=<node3>
    *
    * @param cassandraUrl
    * @return
    */
  private def createCassandraConfig(cassandraUrl: String): CassandraConnectionConfig = {
    val parseURI: URI = new URI(cassandraUrl)
    val databaseHosts: List[String] =
      parseURI.getRawQuery.split("&")
        .filter(_.contains("node="))
        .map(_.replaceAll("node=", ""))
        .toList
    val (userId, pwd) = Some(parseURI.getUserInfo) match {
      case Some(userInfo) => userInfo.split(":") match {
        case Array(user, password) => (user, password)
        case Array(user) => (user, "")
      }
    }
    val cassandraKeyspace = parseURI.getPath.replaceFirst("/", "")
    //get database port, if not present then take default value
    val databasePort = Some(parseURI.getPort).getOrElse(DEFAULT_PORT)

    CassandraConnectionConfig(databaseHosts, databasePort, userId, pwd, cassandraKeyspace)
  }
}