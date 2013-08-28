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

import herzog3d.Game;
import herzog3d.Player;
import model.ModelState;
import physics.CylindricalShell;
import resource.Material;
import resource.ResourceManager;
import util.GameMath;
import util.Vector3f;
import ai.UnitAI.UnitAction;
import ai.UnitAI.UnitTurn;

/**
 * @author Shannon Smith
 */
public class Tank extends Unit {

	public static final float PROJECTILE_SPEED = 50.0f;
    public static final int COST = 1000;
    private int turretTurnSpeed;
    
    public Tank(Player owner, ResourceManager res, UnitStats stats) {
    	super(owner,"Tank",stats);
    	shell = new CylindricalShell(pos, 0.5f, 0.5f);
    	skin = Material.DEFAULT_BLUE;
        turretTurnSpeed = 1;
        setModel(res.getModel("tank"));
        //setSkin(res.getMaterial("tank_skin"));
        state = new ModelState(model);
    }
    
    public float getAbsTurretDir(){
        return GameMath.normaliseAngle(state.getAngle("turret") + dir);
    }

    public float getRelTurretDir(){
        return state.getAngle("turret");
    }

    
    public void setTurretDir(float dir){
        state.setAngle("turret",dir);
    }
    
    public float getFireDir(){
    	return getAbsTurretDir();
    }
    
    public Vector3f getFirePos(){
    	return pos;
    }
    
    public void update(Game game,float step){
        super.update(game,step);

        if (ai.getTurretTurn() != UnitTurn.TURN_STOP){
        	float turretAngle = state.getAngle("turret");
        	turretAngle += turretTurnSpeed*step*ai.getTurretTurn().value;
        	state.setAngle("turret",turretAngle);
        }
//        if (controller.getFire()){
//            GL11.glLoadIdentity();
//            Connector c = model.getTransformedConnector("cannon",state);
//            c.getAxis().scale(10.0f);
//            game.addEffect(new Projectile(c.getPosition(),c.getAxis()));
//        }
        
    }
    
    public boolean isActionSupported(UnitAction action) {
    	return true;
    }



}
