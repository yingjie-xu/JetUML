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

import ca.mcgill.cs.jetuml.diagram.Diagram;
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
	
	@Override
	public final void draw(Diagram pDiagram, GraphicsContext pGraphics)
	{
		assert pDiagram != null && pGraphics != null;
		pDiagram.rootNodes().forEach(node -> drawNode(node, pGraphics));
		
		LAYOUTER.layOut(aEdgeLayout, pDiagram, pGraphics);
		pDiagram.edges().forEach(edge -> EdgeViewerRegistry.draw(edge, pGraphics));
	}
}
