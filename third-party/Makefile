
.PHONY: all deploy antlr hadoop other jdbc-old svnkit

all : deploy


DEPLOY_GROUP = -DgroupId=org.utgenome.thirdparty  -Dpackaging=jar -DrepositoryId=utgb.dav -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true 

DEPLOY_CMD = mvn deploy:deploy-file  $(DEPLOY_GROUP)

deploy: sam gwt-incubator picard

PICARD_VERSION=1.35

sam:
	$(DEPLOY_CMD) -DartifactId=sam -Dversion=$(PICARD_VERSION) -Dfile=lib/sam-$(PICARD_VERSION).jar

picard: sam
	$(DEPLOY_CMD) -DartifactId=picard -Dversion=$(PICARD_VERSION) -Dfile=lib/picard-$(PICARD_VERSION).jar

GWT_INCUBATOR_VERSION=20100204-r1747

gwt-incubator:
	$(DEPLOY_CMD) -DartifactId=gwt-incubator -Dversion=$(GWT_INCUBATOR_VERSION) -Dfile=lib/gwt-incubator-$(GWT_INCUBATOR_VERSION).jar


