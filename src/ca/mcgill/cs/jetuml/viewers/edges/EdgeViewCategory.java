package ca.mcgill.cs.jetuml.viewers.edges;

/**
 * An categorization of edges into different groups based on their
 * visual properties, which may not be in direct relation to their types
 * or properties. 
 * 
 * The values should be listed in order in which edges of different
 * categories should be drawn.
 */
public enum EdgeViewCategory
{
	INHERITANCE, IMPLEMENTATION, OTHER
}
