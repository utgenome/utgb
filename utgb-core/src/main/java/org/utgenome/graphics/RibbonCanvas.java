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
// RibbonCanvas.java
// Since: 2009/04/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.graphics;

import org.utgenome.format.silk.read.Read;
import org.utgenome.format.silk.read.Reference;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * genome drawing canvas supporting indel display
 * 
 * @author leo
 * 
 */
public class RibbonCanvas
{

    private int width = 800;

    private final GenomeWindow window;
    private BufferedImage image;
    private Graphics2D g;
    private int canvasWidth;
    private int canvasHeight;

    private int yOffset = 0;

    private static final String DEFAULT_COLOR_A = "50b6e8";
    private static final String DEFAULT_COLOR_C = "e7846e";
    private static final String DEFAULT_COLOR_G = "84ab51";
    private static final String DEFAULT_COLOR_T = "ffe980";
    private static final String DEFAULT_COLOR_N = "333333";

    private String colorA = DEFAULT_COLOR_A;
    private String colorC = DEFAULT_COLOR_C;
    private String colorG = DEFAULT_COLOR_G;
    private String colorT = DEFAULT_COLOR_T;
    private String colorN = DEFAULT_COLOR_N;

    private Color UNKNOWN_BASE_COLOR = GraphicUtil.parseColor(colorN);

    private HashMap<Character, Color> colorTable = new HashMap<Character, Color>();

    public RibbonCanvas(int width, int height, GenomeWindow window)
    {
        this.window = window;
        setPixelSize(width, height);

        colorTable.put('a', GraphicUtil.parseColor(colorA));
        colorTable.put('c', GraphicUtil.parseColor(colorC));
        colorTable.put('g', GraphicUtil.parseColor(colorG));
        colorTable.put('t', GraphicUtil.parseColor(colorT));
        colorTable.put('A', GraphicUtil.parseColor(colorA));
        colorTable.put('C', GraphicUtil.parseColor(colorC));
        colorTable.put('G', GraphicUtil.parseColor(colorG));
        colorTable.put('T', GraphicUtil.parseColor(colorT));

    }

    public Color getBaseColor(char base)
    {
        Color color = colorTable.get(base);
        if (color == null)
            return UNKNOWN_BASE_COLOR;
        else
            return color;
    }

    public void setPixelSize(int width, int height)
    {
        canvasWidth = width;
        canvasHeight = height;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void setReference(Reference reference)
    {

    }

    public void draw(Read read)
    {
        if (!window.hasOverlap(read.start, read.end))
            return;

        if (window.getGenomeRange() <= 400)
        {
            drawSequence(read);
            return;
        }

        long start = read.start;
        long end = read.end;

        if (start >= end)
        {
            // swap
            long tmp = start;
            start = end;
            end = tmp;
        }

        int x1 = window.getXPosOnWindow(start, canvasWidth);
        int x2 = window.getXPosOnWindow(end, canvasWidth);

        if (x1 == x2)
            x2 = x1 + 1;

        int width = x2 - x1;
        if (width < 0)
            width = 1;

        g.setColor(new Color(100, 100, 255, 240));
        g.fillRect(x1, yOffset, width, 4);
        yOffset += 5;

    }

    private void drawSequence(Read read)
    {
        int boxHeight = 10;

        long start = read.start;
        long end = read.end;

        if (start >= end)
        {
            // swap
            long tmp = start;
            start = end;
            end = tmp;
        }
        int width = (int) (end - start);

        int x1 = window.getXPosOnWindow(start, canvasWidth);
        int x2 = window.getXPosOnWindow(start + 1, canvasWidth);

        int letterSize = x2 - x1;

        boolean drawBase = letterSize > 10;
        int fontHeight = 0;
        FontMetrics fontMetrics = g.getFontMetrics();
        if (drawBase)
        {
            g.setFont(new Font("SansSerif", Font.PLAIN, boxHeight - 1));
            fontHeight = fontMetrics.getHeight();
        }

        String seq = read.sequence.substring((int) (start - read.start), width);

        int offset = x1;
        for (int i = 0; i < seq.length(); ++i)
        {
            char ch = seq.charAt(i);

            if (ch == '-')
            {
                g.setColor(GraphicUtil.parseColor("9999FF"));
                g.drawLine(offset, yOffset + boxHeight - 2, offset + letterSize, yOffset + boxHeight - 2);
            }
            else
            {
                Color c = getBaseColor(ch);
                g.setColor(c);
                g.fillRect(offset, yOffset, letterSize, boxHeight - 1);

                if (drawBase)
                {
                    g.setColor(Color.DARK_GRAY);
                    String base = String.valueOf(ch);
                    int fontWidth = fontMetrics.stringWidth(base);
                    int xOffset = letterSize / 2 - fontWidth / 2;
                    g.drawString(base, offset + xOffset, yOffset + boxHeight - 2);
                }
            }

            offset += letterSize;
        }

        yOffset += boxHeight;

    }

    public void drawText(String text, long startIndexOnGenome, long endIndexOnGenome, int yOffset, int fontSize,
            Color color)
    {
        int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
        int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);
        int width = (start < end) ? end - start : 1;

        g.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int fontWidth = fontMetrics.stringWidth(text);

        g.setColor(color);
        int xOffset = (width - fontWidth) / 2;
        g.drawString(text, start + xOffset, yOffset);
    }

    public void toPNG(OutputStream out) throws IOException
    {
        ImageIO.write(image, "png", out);
    }

}
