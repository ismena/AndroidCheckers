package com.example.checkersgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Cell extends View {
	private Coordinate coordinate;
	private boolean isWhite;
	private boolean isFramed;
	private boolean isOccupied;
	private boolean isOccupuiedByWhiteChecker;
	private boolean isOccupiedByKing;
	private int sideLength;
	private Paint paint;
	
	private int whiteCellColor = Color.parseColor("#FEE9CC");
	private int blackCellColor = Color.parseColor("#8E6540");
	private int checkerRadius;
	
	public Cell(Context context, Coordinate coordinate, boolean isWhite, boolean isFramed, boolean isOccupied, 
				boolean isOccupuiedByWhiteChecker, boolean isOccupiedByKing, int sideLength) {
		super(context);
		this.coordinate = coordinate;
		this.isWhite = isWhite;
		this.isFramed = isFramed;
		this.isOccupied = isOccupied;
		this.isOccupuiedByWhiteChecker = isOccupuiedByWhiteChecker;
		this.isOccupiedByKing = isOccupiedByKing;
		this.sideLength = sideLength;
		this.checkerRadius = sideLength / 2;
		paint = new Paint();
		
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}
	
	public boolean isFramed() {
		return this.isFramed;
	}
	
	@Override
	  protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
	    int width = this.sideLength;
	    int height = this.sideLength;
	    setMeasuredDimension(width, height);
	  }
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (this.isWhite) DrawEmptyCell(canvas, true);
		else if (this.isOccupied) {
			if (this.isOccupuiedByWhiteChecker) {
				if (this.isOccupiedByKing)DrawOccupiedCell(canvas, true, true);
				else DrawOccupiedCell(canvas, false, true);
			}
			else {
				if (this.isOccupiedByKing)DrawOccupiedCell(canvas, true, false);
				else DrawOccupiedCell(canvas, false, false);
			}
		}
		else {
			if (this.isFramed) DrawFramedCell(canvas);
			else DrawEmptyCell(canvas, false);
		}
	}
	
	private void DrawEmptyCell(Canvas canvas, boolean isWhite) {
		int color = isWhite ? whiteCellColor : blackCellColor;
		paint.setColor(color);
		canvas.drawRect(0, 0, sideLength, sideLength, paint);
	}
	
	private void DrawFramedCell(Canvas canvas) {
		canvas.drawColor(blackCellColor);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE); 
		paint.setStrokeWidth(4.5f);
		canvas.drawRect(0, 0, sideLength, sideLength, paint);
	}
	
	private void DrawOccupiedCell(Canvas canvas, boolean occupiedByKing, boolean occupiedByWhitePiece) {
		this.setBackgroundColor(blackCellColor);
		if (occupiedByWhitePiece) {paint.setColor(Color.WHITE);}
	    else {paint.setColor(Color.BLACK);}
		canvas.drawCircle(sideLength / 2, sideLength / 2, checkerRadius, paint);
		if (occupiedByKing) {
			paint.setColor(Color.GRAY);
			canvas.drawCircle((float)sideLength / 2, (float)sideLength / 2, (float) (checkerRadius / 1.5), paint);
		}
	}

	
	
	

}
