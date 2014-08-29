package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MediumAI extends AI {

	public MediumAI(Board board) {
		super(board);
	}
	
	protected void makeBestJumpMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		// look for possibility to jump and not be captured as a result. If found one - perform
		for(Checker checker: checkersWithLegalMoves){
			ArrayList<Coordinate> moves = allLegalMoves.get(checker);
			for (Coordinate move: moves){
				if (!leadsToCapture(this.board, checker.getCoordinate(), move)) {
					this.board.makeMove(checker.getCoordinate(), move);
					return;
				}
			}
        }

		// otherwise make any jump that won't allow the opponent to perform double-capture next turn
		for(Checker checker: checkersWithLegalMoves){
			ArrayList<Coordinate> moves = allLegalMoves.get(checker);
			for (Coordinate move: moves){
				if (!leadsToDoubleCapture(this.board, checker.getCoordinate(), move)) {
					this.board.makeMove(checker.getCoordinate(), move);
					return;
				}
			}
        }
		// in case they all do, embrace the fate and perform any
		makeRandomMove(allLegalMoves, checkersWithLegalMoves);
	}

	protected void makeBestSimpleMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		// sort out ones that lead to being captured
		HashMap<Checker, ArrayList<Coordinate>> safeMoves = getNotLeadingToCaptureMoves(allLegalMoves, checkersWithLegalMoves);
		
		// if all available moves lead to capture, make either of them
		if (safeMoves.isEmpty()) {
			makeRandomMove(allLegalMoves, checkersWithLegalMoves);
			return;
		}
		
		// otherwise sort out ones that require checkers of the last row to move
		HashMap<Checker, ArrayList<Coordinate>> kingPreventedMoves = getKingPreventedMoves(safeMoves, safeMoves.keySet());
		
		// if all safe moves involve third-row checkers, choose from them, otherwise update safeMoves to also be foreign kingPrevented
		if (!kingPreventedMoves.isEmpty()) {
			safeMoves = kingPreventedMoves;
		}
		Set<Checker> chosenCheckers = safeMoves.keySet();
		// look for move to back up the advance of another checker
		for(Checker checker: chosenCheckers){
			ArrayList<Coordinate> moves = safeMoves.get(checker);
			for (Coordinate move: moves){
				if(defendsAnotherChecker(move) && !checker.isaKing()) {
					this.board.makeMove(checker.getCoordinate(), move);
					return;
				}
			}
        }
		
		// if still no luck, make a move that will bring any non-kinged checker as close to becoming one as it gets at this point
		Checker checkerToMove = chosenCheckers.iterator().next();
		Coordinate targetCoordinate = safeMoves.get(checkerToMove).get(0);
		for(Checker checker: chosenCheckers){
			ArrayList<Coordinate> moves = safeMoves.get(checker);
			for (Coordinate move: moves){
				if((move.getY() > targetCoordinate.getY() && !checker.isaKing()) || (checkerToMove.isaKing() && !checker.isaKing())) {
					checkerToMove = checker;
					targetCoordinate = move;
				}
			}
        }
		this.board.makeMove(checkerToMove.getCoordinate(), targetCoordinate);
	}
}
