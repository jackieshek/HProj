package runnersAndTesters;

import static org.junit.Assert.*;

import org.junit.Test;

import tool.GameGeneration;
import tool.PDGame;
import tool.ToolMethods;

public class AddVertexTest {
	
	PDGame game = new PDGame(GameGeneration.generateConnectedERGraph(30, Math.random()),
			GameGeneration.generateRandomDistribution(30, Math.random()));

	@Test
	public void test() {
		System.out.println("Testing adding a vertex to a game.");
		assertEquals(30, game.getNumberOfVertices());
		game.addVertex(0, ToolMethods.createZeroArray(30));
		assertEquals(31, game.getNumberOfVertices());
		assertEquals(1, game.getNumberOfAddedVertices());
	}

}
