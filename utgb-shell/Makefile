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

INSTALL = install
MVN = mvn ${MVN_OPTS}
PREFIX=${HOME}/.utgb

ARCHIVE = target/utgb-package.zip
SCRIPT_DIR = src/main/java/org/utgenome/shell/script

JAVA=$(shell find src/main/java -name '*.java')

.phony: all install clean package 


all: package

package: $(ARCHIVE)


$(ARCHIVE): $(JAVA)
	$(MVN) package

install: $(ARCHIVE) $(SCRIPT_DIR)/bin/utgb
	mkdir -p "$(PREFIX)"
	unzip -o $(ARCHIVE) -d target
	cp -rp target/utgb/* "$(PREFIX)"
	rm -rf target/utgb

dist:	

clean:
	mvn clean


