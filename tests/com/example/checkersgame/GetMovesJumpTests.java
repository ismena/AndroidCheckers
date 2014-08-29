package com.example.checkersgame;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


public class GetMovesJumpTests {
	
	private Coordinate whiteChecker1Coordinate = new Coordinate(3, 2);
	private Checker whiteChecker1 = new Checker(whiteChecker1Coordinate, true, false);
	
	private Coordinate whiteChecker2Coordinate = new Coordinate(1, 4);
	private Checker whiteChecker2 = new Checker(whiteChecker2Coordinate, true, false);
	
	private Coordinate blackChecker1Coordinate = new Coordinate(2, 3);
	private Checker blackChecker1 = new Checker(blackChecker1Coordinate, false, false);
	
	private Coordinate blackChecker2Coordinate = new Coordinate(2, 1);
	private Checker blackChecker2 = new Checker(blackChecker2Coordinate, false, false);
	
	private ArrayList<Checker> checkers = new ArrayList<Checker>();

	private Board board;
	
	@Before
	public void setUp() throws Exception {
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		
		board = new Board(checkers, false);
	}

	@Test
	public void simpleMoveIsNotLegalWhileAJumpIsPossible() {
		ArrayList<Coordinate> actual = board.getMoves(blackChecker1Coordinate);
		assertFalse(actual.contains(new Coordinate(3, 4)));
	}
	
	@Test
	public void checkerCannotJumpBackwards() {
		ArrayList<Coordinate> actual = board.getMoves(blackChecker1Coordinate);
		assertFalse(actual.contains(new Coordinate(1, 4)));
	}
	
	@Test
	public void ifSeveralJumpsArePossibleAllOfThemAreLegal() {
		ArrayList<Coordinate> firstCheckerMoves = board.getMoves(blackChecker1Coordinate);
		ArrayList<Coordinate> secondCheckerMoves = board.getMoves(blackChecker2Coordinate);
	
		assertTrue(firstCheckerMoves.contains(new Coordinate(0, 5)) && secondCheckerMoves.contains(new Coordinate(4, 3)));
	}

}
