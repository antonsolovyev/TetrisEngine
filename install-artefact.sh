#!/bin/sh

mvn install:install-file -Dfile=./jar/tetris_engine.jar  -DgroupId=com.solovyev.games.tetris -DartifactId=tetris_engine -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
