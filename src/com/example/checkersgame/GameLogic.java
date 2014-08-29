package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;

public final class GameLogic {
	
	public static HashMap<Checker, ArrayList<Coordinate>> getAllValidMoves(ArrayList<Checker> checkers, boolean isWhiteTurn) {
		HashMap<Checker, ArrayList<Coordinate>> validMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		ArrayList<Checker> currentPlayerCheckers = new ArrayList<Checker>();
		for (int i = 0; i < checkers.size(); i++) {
			if (checkers.get(i).isWhite() == isWhiteTurn) {
				currentPlayerCheckers.add(checkers.get(i));
			}
		}

		// see if any jumps are available
		for (int i = 0; i < currentPlayerCheckers.size(); i++){
			ArrayList<Coordinate> possibleJumps = getJumps(currentPlayerCheckers.get(i), checkers);
			
			// if there are, populate validMoves with them
			if (possibleJumps.size() > 0) {
				validMoves.put(currentPlayerCheckers.get(i), possibleJumps);
			}
		}
		
		// if no jumps are available, populate validMoves with simple moves
		if (validMoves.size() < 1) {
			for (int i = 0; i < currentPlayerCheckers.size(); i++){
				ArrayList<Coordinate> legalMoves = getSimpleMoves(currentPlayerCheckers.get(i), checkers);
				if (legalMoves.size() > 0) {
					validMoves.put(currentPlayerCheckers.get(i), legalMoves);
				}
			}
		}
		return validMoves;
	}
	
	public static ArrayList<Coordinate> getJumps(Checker checker, ArrayList<Checker> checkers) {
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		Coordinate downLeftJump = new Coordinate(checker.getCoordinate().getX() - 2, checker.getCoordinate().getY() + 2);
		Coordinate downRightJump = new Coordinate(checker.getCoordinate().getX() + 2, checker.getCoordinate().getY() + 2);
		Coordinate upLeftJump = new Coordinate(checker.getCoordinate().getX() + 2, checker.getCoordinate().getY() - 2);
		Coordinate upRightJump = new Coordinate(checker.getCoordinate().getX() - 2, checker.getCoordinate().getY() - 2);
		
		if (!checker.isWhite() || checker.isaKing())
		{
			if (jumpIsPossible(checker, downLeftJump, checkers)) {
				result.add(downLeftJump);
			}
			
			if (jumpIsPossible(checker, downRightJump, checkers)) {
				result.add(downRightJump);
			}
		}
		if (checker.isWhite() || checker.isaKing())
		{
			if (jumpIsPossible(checker, upLeftJump, checkers)) {
				result.add(upLeftJump);
			}
			
			if (jumpIsPossible(checker, upRightJump, checkers)) {
				result.add(upRightJump);
			}
		}
		
		return result;
	}
	
	private static Boolean jumpIsPossible(Checker checker, Coordinate targetCoordinate, ArrayList<Checker> checkers) {
		if (!legalTargetCoordinate(targetCoordinate, checkers)) {
			return false;
		}
		
		// check for an opposite player's checker to jump over
		Coordinate coordinateToJumpOver = findJumpedOverCoordinate(checker.getCoordinate(), targetCoordinate);
		Checker checkerToJumpOver = getCheckerByCoordinate(coordinateToJumpOver, checkers);
		if (checkerToJumpOver == null) {
			return false;
		}
		
		if (checkerToJumpOver.isWhite() == checker.isWhite()) {
			return false;
		}
		
		// if everything satisfies, jump is possible
		return true;
	}
	
	private static ArrayList<Coordinate> getSimpleMoves(Checker checker, ArrayList<Checker> checkers) {
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		
		// create potentially legal moves to try depending on the color of the checker (try all directions if checker wears a crown)
		if (checker.isWhite() || checker.isaKing()) {
			Coordinate move1 = new Coordinate(checker.getCoordinate().getX() - 1, checker.getCoordinate().getY() - 1);
			Coordinate move2 = new Coordinate(checker.getCoordinate().getX() + 1, checker.getCoordinate().getY() - 1);
			
			// if moves are valid indeed add them to the result list
			if (legalTargetCoordinate(move1, checkers)) {
				result.add(move1);
			}
			
			if (legalTargetCoordinate(move2, checkers)) {
				result.add(move2);
			}
		}
		
		if (!checker.isWhite() || checker.isaKing()) {
			Coordinate move3 = new Coordinate(checker.getCoordinate().getX() + 1, checker.getCoordinate().getY() + 1);
			Coordinate move4 = new Coordinate(checker.getCoordinate().getX() - 1, checker.getCoordinate().getY() + 1);
			
			if (legalTargetCoordinate(move3, checkers)) {
				result.add(move3);
			}
			
			if (legalTargetCoordinate(move4, checkers)) {
				result.add(move4);
			}
		}
				
		return result;		
	}
	
	private static Boolean legalTargetCoordinate(Coordinate targetCoordinate, ArrayList<Checker> checkers) {
		// check for end of the board
		if (targetCoordinate.getX() > 7 || targetCoordinate.getX() < 0 || targetCoordinate.getY() > 7 || targetCoordinate.getY() < 0) {
			return false;
		}
		
		// check if target coordinate is free
		Checker checkerOnTargetCoordinate = getCheckerByCoordinate(targetCoordinate, checkers);
		if (checkerOnTargetCoordinate != null) {
			return false;
		}
		
		return true;
	}
	
	public static Checker getCheckerByCoordinate(Coordinate coordinate, ArrayList<Checker> checkers){
		// loop through all the checkers in checkers collection
		for (int i = 0; i < checkers.size(); i++) {
			// if found one with matching coordinates return it
			   if (checkers.get(i).getCoordinate().equals(coordinate)){
				  return checkers.get(i);
			   }
			}
		// otherwise return null
		return null;
	}
	
	// using start and target coordinate of a checker that jumped find coordinates of a checker has been jumped over
	public static Coordinate findJumpedOverCoordinate(Coordinate startCoordinate, Coordinate targetCoordinate) {
		// if jumped down and left
		if (targetCoordinate.getX() == startCoordinate.getX() - 2 && targetCoordinate.getY() == startCoordinate.getY() + 2) {
			return new Coordinate(startCoordinate.getX() - 1, startCoordinate.getY() + 1);
		}
		//  if jumped down and right
		if (targetCoordinate.getX() == startCoordinate.getX() + 2 && targetCoordinate.getY() == startCoordinate.getY() + 2) {
			return new Coordinate(startCoordinate.getX() + 1, startCoordinate.getY() + 1);
		}
		//  if jumped up and left
		if (targetCoordinate.getX() == startCoordinate.getX() + 2 && targetCoordinate.getY() == startCoordinate.getY() - 2) {
			return new Coordinate(startCoordinate.getX() + 1, startCoordinate.getY() - 1);
		}
		// if jumped up and right
		return new Coordinate(startCoordinate.getX() - 1, startCoordinate.getY() - 1);
	}
	
	// see if a move is a jump (function assumes move is valid)
	public static Boolean isAJump(Coordinate startCoordinate, Coordinate targetCoordinate){
		if (targetCoordinate.getY() == (startCoordinate.getY() + 2) || targetCoordinate.getY() == (startCoordinate.getY() - 2)){
			return true;
		}
		return false;
	}
}
