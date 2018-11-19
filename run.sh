#add URL in following format in double quote cassandra://<user>:<pwd>@<node0>:<port>/keyspace?dc=<dcname>&node=<nodename>&node=<nodename>
cassandraURl=$1
tableName=$2
if [[ -z "$cassandraURl" ]]; then
   echo "Cassandra Connection URL is missing"
   exit 1
fi

if [[ -z "$tableName" ]]; then
   echo "Target table name is missing"
   exit 1
fi

sbt assembly

java -DCASSANDRA_URL=$cassandraURl -jar target/scala-2.11/cassandracountrows-assembly-1.0.jar $tableName
