package runnersAndTesters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import tool.GameGeneration;
import tool.PDGame;

public class PDGameTest2 {

	PDGame testGame = new PDGame(GameGeneration.generatePolygonGraph(10), 
			new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
	
	@Test
	public void test() {
		System.out.println("Testing play for cyclic all coop game.");
		testGame.updateToEnd();
		assertEquals(1, testGame.getIterations());
	}

}
