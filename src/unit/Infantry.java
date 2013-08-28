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
package unit;

import herzog3d.Player;
import model.ModelState;
import resource.ResourceManager;
import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class Infantry extends Unit {

    public Infantry(Player owner, ResourceManager res, UnitStats stats) {
    	super(owner,"Infantry", stats);
        setModel(res.getModel("infantry"));
        state = new ModelState(model);
    }
    
	public Vector3f testCollision(Vector3f v1, Vector3f v2) {
		return null;
	}

	@Override
	public float getFireDir() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector3f getFirePos() {
		// TODO Auto-generated method stub
		return null;
	}
    

    
}
