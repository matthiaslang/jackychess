#!/bin/bash

# script to create a new version with a new version number.

if [ -n "$1" ]; then
  echo "building version named $1"
else
    echo "Argument: Name needed!"
    break 1;
fi


NEWMVNVERSION=$1

echo "set new maven version ${NEWMVNVERSION}"
mvn versions:set -DnewVersion=${NEWMVNVERSION}


git add pom.xml **/pom.xml
git commit -m "Version $NEWMVNVERSION"
git push

# building test version
bash ./build.sh