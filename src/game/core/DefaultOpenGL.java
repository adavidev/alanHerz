package game.core;

import game.core.resource.AbstractResource;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import util.GLUtils;

public class DefaultOpenGL extends AbstractResource {

	public DefaultOpenGL() {
		super();
	}

	public void init() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glShadeModel(GL11.GL_SMOOTH);
//		GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_LINE); 
  		GLUtils.glLightColour(GL11.GL_SPECULAR, 0.1f,0.1f,0.1f, 1.0f);
  		GLUtils.glLightColour(GL11.GL_DIFFUSE, 1.0f,1.0f,1.0f, 1.0f);
  		GLUtils.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 0.3f,0.3f,0.3f, 1.0f);
		
	 	GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);

        // Set up texture unit 0
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        // Set up texture unit 1
	 	GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
	}

	public void update() {
	}

	public void destroy() {
	}

}
