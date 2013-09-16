/*
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
import map.Tile;
import model.KeyFrameAnimation;
import model.Model;
import model.ModelState;

import org.lwjgl.opengl.GL11;

import physics.CylindricalShell;
import physics.Shell;
import resource.Material;
import resource.ResourceManager;
import resource.ResourceNotFoundException;
import util.GameMath;
import util.Vector3f;
import ai.UnitAI.UnitMove;

/**
 * @author Shannon Smith
 */
public class Mech extends Unit {

	private static final UnitStats ROBOT_STATS = new UnitStats(
			0,0,100,100,100,0,20,2,10
	);

	private static final UnitStats JET_STATS = new UnitStats(
			0,0,100,100,100,0,100,2,10
	);
	
	
    public enum MechForm {
        JET(JET_STATS),
        ROBOT(ROBOT_STATS);
        
        public UnitStats stats;
        
        private MechForm(UnitStats stats){
        	this.stats = stats;
        }
        
    }

    private MechForm form;
    private float aniPos;
    private KeyFrameAnimation ani;
    private boolean transforming;
    private Shell jetShell;
    private Shell robotShell;
    
    public Mech(Player owner, ResourceManager res) throws ResourceNotFoundException {
    	super(owner,"Mech",MechForm.JET.stats);
        setModel(res.getModel("mech"));
        setSkin(res.getMaterial("green"));
        transforming = false;
        form = MechForm.JET;
        state = new ModelState(model);
        aniPos = 0;
        ani = model.getAnimation("flying");
        energy = 100;
        ammo = 100;
        armour = 100;
        jetShell = new CylindricalShell(pos,0.5f,0.4f);
        robotShell = new CylindricalShell(pos,0.2f,1.4f);
        shell = jetShell;
    }
    
    public MechForm getForm(){
        return form;
    }
    
    public boolean isFlying(){
    	return form == MechForm.JET; 
    }
    
    private void transform(MechForm newForm){
        switch (newForm){
        	case JET:
                switchAnimation("transform_jet");
        	    transforming = true;
                form = newForm;
                shell = jetShell;
                break;
        	case ROBOT:
                switchAnimation("transform_robot");
                transforming = true;
                form = newForm;
                shell = robotShell;
                break;
        	    
        }
        setStats(form.stats);
    }
    
    public void switchAnimation(String name){
        aniPos = 0;
        ani = model.getAnimation(name);
    }
    
    public float getFireDir(){
    	return dir;
    }
    
    public Vector3f getFirePos(){
    	Vector3f firePos = new Vector3f(pos);
    	if (form == MechForm.ROBOT){
    		firePos.z += 0.2;
    	} else {
    		
    	}
    	return firePos;
    }    
    
    
    public void update(Game game, float step){
        if (((Player)ai).getTransform()){
            switch (form){
                case JET:
                	if (game.getMap().canLand(pos.x,pos.y) && game.getBase(pos.x,pos.y) == null){
                    		transform(MechForm.ROBOT);
                    }
                    break;
                case ROBOT:
                    transform(MechForm.JET);
                    break;
            }
        }
        
        if (ani.getName().equals("walking") && ai.getMove() == UnitMove.MOVE_STOP) {
            switchAnimation("standing");
        } else if (ani.getName().equals("standing") && ai.getMove() != UnitMove.MOVE_STOP) {
            switchAnimation("walking");
        }
        if (ani.getLength() != 0){
	        ani.setState(aniPos,state);
            aniPos += step;
	        if (aniPos >= ani.getLength()){
	            if (transforming){
                    switch (form){
                    case JET:
                        switchAnimation("flying");
                        break;
                    case ROBOT:
                        switchAnimation("walking");
                        break;
                    }
	                transforming = false;
	            } else {
                    while (aniPos > ani.getLength()){
                        aniPos -= ani.getLength();
                    }         
	            }
	        }
        }
        if (!transforming){
        	super.update(game,step);
        }
    }
}
