package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	// * Valid moves for all the checkers that are currently allowed to move by game business rules
	private Map<Checker, ArrayList<Coordinate>> validMoves;
	// * Which player's turn is it
	private Boolean isWhiteTurn;
	// * All checkers currently on board
	private ArrayList<Checker> checkers;
	private boolean gameOver = false;
	
	public Board (ArrayList<Checker> checkers, Boolean isWhiteTurn) {
		this.checkers = checkers;
		this.isWhiteTurn = isWhiteTurn;
		this.validMoves = GameLogic.getAllValidMoves(checkers, isWhiteTurn);
	}
	
	// return defensive copy of checkers list
	public ArrayList<Checker> getCheckers() {
		ArrayList<Checker> checkersCopy = new ArrayList<Checker>();
		for(Checker checker : this.checkers) {
			checkersCopy.add((Checker)checker.clone());
		}
		return checkersCopy;
	}
	
	public boolean gameOver() {
		return this.gameOver;
	}
	
	public boolean isWhiteTurn() {
		return this.isWhiteTurn;
	}
	
	// * Get available moves (if any) for a checker with given coordinates
	public ArrayList<Coordinate> getMoves(Coordinate coordinate)
	{
		// find a checker with given coordinates
		Checker checker = GameLogic.getCheckerByCoordinate(coordinate, this.checkers);

		// retrieve any valid moves the checker has and return them
		if (checker != null && validMoves.containsKey(checker)) {
			return validMoves.get(checker);
		}
		// return null if checker is invalid or has no valid moves
		return null;
	}
	
	public Boolean makeMove(Coordinate startCoordinate, Coordinate targetCoordinate) {
		// get all valid moves for the checker with a given start coordinate
		ArrayList<Coordinate> moves = getMoves(startCoordinate);
		
		// if a move from start coordinate to target coordinate is legal, make it (change the checker's coordinate)
		if (moves != null && moves.contains(targetCoordinate)){
			Checker checker = GameLogic.getCheckerByCoordinate(startCoordinate, this.checkers);
			checker.setCoordinate(targetCoordinate);
			
			// if checker just moved now qualifies to become a king, make it one
			if (checker.canBecomeKing()) checker.makeKing();
			
			// if move was a jump 
			if (GameLogic.isAJump(startCoordinate, targetCoordinate)) {
				
				// find a checker jumped over and remove it from the board
				Coordinate jumpedOver = GameLogic.findJumpedOverCoordinate(startCoordinate, targetCoordinate);
				Checker toDelete = GameLogic.getCheckerByCoordinate(jumpedOver, this.checkers);
				this.checkers.remove(toDelete);
				
				// if the checker can jump again
				ArrayList<Coordinate> otherJumps = GameLogic.getJumps(checker, this.checkers);
				if (otherJumps.size() > 0) {
					// set validMoves to contain those jumps only. Return true, but do not switch turn until it's done jumping
					this.validMoves = new HashMap<Checker, ArrayList<Coordinate>>();
					this.validMoves.put(checker, otherJumps);
					return true;
				}
			}
			// give turn to another player and return true
			switchTurn();
			return true;
		}
		
		// otherwise return false
		return false;
	}
	
	
	private void switchTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
		this.validMoves = GameLogic.getAllValidMoves(this.checkers, this.isWhiteTurn);
		if (this.validMoves.size() < 1) {
			this.gameOver = true;
		}
	}
}

