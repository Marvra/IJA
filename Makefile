all:
	mvn install:install-file   -Dfile=lib/ijatool.jar   -DgroupId=cz.vutbr.fit   -DartifactId=ijatool   -Dversion=1.0   -Dpackaging=jar
	mvn clean package

run:
	java --module-path /usr/lib/jvm/openjfx/  --add-modules javafx.controls,javafx.fxml -jar target/Connect-it-1.0.jar