LOGIN="LOGINs"
JUNIT="junit-platform-console-standalone-1.11.4.jar"
TOOL="ijatool.jar"

.PHONY: all test clean zip run

all:
	javac -cp .:${JUNIT}:${TOOL} -d cls ija/ija2024/homework2/Homework2Test.java

run: all
	java -cp cls:${TOOL} ija/ija2024/homework2/Homework2.java 

test: all
	java -ea -cp cls:${JUNIT}:${TOOL} org.junit.platform.console.ConsoleLauncher execute --scan-class-path

clean:
	rm -rf cls

zip:
	zip ${LOGIN}.zip -r ija