/*
 *	 Herzog3D - 3D Real Time Strategy game.
 *   Copyright (C) 2005  Shannon Smith
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package util;

public class Vector2f implements ReadableVector2f{

	public float x,y;
	
	public Vector2f(){
		x = y;
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(ReadableVector2f v){
		x = v.getX();
		y = v.getY();
	}
	
	public void set(ReadableVector2f v){
		x = v.getX();
		y = v.getY();
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void add(ReadableVector2f v){
		x += v.getX();
		y += v.getY();
	}
	
	public void sub(ReadableVector2f v){
		x -= v.getX();
		y -= v.getY();
	}
	
	public float lengthSquared(){
		return x*x + y*y;
	}
	
	public float distanceSquaredTo(Vector3f v){
		return (x - v.x)*(x - v.x) + (y - v.y)*(y - v.y);
	}

	public float distanceTo(Vector3f v){
		return (float)Math.sqrt(distanceSquaredTo(v));
	}

	
	public float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	
	public float dot(ReadableVector2f v){
		return x*v.getX() + y*v.getY();
	}
	
	public void normalise(){
		float len = length();
		if (len == 0){
			throw new ArithmeticException("Can't normalise zero length vector!");
		}
		x/=len;
		y/=len;
	}
	
	public void scale(float val){
		x*=val;
		y*=val;
	}
}
