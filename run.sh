#!/bin/sh

THE_CLASSPATH=
PROGRAM_NAME=src/h1b_analysis.java
CLASS_NAME=solution


javac  $PROGRAM_NAME

if [ $? -eq 0 ]
then
  echo "compile worked!"
  cd src
  java  $CLASS_NAME ../input/H1B_FY_2014.csv
fi