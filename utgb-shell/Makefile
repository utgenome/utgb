#--------------------------------------------------------------------------
#  Copyright 2007 utgenome.org
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#--------------------------------------------------------------------------

INSTALL:=install
MVN:=mvn ${MVN_OPTS}
PROG:=utgb
PREFIX:=${HOME}/local
INSTALL_DIR:=$(PREFIX)/$(PROG)
MKDIR:=/bin/mkdir


# Read the version number from the pom.xml by using lib/silk-weaver.jar 
VERSION:=$(shell java -jar lib/silk-weaver.jar find "project(artifactId, version)" pom.xml | grep utgb-shell | cut -f 2)
ARCHIVE = target/utgb-toolkit-$(VERSION)-bin.tar.gz

SRC=$(shell /bin/find src)

.phony: all install clean package test

all: package

package: $(ARCHIVE)

$(ARCHIVE): $(SRC)
	$(MVN) package -Dmaven.test.skip=true

UTGB_DIR:="$(INSTALL_DIR)/utgb-toolkit-$(VERSION)"

install: $(ARCHIVE) 
	$(MKDIR) -p "$(UTGB_DIR)"
	$(MKDIR) -p "$(PREFIX)/bin"
	if [ -d "$(UTGB_DIR)/lib" ]; then rm -rf "$(UTGB_DIR)/lib"; fi
	tar xvfz $< -C "$(INSTALL_DIR)"
	ln -sfn "utgb-toolkit-$(VERSION)" "$(INSTALL_DIR)/current"
	ln -sf "../$(PROG)/current/bin/$(PROG)" "$(PREFIX)/bin/$(PROG)"
	ln -sf "../$(PROG)/current/bin/$(PROG)-debug" "$(PREFIX)/bin/$(PROG)-debug"

test: 
	$(MVN) test

clean:
	$(MVN) clean


