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
// tss-toolkit Project
//
// DrawHistogram.java
// Since: 2011/01/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.core.cui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.utgenome.util.StandardInputStream;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;

public class DrawHistogram extends UTGBCommandBase {

	private static Logger _logger = Logger.getLogger(DrawHistogram.class);

	@Argument(index = 0, name = "input data file (default:STDIN)")
	private String input = "-";

	@Option(longName = "width", description = "pixel width (default = 400)")
	private int width = 400;
	@Option(longName = "height", description = "pixel height (default = 250)")
	private int height = 250;

	@Option(longName = "x1", description = "min X")
	private double xMin = Double.NaN;
	@Option(longName = "x2", description = "max X")
	private double xMax = Double.NaN;

	@Option(longName = "y1", description = "min Y")
	private Double yMin = Double.NaN;
	@Option(longName = "y2", description = "max Y")
	private double yMax = Double.NaN;

	@Option(longName = "logy", description = "log scale Y")
	private boolean yLog = false;

	@Option(symbol = "b", description = "num bins (default = 30)")
	private int numBins = 30;

	@Option(symbol = "h", longName = "help", description = "display help")
	private boolean displayHelp = false;

	@Option(symbol = "o", description = "output file name. default: out.png")
	private String output = "out.png";

	public static void main(String[] args) {

		try {
			DrawHistogram d = new DrawHistogram();
			OptionParser opt = new OptionParser(d);
			opt.parse(args);

			if (d.displayHelp) {
				opt.printUsage();
				return;
			}

			d.execute();
		}
		catch (Exception e) {
			_logger.error(e);
		}

	}

	public void execute() throws Exception {

		InputStream in = null;
		if ("-".equals(input)) {
			_logger.info("reading from STDIN");
			in = new StandardInputStream();
		}
		else {
			_logger.info("reading from " + input);
			in = new FileInputStream(input);
		}

		List<Double> data = new ArrayList<Double>();
		BufferedReader dataSetInput = new BufferedReader(new InputStreamReader(in));
		int lineCount = 1;
		try {
			// read data set
			boolean cutOffTail = !Double.isNaN(xMax);
			boolean cutOffHead = !Double.isNaN(xMin);
			for (String line; (line = dataSetInput.readLine()) != null; lineCount++) {

				if (lineCount % 100000 == 0)
					_logger.info(String.format("read %,d data points", lineCount));

				if (line.startsWith("#"))
					continue;
				double v = Double.parseDouble(line);
				if (cutOffTail && v > xMax)
					continue;
				if (cutOffHead && v < xMin)
					continue;

				data.add(v);
			}

			double[] value = new double[data.size()];
			for (int i = 0; i < data.size(); ++i) {
				value[i] = data.get(i);
			}

			// draw histogram
			HistogramDataset dataSet = new HistogramDataset();
			dataSet.setType(HistogramType.FREQUENCY);
			dataSet.addSeries("data", value, numBins);
			JFreeChart chart = ChartFactory.createHistogram(null, null, "Frequency", dataSet, PlotOrientation.VERTICAL, false, false, false);

			ValueAxis domainAxis = chart.getXYPlot().getDomainAxis();
			if (cutOffHead) {
				domainAxis.setLowerBound(xMin);
			}
			if (cutOffTail) {
				domainAxis.setUpperBound(xMax);
			}

			if (yLog) {
				LogarithmicAxis logAxis = new LogarithmicAxis("Frequency");
				logAxis.setAllowNegativesFlag(true);
				logAxis.setAutoRangeIncludesZero(true);
				chart.getXYPlot().setRangeAxis(logAxis);
			}

			if (!Double.isNaN(yMin)) {
				chart.getXYPlot().getRangeAxis().setLowerBound(yMin);
			}
			if (!Double.isNaN(yMax)) {
				chart.getXYPlot().getRangeAxis().setUpperBound(yMax);
			}

			File outputFile = new File(output);
			_logger.info("output to " + outputFile);
			ChartUtilities.saveChartAsPNG(outputFile, chart, width, height);

		}
		catch (Exception e) {
			throw new Exception(String.format("error at line %d: %s", lineCount, e));
		}

	}

	@Override
	public String name() {
		return "draw-hist";
	}

	@Override
	public String getOneLineDescription() {
		return "draw a histogram";
	}

	@Override
	public void execute(String[] args) throws Exception {
		execute();
	}

}
