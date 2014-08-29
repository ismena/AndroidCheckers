package com.example.checkersgame;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class KingTests {
	private Board board;
	private ArrayList<Checker> checkers; 
	private Coordinate kingCoordinate;
	private Checker king;
	private Checker blackChecker1;
	private Checker blackChecker2;

	@Before
	public void setUp() throws Exception {
		checkers = new ArrayList<Checker>(); 

	}

	@Test
	public void kingSuccesfullyCrowned() {
		Coordinate coordinate = new Coordinate(2, 1);
		Checker checker = new Checker(coordinate, true, false);
		checkers.add(checker);
		board = new Board(checkers, true);
		board.makeMove(coordinate, new Coordinate(1, 0));
		assertTrue(checker.isaKing());
	}
	
	@Test
	public void kingCanMoveInAllDirections() {
		kingCoordinate = new Coordinate(6, 3);
		king = new Checker(kingCoordinate, true, true);
		checkers = new ArrayList<Checker>(); 
		checkers.add(king);
		board = new Board(this.checkers, true);
		ArrayList<Coordinate> moves = board.getMoves(kingCoordinate);
		assertTrue(moves.contains(new Coordinate(5, 2)) && moves.contains(new Coordinate(7, 2))
				&& moves.contains(new Coordinate(5, 4)) && moves.contains(new Coordinate(7, 4)));
	}
	
	@Test
	public void kingCanJumpInAllDirections() {
		kingCoordinate = new Coordinate(3, 4);
		king = new Checker(kingCoordinate, true, true);
		blackChecker1 = new Checker(new Coordinate(2, 3), false, false);
		blackChecker2 = new Checker(new Coordinate(4, 3), false, false);
		Checker blackChecker3 = new Checker(new Coordinate(2, 5), false, false);
		Checker blackChecker4 = new Checker(new Coordinate(4, 5), false, false);
		checkers.add(king);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		checkers.add(blackChecker3);
		checkers.add(blackChecker4);
		board = new Board(this.checkers, true);
		ArrayList<Coordinate> moves = board.getMoves(kingCoordinate);
		assertTrue(moves.contains(new Coordinate(1, 2)) && moves.contains(new Coordinate(5, 2))
				&& moves.contains(new Coordinate(1, 6)) && moves.contains(new Coordinate(5, 6)));
	}
	
	@Test
	public void kingCanJumpInDifferentDirectionSecondTime() {
		kingCoordinate = new Coordinate(3, 4);
		king = new Checker(kingCoordinate, true, true);
		blackChecker1 = new Checker(new Coordinate(4, 3), false, false);
		blackChecker2 = new Checker(new Coordinate(6, 3), false, false);
		checkers.add(king);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		board = new Board(this.checkers, true);
		Boolean jumpForward = board.makeMove(kingCoordinate, new Coordinate(5, 2));
		Boolean jumpBackwards = board.makeMove(new Coordinate(5, 2), new Coordinate(7, 4));
		assertTrue(jumpForward && jumpBackwards);
	}

}
