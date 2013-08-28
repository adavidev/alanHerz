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
package resource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @author Shannon Smith
 */
public class Material {

	public static final int MAX_TEXTURE_UNITS = 4;
	private static FloatBuffer scratch = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asFloatBuffer();
	public static final Material DEFAULT_WHITE = new Material("WHITE");
	public static final Material DEFAULT_BLUE = new Material("BLUE");
	public static final Material DEFAULT_RED = new Material("RED");
	public static final Material DEFAULT_GREEN = new Material("GREEN");
	public static final float[] DEFAULT_DIFFUSE = new float[]{1.0f,1.0f,1.0f,1.0f}; 
	public static final float[] DEFAULT_SPECULAR = new float[]{1.0f,1.0f,1.0f,1.0f}; 
	public static final float DEFAULT_SHININESS = 10.0f; 

	static {
	    DEFAULT_WHITE.setDiffuse(new float[]{1.0f,1.0f,1.0f,1.0f});
	    DEFAULT_WHITE.setSpecular(new float[]{1.0f,1.0f,1.0f,1.0f});
	    DEFAULT_WHITE.setShininess(10.0f);
	    DEFAULT_BLUE.setDiffuse(new float[]{0.0f,0.0f,1.0f,1.0f});
	    DEFAULT_BLUE.setSpecular(new float[]{0.0f,0.0f,1.0f,1.0f});
	    DEFAULT_BLUE.setShininess(10.0f);
	    DEFAULT_RED.setDiffuse(new float[]{1.0f,0.0f,0.0f,1.0f});
	    DEFAULT_RED.setSpecular(new float[]{1.0f,0.0f,0.0f,1.0f});
	    DEFAULT_RED.setShininess(10.0f);
	    DEFAULT_GREEN.setDiffuse(new float[]{0.0f,1.0f,0.0f,1.0f});
	    DEFAULT_GREEN.setSpecular(new float[]{0.0f,1.0f,0.0f,1.0f});
	    DEFAULT_GREEN.setShininess(10.0f);
	}
	
	private float[] specular;
	private float[] diffuse;
	private float shininess;
	private final String name;
	private final Texture[] textures;
    
    public Material(String name){
        this.name = name;
        textures = new Texture[MAX_TEXTURE_UNITS];
        diffuse = DEFAULT_DIFFUSE;
        specular = DEFAULT_SPECULAR;
        shininess = DEFAULT_SHININESS;
    }
    
    public void setTexture(int unit, Texture tex){
    	textures[unit] = tex;
    }
    
    public String getName(){
        return name;
    }
    
    public void setSpecular(float[] specular){
        this.specular = specular;
    }
    
    public void setDiffuse(float[] diffuse){
        this.diffuse = diffuse;
    }
    
    public void setShininess(float shininess){
        this.shininess = shininess;
    }
    
    
    public void bind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
    	if(textures[0] == null){
    		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	} else {
    		GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,textures[0].getID());
//            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
         }
    	GL13.glActiveTexture(GL13.GL_TEXTURE1);
    	if(textures[1] == null){
    		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	} else {
    		GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,textures[1].getID());
//            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    	}
        
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
    	// And set the materails
	    GL11.glMaterial(GL11.GL_FRONT,GL11.GL_SPECULAR,get(specular));
	    GL11.glMaterial(GL11.GL_FRONT,GL11.GL_DIFFUSE, get(diffuse));
	    GL11.glMaterial(GL11.GL_FRONT,GL11.GL_SHININESS,get(shininess));
    }
    
    public Texture getTexture(int unit){
    	return textures[unit];
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
	
}
