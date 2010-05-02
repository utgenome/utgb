
.PHONY: install test release-prepare release-perform


install:
	mvn install -Dmaven.test.skip=true
	cd utgb-shell; make MVN_OPTS='-Dmaven.test.skip=true' install 

test: install
	mvn test


RELEASE_OPT="-DlocalCheckout=true -DconnectionUrl=scm:hg:default"
release-prepare:
	mvn release:prepare $(RELEASE_OPT) 

release-perform:
	mvn release:perform $(RELEASE_OPT)


clean:
	mvn clean


update-version:
	mvn release:update-versions -DautoVersionSubmodules=true
