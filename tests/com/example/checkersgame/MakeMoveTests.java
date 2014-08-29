package com.example.checkersgame;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class MakeMoveTests {
	private Coordinate whiteChecker1Coordinate = new Coordinate(5, 4);
	private Checker whiteChecker1 = new Checker(whiteChecker1Coordinate, true, false);
	
	private Coordinate whiteChecker2Coordinate = new Coordinate(1, 6);
	private Checker whiteChecker2 = new Checker(whiteChecker2Coordinate, true, false);
	
	private Coordinate whiteChecker3Coordinate = new Coordinate(4, 7);
	private Checker whiteChecker3 = new Checker(whiteChecker3Coordinate, true, false);
	
	private Coordinate blackChecker1Coordinate = new Coordinate(4, 3);
	private Checker blackChecker1 = new Checker(blackChecker1Coordinate, false, false);
	
	private Coordinate blackChecker2Coordinate = new Coordinate(2, 1);
	private Checker blackChecker2 = new Checker(blackChecker2Coordinate, false, false);
	
	private Coordinate blackChecker3Coordinate = new Coordinate(2, 5);
	private Checker blackChecker3 = new Checker(blackChecker3Coordinate, false, false);
	
	private ArrayList<Checker> checkers;
	private Board board;


	@Before
	public void setUp() throws Exception {
		checkers = new ArrayList<Checker>()
				{{
				    add(whiteChecker1);
				    add(whiteChecker2);
				    add(whiteChecker3);
				    add(blackChecker1);
				    add(blackChecker2);
				    add(blackChecker3);
				}};
			}
	
	@Test
	public void cannotMakeIllegalMove() {
		board = new Board(checkers, false);
		Boolean result = board.makeMove(whiteChecker1Coordinate, new Coordinate(4, 5));
		assertFalse(result);
	}
	

	@Test
	public void jumpedOverCheckerSuccessfullyRemoved() {
		board = new Board(checkers, false);
		board.makeMove(blackChecker3Coordinate, new Coordinate(2, 5));
		assertNull(board.getMoves(whiteChecker2Coordinate));

	}
	
	@Test
	public void checkerCanJumpTwice() {
		board = new Board(checkers, true);
		board.makeMove(whiteChecker1Coordinate, new Coordinate(3, 2));
		Boolean secondJumpSuccessful = board.makeMove(new Coordinate(3, 2), new Coordinate(1, 0));
		assertTrue(secondJumpSuccessful);

	}
	
	@Test
	public void turnSuccesfullySwitched() {
		board = new Board(checkers, true);
		board.makeMove(whiteChecker1Coordinate, new Coordinate(3, 2));
		board.makeMove(new Coordinate(3, 2), new Coordinate(1, 0));
		Boolean blackCheckerJumped = board.makeMove(blackChecker3Coordinate, new Coordinate(0, 7));
		assertTrue(blackCheckerJumped);
	}
	
	@Test
	public void canMakeSimpleMove() {
		board = new Board(checkers, true);
		board.makeMove(whiteChecker1Coordinate, new Coordinate(3, 2));
		board.makeMove(new Coordinate(3, 2), new Coordinate(1, 0));
		board.makeMove(blackChecker3Coordinate, new Coordinate(0, 7));
		Boolean whiteCheckerMoved = board.makeMove(whiteChecker3Coordinate, new Coordinate(5, 6));
		assertTrue(whiteCheckerMoved);
	}
	
	@Test
	public void ifCheckerCanJumpSecondTimeItsTheOnlyValidMove() {
		board = new Board(checkers, true);
		board.makeMove(whiteChecker1Coordinate, new Coordinate(3, 2));
		ArrayList<Coordinate> jumpedCheckerMoves = board.getMoves(new Coordinate(3, 2));
		ArrayList<Coordinate> anotherWhiteCheckerMoves = board.getMoves(whiteChecker2Coordinate);
		ArrayList<Coordinate> blackCheckerMoves = board.getMoves(blackChecker3Coordinate);
		assertTrue(jumpedCheckerMoves.contains(new Coordinate(1, 0)) && anotherWhiteCheckerMoves == null && blackCheckerMoves == null);

	}

}
