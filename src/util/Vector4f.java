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

public class Vector4f implements ReadableVector4f{

	public float x,y,z,w;
	
	public Vector4f(){
		x = y = z = w = 0;
	}
	
	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(ReadableVector4f v){
		x = v.getX();
		y = v.getY();
		z = v.getZ();
		w = v.getW();
	}
	
	public void set(ReadableVector4f v){
		x = v.getX();
		y = v.getY();
		z = v.getZ();
		w = v.getW();
	}
	
	public void set(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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
	
	public float getW(){
		return w;
	}
	
	public void add(ReadableVector4f v){
		x += v.getX();
		y += v.getY();
		z += v.getZ();
		w += v.getW();
	}
	
	public void add(float x, float y, float z, float w){
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
	}
	
	public void sub(ReadableVector4f v){
		x -= v.getX();
		y -= v.getY();
		z -= v.getZ();
		w -= v.getW();
	}
	
	public float lengthSquared(){
		return x*x + y*y + z*z + w*w;
	}
	
	public float distanceSquaredTo(Vector4f v){
		return (x - v.x)*(x - v.x) + (y - v.y)*(y - v.y) + (z - v.z)*(z - v.z) + (w - v.w)*(w - v.w);
	}

	public float distanceTo(Vector4f v){
		return (float)Math.sqrt(distanceSquaredTo(v));
	}

	
	public float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	
	public float dot(ReadableVector4f v){
		return x*v.getX() + y*v.getY() + z*v.getZ() + w*v.getW();
	}
	
	public void normalise(){
		float len = length();
		if (len == 0){
			throw new ArithmeticException("Can't normalise zero length vector!");
		}
		x/=len;
		y/=len;
		z/=len;
		w/=len;
	}
	
	public void scale(float val){
		x*=val;
		y*=val;
		z*=val;
		w*=val;
	}
	
	public boolean equals(Object obj){
		Vector4f v = (Vector4f)obj;
		return v.x == x && v.y == y && v.z == z && v.w == w;
	}
	
	public String toString(){
		return "Vector4f (" + x + "," + y + "," + z + "," + w + ")";
	}
}
