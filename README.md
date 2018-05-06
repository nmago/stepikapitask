# Stepic API test task

### How to build:

`./gradlew build`

OR

`gradlew.bat build`

Shadow plugin is included to build, so the final .jar file (with libs within) is in `build/libs/*-all.jar`

### How to use:
Example1: `java -jar stepikapitest-1.0-all.jar`
It will ask a number N (how many courses in top is needed to return) and whether it should show the loading progress (y/n)

Example2: `echo 5 y | java -jar stepikapitest-1.0-all.jar`
Where 5 is N and y - do show the progress

### Requirements:
* Java version: >= 8
* Internet connection (stepik.org should be available)

