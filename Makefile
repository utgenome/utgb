
.PHONY: install test release


install:
	mvn install -Dmaven.test.skip=true
	cd utgb-shell; make MVN_OPTS='-Dmaven.test.skip=true' install 

test: install
	mvn test

release:
	mvn release:prepare
	mvn release:perform


