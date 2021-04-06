package ca.mcgill.cs.jetuml.layout;

import java.util.IdentityHashMap;
import java.util.Map;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.diagram.Node;
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
	
	private Map<Node, Map<Node, Integer>> aPositions = new IdentityHashMap<>();
	
	@Override
	public void layOut(EdgeLayout pLayout, Diagram pDiagram, GraphicsContext pGraphics)
	{
		pLayout.clear();
		aPositions.clear(); 
		
		for( Edge edge : pDiagram.edges() )
		{
			pLayout.put(edge, getPath(edge));
		}
	}
	
	/**
	 * Calculate the edge position and cache previous results in a map.
	 * @param pEdge edge to calculate
	 * @return position of the edge
	 */
	private int getPosition(Edge pEdge)
	{
		assert pEdge.getDiagram() != null;
		Node start = pEdge.getStart();
		Node end = pEdge.getEnd();
		if (!aPositions.containsKey(start))
		{
			aPositions.put(start, new IdentityHashMap<>());
		}
		if (!aPositions.get(start).containsKey(end))
		{
			aPositions.get(start).put(end, 0);
		}
		int lReturn = aPositions.get(start).get(end)+1;
		aPositions.get(start).put(end, lReturn);
		return lReturn;
	}
	
	private EdgePath getPath(Edge pEdge)
	{
		
		if( isSelfEdge(pEdge) )
		{
			return getSelfEdgePath(pEdge);
		}
		else
		{
			return getNormalEdgePath(pEdge);
		}
	}
	
	private boolean isSelfEdge(Edge pEdge)
	{
		return pEdge.getStart() == pEdge.getEnd();
	}
	
	private EdgePath getSelfEdgePath(Edge pEdge)
	{
		int position = getPosition(pEdge);
		if( position == 1 )
		{
			Point2D point1 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getMaxX() - SELF_EDGE_OFFSET, 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY());
			Point2D point2 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getMaxX(), 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY() + SELF_EDGE_OFFSET);
			return new StateDiagramEdgePath(position, Conversions.toPoint(point1), Conversions.toPoint(point2));
		}
		else
		{
			Point2D point1 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getX(), 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY() + SELF_EDGE_OFFSET);
			Point2D point2 = new Point2D(NodeViewerRegistry.getBounds(pEdge.getStart()).getX() + SELF_EDGE_OFFSET, 
					NodeViewerRegistry.getBounds(pEdge.getStart()).getY());
			return new StateDiagramEdgePath(position, Conversions.toPoint(point1), Conversions.toPoint(point2));
		}
	}
	
	private EdgePath getNormalEdgePath(Edge pEdge)
	{
		Rectangle start = NodeViewerRegistry.getBounds(pEdge.getStart());
		Rectangle end = NodeViewerRegistry.getBounds(pEdge.getEnd());
		Point startCenter = start.getCenter();
		Point endCenter = end.getCenter();
		int turn = DEGREES_5;
		int position = getPosition(pEdge);
		if( pEdge.getDiagram() != null && position > 1 )
		{
			turn = DEGREES_20;
		}
		Direction d1 = Direction.fromLine(startCenter, endCenter).rotatedBy(-turn);
		Direction d2 = Direction.fromLine(endCenter, startCenter).rotatedBy(turn);
		return new StateDiagramEdgePath(position, NodeViewerRegistry.getConnectionPoints(pEdge.getStart(), d1), 
				NodeViewerRegistry.getConnectionPoints(pEdge.getEnd(), d2));
	}
	

}
