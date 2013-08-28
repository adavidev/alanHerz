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
package effects;

import herzog3d.Game;
import resource.ResourceManager;
import util.ReadableVector3f;
import util.Vector3f;

public class Explosion extends Effect {

	private ParticleEffect explode;
	private ParticleEffect smoke;
	
	public Explosion(ResourceManager resManager, Vector3f pos, Vector3f dir){
		super(pos);
    	this.explode = new ParticleEffect(resManager.getParticleModel("explosion"),0.5f, pos, dir);
    	this.smoke = new ParticleEffect(resManager.getParticleModel("smoke"),0.5f, pos, dir);
    }

	public void update(Game g, float step) {
		explode.update(g,step);
		smoke.update(g,step);
	}

	public void draw() {
		smoke.draw();
		explode.draw();
	}

    
}
