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
// utgb-core Project
//
// ChromosomeWindow.java
// Since: Sep. 29, 2009
//
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.graphics;

public class ChromosomeWindow
{
    private long startIndexOnChromosome;
    private long endIndexOnChromosome;
    private long range;
    private int rank = -1;
    private int leftMargin = 50;
    
    public ChromosomeWindow(long startIndexOnChromosome, long endIndexOnChromosome, int rank)
    {
        this.startIndexOnChromosome = startIndexOnChromosome;
        this.endIndexOnChromosome = endIndexOnChromosome;
        this.rank = rank;
        // inclusive
        range = width(endIndexOnChromosome, startIndexOnChromosome) + 1;
    }

    public ChromosomeWindow(long startIndexOnChromosome, long endIndexOnChromosome)
    {
        this(startIndexOnChromosome, endIndexOnChromosome, -1);
    }

    public static long width(long x1, long x2)
    {
        return (x1 < x2) ? x2 - x1 : x1 - x2;
    }

    public int getXPosOnWindow(long indexOnChromosome, int canvasWidth)
    {
        double v = (indexOnChromosome - startIndexOnChromosome) * (double) canvasWidth;

        double v2 = v / (double) range;
        return (int) v2 + leftMargin;
    }

    public long getChromosomeStart()
    {
        return startIndexOnChromosome;
    }    
    public void setChromosomeStart(long startIndexOnChromosome)
    {
    	this.startIndexOnChromosome = startIndexOnChromosome;
        range = width(endIndexOnChromosome, startIndexOnChromosome) + 1;
    }
    
    public long getChromosomeEnd()
    {
        return endIndexOnChromosome;
    }    
    public void setChromosomeEnd(long endIndexOnChromosome)
    {
    	this.endIndexOnChromosome = endIndexOnChromosome;
        range = width(endIndexOnChromosome, startIndexOnChromosome) + 1;
    }
    
    public long getChromosomeRange()
    {
        return range;
    }

    public int getRank()
    {
    	return rank;
    }
    public void setRank(int rank)
    {
    	this.rank = rank;
    }
    
    public int getLeftMargin(){
    	return leftMargin;
    }
    public void setLeftMargin(int leftMargin){
    	this.leftMargin = leftMargin;
    }
    
    public long getRange(){
    	return range;
    }
}
