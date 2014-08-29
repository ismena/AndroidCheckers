package com.example.checkersgame;

public class Checker implements Cloneable {
	private Coordinate coordinate;
	private boolean isWhite;
	private boolean isaKing;
	
	public Checker(Coordinate coordinate, Boolean isWhite, Boolean isaKing) {
		this.coordinate = coordinate;
		this.isWhite = isWhite;
		this.isaKing = isaKing;
	}
	
	public Coordinate getCoordinate(){
		return coordinate;
	}
	
	public void setCoordinate(Coordinate coordinate){
		this.coordinate = coordinate;
	}
	
	public boolean isWhite(){
		return this.isWhite;
	}
	
	public boolean isaKing() {
		return this.isaKing;
	}
	
	public void makeKing() {
		this.isaKing = true;
	}
	
	// see if checker's reached the opposite side of the board
	public boolean canBecomeKing() {
		if (this.isWhite) {
			if (this.coordinate.getY() == 0) return true;
		}
		else {
			if (this.coordinate.getY() == 7) return true;
		}
		return false;
	}
	
	@Override
	public Checker clone() {
		Coordinate coordinate = new Coordinate(this.coordinate.getX(), this.coordinate.getY());
		Checker newChecker = new Checker(coordinate, this.isWhite, this.isaKing);
		return newChecker;
	}
}