#!/bin/sh

JACOCOCLI="./src/test/lib/jacococli.jar"
FASTJSONJAR="./src/test/lib/fastjson-1.2.79.jar"
FASTJSONSRC="./src/test/resources/fastjson-1.2.79/src"
TARGETDIR="./target/site/jacoco"

mkdir -p ${TARGETDIR}

java -jar ${JACOCOCLI} report target/jacoco.exec \
  --classfiles ${FASTJSONJAR} \
  --sourcefiles ${FASTJSONSRC} \
  --html "${TARGETDIR}/" \
  --xml "${TARGETDIR}/jacoco.xml" \
  --csv "${TARGETDIR}/jacoco.csv"
