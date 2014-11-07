package runnersAndTesters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import tool.GameGeneration;
import tool.PDGame;

public class PDGameTest4 {

	PDGame testGame = new PDGame(GameGeneration.generatePolygonGraph(10), 
			new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1,1,1)));
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void test() {
		System.out.println("Testing play for a badly configured game.");
		testGame.updateToEnd();
		//Check the number of rounds
		assertEquals(6, testGame.getIterations());
		//Check it is indeed at stable point - should hold unless game has lasted > 500 rounds
		assertTrue(testGame.atStablePoint());
	}

}
