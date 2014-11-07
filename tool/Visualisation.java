package tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/** Visualisation class which interacts with the external visual
 *  library, JUNG.
 * 
 * @author Jackie, s1035578
 *
 */
public class Visualisation {
	
	private static VisualizationViewer graphIm;
	
	/** Draws the given graph, and adds it to the display panel
	 * 
	 * @param graph
	 * @param panel
	 * @param layout
	 */
	public static void drawGraph(GameGraph graph, JPanel panel, String layout) {//frame is not necessary, just pass the image server
		
		//Set up graph visualisation
		UndirectedSparseGraph vGraph = new UndirectedSparseGraph();
		
		ArrayList<ArrayList<Boolean>> adjMatrix = graph.getAdjMatrix();
		final ArrayList<Integer> coopList = graph.getCoopVertices();
		final ArrayList<Integer> defectList = graph.getDefectVertices();
		ArrayList<Integer> vertices = graph.getVertices();
		
		for (int i=0;i<graph.getVertices().size();i++) {
			if (vertices.get(i)!=null) {
				vGraph.addVertex(i);
				for (int j=0;j<i;j++) {
					if (vertices.get(j)!=null) {
						if (graph.getAdjMatrix().get(i).get(j)) {
							if (graph.getShowEdges())
								vGraph.addEdge("Edge"+i+"to"+j, i,j);
						}
					}
				}
			}
		}
		
	    Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
	        public Paint transform(Integer i) {
	        	if (ToolMethods.isIn(i, coopList))
	        		return Color.BLUE;
	        	if (ToolMethods.isIn(i, defectList))
	        		return Color.RED;
	        	return Color.GREEN;
	        }
	    };
		
	    Dimension dim = new Dimension(graph.getNumberOfVertices()*10+250,graph.getNumberOfVertices()*10+250);
	    
	    graphIm = new VisualizationViewer(new CircleLayout(vGraph), new Dimension(500,500));
	    if (layout.equals("circ")) {
	    	graphIm = new VisualizationViewer(new CircleLayout(vGraph), new Dimension(500,500));
	    }
	    if (layout.equals("kk")) {
	    	graphIm = new VisualizationViewer(new KKLayout(vGraph), new Dimension(500,500));
	    }
	    
	    graphIm.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
	    graphIm.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());	    
	    
	    GraphZoomScrollPane pane = new GraphZoomScrollPane(graphIm);
	    panel.add(pane);
	}
	
	/** Repaint the graph.  Mainly evoked if there are no major structural changes to
	 *  the graph, i.e. addition/removal of vertices/edges.
	 * 
	 * @param graph
	 * @param panel
	 */
	public static void repaintImage(GameGraph graph, JPanel panel) {
		
		final ArrayList<Integer> coopList = graph.getCoopVertices();
		final ArrayList<Integer> defectList = graph.getDefectVertices();
		
	    Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
	        public Paint transform(Integer i) {
	        	if (ToolMethods.isIn(i, coopList))
	        		return Color.BLUE;
	        	if (ToolMethods.isIn(i, defectList))
	        		return Color.RED;
	        	return Color.GREEN;
	        }
	    };
		
	    graphIm.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
	    graphIm.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());	    
	    
	    GraphZoomScrollPane pane = new GraphZoomScrollPane(graphIm);
	    panel.add(pane);
	}
	
}
