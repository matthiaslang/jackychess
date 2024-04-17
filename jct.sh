#!/usr/bin/env sh

# exit if a command has non-zero exit code
set -e

PATH=$PATH:.

java -jar tuner/target/tuner-23.12.7-jar-with-dependencies.jar "$@"
