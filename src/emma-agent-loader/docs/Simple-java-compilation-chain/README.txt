javac ./HelloWorld.java
jar cfm Hello.jar ./MANIFEST.MF ./*.class
java -jar ./Hello.jar
