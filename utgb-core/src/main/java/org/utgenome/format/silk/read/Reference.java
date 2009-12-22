/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-shell Project
//
// Reference.java
// Since: Apr 10, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.silk.read;

/**
 * Reference Sequence
 * 
 * @author leo
 * 
 */
public class Reference
{
    public String name;
    public long start;
    public String strand;
    public int tag;
    public long s2st;
    public long s2ed;
    public String s2strand;
    public int score;

    public void addRead(Read read)
    {

    }

    public void appendSequence(String sequence)
    {
    // incrementally process long sequence data

    }

}
