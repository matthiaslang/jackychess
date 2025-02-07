#!/bin/bash

# script to create a "test" release for verification testing in tournaments, etc.

NAME=$1

TESTBRANCHNAME=testing/${NAME}

CURRBRANCH=$(git rev-parse --abbrev-ref HEAD)
MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

MVNTESTVERSION=${MVNVERSION}${NAME}

echo "set new maven version ${MVNTESTVERSION}"
mvn versions:set -DnewVersion=${MVNTESTVERSION}

git checkout -b ${TESTBRANCHNAME}

git add .
git commit -m "testversion $MVNTESTVERSION"
git push --set-upstream origin $TESTBRANCHNAME

# building test version
bash ./build.sh


# checkout original source branch
git checkout $CURRBRANCH