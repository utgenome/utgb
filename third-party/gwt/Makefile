
CURRENT_DIR = $(shell cygpath -w `pwd`)

VERSION=1.7.0
#VERSION = 1.4.62

#MAC_TIGER=mac_10.4
#MAC_LEOPARD=mac_10.5

MAC_TIGER=mac
MAC_LEOPARD=leopard

DEPLOY_OPT = -DgroupId=com.google.gwt -Dversion=$(VERSION) -Dpackaging=jar -DrepositoryId=utgb -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true -DrepositoryId=utgb.dav
DEPLOY_CMD = mvn deploy:deploy-file $(DEPLOY_OPT)
GWT_LIB=lib/gwt-$(VERSION)
GWT_DOWNLOAD=http://google-web-toolkit.googlecode.com/files


.PHONY: all clean package gwt-user gwt-servlet gwt-benchmark-viewer gwt-dev gwtdnd

all: package 

package: gwt-user gwt-servlet gwt-dev 

gwt-user: gwt-windows-$(VERSION)  
	$(DEPLOY_CMD) -Dfile=$</gwt-user.jar -DartifactId=gwt-user

gwt-servlet: gwt-windows-$(VERSION)
	$(DEPLOY_CMD) -Dfile=$</gwt-servlet.jar -DartifactId=gwt-servlet

gwt-benchmark-viewer: gwt-windows-$(VERSION)
	$(DEPLOY_CMD) -Dfile=$</gwt-benchmark-viewer.jar -DartifactId=gwt-benchmark-viewer

gwt-dev: gwt-dev-windows-$(VERSION).jar gwt-dev-$(MAC_TIGER)-$(VERSION).jar gwt-dev-linux-$(VERSION).jar 
	$(DEPLOY_CMD) -Dfile=gwt-dev-windows-$(VERSION).jar -DartifactId=gwt-dev-windows
	$(DEPLOY_CMD) -Dfile=gwt-dev-linux-$(VERSION).jar -DartifactId=gwt-dev-linux
	$(DEPLOY_CMD) -Dfile=gwt-dev-$(MAC_TIGER)-$(VERSION).jar -DartifactId=gwt-dev-mac



ARCHIVES=gwt-windows-$(VERSION).zip gwt-$(MAC_TIGER)-$(VERSION).tar.gz gwt-linux-$(VERSION).tar.bz2

work:
	mkdir work

$(ARCHIVES):
	wget -O $@ $(GWT_DOWNLOAD)/$@

gwt-windows-$(VERSION): gwt-windows-$(VERSION).zip
	unzip -n $@.zip 
gwt-$(MAC_TIGER)-$(VERSION): gwt-$(MAC_TIGER)-$(VERSION).tar.gz
	tar xvfz $<
gwt-linux-$(VERSION): gwt-linux-$(VERSION).tar.bz2
	tar xvfj $< 

gwt-dev-windows-$(VERSION).jar: gwt-windows-$(VERSION)
	cd $<; jar cvf ../$@ gwt-dev-windows.jar *.dll
gwt-dev-$(MAC_TIGER)-$(VERSION).jar: gwt-$(MAC_TIGER)-$(VERSION)
	cd $<; jar cvf ../$@ gwt-dev-mac.jar *.jnilib
gwt-dev-linux-$(VERSION).jar: gwt-linux-$(VERSION)
	cd $<; jar cvf ../$@ gwt-dev-linux.jar *.so

clean:
	rm -rf gwt-dev-*-$(VERSION).jar gwt-windows* gwt-$(MAC_TIGER)* gwt-$(MAC_LEOPARD)* gwt-linux* work

GWTDND_VERSION=3.1.2

gwtdnd:
	mvn deploy:deploy-file -DgroupId=com.allen_sauer.gwt -DartifactId=gwt-dnd -Dversion=$(GWTDND_VERSION) -Dpackaging=jar -DrepositoryId=utgb -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true -DrepositoryId=utgb.dav -Dfile=lib/gwt-dnd-$(GWTDND_VERSION).jar

gwtdnd-javadoc:
	mvn deploy:deploy-file -DgroupId=com.allen_sauer.gwt -DartifactId=gwt-dnd -Dversion=$(GWTDND_VERSION) -Dclassifier=javadoc -Dpackaging=jar -DrepositoryId=utgb -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true -DrepositoryId=utgb.dav -Dfile=lib/gwt-dnd-$(GWTDND_VERSION)-javadoc.jar

gwt-incubator:
	mvn deploy:deploy-file -DgroupId=com.google.gwt -DartifactId=gwt-incubator -Dversion=1.7-july-14-2009 -Dpackaging=jar -DrepositoryId=utgb -Durl=dav:http://maven.utgenome.org/repository/artifact -Dmaven.test.skip=true -DrepositoryId=utgb.dav -Dfile=lib/gwt-incubator-july-14-2009.jar
