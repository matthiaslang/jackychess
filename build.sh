#!/bin/bash

# helper script to easy create test versions used in my local arena or cutechess environment

# build
mvn clean package -DskipTests

CURRBRANCH=$(git rev-parse --abbrev-ref HEAD)

MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

JARFILE=jackychess-${MVNVERSION}.jar

LOCALARENAFOLDER=../jcversions/
LOCALTESTPROJFOLDER=../jackyChessDockerTesting
ENGINESFILE=${LOCALTESTPROJFOLDER}/scripts/engines.json
VERSIONLOGFILE=${LOCALTESTPROJFOLDER}/versionlog.md

# copy to our folders where the test programs have access:
echo "copy to test folders"
cp -v target/$JARFILE   $LOCALARENAFOLDER
cp -v target/$JARFILE   ${LOCALTESTPROJFOLDER}/jackychess

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



# add the new version to the cutechess engines.json in our test project
if grep -q "$JARFILE" "$ENGINESFILE"; then
  echo "engines config already added..."
else

  # create the cutechess engine definition in a temp file:
  cat << EOF > ${ENGINESFILE}.insert
  {
    "workingDirectory": "/jackychess",
    "command": "java -Djacky.logging.activate=true -Djacky.logging.level=SEVERE -Duser.home=/logs -jar /jackychess/$JARFILE",
    "name": "jacky${MVNVERSION}",
    "protocol": "uci",
    "options": [{"name": "maxThreads", "value": "1"}, {"name": "Hash","value": "128"}]
  },
    {
      "workingDirectory": "/jackychess",
      "command": "java -Djacky.logging.activate=true -Djacky.logging.level=INFO -Duser.home=/logs -jar /jackychess/$JARFILE",
      "name": "jacky${MVNVERSION}4T",
      "protocol": "uci",
      "options": [{"name": "maxThreads", "value": "4"}, {"name": "Hash","value": "512"}]
    },
    {
      "workingDirectory": "/jackychess",
      "command": "java -Djacky.logging.activate=true -Djacky.logging.level=INFO -Duser.home=/logs -jar /jackychess/$JARFILE",
      "name": "jacky${MVNVERSION}INFO",
      "protocol": "uci",
      "options": [{"name": "maxThreads", "value": "1"}, {"name": "Hash","value": "128"}]
    },
EOF
# insert the temp file after the first match of "[" in the config file:
    sed -i.bak -e "0,/\[/r${ENGINESFILE}.insert" $ENGINESFILE


# write some version infos into the version log file
GITLOG=$(git log develop..${CURRBRANCH}  --pretty=oneline)

 cat << EOF > ${VERSIONLOGFILE}.insert
# $JARFILE

- Tag $TAGNAME
- Git Describe $GITDESCR


## Changes

\`\`\`
$GITLOG
\`\`\`

## Results

EOF

echo -e "$(cat ${VERSIONLOGFILE}.insert)\n$(cat ${VERSIONLOGFILE})" > ${VERSIONLOGFILE}

fi

# add the created jar to the git repository of the cutechess test project:
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  jackychess/$JARFILE