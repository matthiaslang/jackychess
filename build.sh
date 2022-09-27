#!/bin/bash

# helper script to easy create test versions used in my local arena or cutechess environment

# build
mvn clean package -DskipTests

# copy to our folders where the test programs have access:
echo "copy to test folders"
cp -v target/jackychess*   ../jcversions/
cp -v target/jackychess*   ../jackyChessDockerTesting/jackychess

# create a temporary tag:
MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

#GITDESCR=$(git describe --tags)
GITDESCR=$(git rev-parse --short HEAD)

TAGNAME=${MVNVERSION}_${GITDESCR}

echo "creating tag"
git tag ${TAGNAME}