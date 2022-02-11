#!/bin/bash
set -e
docker-compose down
docker-compose up -d

mvn clean package

java -jar target/dropwizard-sample-1.0-SNAPSHOT.jar db migrate config.yml
java -jar target/dropwizard-sample-1.0-SNAPSHOT.jar hbase
