package com.example.checkersgame;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class HardAITests {
	
	private Board board;
	private Checker blackChecker1;
	private Checker whiteChecker1;
	private Checker whiteChecker2;
	private ArrayList<Checker> checkers = new ArrayList<Checker>();
	private HardAI AI;


	@Test
	public void AIChoosesDoubleJumpOverASingleOne() {
		blackChecker1 = new Checker(new Coordinate(5, 0), false, false);
		whiteChecker1 = new Checker(new Coordinate(4, 1), true, false);
		whiteChecker2 = new Checker(new Coordinate(6, 1), true, false);
		Checker whiteChecker3 = new Checker(new Coordinate(2, 3), true, false);
		checkers.add(blackChecker1);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		
		board = new Board(checkers, false);
		AI = new HardAI(board);
		AI.makeMove();
		assertFalse(board.isWhiteTurn());
	}
	
	@Test
	public void AIChoosesMoveThatSavesAKing() {
		blackChecker1 = new Checker(new Coordinate(7, 0), false, false);
		Checker blackChecker2 = new Checker(new Coordinate(7, 2), false, true);
		whiteChecker1 = new Checker(new Coordinate(5, 0), true, true);
		whiteChecker2 = new Checker(new Coordinate(5, 2), true, false);
		Checker whiteChecker3 = new Checker(new Coordinate(5, 4), true, false);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		
		board = new Board(checkers, false);
		AI = new HardAI(board);
		AI.makeMove();
		assertTrue(blackChecker2.getCoordinate().equals(new Coordinate(7, 2)));
	}
	
	@Test
	public void AIChoosesMoveThatCapturesOpponentsKing() {
		blackChecker1 = new Checker(new Coordinate(4, 1), false, false);
		whiteChecker1 = new Checker(new Coordinate(5, 2), true, true);
		whiteChecker2 = new Checker(new Coordinate(3, 2), true, false);
		checkers.add(blackChecker1);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		
		board = new Board(checkers, false);
		AI = new HardAI(board);
		AI.makeMove();
		assertTrue(blackChecker1.getCoordinate().equals(new Coordinate(6, 3)));
	}
	
	@Test
	public void AIChoosesMoveToAviodDoubleCapture() {
		blackChecker1 = new Checker(new Coordinate(4, 1), false, false);
		Checker blackChecker2 = new Checker(new Coordinate(3, 2), false, false);
		whiteChecker1 = new Checker(new Coordinate(1, 4), true, false);
		whiteChecker2 = new Checker(new Coordinate(5, 4), true, false);
		Checker whiteChecker3 = new Checker(new Coordinate(6, 3), true, false);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		
		board = new Board(checkers, false);
		AI = new HardAI(board);
		AI.makeMove();
		assertTrue(blackChecker1.getCoordinate().equals(new Coordinate(5, 2)));
	}

	@Test
	public void AIChoosesMoveToCapturingBackNextTurn() {
		blackChecker1 = new Checker(new Coordinate(3, 0), false, false);
		Checker blackChecker2 = new Checker(new Coordinate(2, 1), false, false);
		Checker blackChecker3 = new Checker(new Coordinate(4, 1), false, false);
		whiteChecker1 = new Checker(new Coordinate(0, 3), true, false);
		whiteChecker2 = new Checker(new Coordinate(2, 3), true, false);
		Checker whiteChecker3 = new Checker(new Coordinate(4, 3), true, false);
		checkers.add(blackChecker1);
		checkers.add(blackChecker2);
		checkers.add(blackChecker3);
		checkers.add(whiteChecker1);
		checkers.add(whiteChecker2);
		checkers.add(whiteChecker3);
		
		board = new Board(checkers, false);
		AI = new HardAI(board);
		AI.makeMove();
		assertFalse(blackChecker3.getCoordinate().equals(new Coordinate(5, 2)) || blackChecker2.getCoordinate().equals(new Coordinate(1, 2)));
	}



}
