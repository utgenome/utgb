/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// BED2SilkTest.java
// Since: 2009/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.bed;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.utgenome.UTGBException;

public class BED2SilkTest
{

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        BED2Silk bed2silk;
        try
        {
            bed2silk = new BED2Silk(new File("db/sample.bed"));
            //			PipedWriter out = new PipedWriter();
            //			PipedReader in = new PipedReader(out);
            String in = new String();
            in = bed2silk.toSilk();

            //			BufferedReader reader = new BufferedReader(in);
            StringReader reader = new StringReader(in);

            //			for(String s;(s=reader.readLine())!=null;){
            System.out.println(in);
            //			}

            //			in.close();
        }
        catch (UTGBException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void test()
    {

    }
}
