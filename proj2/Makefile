LOGIN="LOGINs"
JUNIT="junit-platform-console-standalone-1.11.4.jar"

.PHONY: all test clean zip

all:
	javac -cp .:${JUNIT} -d cls ija/ija2024/homework2/Homework2Test.java
	
test: all
	java -ea -jar ./${JUNIT} execute --cp cls --scan-class-path
	
clean:
	rm -rf cls
	
zip:
	zip ${LOGIN}.zip -r ija

