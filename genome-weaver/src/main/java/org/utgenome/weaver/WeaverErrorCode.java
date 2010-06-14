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
// SilkWeaverErrorCode.java
// Since: 2009/06/24 20:24:31
//
// $URL$
// $Author$
//--------------------------------------
package org.utgenome.weaver;

/**
 * Error codes for the GenomeWeaver projects
 * 
 * @author leo
 * 
 */
public enum WeaverErrorCode {
    INVALID_COMMAND("invalid command"), INVALID_FORMAT("invalid format");

    private final String description;

    private WeaverErrorCode(String description) {
        this.description = description;
    }

    public String getCodeName() {
        return name();
    }

    public String getDescription() {
        return description;
    }

}
