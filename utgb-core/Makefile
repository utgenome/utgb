all: trackloader


ant: 
	ant

trackloader:
	ant generateTrackFactorySetup
	

EGT2XML:
	mvn assembly:single -Ddescriptor=src/main/assembly/EGT2XML.xml -DfinalName=EGT2XML.jar
	

draft: 
	mvn dependency:build-classpath -Dmdep.outputFile="target/classpath"
	ant -f build-gwt.xml draft-compile -Dgwt.module=org.utgenome.gwt.utgb.UTGBEntry

compile: 
	mvn dependency:build-classpath -Dmdep.outputFile="target/classpath"
	ant -f build-gwt.xml compile -Dgwt.module=org.utgenome.gwt.utgb.UTGBEntry
	