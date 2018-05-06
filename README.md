# Stepik API test task

[![Build status](https://travis-ci.org/nmago/stepikapitask.svg?branch=master)](https://travis-ci.org/nmago/stepikapitask/builds/375575898)

### How to build:

$`./gradlew build`

OR

$`gradlew.bat build`

Shadow plugin is included to build, so the final .jar file (with libs within) will be in `build/libs/*-all.jar`
*Note:* You can run already built jar that is in [built-jar](https://github.com/nmago/stepikapitask/tree/master/built-jar) dir

### How to use:
Example1: $`java -jar stepikapitest-1.0-all.jar`
It will ask a number N (how many courses in top is needed to return) and whether it should show the loading progress (y/n)

Example2: $`echo 5 y | java -jar stepikapitest-1.0-all.jar`
Where 5 is N and y - do show the progress

### Requirements:
* Java version: >= 8
* Internet connection (stepik.org should be available)
