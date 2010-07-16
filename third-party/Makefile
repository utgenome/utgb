
.PHONY: all deploy antlr hadoop other jdbc-old svnkit

all : deploy


DEPLOY_GROUP = -DgroupId=org.utgenome.thirdparty  -Dpackaging=jar -DrepositoryId=utgb.dav -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true 

DEPLOY_CMD = mvn deploy:deploy-file  $(DEPLOY_GROUP)

deploy: sam gwt-incubator picard

sam:
	$(DEPLOY_CMD) -DartifactId=sam -Dversion=1.25 -Dfile=lib/sam-1.25.jar

picard:
	$(DEPLOY_CMD) -DartifactId=picard -Dversion=1.25 -Dfile=lib/picard-1.25.jar

GWT_INCUBATOR_VERSION=20100204-r1747

gwt-incubator:
	$(DEPLOY_CMD) -DartifactId=gwt-incubator -Dversion=$(GWT_INCUBATOR_VERSION) -Dfile=lib/gwt-incubator-$(GWT_INCUBATOR_VERSION).jar


