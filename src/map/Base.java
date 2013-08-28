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
package map;

import java.awt.Color;

import herzog3d.HZObject;
import herzog3d.Player;
import model.Model;
import model.ModelState;

import org.lwjgl.opengl.GL11;

import physics.BoxShell;
import resource.Material;
import resource.ResourceManager;
import resource.ResourceNotFoundException;
import util.ReadableVector3f;
import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class Base extends HZObject {

    public enum BaseType {
    	MINI_BASE(1),
    	HOME_BASE(2);
    	public final int size;
    	private BaseType(int size){
    		this.size = size;
    	}
    	
    }
    
    private Tile centerTile;
    private Model model;
    private Player owner;
    private ModelState state;
    private BaseType type;
    private int ownerUnits;
    private int enemyUnits;
    private int life;
    
    public Base(Tile centerTile, BaseType type, ResourceManager res) throws ResourceNotFoundException {
    	super(new Vector3f(centerTile.getX() + 0.5f, centerTile.getY() + 0.5f, centerTile.getHeightAt(0.5f,0.5f)));
    	this.type =type;
    	if (type == BaseType.HOME_BASE){
            model = res.getModel("base");
            skin = res.getMaterial("base");
    	} else {
    		model = res.getModel("mini_base");
    	}
        this.centerTile = centerTile;
        state = new ModelState(model);
        shell = new BoxShell(pos,5f,1.0f);
        life = 1000;
    }
    
    public ReadableVector3f getCenter(){
    	return centerTile.getCenter();
    }
    
    public BaseType getType(){
    	return type;
    }
    
    public int getLife(){
    	return life;
    }
    
    public void dammage(int dammage){
    	life -= dammage;
    }

    public boolean isAlive(){
    	return type == BaseType.MINI_BASE || life > 0;
    }
    
    public boolean isInTile(int x,int y){
        if (Math.abs(centerTile.getX() - x) <= type.size && Math.abs(centerTile.getY() - y) <= type.size){
            return true;
        } else {
            return false;
        }
    }
    
    public int getSize(){
        return type.size;
    }
    
    public void setOwner(Player owner){
        this.owner = owner;
    }
    
    public void draw(GameMap map){
    	if (!isAlive()){
    		return;
    	}
    	if (owner == null){
    		GL11.glColor3f(1.0f,1.0f,1.0f);
    	} else {
    		Color playerColour = owner.getColour();
    		GL11.glColor3f(playerColour.getRed()/255.0f, playerColour.getGreen()/255.0f, playerColour.getBlue()/255.0f);
    	}
    	
    	skin.bind();
    	
    	GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,pos.z);
        model.draw(state);
//        shell.draw();
        GL11.glPopMatrix();
    }

	public Vector3f testCollision(Vector3f v1, Vector3f v2) {
		return shell.testCollision(v1,v2);
	}
	
	public boolean canInfiltrate(Player player){
		return type == BaseType.MINI_BASE;
	}
}
	
