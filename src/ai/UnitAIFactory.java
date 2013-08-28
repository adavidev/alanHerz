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
package ai;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import resource.ResourceManager;
import unit.Unit;
import unit.UnitStats;

public class UnitAIFactory {

	private Map<String,AISpec> ais;
	private ResourceManager resManager;
	
	public UnitAIFactory(ResourceManager resManager){
		this.resManager = resManager;
		ais = new HashMap<String,AISpec>();
	}
	
	public int getUnitAICount(){
		return ais.size();
	}

	public void addUnitAI(String name, String provider, int cost){
		try {
			ais.put(name,new AISpec(Class.forName("ai." + provider), cost));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<String> getAINames(){
		return ais.keySet();
	}
	
	public UnitAI buildAI(String name, Unit unit){
		AISpec spec = ais.get(name);
		UnitAI ai = null;
		try {
			ai = (UnitAI)spec.provider.getConstructor(ResourceManager.class, Unit.class).newInstance(resManager,unit);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return ai;
	}
	
	
	private class AISpec {
		
		int cost;
		Class provider;
		
		public AISpec(Class provider, int cost){
			this.provider = provider;
			this.cost = cost;
		}
		
	}
	
}
