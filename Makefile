
.PHONY: install test release


install:
	mvn install -Dmaven.test.skip=true

test: install
	mvn test

release:
	mvn release:prepare
	mvn release:perform


