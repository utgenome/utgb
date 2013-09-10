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

PREFIX=${HOME}/local
MVN_OPT=
SBT=./sbt
PERL=perl
SED=sed

.PHONY: compile archive pack install test release-sonatype clean superdev container

version:=$(shell $(PERL) -npe "s/version in ThisBuild\s+:=\s+\"(.*)\"/\1/" version.sbt | $(SED) -e "/^$$/d")

pack:
	$(SBT) pack

compile: 
	$(SBT) compile

archive: target/utgb-$(version).tgz

target/utgb-$(version).tgz: 
	$(SBT) pack-archive

install: pack
	cd target/pack && $(MAKE) PREFIX=$(PREFIX) install

test: install
	$(SBT) test


release-sonatype:
	$(SBT) publish-signed


clean:
	$(SBT) clean

# Run GWT super-dev mode
superdev:
	$(SBT) gwt-superdev

# Run web container for debugging UTGB
container:
	$(SBT) "~; container:start; container:reload /"

