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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import resource.ResourceManager;

public class UnitFactory {

	private Map<String,UnitSpec> units;
	private ResourceManager res;
	
	public UnitFactory(ResourceManager res){
		this.res = res;
		units = new HashMap<String,UnitSpec>();
	}
	
	public Collection<String> getUnitNames(){
		return units.keySet();
	}
	
	public int getTimeToBuild(String unitName){
		return units.get(unitName).stats.getBuildTime();
	}
	
	public void addUnit(String name, String provider, UnitStats stats){
		try {
			units.put(name,new UnitSpec(Class.forName("unit." + provider),stats));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Unit buildUnit(Player player, String name){
		UnitSpec spec = units.get(name);
		Unit unit = null;
		try {
			unit = (Unit)spec.provider.getConstructor(Player.class, ResourceManager.class, UnitStats.class).newInstance(player,res,spec.stats);
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
		
		return unit;
	}
	
	private class UnitSpec {
		Class provider;
		UnitStats stats;
		
		public UnitSpec(Class provider,UnitStats stats){
			this.provider = provider;
			this.stats = stats;
		}
	}
	
}
