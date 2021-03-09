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
	
	/**
	 * Clear the layout.
	 */
	public void clear()
	{
		aPaths.clear();
	}
	
	/**
	 * Add pEdgePath for the pEdge.
	 * @param pEdge edge to add
	 * @param pPath edge path to add
	 */
	public void put(Edge pEdge, EdgePath pPath)
	{
		aPaths.put(pEdge, pPath);
	}
	
	/**
	 * Get the EdgePath for the specified pEdge.
	 * @param pEdge edge to get the path
	 * @return EdgePath path for the pEdge
	 * @pre pEdge != null
	 */
	public EdgePath get(Edge pEdge)
	{
		assert pEdge != null;
		return aPaths.get(pEdge);
	}
}
