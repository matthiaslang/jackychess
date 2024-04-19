#!/usr/bin/env sh

# exit if a command has non-zero exit code
set -e

MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

PATH=$PATH:.

java -jar tuner/target/tuner-${MVNVERSION}-jar-with-dependencies.jar "$@"
