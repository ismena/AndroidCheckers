package com.example.checkersgame;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class MediumAITests {
	
	private Board board;
	private ArrayList<Checker> checkers;
	private Checker blackChecker1;
	private Checker blackChecker2;
	private Checker whiteChecker1;
	private Checker whiteChecker2;
	private Checker whiteChecker3;
	private Checker whiteChecker4;
	private MediumAI AI;

	@Before
	public void setUp() throws Exception {
		blackChecker2 = new Checker(new Coordinate(2, 5), false, false);
		checkers = new ArrayList<Checker>()
				{{
				    add(blackChecker2);
				}};
	}

	@Test
	public void AIMovesCheckerThatIsCloserToBeingKinged() {
		whiteChecker1 = new Checker(new Coordinate(7, 4), true, false);
		checkers.add(whiteChecker1);
		board = new Board(checkers, false);
		AI = new MediumAI(board);
		AI.makeMove();
		assertTrue(blackChecker2.getCoordinate().equals(new Coordinate(1, 6)) 
				|| blackChecker2.getCoordinate().equals(new Coordinate(3, 6)));
	}
	
	@Test
	public void AIChoosesMoveThatDoesntLeadToCapture() {
		whiteChecker1 = new Checker(new Coordinate(0, 7), true, false);
		checkers.add(whiteChecker1);
		board = new Board(checkers, false);
		AI = new MediumAI(board);
		AI.makeMove();
		assertFalse(blackChecker2.getCoordinate().equals(new Coordinate(1, 6)));
	}
	
	@Test
	public void AIMovesToBackAnotherChecker() {
		blackChecker1 = new Checker(new Coordinate(4, 3), false, false);
		checkers.add(blackChecker1);
		board = new Board(checkers, false);
		AI = new MediumAI(board);
		AI.makeMove();
		assertTrue(blackChecker1.getCoordinate().equals(new Coordinate(3, 4)));
	}
	
	@Test
	public void AIChoosesMoveThatDoesntLeadToDoubleCapture() {
		blackChecker1 = new Checker(new Coordinate(3, 2), false, false);
		whiteChecker1 = new Checker(new Coordinate(1, 2), true, true);
		whiteChecker2 = new Checker(new Coordinate(5, 4), true, false);
		whiteChecker3 = new Checker(new Coordinate(0, 7), true, false);
		whiteChecker4 = new Checker(new Coordinate(4, 7), true, false);
		checkers.add(blackChecker1);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		checkers.add(whiteChecker4);
		board = new Board(checkers, false);
		AI = new MediumAI(board);
		AI.makeMove();
		assertFalse(blackChecker1.getCoordinate().equals(new Coordinate(2, 3)));
	}	
	
	@Test
	public void test() {
		blackChecker1 = new Checker(new Coordinate(3, 0), false, false);
		blackChecker2 = new Checker(new Coordinate(7, 2), false, false);
		whiteChecker1 = new Checker(new Coordinate(5, 4), true, true);
		checkers = new ArrayList<Checker>();
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		checkers.add(whiteChecker1);
		board = new Board(checkers, false);
		AI = new MediumAI(board);
		AI.makeMove();
		assertTrue(blackChecker2.getCoordinate().equals(new Coordinate(7, 2)));
	}
}

