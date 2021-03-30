package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.geom.Conversions;
import ca.mcgill.cs.jetuml.geom.Direction;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.viewers.nodes.NodeViewerRegistry;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * All edges are straight and connected through the intersection point with the node.
 */
public class StateDiagramEdgeLayouter implements EdgeLayouter
{
	private static final int SELF_EDGE_OFFSET = 15;
	private static final int DEGREES_5 = 5;
	private static final int DEGREES_20 = 20;
	
	@Override
	public void layOut(EdgeLayout pLayout, Diagram pDiagram, GraphicsContext pGraphics)
	{
		pLayout.clear();
		
		for( Edge edge : pDiagram.edges() )
		{
			pLayout.put(edge, getPath(edge, pLayout));
		}
	}
	
	private boolean isFirst(Edge pEdge, EdgeLayout pLayout) 
	{
		if (pLayout.getSameStartAndEnd(pEdge).size() >= 1)
		{
			return false;
		}
		return true;
	}
	
	private EdgePath getPath(Edge pEdge, EdgeLayout pLayout)
	{
		
		if( isSelfEdge(pEdge) )
		{
			return getSelfEdgePath(pEdge, pLayout);
		}
		else
		{
			return getNormalEdgePath(pEdge, pLayout);
		}
	}
	
	private boolean isSelfEdge(Edge pEdge)
	{
		return pEdge.getStart() == pEdge.getEnd();
	}
	
	private EdgePath getSelfEdgePath(Edge pEdge, EdgeLayout pLayout)
	{
		if( isFirst(pEdge, pLayout) )
		{
			Point2D point1 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getMaxX() - SELF_EDGE_OFFSET, 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY());
			Point2D point2 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getMaxX(), 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY() + SELF_EDGE_OFFSET);
			return new EdgePath(Conversions.toPoint(point1), Conversions.toPoint(point2));
		}
		else
		{
			Point2D point1 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getX(), 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY() + SELF_EDGE_OFFSET);
			Point2D point2 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getX() + SELF_EDGE_OFFSET, 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY());
			return new EdgePath(Conversions.toPoint(point1), Conversions.toPoint(point2));
		}
	}
	
	private EdgePath getNormalEdgePath(Edge pEdge, EdgeLayout pLayout)
	{
		Rectangle start = NodeViewerRegistry.getBounds(pEdge.getStart());
		Rectangle end = NodeViewerRegistry.getBounds(pEdge.getEnd());
		Point startCenter = start.getCenter();
		Point endCenter = end.getCenter();
		int turn = DEGREES_5;
		if( pEdge.getDiagram() != null && !isFirst(pEdge, pLayout) )
		{
			turn = DEGREES_20;
		}
		Direction d1 = Direction.fromLine(startCenter, endCenter).rotatedBy(-turn);
		Direction d2 = Direction.fromLine(endCenter, startCenter).rotatedBy(turn);
		return new EdgePath(NodeViewerRegistry.getConnectionPoints(pEdge.getStart(), d1), 
				NodeViewerRegistry.getConnectionPoints(pEdge.getEnd(), d2));
	}
	

}
