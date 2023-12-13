# JokeInterceptor CDK

Refer https://docs.conduktor.io/gateway/OSS/write-an-interceptor/
#

Use provided application.yml to "enroll" the plugin
Add generated jar to classpath (Plus added dependency)

```bash
export CLASSPATH=jokeinterceptor/target/jokeinterceptor-1.0-SNAPSHOT.jar:~/.m2/repository/org/json/json/20231013/json-20231013.jar
```

Start gateway with :

```bash
./bin/run-gateway.sh
```

Produce some data using :
```bash
./kafka-console-producer.sh --bootstrap-server localhost:6969 --topic sometopic
```

And consume same data (either using gatway or directy to Kafka) and **check header** addedJoke:

```bash
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sometopic --from-beginning --property print.key=true --property print.headers=true --property print.timestamp=true
```

Expected messages :
```
CreateTime:1702483428915        addedJoke:a king cobra once bit Chuck Norris... after 5 days of terrible pain... the snake died null    anotherText
```

## Limition
Just for demo all messages in same intercept will have same joke

##
***Not to be used in production / Only for Demo***
