package ca.mcgill.cs.stg.jetuml.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.stg.jetuml.diagrams.SequenceDiagramGraph;
import ca.mcgill.cs.stg.jetuml.framework.Clipboard;
import ca.mcgill.cs.stg.jetuml.framework.GraphPanel;
import ca.mcgill.cs.stg.jetuml.framework.Grid;
import ca.mcgill.cs.stg.jetuml.framework.SelectionList;
import ca.mcgill.cs.stg.jetuml.framework.ToolBar;

/**
 * Tests various interactions with Sequence Diagram normally triggered from the 
 * GUI. Here we use the API to simulate GUI Operation for Sequence Diagram.
 * 
 * @author Jiajun Chen
 *
 */


public class TestUsageScenariosSequenceDiagram 
{
	private SequenceDiagramGraph diagram;
	private Graphics2D aGraphics;
	private GraphPanel aPanel;
	private Grid aGrid;
	private Clipboard aClipboard;
	private SelectionList aList;
	private ImplicitParameterNode aParameterNode1;
	private ImplicitParameterNode aParameterNode2;
	private CallNode aCallNode1;
	private CallNode aCallNode2;
	private CallEdge aCallEdge1;
	
	/**
	 * General setup.
	 */
	@Before
	public void setup()
	{
		diagram = new SequenceDiagramGraph();
		aGraphics = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB).createGraphics();
		aPanel = new GraphPanel(diagram, new ToolBar(diagram));
		aGrid = new Grid();
		aClipboard = new Clipboard();
		aList = new SelectionList();
		aParameterNode1 = new ImplicitParameterNode();
		aParameterNode2 = new ImplicitParameterNode();
		aCallNode1 = new CallNode();
		aCallNode2 = new CallNode();
		aCallEdge1 = new CallEdge();
	}
	
	/**
	 * Test the creation of ParameterNode and link them with edges (not allowed).
	 */
	@Test
	public void testCreateAndLinkParameterNode()
	{
		aParameterNode1.getName().setText("client");
		aParameterNode2.getName().setText("platform");
		diagram.addNode(aParameterNode1, new Point2D.Double(5, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(25, 0));
		assertEquals(2, diagram.getRootNodes().size());
		assertEquals("client", aParameterNode1.getName().getText());
		assertEquals("platform", aParameterNode2.getName().getText());
		
		CallEdge edge1 = new CallEdge();
		ReturnEdge edge2 = new ReturnEdge();
		NoteEdge edge3 = new NoteEdge();
		diagram.addEdge(edge1, new Point2D.Double(7, 0), new Point2D.Double(26, 0));
		diagram.addEdge(edge2, new Point2D.Double(7, 0), new Point2D.Double(26, 0));
		diagram.addEdge(edge3, new Point2D.Double(7, 0), new Point2D.Double(26, 0));
		assertEquals(0, diagram.getEdges().size());
	}
	
	/**
	 * Testing create the CallNode and link it to the right Parameter's life line
	 * except CallEdge (not allowed).
	 */
	@Test
	public void testCreateCallNodeAndLinkParameterNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(5, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(25, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(7, 75));
		
		assertEquals(2, diagram.getRootNodes().size());
		assertEquals(1, aParameterNode1.getChildren().size());
		
		ReturnEdge reEdge = new ReturnEdge();
		NoteEdge noEdge = new NoteEdge();
		diagram.addEdge(reEdge, new Point2D.Double(7, 75), new Point2D.Double(26, 0));
		diagram.addEdge(noEdge, new Point2D.Double(7, 75), new Point2D.Double(26, 0));
		assertEquals(0, diagram.getEdges().size());
		
		diagram.addEdge(new CallEdge(), new Point2D.Double(7, 75), new Point2D.Double(26, 0));
		assertEquals(1, diagram.getEdges().size());
	}
	
	/**
	 * Testing link CallNode to ParameterNode life line and other CallNode.
	 */
	@Test
	public void testLinkCallNodeToLifeLineAndCallNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(5, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(25, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(7, 75));
		
		diagram.addEdge(aCallEdge1, new Point2D.Double(7, 75), new Point2D.Double(25,75));
		assertEquals(1, diagram.getEdges().size());
		assertEquals(1, aParameterNode2.getChildren().size());
		diagram.draw(aGraphics, aGrid);
		
		diagram.addEdge(new CallEdge(), new Point2D.Double(62,85), new Point2D.Double(64,88));
		assertEquals(2, diagram.getEdges().size());
		assertEquals(2, aParameterNode2.getChildren().size());
	}
	
	/**
	 * Testing link CallNode to ParameterNode's top box. A CallEdge with 
	 * "<<create>>" should appear.
	 */
	@Test
	public void testCreateCallEdgeWithCreateTag()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(5, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(105, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(7, 75));
		diagram.addEdge(aCallEdge1, new  Point2D.Double(7, 75), new Point2D.Double(8,85));
		diagram.draw(aGraphics, aGrid);

		diagram.addEdge(new CallEdge(), new Point2D.Double(59, 110), new Point2D.Double(116,0));
		diagram.draw(aGraphics, aGrid);
		assertEquals(2, diagram.getEdges().size());
		assertEquals("«create»", ((CallEdge) diagram.getEdges().toArray()[1]).getMiddleLabel());
	}
	
	/**
	 * Testing adding more edges to the diagram.
	 */
	@Test
	public void testAddMoreEdges()
	{
		ImplicitParameterNode newParaNode = new ImplicitParameterNode();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(newParaNode, new Point2D.Double(210, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.draw(aGraphics, aGrid);
		
		ReturnEdge reEdge1 = new ReturnEdge();
		diagram.addEdge(reEdge1, new Point2D.Double(145,90), new Point2D.Double(45, 90));
		assertEquals(2, diagram.getEdges().size());
		
		// call edge from first CallNode to third ParameterNode life line
		CallEdge callEdge2 = new CallEdge();
		diagram.addEdge(callEdge2, new Point2D.Double(45, 75), new Point2D.Double(210,75));
		diagram.draw(aGraphics, aGrid);
		assertEquals(3, diagram.getEdges().size());
		
		// call edge from first CallNode to third ParameterNode's top box
		CallEdge callEdge3 = new CallEdge();
		diagram.addEdge(callEdge3, new Point2D.Double(45, 75), new Point2D.Double(210,0));
		diagram.draw(aGraphics, aGrid);
		assertEquals(4, diagram.getEdges().size());
	}
	
	/**
	 * Testing NoteNode and NoteEdge creation in a sequence diagram.
	 */
	@Test
	public void testNoteNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(aCallNode2, new Point2D.Double(115, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(120,75));
		diagram.draw(aGraphics, aGrid);
		
		NoteNode noteNode = new NoteNode();
		NoteEdge edge1 = new NoteEdge();
		NoteEdge edge2 = new NoteEdge();
		NoteEdge edge3 = new NoteEdge();
		NoteEdge edge4 = new NoteEdge();
		NoteEdge edge5 = new NoteEdge();
		diagram.addNode(noteNode, new Point2D.Double(55, 55));
		diagram.addEdge(edge1, new Point2D.Double(60, 60), new Point2D.Double(87,65));
		diagram.addEdge(edge2, new Point2D.Double(62, 68), new Point2D.Double(47,75));
		diagram.addEdge(edge3, new Point2D.Double(63, 69), new Point2D.Double(47,35));
		diagram.addEdge(edge4, new Point2D.Double(64, 70), new Point2D.Double(17,5));
		diagram.addEdge(edge5, new Point2D.Double(65, 60), new Point2D.Double(67,265));
		
		assertEquals(6, diagram.getEdges().size());
		assertEquals(8, diagram.getRootNodes().size());
		
		// from ParameterNode to NoteNode
		diagram.addEdge(new NoteEdge(), new Point2D.Double(10, 10), new Point2D.Double(62, 68));
		// from CallNode to NoteNode 
		diagram.addEdge(new NoteEdge(), new Point2D.Double(10, 10), new Point2D.Double(62, 68));
		assertEquals(8, diagram.getRootNodes().size());
	}
	
	/**
	 * Testing Node movement for individual node. 
	 * Note edge could not be moved individually.
	 */
	@Test
	public void testIndividualNodeMoveMent()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(aCallNode2, new Point2D.Double(115, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(120,75));
		diagram.draw(aGraphics, aGrid);
		
		// testing moving ParameterNode
		aParameterNode1.translate(5, 15);
		diagram.draw(aGraphics, aGrid);
		assertTrue(15 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		aParameterNode1.translate(25, 0);
		diagram.draw(aGraphics, aGrid);
		assertTrue(40 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		aParameterNode2.translate(105, 25);
		diagram.draw(aGraphics, aGrid);
		assertTrue(215 == aParameterNode2.getBounds().getX());
		assertTrue(0 == aParameterNode2.getBounds().getY());
		aParameterNode2.translate(0, 15);
		diagram.draw(aGraphics, aGrid);
		assertTrue(215 == aParameterNode2.getBounds().getX());
		assertTrue(0 == aParameterNode2.getBounds().getY());
		
		// testing moving left call node
		double callNode1X = aCallNode1.getBounds().getX();
		double callNode1Y = aCallNode1.getBounds().getY();
		aCallNode1.translate(5, 15);
		diagram.draw(aGraphics, aGrid);
		assertEquals(callNode1X, aCallNode1.getBounds().getX());
		assertTrue(callNode1Y + 15 == aCallNode1.getBounds().getY());
		aCallNode1.translate(0, 15);
		diagram.draw(aGraphics, aGrid);
		assertTrue(callNode1X == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y + 15 + 15 == aCallNode1.getBounds().getY());
		aCallNode1.translate(20, 0);
		diagram.draw(aGraphics, aGrid);
		assertTrue(callNode1X == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y + 15 + 15 == aCallNode1.getBounds().getY());
		
		// testing moving right call node
		double callNode2X = aCallNode2.getBounds().getX();
		double callNode2Y = aCallNode2.getBounds().getY();
		aCallNode2.translate(5, 15);
		diagram.draw(aGraphics, aGrid);
		assertTrue(callNode2X == aCallNode2.getBounds().getX());
		assertTrue(callNode2Y == aCallNode2.getBounds().getY());
		aCallNode2.translate(0, 15);
		diagram.draw(aGraphics, aGrid);
		assertTrue(callNode2X == aCallNode2.getBounds().getX());
		assertTrue(callNode2Y == aCallNode2.getBounds().getY());
		aCallNode2.translate(20, 0);
		diagram.draw(aGraphics, aGrid);
		assertTrue(callNode2X == aCallNode2.getBounds().getX());
		assertTrue(callNode2Y == aCallNode2.getBounds().getY());
	}
	
	/**
	 * Testing moving entire graph.
	 */
	@Test
	public void testMoveEntireGraph()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(aCallNode2, new Point2D.Double(115, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(120,75));
		diagram.draw(aGraphics, aGrid);
		double callNode1X = aCallNode1.getBounds().getX();
		double callNode1Y = aCallNode1.getBounds().getY();
		double callNode2X = aCallNode2.getBounds().getX();
		double callNode2Y = aCallNode2.getBounds().getY();
		
		aPanel.selectAll();
		for(GraphElement element: aPanel.getSelectionList())
		{
			if(element instanceof Node)
			{
				((Node) element).translate(15, 0);
			}
		}
		aPanel.getSelectionList().clearSelection();
		diagram.draw(aGraphics, aGrid);
		
		assertTrue(25 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		assertTrue(125 == aParameterNode2.getBounds().getX());
		assertTrue(0 == aParameterNode2.getBounds().getY());
		assertTrue(callNode1X + 15 == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y == aCallNode1.getBounds().getY());
		assertTrue(callNode2X + 15 == aCallNode2.getBounds().getX());
		assertTrue(callNode2Y == aCallNode2.getBounds().getY());
		
		aPanel.selectAll();
		for(GraphElement element: aPanel.getSelectionList())
		{
			if(element instanceof Node)
			{
				((Node) element).translate(-25, 0);
			}
		}
		aPanel.getSelectionList().clearSelection();
		diagram.draw(aGraphics, aGrid);
		
		assertTrue(0 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		assertTrue(100 == aParameterNode2.getBounds().getX());
		assertTrue(0 == aParameterNode2.getBounds().getY());
		assertTrue(callNode1X + 15 - 25 == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y == aCallNode1.getBounds().getY());
		assertTrue(callNode2X + 15 - 25 == aCallNode2.getBounds().getX());
		assertTrue(callNode2Y == aCallNode2.getBounds().getY());
	}
	
	/**
	 * Testing moving entire graph with a <<create>> CallEdge.
	 */
	@Test
	public void testMoveEntireGraphWithCallEdge()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(15, 80), new Point2D.Double(116,0));
		diagram.draw(aGraphics, aGrid);
		
		double callNode1X = aCallNode1.getBounds().getX();
		double callNode1Y = aCallNode1.getBounds().getY();
		double paraNode2X = aParameterNode2.getBounds().getX();
		double paraNode2Y = aParameterNode2.getBounds().getY();
		
		aPanel.selectAll();
		for(GraphElement element: aPanel.getSelectionList())
		{
			if(element instanceof Node)
			{
				((Node) element).translate(15, 0);
			}
		}
		aPanel.getSelectionList().clearSelection();
		diagram.draw(aGraphics, aGrid);
	
		assertTrue(25 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		assertTrue(paraNode2X + 15 == aParameterNode2.getBounds().getX());
		assertTrue(paraNode2Y == aParameterNode2.getBounds().getY());
		assertTrue(callNode1X + 15 == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y == aCallNode1.getBounds().getY());
		
		aPanel.selectAll();
		for(GraphElement element: aPanel.getSelectionList())
		{
			if(element instanceof Node)
			{
				((Node) element).translate(-25, 0);
			}
		}
		aPanel.getSelectionList().clearSelection();
		diagram.draw(aGraphics, aGrid);
	
		assertTrue(0 == aParameterNode1.getBounds().getX());
		assertTrue(0 == aParameterNode1.getBounds().getY());
		assertTrue(paraNode2X + 15 - 25 == aParameterNode2.getBounds().getX());
		assertTrue(paraNode2Y == aParameterNode2.getBounds().getY());
		assertTrue(callNode1X + 15 - 25 == aCallNode1.getBounds().getX());
		assertTrue(callNode1Y == aCallNode1.getBounds().getY());
	}
	
	/**
	 * Below are methods testing the deletion and undo feature
	 * for sequence diagram. Currently no testing for edge deletion.
	 *
	 *
	 *
	 * Testing delete single ParameterNode
	 */
	@Test
	public void testDeleteSignleParameterNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		Rectangle2D paraNode1Bond = aParameterNode1.getBounds();
		aPanel.getSelectionList().add(aParameterNode1);
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(0, diagram.getRootNodes().size());
		aPanel.undo();
		assertEquals(1, diagram.getRootNodes().size());
		assertEquals(paraNode1Bond, ((ImplicitParameterNode) (diagram.getRootNodes().toArray()[0])).getBounds());
	}
	
	/**
	 * Testing delete single CallNode.
	 */
	@Test
	public void testDeleteSignleCallNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.draw(aGraphics, aGrid);

		Rectangle2D callNode1Bond = aCallNode1.getBounds();
		aPanel.getSelectionList().add(aCallNode1);
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(1, diagram.getRootNodes().size());
		assertEquals(0, aParameterNode1.getChildren().size());
		
		aPanel.undo();
		assertEquals(1, aParameterNode1.getChildren().size());
		assertEquals(callNode1Bond, ((CallNode) (aParameterNode1.getChildren().toArray()[0])).getBounds());
	}
	
	/**
	 * Testing delete a ParameterNode in call sequence.
	 */
	@Test
	public void testDeleteParameterNodeInCallSequence()
	{
		// sepcific test case set up 
		ImplicitParameterNode newParaNode = new ImplicitParameterNode();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(newParaNode, new Point2D.Double(210, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.draw(aGraphics, aGrid);
		ReturnEdge reEdge1 = new ReturnEdge();
		diagram.addEdge(reEdge1, new Point2D.Double(145,90), new Point2D.Double(45, 90));		
		CallEdge callEdge2 = new CallEdge();
		diagram.addEdge(callEdge2, new Point2D.Double(45, 75), new Point2D.Double(210,75));
		diagram.draw(aGraphics, aGrid);
		
		aPanel.getSelectionList().add(aParameterNode1);
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		assertEquals(2, diagram.getRootNodes().size());
		assertEquals(0, newParaNode.getChildren().size());
		/*
		 *  since a return edge is added, the call node will still remain there
		 *  however the edges are still removed
		 */
		assertEquals(1, aParameterNode2.getChildren().size()); 
		assertEquals(0, diagram.getEdges().size());
		
		aPanel.undo();
		diagram.draw(aGraphics, aGrid);
		assertEquals(3, diagram.getRootNodes().size());
		assertEquals(1, newParaNode.getChildren().size());
		assertEquals(1, aParameterNode2.getChildren().size()); 
		assertEquals(3, diagram.getEdges().size());
	}
	
	/**
	 * Testing delete a call node in the middle Parameter Node in call sequence.
	 */
	@Test
	public void testDeleteMiddleCallNode()
	{
		// sepcific test case set up 
		ImplicitParameterNode newParaNode = new ImplicitParameterNode();
		CallNode midNode = new CallNode();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(newParaNode, new Point2D.Double(210, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(midNode, new Point2D.Double(115, 75));
		diagram.addNode(new CallNode(), new Point2D.Double(215, 75));
		
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(215,75));
		diagram.addEdge(new ReturnEdge(), new Point2D.Double(118, 75), new Point2D.Double(18,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(210,115));
		
		aPanel.getSelectionList().add(midNode);
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(1, aParameterNode1.getChildren().size()); 
		assertEquals(0, aParameterNode2.getChildren().size()); 
		assertEquals(0, newParaNode.getChildren().size()); 
		assertEquals(0, diagram.getEdges().size());
		
		aPanel.undo();
		diagram.draw(aGraphics, aGrid);
		assertEquals(1, aParameterNode1.getChildren().size()); 
		assertEquals(1, aParameterNode2.getChildren().size()); 
		assertEquals(2, newParaNode.getChildren().size()); 
		assertEquals(4, diagram.getEdges().size());
	}
	
	/**
	 * Testing delete a return edge.
	 */
	@Test
	public void testDeleteReturnEdge()
	{
		CallNode midNode = new CallNode();
		ReturnEdge returnEdge = new ReturnEdge();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(midNode, new Point2D.Double(115, 75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.addEdge(returnEdge, new Point2D.Double(118, 75), new Point2D.Double(18,75));
		
		aPanel.getSelectionList().add(returnEdge);
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		assertEquals(1, aParameterNode1.getChildren().size()); 
		assertEquals(1, aParameterNode2.getChildren().size()); 
		assertEquals(1, diagram.getEdges().size());
		
		aPanel.undo();
		assertEquals(2, diagram.getEdges().size());
	}
	
	/**
	 * Testing delete a CallNode with both incoming and return edge.
	 */
	@Test
	public void testDeleteCallNodeWithIncomingAndReturnEdge()
	{
		CallNode midNode = new CallNode();
		ReturnEdge returnEdge = new ReturnEdge();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(midNode, new Point2D.Double(115, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(118, 75), new Point2D.Double(215,75));
		diagram.addEdge(returnEdge, new Point2D.Double(118, 75), new Point2D.Double(18,75));
		
		aPanel.getSelectionList().add(returnEdge);
		aPanel.getSelectionList().add(aCallEdge1);
		aPanel.getSelectionList().add(midNode);

		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		assertEquals(1, aParameterNode1.getChildren().size()); 
		assertEquals(0, aParameterNode2.getChildren().size()); 
		assertEquals(0, diagram.getEdges().size());
		
		aPanel.undo();
		assertEquals(1, aParameterNode2.getChildren().size()); 
		assertEquals(2, diagram.getEdges().size());
	}
	
	/**
	 * Below are methods testing the copy and paste feature
	 * for sequence diagram.
	 * 
	 * 
	 * Testing copy and paste signle Parameter Node.
	 */
	@Test
	public void testCopyPasteParameterNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		aPanel.getSelectionList().add(aParameterNode1);
		aClipboard.copy(aPanel.getSelectionList());
		aClipboard.paste(aPanel);
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(2, diagram.getRootNodes().size());
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[1])).getBounds().getX()));
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[1])).getBounds().getY()));
	}
	
	/**
	 * Testing cut and paste single Parameter Node.
	 */
	@Test
	public void testCutPasteParameterNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		aPanel.getSelectionList().add(aParameterNode1);
		aClipboard.copy(aPanel.getSelectionList());
		aPanel.removeSelected();
		diagram.draw(aGraphics, aGrid);
		assertEquals(0, diagram.getRootNodes().size());

		aClipboard.paste(aPanel);
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(1, diagram.getRootNodes().size());
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[0])).getBounds().getX()));
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[0])).getBounds().getY()));
	}
	
	/**
	 * Testing copy and paste Parameter Node with Call Node.
	 */
	@Test
	public void testCopyPasteParameterNodeWithCallNode()
	{
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		aPanel.getSelectionList().add(aParameterNode1);
		aClipboard.copy(aPanel.getSelectionList());
		aClipboard.paste(aPanel);
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(2, diagram.getRootNodes().size());
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[1])).getBounds().getX()));
		assertTrue(0 == (((Node)(diagram.getRootNodes().toArray()[1])).getBounds().getX()));
		assertTrue(1 == (((ImplicitParameterNode)(diagram.getRootNodes().toArray()[1])).getChildren().size()));
	}
	
	/**
	 * Testing copy and paste a whole diagram.
	 */
	@Test
	public void testCopyPasteSequenceDiagram()
	{
		// sepcific test case set up 
		ImplicitParameterNode newParaNode = new ImplicitParameterNode();
		CallNode midNode = new CallNode();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(newParaNode, new Point2D.Double(210, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(midNode, new Point2D.Double(115, 75));
		diagram.addNode(new CallNode(), new Point2D.Double(215, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(215,75));
		diagram.addEdge(new ReturnEdge(), new Point2D.Double(118, 75), new Point2D.Double(18,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(210,115));
		
		aPanel.selectAll();
		aClipboard.copy(aPanel.getSelectionList());
		aClipboard.paste(aPanel);
		diagram.draw(aGraphics, aGrid);
		
		assertEquals(6, diagram.getRootNodes().size());
		assertEquals(8, diagram.getEdges().size());
	}
	
	/**
	 * Testing copy and paste a whole diagram.
	 */
	@Test
	public void testCopyPartialGraph()
	{
		// sepcific test case set up 
		ImplicitParameterNode newParaNode = new ImplicitParameterNode();
		CallNode midNode = new CallNode();
		CallNode endNode = new CallNode();
		diagram.addNode(aParameterNode1, new Point2D.Double(10, 0));
		diagram.addNode(aParameterNode2, new Point2D.Double(110, 0));
		diagram.addNode(newParaNode, new Point2D.Double(210, 0));
		diagram.addNode(aCallNode1, new Point2D.Double(15, 75));
		diagram.addNode(midNode, new Point2D.Double(115, 75));
		diagram.addNode(endNode, new Point2D.Double(215, 75));
		diagram.addEdge(aCallEdge1, new Point2D.Double(18, 75), new Point2D.Double(115,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(215,75));
		diagram.addEdge(new ReturnEdge(), new Point2D.Double(118, 75), new Point2D.Double(18,75));
		diagram.addEdge(new CallEdge(), new Point2D.Double(118, 75), new Point2D.Double(210,115));
		
		aList.add(aCallNode1);
		aList.add(midNode);
		aList.add(endNode);
		for(Edge edge: diagram.getEdges())
		{
			aList.add(edge);
		}
		aClipboard.copy(aList);
		SequenceDiagramGraph tempDiagram = new SequenceDiagramGraph();
		GraphPanel tempPanel = new GraphPanel(tempDiagram, new ToolBar(diagram));
		aClipboard.paste(tempPanel);
		tempDiagram.draw(aGraphics, aGrid);
		
		assertEquals(0, tempDiagram.getRootNodes().size());
		assertEquals(0, tempDiagram.getEdges().size());
	}	
}
