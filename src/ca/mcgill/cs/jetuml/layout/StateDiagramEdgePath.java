package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.annotations.Immutable;
import ca.mcgill.cs.jetuml.geom.Point;

/**
 * Represents the path of a state transition edge.
 */
@Immutable
public class StateDiagramEdgePath extends EdgePath
{
	private final Integer aPosition;
	
	/**
	 * @param pPosition The position of the edge.
	 * @param pPoints The points that form the path.
	 */
	public StateDiagramEdgePath(Integer pPosition, Point... pPoints)
	{
		super(pPoints);
		aPosition = pPosition;
	}
	
	/**
	 * @return position of the edge
	 */
	public Integer getPosition()
	{
		return aPosition;
	}
}
