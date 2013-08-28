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
package physics;

import util.ReadableVector3f;
import util.Vector3f;

public class SphericalShell extends Shell {

	private float radius;
	private Vector3f v1 = new Vector3f();
	private Vector3f v2 = new Vector3f();
	

	public SphericalShell(ReadableVector3f pos, float radius) {
		super(pos);
		this.radius = radius;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public boolean isInside(ReadableVector3f v){
    	v1.set(v);
    	v1.sub(pos);
        if (v1.lengthSquared() < radius*radius){
            return true;
        } else {
        	return false;
        }
	}
	
	public boolean isInside(SphericalShell s){
    	v1.set(s.pos);
    	v1.sub(pos);
        if (v1.lengthSquared() < ((radius+s.radius)*(radius+s.radius))){
            return true;
        } else {
        	return false;
        }
	}

    public Vector3f testCollision(ReadableVector3f p1, ReadableVector3f p2){
    	v1.set(p1);
    	v1.sub(pos);
    	v2.set(p2);
    	v2.sub(pos);
        
        if (v1.lengthSquared() < radius*radius){
            return null;
        }
        
        v2.sub(v1);
        float a, b, c;
        a = v2.lengthSquared();
        b = 2.0f * v2.dot(v1);
        c = v1.lengthSquared() - radius*radius;
        
        float d = b*b - 4*a*c;
        if(d < 0){
            return null;
        }

        float u;
        float u1 = (-b + (float)Math.sqrt(d))/(2.0f*a);
        float u2 = (-b - (float)Math.sqrt(d))/(2.0f*a);

        if (u1 > 0 && u1 < 1.0f && u2 > 0 && u2 < 1.0f){
            u = Math.min(u1,u2);
        } else if (u1 > 0 && u1 < 1.0f){
            u = u1;
        } else if (u2 > 0 && u2 < 1.0f){
            u = u2;
        } else {
            return null;
        }
        
        /* Calculate the position of the collision */
        Vector3f loc = new Vector3f(v1.x + v2.x * u + pos.getX(),
                                    v1.y + v2.y * u + pos.getY(),
                                    v1.z + v2.z * u + pos.getZ());
        

        return loc;
    }

	public Vector3f intersects(Shell shell) {
		if (shell instanceof SphericalShell){
			return intersects((SphericalShell)shell);
		} else if (shell instanceof BoxShell){
			return intersects((BoxShell)shell);
		} else {
			throw new UnsupportedOperationException("Unknown shell type: " + shell);
		}
	}
	
	public Vector3f intersects(SphericalShell shell){
		float dist = radius + ((SphericalShell)shell).getRadius();
		if (shell.getPos().distanceSquaredTo(pos) >= dist*dist){
			return null;
		} else {
			Vector3f ret = new Vector3f(pos);
			ret.sub(shell.getPos());
			dist = dist - ret.length();
			ret.normalise();
			ret.scale(dist);
			return ret;
		}
	}
	
	public Vector3f intersects(BoxShell shell){
		throw new UnsupportedOperationException("Sphere/Rect collision Not implemented yet!");
	}
}
