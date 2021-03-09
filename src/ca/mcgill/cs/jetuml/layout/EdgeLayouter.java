package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import javafx.scene.canvas.GraphicsContext;

/**
 * Calculate the layout for edges.
 */
public interface EdgeLayouter
{
	/**
	 * Calculate the layout for the new diagram.
	 * @param pLayout layout to store 
	 * @param pDiagram diagram to draw on
	 * @param pGraphics graphic context
	 */
	void layOut(EdgeLayout pLayout, Diagram pDiagram, GraphicsContext pGraphics);
}
