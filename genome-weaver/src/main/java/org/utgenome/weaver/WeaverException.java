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
// Genome Weaver Project 
//
// WeaverException.java
// Since: Jul 21, 2009 4:15:34 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.utgenome.weaver;

/**
 * Exception base class of the GenomeWeaver project
 * 
 * @author leo
 * 
 */
public class WeaverException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final WeaverErrorCode errorCode;

    public WeaverException(WeaverErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public WeaverException(WeaverErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public WeaverException(WeaverErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public WeaverException(WeaverErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WeaverException(WeaverException e) {
        this(e.getErrorCode(), e);
    }

    public WeaverErrorCode getErrorCode() {
        return errorCode;
    }

}
