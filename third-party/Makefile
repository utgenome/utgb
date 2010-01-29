
.PHONY: all deploy antlr hadoop other jdbc-old svnkit

all : deploy


DEPLOY_GROUP = -DgroupId=org.utgenome.thirdparty  -Dpackaging=jar -DrepositoryId=utgb.dav -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true 

DEPLOY_CMD = mvn deploy:deploy-file  $(DEPLOY_GROUP)

deploy: sam gwt-incubator

sam:
	$(DEPLOY_CMD) -DartifactId=sam -Dversion=1.11 -Dfile=lib/sam-1.11.jar

gwt-incubator:
	$(DEPLOY_CMD) -DartifactId=gwt-incubator -Dversion=20091216-r1739 -Dfile=lib/gwt-incubator-20091216-r1739.jar

