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

public abstract class Shell {

	protected ReadableVector3f pos;

	public Shell(ReadableVector3f pos){
		this.pos = pos;
	}
	
	public ReadableVector3f getPos(){
		return pos;
	}

	public abstract Vector3f testCollision(ReadableVector3f v1, ReadableVector3f v2);
	
	public abstract Vector3f intersects(Shell shell);
	
	public void draw(){}
}
