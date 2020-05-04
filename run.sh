#!/bin/bash

mvn clean && mvn package
cd target/classes/
java Main -g
