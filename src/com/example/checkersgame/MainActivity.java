package com.example.checkersgame;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends Activity {
	RelativeLayout layout;
	
	private Coordinate activeCheckerCoordinate = null;
	private AI blackPlayer;
	private Board board;

	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	  }
	  
	  public void exit(View view) {
		  this.finish(); 
	  }
	  
	  public void chooseLevel(View view) {
		  setContentView(R.layout.choose_level); 
	  }
	  
	  public void startGameEasy(View view) {
		  Board board = setBoardForGame();
		  AI blackPlayer = new EasyAI(board);
		  startNewGame(blackPlayer, board); 
	  }
	  
	  public void startGameMedium(View view) {
		  Board board = setBoardForGame();
		  AI blackPlayer = new MediumAI(board);
		  startNewGame(blackPlayer, board); 
	  }
	  
	  public void startGameHard(View view) {
		  Board board = setBoardForGame();
		  AI blackPlayer = new HardAI(board);
		  startNewGame(blackPlayer, board); 
	  }
	  
	  public void startNewGame(AI blackPlayer, Board board) {
		  layout = new RelativeLayout(this);
		  setContentView(layout);
		  this.board = board;
		  this.blackPlayer = blackPlayer;
		  int cellSize = computeCellSize();
		  playGame(board, null, cellSize);
	  }
	  
	  	@SuppressLint("NewApi") 
  		public int computeCellSize() {
		  Display display = getWindowManager().getDefaultDisplay();
		  Point size = new Point();
		  int width;
		  int height;
		  int version = android.os.Build.VERSION.SDK_INT;
		  if (version > 3.1) {
			  display.getSize(size);
			  width = size.x;
			  height = size.y;
		  }
		  else {
			  width = display.getWidth();
			  height = display.getHeight();
		  }
		  int boardSideLength = (width == 0) ? height : (height == 0) ? width
			        : (width < height) ? width : height;
		  return boardSideLength/8;
	  }
	  
	  public void playGame(final Board board, Cell clickedCell, int cellSize) {
		  if (board.isWhiteTurn()) {
			  // to hold possible destinations of the active checker if any
			  ArrayList<Coordinate> framedCellsCoordinates = null;
			  if (clickedCell != null) {
				  
				  // if the user clicked on a highlighted cell, move the active checker there
				  if (clickedCell.isFramed()) {
					  board.makeMove(activeCheckerCoordinate, clickedCell.getCoordinate());
				  }
				  framedCellsCoordinates = board.getMoves(clickedCell.getCoordinate());
				  
				  // if the clicked cell has a checker with valid moves on it, make it global active checker
				  if (framedCellsCoordinates != null) activeCheckerCoordinate = clickedCell.getCoordinate();
				  
				  // if user clicked somewhere else, null global active checker
				  else activeCheckerCoordinate = null;
			  }
			  
			  // draw the board's current state
			  DrawBoard(board, framedCellsCoordinates, cellSize);
		  }
		  // check for the end of the game
		  if (board.gameOver()) {
			  gameOver(!board.isWhiteTurn());
			  return;
		  }
			  // check for computer's turn and make move if need be	  
		  if (!board.isWhiteTurn()) {
			  blackPlayer.makeMove();
			  try {
			    Thread.sleep(100);
			  } 
			  catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			  }
			  DrawBoard(board, null, cellSize);
			  
			  // in case AI needs to move twice
			  if (!board.isWhiteTurn()) {
				  playGame(board, null, cellSize);
			  }
		  }		  
		  // check for the end of the game
		  if (board.gameOver()) {
			  gameOver(!board.isWhiteTurn());
			  return;
		  }
	  }
	  
	  public void DrawBoard(final Board board, ArrayList<Coordinate> framedCellsCoordinates, final int cellSize) {
		  layout.removeAllViews();
		  boolean isWhite = true;
		  Cell cell;
		  for(int i = 0; i < 8; i++) {
			  for(int j = 0; j < 8; j++) {					  
				  // coordinate of the current board cell
				  Coordinate coordinate = new Coordinate(j, i);
				  
				  // if there is a checker on that cell, draw it
				  Checker checker = getCheckerByCoordinate(board.getCheckers(), coordinate);
				  if (checker != null) {
					 cell = new Cell(this, coordinate, isWhite, false, true, checker.isWhite(), checker.isaKing(), cellSize);
				  }
				  
				  // else see if the cell is a potential destination for the active checker, highlight it
				  else if(framedCellsCoordinates != null && framedCellsCoordinates.contains(coordinate)) {
					  cell = new Cell(this, coordinate, isWhite, true, false, false, false, cellSize);
				  }
				  
				  // otherwise draw an empty cell
				  else {
					  cell = new Cell(this, coordinate, isWhite, false, false, false, false, cellSize);
				  }
				  
				  
			      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			      params.topMargin = i * cellSize;
			      params.leftMargin = j * cellSize;
			      
			      cell.setOnTouchListener(new OnTouchListener() {
			    	@Override
					public boolean onTouch(View v, MotionEvent event) {
			    		Cell cell = (Cell)v; 
			    		playGame(board, cell, cellSize);
						return false;
						}
			      	});
			     	      
    		      layout.addView(cell, params);
			      
				  isWhite = !isWhite;
			  }
			  
			  isWhite = !isWhite;
		  }
	  }
	  
	  private Checker getCheckerByCoordinate(ArrayList<Checker> checkers, Coordinate coordinate) {
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
	  
	  private Board setBoardForGame() {
		ArrayList<Checker> checkers = new ArrayList<Checker>();
		 
		// create white checkers
		// first row
		for (int i = 0; i < 7; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 7), true, false);
			checkers.add(checker);
		}
		
		//second row
		for (int i = 1; i < 8; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 6), true, false);
			checkers.add(checker);
		}
		
		// third row
		for (int i = 0; i < 7; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 5), true, false);
			checkers.add(checker);
		}
		
		// create black checkers
		//first row
		for (int i = 1; i < 8; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 0), false, false);
			checkers.add(checker);
		}
		
		// second row
		for (int i = 0; i < 7; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 1), false, false);
			checkers.add(checker);
		}
		
		// third row
		for (int i = 1; i < 8; i = i + 2) {
			Checker checker = new Checker(new Coordinate(i, 2), false, false);
			checkers.add(checker);
		}
		
         Board board = new Board(checkers, true);
         return board;						
	  }
	  

	  private void gameOver(boolean whiteWon) {
		  // build a dialog to show user
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		  
		  // build a game over message depending on winner
		  if (whiteWon) builder.setMessage(R.string.white_won);
		  else builder.setMessage(R.string.black_won);
		  builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // back to the main menu
		        	   setContentView(R.layout.activity_main);
		           }
		       });
		  builder.create().show();
	  }
}
