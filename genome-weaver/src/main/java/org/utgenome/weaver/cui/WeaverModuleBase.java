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
// WeaverModuleBase.java
// Since: 2009/07/22 14:27:21
//
// $URL$
// $Author$
//--------------------------------------
package org.utgenome.weaver.cui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xerial.util.FileResource;
import org.xerial.util.log.LogLevel;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;

/**
 * Base implementation of WeaverModule
 * 
 * @author leo
 * 
 */
public abstract class WeaverModuleBase implements WeaverModule {

    private final Package commandBasePackage;
    private final Logger _logger;
    private final List<WeaverModule> subModuleList = new ArrayList<WeaverModule>();

    /**
     * Command-line option parameter definition
     * 
     */

    @Argument(index = 0, required = false, name = "command")
    protected String commandName = null;

    @Option(symbol = "h", longName = "help", description = "display help message")
    protected boolean displayHelp = false;

    @Option(longName = "loglevel", varName = "LEVEL", description = "set log level: OFF, TRACE, DEBUG, INFO(default), WARN, ERROR, FATAL")
    protected LogLevel logLevel = LogLevel.INFO;

    @Option(longName = "logconfig", varName = "path", description = "specify the log config file")
    protected String logConfigFilePath = null;

    protected WeaverModuleBase(Package commandBasePackage) {
        this.commandBasePackage = commandBasePackage;
        this._logger = Logger.getLogger(this.commandBasePackage);
    }

    public Package getCommandBasePackage() {
        return commandBasePackage;
    }

    public void addModule(WeaverModule module) {
        subModuleList.add(module);
    }

    public List<WeaverModule> getModuleList() {
        return subModuleList;
    }

    public void execute(WeaverModule module, String[] unusedArgs) throws Exception {

        // set the log level
        Logger packageLogger = Logger.getLogger("org.xerial.weaver");
        packageLogger.setLogLevel(logLevel);

        // load log config file
        if (logConfigFilePath != null) {
            File logConfig = new File(logConfigFilePath);
            if (logConfig.exists()) {
                try {
                    Logger.configure(logConfig.getAbsolutePath());
                }
                catch (IOException e) {
                    _logger.warn(e);
                }
            }
            else
                _logger.warn("log config file doesn't exist: " + logConfig.getAbsolutePath());
        }

        if (commandName == null) {
            System.out.println(getProgramInfo());
            if (displayHelp) {
                displayHelp();
                return;
            }
            else {
                System.out.println("type --help for a list of the available commands");
                return;
            }
        }
        else {
            // search target commands
            WeaverCommand c = find(commandName);
            if (c == null) {
                System.err.println("unknown command: " + commandName);
                System.err.println("type --help for a list of the available commands");
                return;
            }
            else {
                // bind the unused arguments to the sub command
                OptionParser p = new OptionParser(c);
                p.setIgnoreUnknownOption(true);
                try {
                    p.parse(unusedArgs);
                }
                catch (OptionParserException e) {
                    System.err.println(e.getMessage());
                    displayHelp = true;
                }

                if (!displayHelp) {
                    // execute the sub command
                    c.execute(this, p.getUnusedArguments());
                }
                else {
                    // display help message for the specified command 
                    if (WeaverModule.class.isInstance(c)) {
                        WeaverModule.class.cast(c).displayHelp();
                    }
                    else {
                        displayHelp();
                    }
                }
            }
        }

    }

    public void displayHelp() {
        displayHelp(commandName);
    }

    public void displayHelp(String commandName) {
        if (commandName == null) {
            // display command list
            OptionParser parser = new OptionParser(this);
            parser.printUsage();
            displayCommandList();
            return;
        }
        else {
            // search target commands
            WeaverCommand c = find(commandName);
            if (c == null) {
                System.err.println("unknown command: " + commandName);
                return;
            }
            else {
                // display help message for the specified command
                OptionParser p = new OptionParser(c);
                p.printUsage();
            }
        }
    }

    public void displayCommandList() {
        System.out.println("[available commands]");

        for (WeaverCommand c : getSubCommandList()) {
            System.out.println(String.format("  %-15s %s", c.getCommandName(), c
                    .getOneLineDescription()));
        }
    }

    public WeaverCommand find(String commandName) {
        for (WeaverCommand each : getSubCommandList()) {
            if (each.getCommandName().equals(commandName))
                return each;
        }
        return null;
    }

    public List<WeaverCommand> getSubCommandList() {
        List<Class<WeaverCommand>> subCommandTypes = FileResource.findClasses(commandBasePackage,
                WeaverCommand.class, Thread.currentThread().getContextClassLoader());
        ArrayList<WeaverCommand> commandList = new ArrayList<WeaverCommand>();

        if (subCommandTypes == null)
            return commandList;

        for (Class<WeaverCommand> each : subCommandTypes) {
            try {
                WeaverCommand c = each.newInstance();
                if (c.getCommandName() != null && !WeaverModule.class.isInstance(c))
                    commandList.add(c); // add except global module
            }
            catch (InstantiationException e) {
                _logger.warn(e);
            }
            catch (IllegalAccessException e) {
                _logger.warn(e);
            }
        }

        for (WeaverModule m : getModuleList()) {
            commandList.add(m);
        }

        // lexicographical sort the command list
        Collections.sort(commandList, new Comparator<WeaverCommand>() {
            public int compare(WeaverCommand o1, WeaverCommand o2) {
                int diff = o1.getCommandName().compareTo(o2.getCommandName());
                if (diff == 0)
                    _logger.warn(String.format("duplicate commands found: %s and %s",
                            o1.getClass(), o2.getClass()));
                return diff;
            }
        });

        return commandList;
    }

}
