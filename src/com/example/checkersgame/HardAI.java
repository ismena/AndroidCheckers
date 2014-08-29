package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HardAI extends AI {

	public HardAI(Board board) {
		super(board);
	}

	protected void makeBestJumpMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		boolean safeJumpsExist = false;
		HashMap<Checker, ArrayList<Coordinate>> chosenMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		// look for possibility to jump and not be captured as a result
		for(Checker checker: checkersWithLegalMoves){
			ArrayList<Coordinate> moves = allLegalMoves.get(checker);
			ArrayList<Coordinate> chosenMovesCurrentChecker = new ArrayList<Coordinate>();
			for (Coordinate move: moves){
				// if found, check if double-capture can be safely performed
				if (!leadsToCapture(this.board, checker.getCoordinate(), move)) {
					if (safeDoubleCapture(checker, move)) {
						this.board.makeMove(checker.getCoordinate(), move);
						return;
					}
					chosenMovesCurrentChecker.add(move);
				}
			}
			if (chosenMovesCurrentChecker.size() > 0) {
				chosenMoves.put(checker, chosenMovesCurrentChecker);
				safeJumpsExist = true;
			}
        }
		
		// if no moves have been chosen, look for moves that won't allow the opponent to perform double-capture next turn
		if (!safeJumpsExist) {
			chosenMoves = getDoubleJumpProofMoves(allLegalMoves, checkersWithLegalMoves);
		}
		// if no such move exists, choose from what we have
		if (chosenMoves.isEmpty()) {
			chosenMoves = allLegalMoves;
		}
		
		// if all moves are unsafe, sort out ones that jeopardize our kings
		if (!safeJumpsExist) {
			HashMap<Checker, ArrayList<Coordinate>> kingSafeMoves = getKingSafeMoves(chosenMoves, chosenMoves.keySet());
			if (!kingSafeMoves.isEmpty()) {chosenMoves = kingSafeMoves;}
		}
		// in the collection of jumps we picked search for one that allows to capture an opposite player's king
		Set<Checker> chosenCheckers = chosenMoves.keySet();
		for(Checker checker: chosenCheckers){
			ArrayList<Coordinate> moves = chosenMoves.get(checker);
			for (Coordinate move: moves) {
				if (capturesAKing(checker.getCoordinate(), move)) {
					this.board.makeMove(checker.getCoordinate(), move);
					return;
				}
			}
        }
		// if none does, perform a random move
		makeRandomMove(chosenMoves, chosenCheckers);
	}
	
	protected void makeBestSimpleMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		// sort out ones that lead to being captured
		HashMap<Checker, ArrayList<Coordinate>> safeMoves = getNotLeadingToCaptureMoves(allLegalMoves, checkersWithLegalMoves);

		// if all available moves lead to capture, pass them to a specially designed function to choose the best one
		if (safeMoves.isEmpty()) {
			chooseBestLeadingToCaptureSimpleMove(allLegalMoves, allLegalMoves.keySet());
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
				if(defendsAnotherChecker(move)) {
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
	
	private boolean safeDoubleCapture(Checker checker, Coordinate targetCoordinate) {
		// hypothetically make the move
		Board hypotheticalBoard = new Board(this.board.getCheckers(), this.board.isWhiteTurn());
		hypotheticalBoard.makeMove(checker.getCoordinate(), targetCoordinate);
		
		// if turn has switched no double jump is possible (safe or otherwise), return false
		if (hypotheticalBoard.isWhiteTurn()) {
			return false;
		}
		
		// otherwise check available moves for being capture-proof
		ArrayList<Coordinate> moves = hypotheticalBoard.getMoves(targetCoordinate);
		for (Coordinate move: moves){
			// if safe one is found, return true
			if (!leadsToCapture(hypotheticalBoard, targetCoordinate, move)) {
				return true;
			}
		}
		
		// otherwise return false
		return false;
	}
	
	// return moves that do not get our kings captured (if any)
	private HashMap<Checker, ArrayList<Coordinate>> getKingSafeMoves(HashMap<Checker, ArrayList<Coordinate>> allMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> kingSafeMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		for(Checker checker: checkers){
			ArrayList<Coordinate> moves = allMoves.get(checker);
			ArrayList<Coordinate> chosenMovesCurrentChecker = new ArrayList<Coordinate>();
			for (Coordinate move: moves){
				if (!getsKingCaptured(checker.getCoordinate(), move)) {chosenMovesCurrentChecker.add(move);}
			}
			if (chosenMovesCurrentChecker.size() > 0) {kingSafeMoves.put(checker, chosenMovesCurrentChecker);}
		}
		return kingSafeMoves;
    }
	
	private boolean getsKingCaptured(Coordinate startCoordinate, Coordinate targetCoordinate) {
		// Hypothetically make move
		Board hypotheticalBoard = new Board(this.board.getCheckers(), this.board.isWhiteTurn());
		hypotheticalBoard.makeMove(startCoordinate, targetCoordinate);
		
		// get moves that will be valid if the move is made
		HashMap<Checker, ArrayList<Coordinate>> validMoves = GameLogic.getAllValidMoves(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
		Set<Checker> checkers = validMoves.keySet();
		
		// look at every move
		for(Checker checker: checkers) {
			ArrayList<Coordinate> moves = hypotheticalBoard.getMoves(checker.getCoordinate());
			for (Coordinate move: moves) {
				if (GameLogic.isAJump(checker.getCoordinate(), move)) {
					Coordinate capturedCoordinate = GameLogic.findJumpedOverCoordinate(checker.getCoordinate(), move);
					
					// if opponent will be allowed to capture our king, return true
					Checker capturedChecker = GameLogic.getCheckerByCoordinate(capturedCoordinate, hypotheticalBoard.getCheckers());
					if (capturedChecker.isaKing()) {return true;}
				}
			}
		}
		// otherwise return false
		return false;
	}
	
	
	private HashMap<Checker, ArrayList<Coordinate>> getDoubleJumpProofMoves(HashMap<Checker, ArrayList<Coordinate>> legalMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> getDoubleJumpProofMoves = new HashMap<Checker, ArrayList<Coordinate>>();
		for(Checker checker: checkers){
			ArrayList<Coordinate> moves = legalMoves.get(checker);
			ArrayList<Coordinate> chosenMovesCurrentChecker = new ArrayList<Coordinate>();
			for (Coordinate move: moves){
				if (!leadsToDoubleCapture(this.board, checker.getCoordinate(), move)) {
					chosenMovesCurrentChecker.add(move);
				}
			}
			if (chosenMovesCurrentChecker.size() > 0) {getDoubleJumpProofMoves.put(checker, chosenMovesCurrentChecker);}
        }
		return getDoubleJumpProofMoves;
	}
	
	private boolean capturesAKing(Coordinate startCoordinate, Coordinate targetCoordinate) {
		Coordinate capturedCheckerCoordinate = GameLogic.findJumpedOverCoordinate(startCoordinate, targetCoordinate);
		Checker capturedChecker = GameLogic.getCheckerByCoordinate(capturedCheckerCoordinate, this.board.getCheckers());
		return capturedChecker.isaKing();
	}
	
	private void chooseBestLeadingToCaptureSimpleMove(HashMap<Checker, ArrayList<Coordinate>> legalMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> chosenMoves = legalMoves;
		
		// sort out moves that lead to double capture
		HashMap<Checker, ArrayList<Coordinate>> doubleCaptureProofMoves = getDoubleJumpProofMoves(chosenMoves, checkers);
		if (!doubleCaptureProofMoves.isEmpty()) {chosenMoves = doubleCaptureProofMoves;}
		
		// sort out moves that jeopardize our kings
		HashMap<Checker, ArrayList<Coordinate>> kingSafeMoves = getKingSafeMoves(chosenMoves, chosenMoves.keySet());
		if (!kingSafeMoves.isEmpty()) {chosenMoves = kingSafeMoves;}
		
		// sort out moves that do not allow us to (inevitably) catch opponen't checker in return
		HashMap<Checker, ArrayList<Coordinate>> movesLeadingToCapturingBack = getMovesLeadingToCapturingBack(chosenMoves, chosenMoves.keySet());
		if (!movesLeadingToCapturingBack.isEmpty()) {chosenMoves = movesLeadingToCapturingBack;}
		
		// randomly choose from the ones that are left
		Set<Checker> chosenCheckers = chosenMoves.keySet();
		makeRandomMove(chosenMoves, chosenCheckers);
		
	}
	
	// get moves that (no matter what counter-move opponent makes) will give us an opportunity to capture one of their checkers
	private HashMap<Checker, ArrayList<Coordinate>> getMovesLeadingToCapturingBack(HashMap<Checker, ArrayList<Coordinate>> legalMoves, Set<Checker> checkers) {
		HashMap<Checker, ArrayList<Coordinate>> movesLeadingToCapturingBack = new HashMap<Checker, ArrayList<Coordinate>>();
		for(Checker checker: checkers){
			ArrayList<Coordinate> moves = legalMoves.get(checker);
			ArrayList<Coordinate> chosenMovesCurrentChecker = new ArrayList<Coordinate>();
			for (Coordinate move: moves){
				if (leadsToCapturingBack(checker.getCoordinate(), move)) {chosenMovesCurrentChecker.add(move);}
			}
			if (chosenMovesCurrentChecker.size() > 0) {movesLeadingToCapturingBack.put(checker, chosenMovesCurrentChecker);}
		}
		return movesLeadingToCapturingBack;
    }
	
	private boolean leadsToCapturingBack(Coordinate startCoordinate, Coordinate targetCoordinate) {
		// Hypothetically make move
		Board hypotheticalBoard = new Board(this.board.getCheckers(), this.board.isWhiteTurn());
		hypotheticalBoard.makeMove(startCoordinate, targetCoordinate);
		
		// get moves that will be valid if the move is made
		HashMap<Checker, ArrayList<Coordinate>> validMoves = GameLogic.getAllValidMoves(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
		Set<Checker> checkers = validMoves.keySet();
		
		// hypothetically make each of them
		for(Checker checker: checkers){
			ArrayList<Coordinate> moves = validMoves.get(checker);
			for (Coordinate move: moves){
				Board hypotheticalBoard2 = new Board(hypotheticalBoard.getCheckers(), hypotheticalBoard.isWhiteTurn());
				hypotheticalBoard2.makeMove(checker.getCoordinate(), move);
				
				// get moves that will be valid if the move is made
				HashMap<Checker, ArrayList<Coordinate>> hypotheticallyValidMoves = GameLogic.getAllValidMoves(hypotheticalBoard2.getCheckers(),
																												hypotheticalBoard2.isWhiteTurn());
				// if no moves will be, return false
				if (hypotheticallyValidMoves.isEmpty()) {
					return false;
				}
				// otherwise, if any of them is not a jump, there is a combination for the opponent to avoid being captured given the initial move.
				// Return false
				Checker chosenChecker = hypotheticallyValidMoves.keySet().iterator().next();
				Coordinate chosenMove = hypotheticalBoard2.getMoves(chosenChecker.getCoordinate()).get(0);
				if (!GameLogic.isAJump(chosenChecker.getCoordinate(), chosenMove)) {
					return false;
				}
			}
		}
		// otherwise return true
		return true;
	}
}













