package ca.mcgill.cs.jetuml.layout;

import ca.mcgill.cs.jetuml.diagram.Diagram;
import javafx.scene.canvas.GraphicsContext;

public interface EdgeLayouter
{
	EdgeLayout layOut(Diagram pDiagram, GraphicsContext pGraphics);
}
