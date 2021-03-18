/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/

package ca.mcgill.cs.jetuml.views;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.layout.EdgeLayouter;
import ca.mcgill.cs.jetuml.layout.UseCaseDiagramEdgeLayouter;
import ca.mcgill.cs.jetuml.viewers.edges.EdgeViewerRegistry;
import javafx.scene.canvas.GraphicsContext;

/**
 * A specialized viewer strategy for a use case diagram.
 */
public class UseCaseDiagramViewer extends DiagramViewer
{
	private static final EdgeLayouter LAYOUTER = new UseCaseDiagramEdgeLayouter();
	private static final Map<Edge, Rectangle> BOUND_CACHE = new HashMap<>();
	
	@Override
	public final void draw(Diagram pDiagram, GraphicsContext pGraphics)
	{
		assert pDiagram != null && pGraphics != null;
		pDiagram.rootNodes().forEach(node -> drawNode(node, pGraphics));
		
		LAYOUTER.layOut(aEdgeLayout, pDiagram, pGraphics);
		pDiagram.edges().forEach(edge -> EdgeViewerRegistry.draw(edge, aEdgeLayout.get(edge), pGraphics));
		
		BOUND_CACHE.clear();
		pDiagram.edges().forEach(edge -> BOUND_CACHE.put(edge, EdgeViewerRegistry.getBounds(edge, aEdgeLayout.get(edge))));
	}
	
	/**
	 * get the bounds from precalculated cache.
	 * @param pEdge edge to get the bound
	 * @return Rectangle bound
	 */
	@Override
	public Rectangle getBounds(Edge pEdge)
	{
		Rectangle res = BOUND_CACHE.get(pEdge);
		if (res == null) 
		{
			res = EdgeViewerRegistry.getBounds(pEdge);
		}
		return res;
	}
	
	/**
     * Tests whether pEdge contains a point.
     * @param pEdge the edge to test
     * @param pPoint the point to test
     * @return true if this element contains aPoint
     */
	@Override
	public boolean contains(Edge pEdge, Point pPoint)
	{
		return EdgeViewerRegistry.contains(pEdge, pPoint, aEdgeLayout.get(pEdge));
	}
	
	/**
     * Draw selection handles around pEdge.
     * @param pEdge The target edge
     * @param pGraphics the graphics context
     * @pre pEdge != null && pGraphics != null
	 */
	@Override
	public void drawSelectionHandles(Edge pEdge, GraphicsContext pGraphics)
   	{
		EdgeViewerRegistry.drawSelectionHandles(pEdge, pGraphics);
   	}
	
}
