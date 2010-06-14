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
// WeaverModule.java
// Since: 2009/07/22 13:36:53
//
// $URL$
// $Author$
//--------------------------------------
package org.utgenome.weaver.cui;

import java.util.List;

/**
 * SilkWeaver Modujle
 * 
 * @author leo
 * 
 */
public interface WeaverModule extends WeaverCommand {

    public Package getCommandBasePackage();

    public String getProgramInfo();

    public void displayHelp();

    public void displayHelp(String commandName);

    public void addModule(WeaverModule module);

    public List<WeaverModule> getModuleList();

}
