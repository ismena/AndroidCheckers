package com.example.checkersgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class EasyAI extends AI {

	
	public EasyAI(Board board) {
		super(board);
	}

	@Override
	protected void makeBestJumpMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		makeRandomMove(allLegalMoves, checkersWithLegalMoves);	
	}

	@Override
	protected void makeBestSimpleMove(HashMap<Checker, ArrayList<Coordinate>> allLegalMoves, Set<Checker> checkersWithLegalMoves) {
		makeRandomMove(allLegalMoves, checkersWithLegalMoves);	
	}
}
