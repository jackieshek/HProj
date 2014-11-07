package runnersAndTesters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import tool.GameGeneration;
import tool.PDGame;
import tool.ToolMethods;

public class RemoveVertexTest {

	PDGame game = new PDGame(GameGeneration.generateConnectedERGraph(30, Math.random()),
			GameGeneration.generateRandomDistribution(30, Math.random()));
	int stEdges = game.getNumberOfEdges();

	@Test
	public void test() {
		System.out.println("Testing removing a vertex to a game.");
		assertEquals(30, game.getNumberOfVertices());
		game.removeVertices(new ArrayList<>(Arrays.asList(0)));
		assertEquals(29, game.getNumberOfVertices());
		assertTrue(stEdges>=game.getNumberOfEdges());
	}

}
