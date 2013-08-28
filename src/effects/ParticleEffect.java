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

import org.lwjgl.opengl.ARBPointSprite;
import org.lwjgl.opengl.GL11;

import util.ReadableVector3f;
import util.Vector3f;

public class ParticleEffect extends Effect {

	private ParticleModel model;
	private Particle[] particles;
	private boolean finished;
	private float size;
	
	/**
	 * ParticleEffect, also acts as a point particle emitter. This
	 * functionalty may be pulled out later into an abstract particle
	 * emitter which can be implemented as point/line/plane etc.
	 * 
	 * @param model The particle model to use.
	 * @param size The size of the effect.
	 * @param pos The position of the effect.
	 * @param dir The direction the effect is facing, null if omnidirectional.
	 */
	public ParticleEffect(ParticleModel model, float size, ReadableVector3f pos, Vector3f dir){
		super(pos);
		this.model = model;
		this.size = size;
		int particleCount = Math.round(model.getDensity());
		particles = new Particle[particleCount];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle(pos, model,size);
		}
		finished = false;
	}
	
	public void update(Game g, float step) {
		if (!finished){
			boolean allFinished = true;
			for(int i = 0; i < particles.length; i++){
				if(particles[i] != null){
					particles[i].update(step, model);
					allFinished = false;
					if (!particles[i].isActive()){
						particles[i] = null;
					}
				}
			}
			if (allFinished){
				finished = true;
			}
		}
	}

	public boolean isActive(){
		return !finished;
	}
	
	public void draw() {
		GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        model.getMaterial().bind();
        GL11.glTexEnvf(ARBPointSprite.GL_POINT_SPRITE_ARB,ARBPointSprite.GL_COORD_REPLACE_ARB, GL11.GL_TRUE);
        GL11.glPointSize(model.getParticleSize()*size);
        GL11.glEnable(ARBPointSprite.GL_POINT_SPRITE_ARB);
        GL11.glBegin(GL11.GL_POINTS);
		for(int i = 0; i < particles.length; i++){
			if(particles[i] != null){
				particles[i].draw();
			}
		}
		GL11.glEnd();
        GL11.glDisable(ARBPointSprite.GL_POINT_SPRITE_ARB);
        GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);        
	}

	private static final Vector3f temp = new Vector3f();
	private class Particle{
		
		private float x,y,z;
		private float dx,dy,dz;
		private float fade;
		private float fadeSpeed;
		
		public Particle(ReadableVector3f pos, ParticleModel model, float size){
			temp.set((float)Math.random()-0.5f,(float)Math.random()-0.5f,(float)Math.random()-0.5f);
			temp.normalise();
			x = pos.getX() + temp.x*model.getDispersion()*size;
			y = pos.getY() + temp.y*model.getDispersion()*size;
			z = pos.getZ() + temp.z*model.getDispersion()*size;
			dx = (x - pos.getX())*model.getForce();
			dy = (y - pos.getY())*model.getForce();
			dz = (z - pos.getZ())*model.getForce();
			fade = 1.0f;
			fadeSpeed = 1.0f/model.getLifeTime();
		}
		
		public boolean isActive(){
			return fade > 0;
		}
		
		public void update(float timeStep, ParticleModel model){
			x += dx*timeStep;
			y += dy*timeStep;
			z += dz*timeStep;
			dz += model.getGravity()*timeStep;
			fade -= fadeSpeed*timeStep;
		}
		
		public void draw(){
	        GL11.glColor4f(1.0f,1.0f,1.0f,fade);
			GL11.glVertex3f(x,y,z);
		}
		
		
	}

}
