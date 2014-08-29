package com.example.checkersgame;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class GetMovesSimpleMovesTests {
	
	private Coordinate whiteChecker1Coordinate = new Coordinate(1, 6);
	private Checker whiteChecker1 = new Checker(whiteChecker1Coordinate, true, false);
	
	private Coordinate whiteChecker2Coordinate = new Coordinate(3, 4);
	private Checker whiteChecker2 = new Checker(whiteChecker2Coordinate, true, false);
	
	private Coordinate whiteChecker3Coordinate = new Coordinate(0, 5);
	private Checker whiteChecker3 = new Checker(whiteChecker3Coordinate, true, false);
	
	private Coordinate blackCheckerCoordinate = new Coordinate(6, 1);
	private Checker blackChecker = new Checker(blackCheckerCoordinate, false, false);
	
	private ArrayList<Checker> checkers = new ArrayList<Checker>();
	
	private Board board;


	@Before
	public void setUp() throws Exception {
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		checkers.add(blackChecker);
		
		board = new Board(checkers, true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void checkerCanMoveForwardInBothDirections() {
		ArrayList<Coordinate> expected = new ArrayList<Coordinate>();
		expected.add(new Coordinate(2, 3));
		expected.add(new Coordinate(4, 3));
		ArrayList<Coordinate> actual = board.getMoves(whiteChecker2Coordinate);
		assertEquals(expected, actual);
	}
	
	@Test
	public void checkerCannotMoveInOppositePlayersTurn() {
		ArrayList<Coordinate> actual = board.getMoves(blackCheckerCoordinate);
		assertNull(actual);
	}
	
	@Test
	public void coordinateWithNoCheckerOnItHasNoMoves() {
		ArrayList<Coordinate> actual = board.getMoves(new Coordinate(2, 1));
		assertNull(actual);
	}
	
	@Test
	public void checkerCannotMoveToAnOccupiedCoordinate() {
		ArrayList<Coordinate> actual = board.getMoves(whiteChecker1Coordinate);
		assertFalse(actual.contains(whiteChecker2Coordinate));
	}
	
	@Test
	public void checkerCannotMoveOutsideTheBoard() {
		ArrayList<Coordinate> actual = board.getMoves(whiteChecker3Coordinate);
		assertFalse(actual.contains(new Coordinate(-1, 4)));
	}
	
	@Test
	public void checkerCannotMoveBackwards() {
		ArrayList<Coordinate> actual = board.getMoves(whiteChecker1Coordinate);
		assertFalse(actual.contains(new Coordinate(2, 7)));
	}
	
}
