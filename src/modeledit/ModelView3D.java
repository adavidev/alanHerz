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
package modeledit;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import model.Model;
import model.ModelSection;
import model.ModelState;

import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.GLUtils;

/**
 * @author Shannon Smith
 */
public class ModelView3D extends AWTGLCanvas implements MouseMotionListener, MouseListener, MouseWheelListener {

    private Model model;
    private ModelState state;
    private boolean initialised = false;
    private ModelSection selectedSection;
    private float camTheta,camPhi,camDist;
    private float panX, panY;
    
	private static FloatBuffer scratch = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static float[] whiteLight = new float[]{1.0f,1.0f,1.0f,0.1f};
	private static float[] ambientLight = new float[]{0.5f,0.5f,0.5f,1.0f};
	
	public ModelView3D() throws Exception {
//	    this.setSize(640,480);
//	    this.setPreferredSize(new Dimension(640,480));
//	    this.setMaximumSize(new Dimension(640,480));
	    this.setMinimumSize(new Dimension(320,240));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
        this.addComponentListener(this);
        camDist = 2.0f;
        camPhi = 0.1f;
        camTheta = 0.1f;
        panX = 0;
        panY = 0;
	}

	public Model getModel(){
	    return model;
	}
	
	public ModelState getState(){
	    return state;
	}
	
	public void setModel(Model model, ModelState state){
	    this.model = model;
	    this.state = state;
	}
	
	private void glInit() {
	    try {
	        this.makeCurrent();
	    } catch (Exception e){
            return;
//	        e.printStackTrace();
	    }
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(40,((float)this.getWidth())/((float)this.getHeight()),1,100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glShadeModel(GL11.GL_SMOOTH);
//		GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_LINE); 
//		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        
		GL11.glLight(GL11.GL_LIGHT0,GL11.GL_DIFFUSE, get(whiteLight));
		GL11.glLight(GL11.GL_LIGHT0,GL11.GL_SPECULAR, get(whiteLight));
		
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,get(ambientLight));
		
	 	GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		GLUtils.glLightPos(1.0f,1.0f,1.0f);

//	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
	 	
	}

	
	public static FloatBuffer get(float[] vals){
		scratch.rewind();
		scratch.put(vals);
		scratch.rewind();
		return scratch;
	}

	public static FloatBuffer get(float val){
		scratch.rewind();
		scratch.put(val);
		scratch.rewind();
		return scratch;
	}

	/**
	 * Cleanup
	 */
	private static void cleanup() {
		Display.destroy();
	}

	public void paintGL(){
	    if (!initialised){
		    glInit();
		    initialised = true;
	    }
	    
	    try {
		    this.makeCurrent();
		    draw();
	        this.swapBuffers();
	    } catch (Exception e){
	        e.printStackTrace();
	    }
	}

	public void draw(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glMatrixMode(GL11.GL_VIEWPORT);
		GL11.glViewport(0,0,getWidth(),getHeight());
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(40,((float)this.getWidth())/((float)this.getHeight()),1,100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		
        GL11.glTranslatef(panX,panY,0);

        //(float)(camDist*Math.sin(camTheta)*Math.cos(camPhi))
		GLU.gluLookAt((float)(camDist*Math.sin(camTheta)*Math.sin(camPhi)),
                        (float)(camDist*Math.cos(camTheta)*Math.sin(camPhi)),
                        (float)(camDist*Math.cos(camPhi)),0,0,0,0,0,1);
		if (model != null){
		    model.draw(state);
		}
	}

    
    public void componentResized(ComponentEvent ce){
        glInit();
        repaint();
    }
    
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    private int lastX;
    private int lastY;
    private int button;
    public void mouseDragged(MouseEvent arg0) {
        if (button == MouseEvent.BUTTON1){
            camTheta += (float)(arg0.getX() - lastX)/100.0f;
            camPhi -= (float)(arg0.getY() - lastY)/100.0f;
        } else if (button == MouseEvent.BUTTON2){
            panX += (float)(arg0.getX() - lastX)/100.0f;
            panY -= (float)(arg0.getY() - lastY)/100.0f;
        }
        lastX = arg0.getX();
        lastY = arg0.getY();
        this.repaint();
    }
    
    

    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    public void mousePressed(MouseEvent arg0) {
        button = arg0.getButton();
        lastX = arg0.getX();
        lastY = arg0.getY();
        
    }

    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		camDist += arg0.getWheelRotation()*0.1;
		repaint();
	}
	
	
	

}
