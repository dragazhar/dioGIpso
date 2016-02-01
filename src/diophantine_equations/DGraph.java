package diophantine_equations;


import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;

import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jfree.chart.renderer.xy.XYDotRenderer;


public class DGraph {
    static ArrayList<ArrayList<Double>> color = new ArrayList<ArrayList<Double>>();

    static XYSeries addSeries(String seriesName,
	    TreeMap<DKey, DPoint> seriesPoints) {
	XYSeries series = new XYSeries(seriesName);
	ArrayList<Double> c = new ArrayList<Double>();
	
	for (DPoint p : seriesPoints.values()) {
	    double x = (double) p.getX();
	    double y = (double) p.getY();
	    c.add(p.getValue());
	    series.add(x, y);
	}
	color.add(c);
	return series;
    }

    public static void draw(String chartName,
	    TreeMap<DKey, DPoint> seriesPoints, 
	    int pointSize, double range, int x, int y, ArrayList<DCoordinates> track) throws IOException {

	XYSeriesCollection xyDataset = new XYSeriesCollection();
	color = new ArrayList<ArrayList<Double>>();
	
	xyDataset.addSeries(addSeries("empty string", seriesPoints));

	JFreeChart chart = ChartFactory.createScatterPlot(chartName, "", "",
		xyDataset, PlotOrientation.VERTICAL, true, true, false);

	Plot plot = chart.getPlot();
	plot.setBackgroundPaint(Color.getHSBColor(0.3f, 1.0f, 0.05f));

	XYPlot xyPlot = chart.getXYPlot();
	xyPlot.setRangeGridlinesVisible(false);
	xyPlot.setDomainGridlinesVisible(false);
	xyPlot.getRangeAxis().setRange(range*(-1), range);
	xyPlot.getDomainAxis().setRange(range*(-1), range);
	
	xyPlot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
	//show circle around global best
	XYShapeAnnotation a1 = new XYShapeAnnotation(
		new Ellipse2D.Double(x - 2, y - 2, 2 + 2, 2 + 2), new BasicStroke(1.0f), Color.white
            );
	 xyPlot.addAnnotation(a1);
	 //show circle around track
	
	 for(int i=0; i<track.size();i++){
	
	     xyPlot.addAnnotation(new XYShapeAnnotation(
			new Ellipse2D.Double(track.get(i).getX() - 1, track.get(i).getY() - 1, 2,2), new BasicStroke(1.0f), Color.cyan
		            ));
	 }
	 
		
		 
	 
	chart.removeLegend();


	    MyRenderer renderer = new MyRenderer();
	    renderer.setDotWidth(pointSize);
	    renderer.setDotHeight(pointSize);
	    xyPlot.setRenderer(renderer);
	
	 
	//showChart(chartName, chart);
	OutputStream o=new FileOutputStream("dio\\"+chartName+".png");
	writeAsPNG(chartName, chart, o, 600, 600);

    }

    public static void showChart(String chartName, JFreeChart chart) {
	ChartFrame frame1 = new ChartFrame(chartName, chart);
	frame1.setVisible(true);
	frame1.setSize(600, 600);
	frame1.getFocusOwner();
    }

    public static void writeAsPNG(String chartName, JFreeChart chart,
	    OutputStream out, int width, int height) {
	try {
	    BufferedImage chartImage = chart.createBufferedImage(width, height,
		    null);
	    ImageIO.write(chartImage, "png", out);
	} catch (Exception e) {
	    System.err.println(e);
	}
    }

    private static class MyRenderer extends XYDotRenderer {

	public MyRenderer() {
	    super();
	    // TODO Auto-generated constructor stub
	}

	/**
   	 * 
   	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Paint getItemPaint(int row, int col) {
	    if (color.size() > 0) {
		if (color.get(row).get(col) == 1.0) {
		    return Color.white;

		} else {
		    double db = (color.get(row).get(col));
		    float fl = (float) db;

		    //return Color.getHSBColor(fl, 1-fl, 1 - fl);
		    return Color.getHSBColor(1-fl, 1-(1-fl),  1-(1-fl));
		}

	    } else {
		return super.getItemPaint(row, col);
	    }

	}
    }
}
