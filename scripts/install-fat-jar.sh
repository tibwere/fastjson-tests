#!/bin/sh
FATJAR="./src/test/lib/fastjson-1.2.79-fat.jar"

mvn install:install-file \
	-Dfile=${FATJAR} \
	-DgroupId=com.alibaba \
	-DartifactId=fastjson-fat \
	-Dversion=1.2.79 \
	-Dpackaging=jar
