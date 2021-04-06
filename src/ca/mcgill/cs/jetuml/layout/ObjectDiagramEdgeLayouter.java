package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.geom.Direction;
import ca.mcgill.cs.jetuml.geom.Line;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.viewers.nodes.NodeViewerRegistry;
import javafx.scene.canvas.GraphicsContext;

/**
 * All edges are straight and connected through the intersection point with the node.
 */
public class ObjectDiagramEdgeLayouter implements EdgeLayouter
{
	private static final int ENDSIZE = 10;

	@Override
	public void layOut(EdgeLayout pLayout, Diagram pDiagram, GraphicsContext pGraphics)
	{
		pLayout.clear();
		for( Edge edge : pDiagram.edges() )
		{
			pLayout.put(edge, getPath(edge));
		}
	}
	
	private static EdgePath getPath(Edge pEdge)
	{
		Line connectionPoints = getConnectionPoints(pEdge);
		return new EdgePath(new Point(connectionPoints.getX1(), connectionPoints.getY1()),
				new Point(connectionPoints.getX2(), connectionPoints.getY2()));
	}

	/**
     * 	Tests whether the node should be S- or C-shaped.
     * 	@return true if the node should be S-shaped
	 */
	private static boolean isSShaped(Edge pEdge)
	{
		Rectangle b = NodeViewerRegistry.getBounds(pEdge.getEnd());
		Point p = NodeViewerRegistry.getConnectionPoints(pEdge.getStart(), Direction.EAST);
		return b.getX() >= p.getX() + 2 * ENDSIZE;
	}
	
	/**
	 * @param pEdge edge to get connection points
	 * @return line between 2 points
	 */
	public static Line getConnectionPoints(Edge pEdge)
	{
		Point point = NodeViewerRegistry.getConnectionPoints(pEdge.getStart(), Direction.EAST);
		if (isSShaped(pEdge))
		{
			return new Line(point, NodeViewerRegistry.getConnectionPoints(pEdge.getEnd(), Direction.WEST));
		}
		else
		{
			return new Line(point, NodeViewerRegistry.getConnectionPoints(pEdge.getEnd(), Direction.EAST));
		}
	}
}
