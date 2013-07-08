#!/bin/sh

DIRNAME=`dirname $0`
CLASSPATH=$DIRNAME/mimamememu.jar

java -cp $CLASSPATH -Djava.util.logging.config.file=$DIRNAME/logging.properties -Ddirname.path=$DIRNAME/ com.adr.mmmmm.Main "$@"
