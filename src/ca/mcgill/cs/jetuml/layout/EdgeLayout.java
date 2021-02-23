package ca.mcgill.cs.jetuml.layout;

import java.util.IdentityHashMap;
import java.util.Map;

import ca.mcgill.cs.jetuml.diagram.Edge;

/**
 * A collection of edges and their path.
 */
public class EdgeLayout
{
	private final Map<Edge, EdgePath> aPaths = new IdentityHashMap<>();
	
	public void clear()
	{
		aPaths.clear();
	}
	
	public void put(Edge pEdge, EdgePath pPath)
	{
		aPaths.put(pEdge, pPath);
	}
	
	public EdgePath get(Edge pEdge)
	{
		return aPaths.get(pEdge);
	}
}
