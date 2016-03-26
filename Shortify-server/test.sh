#!/bin/bash
# this script executes the jar containing the JUnit tests for shortify-server
echo "executing JUnit tests..."
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -jar target/shortify-server-test-jar-with-dependencies.jar
