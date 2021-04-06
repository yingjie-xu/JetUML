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
package ca.mcgill.cs.jetuml.viewers.edges;

import static ca.mcgill.cs.jetuml.views.StringViewer.FONT;

import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.diagram.edges.StateTransitionEdge;
import ca.mcgill.cs.jetuml.geom.Conversions;
import ca.mcgill.cs.jetuml.geom.Dimension;
import ca.mcgill.cs.jetuml.geom.Line;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.layout.EdgePath;
import ca.mcgill.cs.jetuml.views.ArrowHead;
import ca.mcgill.cs.jetuml.views.LineStyle;
import ca.mcgill.cs.jetuml.views.ToolGraphics;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * An edge view specialized for state transitions.
 */
public final class StateTransitionEdgeViewer extends AbstractEdgeViewer
{
	private static final int SELF_EDGE_OFFSET = 15;
	private static final int DEGREES_10 = 10;
	private static final int DEGREES_20 = 20;
	private static final int DEGREES_270 = 270;
	private static final double LINE_WIDTH = 0.6;
	
	private static final int RADIANS_TO_PIXELS = 10;
	private static final double HEIGHT_RATIO = 3.5;
	private static final int MAX_LENGTH_FOR_NORMAL_FONT = 15;
	private static final int MIN_FONT_SIZE = 9;
	
	// The amount of vertical difference in connection points to tolerate
	// before centering the edge label on one side instead of in the center.
	private static final int VERTICAL_TOLERANCE = 20; 

	private Font aFont = FONT;

	@Override
	public void draw(Edge pEdge, GraphicsContext pGraphics)
	{
		// not used any more
	}
	
	@Override
	public void draw(Edge pEdge, EdgePath pEdgePath, GraphicsContext pGraphics)
	{
		if(isSelfEdge(pEdge))
		{
			pGraphics.setStroke(Color.BLACK);
			drawSelfEdge(pEdge, pGraphics, pEdgePath);
		}
		else 
		{
			ToolGraphics.strokeSharpPath(pGraphics, (Path) getShape(pEdge, pEdgePath), LineStyle.SOLID);
		}
		drawLabel((StateTransitionEdge)pEdge, pGraphics, pEdgePath);
		drawArrowHead(pEdge, pGraphics, pEdgePath);
	}
	
	protected Shape getShape(Edge pEdge, EdgePath pEdgePath)
	{
		if( isSelfEdge(pEdge) )
		{
			return getSelfEdgeShape(pEdge, pEdgePath);
		}
		else
		{
			return getNormalEdgeShape(pEdge, pEdgePath);
		}
	}
	
	private Shape getNormalEdgeShape(Edge pEdge, EdgePath pEdgePath)
	{
		Line line = new Line(pEdgePath.getStart(), pEdgePath.getEnd());
		Path path = new Path();
		MoveTo moveTo = new MoveTo(line.getPoint1().getX(), line.getPoint1().getY());
		QuadCurveTo curveTo = new QuadCurveTo(getControlPoint(pEdge, pEdgePath).getX(), getControlPoint(pEdge, pEdgePath).getY(), 
				line.getPoint2().getX(), line.getPoint2().getY());
		path.getElements().addAll(moveTo, curveTo);
		return path;
	}
	
	/**
     *  Gets the control point for the quadratic spline.
     * @return the control point
     */
	private Point2D getControlPoint(Edge pEdge, EdgePath pEdgePath)
	{
		Line line = new Line(pEdgePath.getStart(), pEdgePath.getEnd());
		double tangent = Math.tan(Math.toRadians(DEGREES_10));
		if( pEdgePath.getPosition() > 1 )
		{
			tangent = Math.tan(Math.toRadians(DEGREES_20));
		}
		double dx = (line.getX2() - line.getX1()) / 2;
		double dy = (line.getY2() - line.getY1()) / 2;
		return new Point2D((line.getX1() + line.getX2()) / 2 + tangent * dy, (line.getY1() + line.getY2()) / 2 - tangent * dx);         
	}
	
	private Shape getSelfEdgeShape(Edge pEdge, EdgePath pEdgePath)
	{
		Line line = new Line(pEdgePath.getStart(), pEdgePath.getEnd());
		Arc arc = new Arc();
		arc.setRadiusX(SELF_EDGE_OFFSET*2);
		arc.setRadiusY(SELF_EDGE_OFFSET*2);
		arc.setLength(DEGREES_270);
		arc.setType(ArcType.OPEN);
		if( pEdgePath.getPosition() == 1 )
		{
			arc.setCenterX(line.getX1());
			arc.setCenterY(line.getY1()-SELF_EDGE_OFFSET);
			arc.setStartAngle(DEGREES_270);
		}
		else
		{		
			arc.setCenterX(line.getX1()-SELF_EDGE_OFFSET);
			arc.setCenterY(line.getY1()-SELF_EDGE_OFFSET*2);
			arc.setStartAngle(1);
		}
		return arc;
	}
	
	private void drawSelfEdge(Edge pEdge, GraphicsContext pGraphics, EdgePath pEdgePath)
	{
		Arc arc = (Arc) getShape(pEdge, pEdgePath);
		double width = pGraphics.getLineWidth();
		pGraphics.setLineWidth(LINE_WIDTH);
		pGraphics.strokeArc(arc.getCenterX(), arc.getCenterY(), arc.getRadiusX(), arc.getRadiusY(), arc.getStartAngle(), 
				arc.getLength(), arc.getType());
		pGraphics.setLineWidth(width);
	}
	
	private void drawArrowHead(Edge pEdge, GraphicsContext pGraphics, EdgePath pEdgePath)
	{
		if( isSelfEdge(pEdge) )
		{
			Point connectionPoint2 = pEdgePath.getEnd();
			if( pEdgePath.getPosition() == 1 )
			{
				ArrowHead.V.view().draw(pGraphics, new Point(connectionPoint2.getX()+SELF_EDGE_OFFSET, 
						connectionPoint2.getY()-SELF_EDGE_OFFSET/4), connectionPoint2);
			}
			else
			{
				ArrowHead.V.view().draw(pGraphics, new Point(connectionPoint2.getX()-SELF_EDGE_OFFSET/4, 
						connectionPoint2.getY()-SELF_EDGE_OFFSET), connectionPoint2);
			}
		}
		else
		{
			ArrowHead.V.view().draw(pGraphics, Conversions.toPoint(getControlPoint(pEdge, pEdgePath)), pEdgePath.getEnd());
		}
	}
	
	/*
	 *  Draws the label.
	 *  @param pGraphics2D the graphics context
	 */
	private void drawLabel(StateTransitionEdge pEdge, GraphicsContext pGraphics, EdgePath pEdgePath)
	{
		adjustLabelFont(pEdge);
		Rectangle2D labelBounds = getLabelBounds(pEdge, pEdgePath);
		double x = labelBounds.getMinX();
		double y = labelBounds.getMinY();
		
		Paint oldFill = pGraphics.getFill();
		Font oldFont = pGraphics.getFont();
		pGraphics.translate(x, y);
		pGraphics.setFill(Color.BLACK);
		pGraphics.setFont(aFont);
		pGraphics.setTextAlign(TextAlignment.CENTER);
		pGraphics.fillText(pEdge.getMiddleLabel(), labelBounds.getWidth()/2, 0);
		pGraphics.setFill(oldFill);
		pGraphics.setFont(oldFont);
		pGraphics.translate(-x, -y);        
	}
	
	private Rectangle2D getLabelBounds(StateTransitionEdge pEdge, EdgePath pEdgePath)
	{
		if( isSelfEdge(pEdge) )
		{
			return getSelfEdgeLabelBounds(pEdge, pEdgePath);
		}
		else
		{
			return getNormalEdgeLabelBounds(pEdge, pEdgePath);
		}
	}
	
	/**
     * Gets the dimensions for pString.
     * @param pString The input string. Can be null.
     * @return The dimensions of the string.
	 */
	public Dimension getLabelBounds(String pString)
	{
		if(pString == null || pString.length() == 0) 
		{
			return Dimension.NULL;
		}
		return textDimensions(pString);
	}
	
	/*
	 * Gets the bounds of the label text.
	 * @return the bounds of the label text
	 */
	private Rectangle2D getNormalEdgeLabelBounds(StateTransitionEdge pEdge, EdgePath pEdgePath)
	{
		Line line = new Line(pEdgePath.getStart(), pEdgePath.getEnd());
		Point2D control = getControlPoint(pEdge, pEdgePath);
		double x = control.getX() / 2 + line.getX1() / 4 + line.getX2() / 4;
		double y = control.getY() / 2 + line.getY1() / 4 + line.getY2() / 4;

		adjustLabelFont(pEdge);
		Dimension textDimensions = getLabelBounds(pEdge.getMiddleLabel());

		int gap = 3;
		if( line.getY1() >= line.getY2() - VERTICAL_TOLERANCE && 
				line.getY1() <= line.getY2() + VERTICAL_TOLERANCE ) 
		{
			// The label is centered if the edge is (mostly) horizontal
			x -= textDimensions.width() / 2;
		}
		else if( line.getY1() <= line.getY2() )
		{
			x += gap;
		}
		else
		{
			x -= textDimensions.width() + gap;
		}
		
		if( line.getX1() <= line.getX2() )
		{
			y -= textDimensions.height() + gap;
		}
		else
		{
			y += gap;
		}
		
		// Additional gap to make sure the labels don't overlap
		if( pEdge.getDiagram() != null && pEdgePath.getPosition() > 1 )
		{
			double delta = Math.abs(Math.atan2(line.getX2()-line.getX1(), line.getY2()-line.getY1()));
			delta = textDimensions.height() - delta*RADIANS_TO_PIXELS;
			if( line.getX1() <= line.getX2() )
			{
				y -= delta;
			}
			else
			{
				y += delta;
			}
		}
		return new Rectangle2D(x, y, textDimensions.width(), textDimensions.height());
}   
	
	/*
	 * Positions the label above the self edge, centered
	 * in the middle of it.
	 * @return the bounds of the label text
	 */
	private Rectangle2D getSelfEdgeLabelBounds(StateTransitionEdge pEdge, EdgePath pEdgePath)
	{
		Line line = new Line(pEdgePath.getStart(), pEdgePath.getEnd());
		adjustLabelFont(pEdge);
		Dimension textDimensions = getLabelBounds(pEdge.getMiddleLabel());
		if( pEdgePath.getPosition() == 1 )
		{
			return new Rectangle2D(line.getX1() + SELF_EDGE_OFFSET - textDimensions.width()/2,	
					line.getY1() - SELF_EDGE_OFFSET*2, textDimensions.width(), textDimensions.height());
		}
		else
		{
			return new Rectangle2D(line.getX1() - textDimensions.width()/2,	
					line.getY1() - SELF_EDGE_OFFSET * HEIGHT_RATIO, textDimensions.width(), textDimensions.height());
		}
	}   
	
	private void adjustLabelFont(StateTransitionEdge pEdge)
	{
		if(pEdge.getMiddleLabel().length() > MAX_LENGTH_FOR_NORMAL_FONT)
		{
			float difference = pEdge.getMiddleLabel().length() - MAX_LENGTH_FOR_NORMAL_FONT;
			difference = difference / (2*pEdge.getMiddleLabel().length()); // damping
			double newFontSize = Math.max(MIN_FONT_SIZE, (1-difference) * FONT.getSize());
			aFont = new Font(aFont.getName(), newFontSize);
		}
		else
		{
			aFont = FONT;
		}
	}

	private boolean isSelfEdge(Edge pEdge)
	{
		return pEdge.getStart() == pEdge.getEnd();
	}
	
	@Override
	public boolean contains(Edge pEdge, Point pPoint, EdgePath pEdgePath)
	{
		if(pPoint.distance(pEdgePath.getStart()) <= MAX_DISTANCE || pPoint.distance(pEdgePath.getEnd()) <= MAX_DISTANCE)
		{
			return false;
		}

		Shape fatPath = getShape(pEdge, pEdgePath);
		fatPath.setStrokeWidth(2 * MAX_DISTANCE);
		boolean result = fatPath.contains(pPoint.getX(), pPoint.getY());
		if (getShape(pEdge, pEdgePath) instanceof Arc)
		{
			Arc arc = (Arc) getShape(pEdge, pEdgePath);
			arc.setRadiusX(arc.getRadiusX() + 2 * MAX_DISTANCE);
			arc.setRadiusY(arc.getRadiusY() + 2 * MAX_DISTANCE);
			result = arc.contains(pPoint.getX(), pPoint.getY());
		}
		return result;
	}
	
	@Override
	public Rectangle getBounds(Edge pEdge, EdgePath pEdgePath)
	{
		return super.getBounds(pEdge).add(Conversions.toRectangle(getLabelBounds((StateTransitionEdge)pEdge, pEdgePath)));
	}
	
	@Override
	public void drawSelectionHandles(Edge pEdge, GraphicsContext pGraphics, EdgePath pEdgePath)
	{
		ToolGraphics.drawHandles(pGraphics, new Line(pEdgePath.getStart(), pEdgePath.getEnd()));	
	}
	
	@Override
	public Canvas createIcon(Edge pEdge)
	{   //CSOFF: Magic numbers
		Canvas canvas = new Canvas(BUTTON_SIZE, BUTTON_SIZE);
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		graphics.scale(0.6, 0.6);
		Line line = new Line(new Point(2,2), new Point(40,40));
		final double tangent = Math.tan(Math.toRadians(DEGREES_10));
		double dx = (line.getX2() - line.getX1()) / 2;
		double dy = (line.getY2() - line.getY1()) / 2;
		Point control = new Point((int)((line.getX1() + line.getX2()) / 2 + tangent * dy), 
				(int)((line.getY1() + line.getY2()) / 2 - tangent * dx));         
		
		Path path = new Path();
		MoveTo moveTo = new MoveTo(line.getPoint1().getX(), line.getPoint1().getY());
		QuadCurveTo curveTo = new QuadCurveTo(control.getX(), control.getY(), line.getPoint2().getX(), line.getPoint2().getY());
		path.getElements().addAll(moveTo, curveTo);
		
		ToolGraphics.strokeSharpPath(graphics, path, LineStyle.SOLID);
		ArrowHead.V.view().draw(graphics, control, new Point(40, 40));
		return canvas;
	}
}
