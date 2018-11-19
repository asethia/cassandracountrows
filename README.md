# cassandracountrows
This will able to run count(*) on large Cassandra table to compute number of rows. The code base is created using spark and [cassandra connector](https://github.com/datastax/spark-cassandra-connector).

You can run this program using run.sh or from your preferred editor like eclise/IntelliJ.

1. running from run.sh - You must provide CASSANDRA_URL and TABLE_NAME in run.sh before running run.sh 
  The CASSANDRA URL should be in following format cassandra://user:pwd\@\<nodename0\>:\<port\>/\<keyspace\>?dc=dcname\&node=\<nodename1\>\&node=\<nodename2\> 

2. from IntelliJ passing -DCASSANDRA_URL as VM Option and table name as program argument. 
