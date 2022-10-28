#!/bin/bash

# helper script to easy create test versions used in my local arena or cutechess environment

# build
mvn clean package -DskipTests

MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

JARFILE=jackychess-${MVNVERSION}.jar

# copy to our folders where the test programs have access:
echo "copy to test folders"
cp -v target/jackychess*   ../jcversions/
cp -v target/jackychess*   ../jackyChessDockerTesting/jackychess

# copy a windows bat file for the arena test folder with some log settings
BATFILE=jc-${MVNVERSION}.bat
cat << EOF > ../jcversions/${BATFILE}
java  -Xmx2024M  -Djacky.logging.activate=true -Djacky.logging.dir=c:/logs  -Djacky.logging.level=INFO -Djacky.logging.file=jc-${MVNVERSION}  -jar ${JARFILE}

EOF
# convert windows lfs:
unix2dos  ../jcversions/${BATFILE}
# make it executable; sets in cygwin the window right for execution on the file:
chmod +x  ../jcversions/${BATFILE}

# create a temporary tag:
#GITDESCR=$(git describe --tags)
GITDESCR=$(git rev-parse --short HEAD)

TAGNAME=${MVNVERSION}_${GITDESCR}

echo "creating tag"
git tag ${TAGNAME}