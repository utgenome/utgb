/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGB Common Project
//
// UTGBException.java
// Since: 2007/03/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome;
/**
 * A UTGBException is a base class for all exception classes in the org.utgb packages
 * @author leo
 *
 */
@SuppressWarnings("serial")
public class UTGBException extends Exception
{
	private UTGBErrorCode errorCode = UTGBErrorCode.Unknown;
	
    public UTGBException()
    {
    }

    public UTGBException(String message)
    {
        super(message);
    }

    public UTGBException(Throwable cause)
    {
        super(cause);
    }

    public UTGBException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public UTGBException(UTGBErrorCode errorCode, Throwable cause)
    {
    	super(cause);
    	this.errorCode = errorCode;
    }
    
    public UTGBException(UTGBErrorCode errorCode, String message, Throwable cause)
    {
    	super(message, cause);
    	this.errorCode = errorCode;
    }
    
    public UTGBException(UTGBErrorCode errorCode, String message)
    {
    	super(message);
    	this.errorCode = errorCode;
    }
    
    @Override
    public String getMessage() {
    	return "[" + errorCode.name() + "] " + super.getMessage();
    }

}




