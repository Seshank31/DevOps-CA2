#!/bin/zsh

set -e

cd "$(dirname "$0")"

javac -cp ".:lib/selenium-server-4.40.0.jar" com/selenium/test/TestPro.java
java -cp ".:lib/selenium-server-4.40.0.jar" com.selenium.test.TestPro
