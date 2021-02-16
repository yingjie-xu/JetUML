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
	
	/**
	 * @param pPoints The points that form the path.
	 */
	public EdgePath(Point... pPoints)
	{
		assert pPoints != null;
		assert pPoints.length > 1;
		aPoints = Arrays.asList(pPoints);
	}
}