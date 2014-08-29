package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class AI {
	protected Board board;
	
	public AI(Board board){
		this.board = board;
	}
		
	public void makeMove() {
		// get all possible moves
		HashMap<Checker, ArrayList<Coordinate>> allLegalMoves = GameLogic.getAllValidMoves(board.getCheckers(), board.isWhiteTurn());
		Set<Checker> checkersWithLegalMoves = allLegalMoves.keySet();
		
		// see if moves are jumps or simple ones
		Checker checker = checkersWithLegalMoves.iterator().next();
		Coordinate coordinate = allLegalMoves.get(checker).get(0);
		if (GameLogic.isAJump(checker.getCoordinate(), coordinate)) {
			makeBestJumpMove(allLegalMoves, checkersWithLegalMoves);
		}
		else {
			makeBestSimpleMove(allLegalMoves, checkersWithLegalMoves);
		}
	}
	
	 protected abstract void makeBestJumpMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves);
	 protected abstract void makeBestSimpleMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves);
	 
	protected void makeRandomMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		Checker checkerToMove = checkersWithLegalMoves.iterator().next();
		Coordinate targetCoordinate = allLegalMoves.get(checkerToMove).get(0);
		this.board.makeMove(checkerToMove.getCoordinate(), targetCoordinate);
	}
	
	protected boolean leadsToCapture(Board board, Coordinate startCoordinate, Coordinate targetCoordinate) {
		// hypothetically make the move
		Board hypotheticalBoard = new Board(board.getCheckers(), board.isWhiteTurn());
		hypotheticalBoard.makeMove(startCoordinate, targetCoordinate);
		
		// if turn didn't switch, it's double-capture, return false
		if (!hypotheticalBoard.isWhiteTurn()) {return false;}
		
		// moves that will be legal if the move is made
		HashMap<Checker, ArrayList<Coordinate>> legalMoves = GameLogic.getAllValidMoves(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
		if (legalMoves.isEmpty()) {return false;}
		Set<Checker> checkersWithLegalMoves = legalMoves.keySet();
		
		// if non of them is a jump, will be no capture, return false
		Checker checker = checkersWithLegalMoves.iterator().next();
		Coordinate coordinate = legalMoves.get(checker).get(0);
		if (!GameLogic.isAJump(checker.getCoordinate(), coordinate)) {return false;}
		// otherwise return true
		return true;
	}
	
	protected boolean leadsToDoubleCapture(Board board, Coordinate startCoordinate, Coordinate targetCoordinate) {
		// hypothetically make the move
		Board hypotheticalBoard = new Board(board.getCheckers(), board.isWhiteTurn());
		hypotheticalBoard.makeMove(startCoordinate, targetCoordinate);
		
		// moves that will be legal if the move is made
		HashMap<Checker, ArrayList<Coordinate>> legalMoves = GameLogic.getAllValidMoves(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
		Set<Checker> checkersWithLegalMoves = legalMoves.keySet();
		
		// try out each of them. If after a move was performed it's still opponent's turn then double capture is possible
		for(Checker checker: checkersWithLegalMoves){
			ArrayList<Coordinate> moves = legalMoves.get(checker);
			for (Coordinate move: moves){
				Board hypotheticalBoard2 = new Board(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
				hypotheticalBoard2.makeMove(checker.getCoordinate(), move);
				if (hypotheticalBoard2.isWhiteTurn()) return true;
			}
        }
		// if no double capture was found, return false
		return false;
	}
	// see if checker's new position will back up another checker's advance
	protected boolean defendsAnotherChecker(Coordinate targetCoordinate) {
		// search for a friendly checker on either side of the target coordinate
		Coordinate coordinate1 = new Coordinate(targetCoordinate.getX() + 1, targetCoordinate.getY() + 1);
		Checker checker1 = GameLogic.getCheckerByCoordinate(coordinate1, this.board.getCheckers());
		
		Coordinate coordinate2 = new Coordinate(targetCoordinate.getX() - 1, targetCoordinate.getY() + 1);
		Checker checker2 = GameLogic.getCheckerByCoordinate(coordinate2, this.board.getCheckers());
		
		// if one is found return true, otherwise - false
		if ((checker1 != null && !checker1.isWhite()) || checker2 != null && !checker2.isWhite()) return true;
		else return false;
	}
	
	// sort out moves that lead to our checker(s) being captured
	protected HashMap<Checker, ArrayList<Coordinate>> getNotLeadingToCaptureMoves(HashMap<Checker, ArrayList<Coordinate>> allMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> notLeadingToCaptureMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		for(Checker checker: checkers){
			ArrayList<Coordinate> moves = allMoves.get(checker);
			ArrayList<Coordinate> safeMovesForCurrentChecker = new ArrayList<Coordinate>();
			for (Coordinate move: moves) {
				if(!leadsToCapture(this.board, checker.getCoordinate(), move)) {
					safeMovesForCurrentChecker.add(move);
			    }
			}
			if (safeMovesForCurrentChecker.size() > 0) {
				notLeadingToCaptureMoves.put(checker, safeMovesForCurrentChecker);
			}
        }
		return notLeadingToCaptureMoves;
	}
	
	// sort out moves that require checkers of the last row to move
	protected HashMap<Checker, ArrayList<Coordinate>> getKingPreventedMoves(HashMap<Checker, ArrayList<Coordinate>> allMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> kingPreventedMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		for(Checker checker: checkers){
			// if checker is not in the last row, keep it as a candidate to move
			if (checker.getCoordinate().getY() != 0) {
				ArrayList<Coordinate> moves = allMoves.get(checker);
				kingPreventedMoves.put(checker, moves);
	        }
		}
		return kingPreventedMoves;
	}
}
