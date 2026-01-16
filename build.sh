#!/bin/bash

# helper script to easy create test versions used in my local arena or cutechess environment


CREATETAGARG=$1

# build via docker image:
#docker volume create --name maven-repo
#docker run -it --rm --name my-maven-project -v maven-repo:/root/.m2 -v .:/usr/src/mymaven -w /usr/src/mymaven maven:3.9.9-eclipse-temurin-21 mvn clean package -DskipTests
mvn clean package -DskipTests

CURRBRANCH=$(git rev-parse --abbrev-ref HEAD)

MVNVERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

JARFILE=jackychess-${MVNVERSION}.jar
JARFILENNUE=jackychess-nnue-${MVNVERSION}.jar
TUNERJARFILE=tuner-${MVNVERSION}-jar-with-dependencies.jar
CLASSICMODULE=jcClassic
NNUEMODULE=jcNNUE


LOCALARENAFOLDER=../jcversions/
LOCALTESTPROJFOLDER=../jackyChessDockerTesting
ENGINESFILE=${LOCALTESTPROJFOLDER}/scripts/engines.json
VERSIONLOGFILE=${LOCALTESTPROJFOLDER}/versionlog.md
DETAILEDVERSIONLOGFILE=${LOCALTESTPROJFOLDER}/results/resultoverview/jacky${MVNVERSION}.md
SHREDDERENGFILE=engine/target/jackychess-${MVNVERSION}.eng
SHREDDERENGFILENNUE=engine/target/jackychess-nnue-${MVNVERSION}.eng

# copy to our folders where the test programs have access:
echo "copy to test folders"
cp -v $CLASSICMODULE/target/$JARFILE   $LOCALARENAFOLDER
cp -v $NNUEMODULE/target/$JARFILENNUE   $LOCALARENAFOLDER
cp -v $CLASSICMODULE/target/$JARFILE   ${LOCALTESTPROJFOLDER}/jackychess
cp -v $NNUEMODULE/target/$JARFILENNUE   ${LOCALTESTPROJFOLDER}/jackychess
cp -v tuner/target/$TUNERJARFILE   ${LOCALTESTPROJFOLDER}/jackychess

# copy a bash file for starting the engine within cutechess
BASHFILE=jc-${MVNVERSION}.sh
cat << EOF > ${LOCALTESTPROJFOLDER}/jackychess/${BASHFILE}
#!/bin/bash
java -Djacky.logging.activate=true -Djacky.logging.level=SEVERE -Duser.home=/logs -jar /jackychess/$JARFILE
EOF
chmod +x  ${LOCALTESTPROJFOLDER}/jackychess/${BASHFILE}

BASHFILENNUE=jcnnue-${MVNVERSION}.sh
cat << EOF > ${LOCALTESTPROJFOLDER}/jackychess/${BASHFILENNUE}
#!/bin/bash
java -Djacky.logging.activate=true -Djacky.logging.level=SEVERE -Duser.home=/logs --add-modules jdk.incubator.vector -jar /jackychess/$JARFILENNUE
EOF
chmod +x  ${LOCALTESTPROJFOLDER}/jackychess/${BASHFILENNUE}

# copy a windows bat file for the arena test folder with some log settings
BATFILE=jc-${MVNVERSION}.bat
cat << EOF > ../jcversions/${BATFILE}
java  -Djacky.logging.activate=true -Djacky.logging.dir=c:/logs  -Djacky.logging.level=INFO -Djacky.logging.file=jc-${MVNVERSION}  -jar ${JARFILE}

EOF
# convert windows lfs:
unix2dos  ../jcversions/${BATFILE}
# make it executable; sets in cygwin the window right for execution on the file:
chmod +x  ../jcversions/${BATFILE}

BATFILE=jcnnue-${MVNVERSION}.bat
cat << EOF > ../jcversions/${BATFILE}
java  -Djacky.logging.activate=true -Djacky.logging.dir=c:/logs  -Djacky.logging.level=INFO -Djacky.logging.file=jc-${MVNVERSION} --add-modules jdk.incubator.vector -jar ${JARFILENNUE}

EOF
# convert windows lfs:
unix2dos  ../jcversions/${BATFILE}
# make it executable; sets in cygwin the window right for execution on the file:
chmod +x  ../jcversions/${BATFILE}

# create a temporary tag:
#GITDESCR=$(git describe --tags)
GITDESCR=$(git rev-parse --short HEAD)

if [ "$CREATETAGARG" == "createTag" ]
then
TAGNAME=${MVNVERSION}_${GITDESCR}
echo "creating tag"
git tag ${TAGNAME}

fi

# add the new version to the cutechess engines.json in our test project
if grep -q "$JARFILE" "$ENGINESFILE"; then
  echo "engines config already added..."
else

  # create the cutechess engine definition in a temp file:
  cat << EOF > ${ENGINESFILE}.insert
  {
    "workingDirectory": "/jackychess",
    "command": "./${BASHFILE}",
    "name": "jacky${MVNVERSION}",
    "protocol": "uci",
    "options": [{"name": "Threads", "value": "1"}, {"name": "Hash","value": "128"}]
  },
    {
      "workingDirectory": "/jackychess",
      "command": "./${BASHFILE}",
      "name": "jacky${MVNVERSION}4T",
      "protocol": "uci",
      "options": [{"name": "Threads", "value": "4"}, {"name": "Hash","value": "512"}]
    },
  {
    "workingDirectory": "/jackychess",
    "command": "./${BASHFILENNUE}",
    "name": "jackynnue${MVNVERSION}",
    "protocol": "uci",
    "options": [{"name": "Threads", "value": "1"}, {"name": "Hash","value": "128"}]
  },
    {
      "workingDirectory": "/jackychess",
      "command": "./${BASHFILENNUE}",
      "name": "jackynnue${MVNVERSION}4T",
      "protocol": "uci",
      "options": [{"name": "Threads", "value": "4"}, {"name": "Hash","value": "512"}]
    },
EOF
# insert the temp file after the first match of "[" in the config file:
sed -i.bak -e "0,/\[/r${ENGINESFILE}.insert" $ENGINESFILE


# write some version infos into the version log file
GITLOG=$(git log develop..${CURRBRANCH}  --pretty=oneline)

 cat << EOF > $DETAILEDVERSIONLOGFILE
 # $JARFILE

 - Tag $TAGNAME
 - Branch ${CURRBRANCH}
 - Git Describe $GITDESCR
 - Maven Version ${MVNVERSION}
 - Cutechess name: jacky${MVNVERSION}


 ## Changes compared to develop

 \`\`\`
 $GITLOG
 \`\`\`

 ## Results

EOF


 cat << EOF > ${VERSIONLOGFILE}.insert
# $JARFILE $JARFILENNUE

- Tag $TAGNAME
- Branch ${CURRBRANCH}
- Git Describe $GITDESCR
- Maven Version ${MVNVERSION}
- Cutechess name: jacky${MVNVERSION}
- Cutechess name: jackynnue${MVNVERSION}


## Changes compared to develop

\`\`\`
$GITLOG
\`\`\`

## Results

---

EOF

echo -e "$(cat ${VERSIONLOGFILE}.insert)\n$(cat ${VERSIONLOGFILE})" > ${VERSIONLOGFILE}

fi

# add the created jar to the git repository of the cutechess test project:
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  jackychess/$JARFILE
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  jackychess/$JARFILENNUE
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add --chmod=+x jackychess/${BASHFILE}
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add --chmod=+x jackychess/${BASHFILENNUE}
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  scripts/engines.json
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  versionlog.md
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  results/resultoverview/jacky${MVNVERSION}.md
git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git commit -m "testversion ${MVNVERSION}"
#git --work-tree $LOCALTESTPROJFOLDER/ --git-dir $LOCALTESTPROJFOLDER/.git add  jackychess/$TUNERJARFILE



echo "creating shredder engine file"
# create shredder eng file definition for my local system
  cat << EOF > ${SHREDDERENGFILE}
[ENGINE]
Name=jackychess${MVNVERSION}
Author=Matthias Lang
Filename=C:\Users\MLang\.jdks\temurin-21.0.5\bin\java.exe
Parameter=-jar C:\projekte\cygwin_home\mla\jackyChessDockerTesting\jackychess\\${JARFILE}

EOF
  cat << EOF > ${SHREDDERENGFILENNUE}
[ENGINE]
Name=jackychessnnue${MVNVERSION}
Author=Matthias Lang
Filename=C:\Users\MLang\.jdks\temurin-21.0.5\bin\java.exe
Parameter=--add-modules jdk.incubator.vector -jar C:\projekte\cygwin_home\mla\jackyChessDockerTesting\jackychess\\${JARFILENNUE}

EOF


echo "copying shredder engine file"
cp ${SHREDDERENGFILE} /mnt/c/users/mlang/AppData/Local/ShredderChess/GUI13/Engines
cp ${SHREDDERENGFILENNUE} /mnt/c/users/mlang/AppData/Local/ShredderChess/GUI13/Engines