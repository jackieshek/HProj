package runnersAndTesters;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import tool.PDGame;
import tool.GameGeneration;

import org.junit.Test;

public class PDGameTest1 extends TestCase{

	//Set up a cyclic game
	PDGame testGame = new PDGame(GameGeneration.generatePolygonGraph(10), 
			new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0)));
	
	@Test
	public void testPlay() {
		System.out.println("Testing play for cyclic all defect game.");
		testGame.updateToEnd();
		assertEquals(1, testGame.getIterations());
	}
}
