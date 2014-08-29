package com.example.checkersgame;

public class Coordinate {
	private int x;
    private int y;

	public Coordinate(int x, int y){
		this.x = x;
	    this.y = y;
		}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Coordinate))return false;
	    Coordinate otherCoordinate = (Coordinate)other;
	    if (otherCoordinate.x == this.x && otherCoordinate.y == this.y) return true;
	    return false;
		}
}