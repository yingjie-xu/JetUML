package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.geom.Direction;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.viewers.nodes.NodeViewerRegistry;
import javafx.scene.canvas.GraphicsContext;

/**
 * All edges are straight and connected through the intersection point with the node.
 */
public class UseCaseDiagramEdgeLayouter implements EdgeLayouter
{

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
		Rectangle startBounds = NodeViewerRegistry.getBounds(pEdge.getStart());
		Rectangle endBounds = NodeViewerRegistry.getBounds(pEdge.getEnd());
		Point startCenter = startBounds.getCenter();
		Point endCenter = endBounds.getCenter();
		Direction toEnd = Direction.fromLine(startCenter, endCenter);
		return new EdgePath(NodeViewerRegistry.getConnectionPoints(pEdge.getStart(), toEnd), 
				NodeViewerRegistry.getConnectionPoints(pEdge.getEnd(), toEnd.mirrored()));
	}

}
