package ca.mcgill.cs.jetuml.layout;

import java.util.Arrays;
import java.util.List;

import ca.mcgill.cs.jetuml.annotations.Immutable;
import ca.mcgill.cs.jetuml.geom.Point;

/**
 * Represents the path of an edge.
 */
@Immutable
public class EdgePath
{
	private final List<Point> aPoints;
	private final Integer aPosition;
	
	/**
	 * @param pPoints The points that form the path.
	 */
	public EdgePath(Point... pPoints)
	{
		assert pPoints != null;
		assert pPoints.length > 1;
		aPoints = Arrays.asList(pPoints);
		aPosition = null;
	}
	
	/**
	 * @param pPosition The position of the edge.
	 * @param pPoints The points that form the path.
	 */
	public EdgePath(Integer pPosition, Point... pPoints)
	{
		assert pPoints != null;
		assert pPoints.length > 1;
		aPoints = Arrays.asList(pPoints);
		aPosition = pPosition;
	}
	
	/**
	 * @return start point of the path
	 */
	public Point getStart()
	{
		return aPoints.get(0);
	}
	
	/**
	 * @return end point of the path
	 */
	public Point getEnd()
	{
		return aPoints.get(aPoints.size()-1);
	}
	
	/**
	 * @return position of the edge
	 */
	public Integer getPosition()
	{
		return aPosition;
	}
}
