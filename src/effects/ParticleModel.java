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

import resource.Material;

public class ParticleModel {

	private String name;
	private Material material;
	private float lifeTime;
	private float density;
	private float gravity;
	private float force;
	private float dispersion;
	private float particleSize;
	
	public ParticleModel(String name, Material material){
		this.name = name;
		this.material = material;
		lifeTime = 1.0f;
		density = 50.0f;  // Size 1 effect has 50 particles.
		gravity = 0.0f;
		force = 20.0f;
		dispersion = 0.1f;
		particleSize = 100.0f;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public String getName(){
		return name;
	}
	
	public float getParticleSize(){
		return particleSize;
	}
	
	public void setParticleSize(float particleSize){
		this.particleSize = particleSize;
	}
	
	public float getDensity(){
		return density;
	}
	
	public void setDensity(float density){
		this.density = density;
	}
	
	public float getDispersion(){
		return dispersion;
	}
	
	public void setDispersion(float dispersion){
		this.dispersion = dispersion;
	}
	
	public float getForce(){
		return force;
	}
	
	public void setForce(float force){
		this.force = force;
	}
	
	public float getLifeTime(){
		return lifeTime;
	}
	
	public void setLifeTime(float lifeTime){
		this.lifeTime = lifeTime;
	}
	
	public float getGravity(){
		return gravity;
	}
	
	public void setGravity(float gravity){
		this.gravity = gravity;
	}
}
