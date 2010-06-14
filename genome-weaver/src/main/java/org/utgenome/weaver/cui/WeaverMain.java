/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// Xerial Silk Weaver Project 
//
// WeaverMain.java
// Since: 2009/06/24 20:26:27
//
// $URL$
// $Author$
//--------------------------------------
package org.utgenome.weaver.cui;

import java.net.URL;

import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;
import org.xerial.util.opt.Usage;

/**
 * Command-line interface of the Silk Weaver
 * 
 * @author leo
 * 
 */
@Usage(command = "> weaver [option] ", description = "Genome Weaver: A Next-Generation DBMS for Genome Sciences")
public class WeaverMain extends WeaverModuleBase {
    private static Logger _logger = Logger.getLogger(WeaverMain.class);

    /**
     * Entry point of the CUI interface
     * 
     * @param args
     *            command-line arguments
     */
    public static void main(String[] args) {
        WeaverMain m = new WeaverMain();
        //m.addModule(new BisulfiteModule());

        OptionParser parser = new OptionParser(m);
        try {
            parser.setIgnoreUnknownOption(true);
            parser.parse(args);
            m.execute(null, parser.getUnusedArguments());
        }
        catch (OptionParserException e) {
            _logger.error(e.getMessage());
            parser.printUsage();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeaverMain() {
        super(Package.getPackage("org.utgenome.weaver.cui"));
    }

    public String getCommandName() {
        return null;
    }

    public String getOneLineDescription() {
        return "genome weaver";
    }

    public String getProgramInfo() {
        VersionInfo vi = getVersionInfo();
        return String.format("Genome Weaver: version %s", vi.version);
    }

    public static class VersionInfo {
        public String version = "(unknown)";

        public void addDependency_Version(String d, String v) {

        }

        public void addPlugin_Version(String p, String v) {

        }

        public void addParent_Version(String p, String v) {

        }
    }

    public static VersionInfo getVersionInfo() {
        URL pomFile = Thread.currentThread().getContextClassLoader().getResource(
                "META-INF/maven/org.utgenome.weaver/genome-weaver/pom.xml");

        VersionInfo vi = new VersionInfo();
        if (pomFile != null) {
            try {
                Lens.loadXML(vi, pomFile);
            }
            catch (Exception e) {
                _logger.error(e);
            }
        }

        return vi;

    }

}
