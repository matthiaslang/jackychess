#!/bin/bash

# script to create a "test" release for verification testing in tournaments, etc.

if [ -n "$1" ]; then
  echo "building test version named $1"
else
    echo "Argument: Name needed!"
    break 1;
fi


NAME=$1

TESTBRANCHNAME=testing/${NAME}

CURRBRANCH=$(git rev-parse --abbrev-ref HEAD)
MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

MVNTESTVERSION=${MVNVERSION}${NAME}

echo "set new maven version ${MVNTESTVERSION}"
mvn versions:set -DnewVersion=${MVNTESTVERSION}

git checkout -b ${TESTBRANCHNAME}

git add pom.xml **/pom.xml
git commit -m "testversion $MVNTESTVERSION"
git push --set-upstream origin $TESTBRANCHNAME

# building test version
bash ./build.sh


# checkout original source branch
git checkout $CURRBRANCH