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
package herzog3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.*;

import unit.Mech;
import unit.Mech.MechForm;
import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class Camera implements Cloneable {

    public static final float JET_CHASE_DIST = 6;
    public static final float JET_CHASE_HEIGHT = 10;
    public static final float ROBOT_CHASE_DIST = 4;
    public static final float ROBOT_CHASE_HEIGHT = 6 ;
    
    
//    private Unit unit;
    private Mech mech;
    private Vector3f pos;
    private Game game;
    
    /**
     * Create a camera locked on to the given mech.
     * 
     * @param target
     */
    public Camera(Game game){
       this.pos = new Vector3f(0,0,0f);
       this.game = game;
    }
    
//    public void setTarget(Unit unit){
//        this.unit = unit;
//    }
//    
    public void setMech(Mech mech){
        this.mech = mech;
    }
    
    /**
     * Update the camera position, based on the target mech.
     * 
     *
     */
    public void update(float timeStep){
        Vector3f update = new Vector3f(mech.getPos());
        if (mech.getForm() == MechForm.JET){
            update.x -= JET_CHASE_DIST*(float)Math.cos(mech.getDir());
            update.y -= JET_CHASE_DIST*(float)Math.sin(mech.getDir());
            update.z += JET_CHASE_HEIGHT;
        } else {
            update.x -= ROBOT_CHASE_DIST*(float)Math.cos(mech.getDir());
            update.y -= ROBOT_CHASE_DIST*(float)Math.sin(mech.getDir());
            update.z += ROBOT_CHASE_HEIGHT;
        }
        update.x -= pos.x;
        update.y -= pos.y;
        update.z -= pos.z;
        
        pos.x += update.x*timeStep*2.0f;
        pos.y += update.y*timeStep*2.0f;
        pos.z += update.z*timeStep*2.0f;
    }
    
    public void transform(){
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(40,1,1,100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        Vector3f targetPos = new Vector3f(mech.getPos());
        GLU.gluLookAt(pos.x,pos.y,pos.z,targetPos.x,targetPos.y,targetPos.z,0,0,1);
    }
    
    /**
     * Get the position of the camera;
     * 
     * @return
     */
    public Vector3f getPos(){
        return pos;
    }
    
    public Mech getTarget(){
        return mech;
    }
    
    
}
