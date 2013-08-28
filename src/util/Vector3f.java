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

public class Vector3f implements ReadableVector3f{

	public float x,y,z;
	
	public Vector3f(){
		x = y = z = 0;
	}
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(ReadableVector3f v){
		x = v.getX();
		y = v.getY();
		z = v.getZ();
	}
	
	public void set(ReadableVector3f v){
		x = v.getX();
		y = v.getY();
		z = v.getZ();
	}
	
	public void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	public void add(ReadableVector3f v){
		x += v.getX();
		y += v.getY();
		z += v.getZ();
	}
	
	public void add(float x, float y, float z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void sub(ReadableVector3f v){
		x -= v.getX();
		y -= v.getY();
		z -= v.getZ();
	}
	
	public float lengthSquared(){
		return x*x + y*y + z*z;
	}
	
	public float distanceSquaredTo(ReadableVector3f v){
		return (x - v.getX())*(x - v.getX()) + 
			   (y - v.getY())*(y - v.getY()) + 
			   (z - v.getZ())*(z - v.getZ());
	}

	public float distanceTo(ReadableVector3f v){
		return (float)Math.sqrt(distanceSquaredTo(v));
	}

	
	public float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	
	public float dot(ReadableVector3f v){
		return x*v.getX() + y*v.getY() + z*v.getZ();
	}
	
	public Vector3f cross(ReadableVector3f v){
		return cross(v, new Vector3f());
	}

	public Vector3f cross(ReadableVector3f v, Vector3f dest){
		dest.set(y * v.getZ() - z * v.getY(),
		 		 v.getX() * z - v.getZ() * x,
		 		 x * v.getY() - y * v.getX());
		return dest;
	}

	public void normalise(){
		float len = length();
		if (len == 0){
			throw new ArithmeticException("Can't normalise zero length vector!");
		}
		x/=len;
		y/=len;
		z/=len;
	}
	
	public void scale(float val){
		x*=val;
		y*=val;
		z*=val;
	}
	
	public boolean equals(Object obj){
		Vector3f v = (Vector3f)obj;
		return v.x == x && v.y == y && v.z == z;
	}
	
	public String toString(){
		return "Vector3f (" + x + "," + y + "," + z + ")";
	}
}
