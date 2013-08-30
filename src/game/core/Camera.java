package game.core;

import herzog3d.Game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import unit.Mech;
import unit.Mech.MechForm;
import util.Vector3f;

public class Camera {


    public static final float JET_CHASE_DIST = 6;
    public static final float JET_CHASE_HEIGHT = 10;
    public static final float ROBOT_CHASE_DIST = 4;
    public static final float ROBOT_CHASE_HEIGHT = 6 ;
    
    
//    private Unit unit;
    public static Mech mech = null;
    public static Vector3f pos = new Vector3f(0,0,0f);
    
//    public void setTarget(Unit unit){
//        this.unit = unit;
//    }
//    
    public static void setMech(Mech newMech){
        mech = newMech;
    }
    
    /**
     * Update the camera position, based on the target mech.
     * 
     *
     */
    public static void update(float timeStep){
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
    
    public static void transform(){
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
    public static  Vector3f getPos(){
        return pos;
    }
    
    public static Mech getTarget(){
        return mech;
    }
}
