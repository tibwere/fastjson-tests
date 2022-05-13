#!/bin/sh

# Remember that working dir is set to ${project.baseDir}
JACOCOCLI="./src/test/lib/jacococli.jar"
FASTJSONJAR="./src/test/lib/fastjson-1.2.79.jar"
FASTJSONSRC="./original/fastjson-1.2.79/src/main/java"
TARGETDIR="./target/site/jacoco"

mkdir -p ${TARGETDIR}

java -jar ${JACOCOCLI} report target/jacoco.exec \
  --classfiles ${FASTJSONJAR} \
  --sourcefiles ${FASTJSONSRC} \
  --html "${TARGETDIR}/" \
  --xml "${TARGETDIR}/jacoco.xml" \
  --csv "${TARGETDIR}/jacoco.csv"
