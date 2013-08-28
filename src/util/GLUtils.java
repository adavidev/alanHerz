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
package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class GLUtils {

    private static FloatBuffer floatBuffer = ByteBuffer.allocateDirect(4*16).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private static IntBuffer intBuffer = ByteBuffer.allocateDirect(4*16).order(ByteOrder.nativeOrder()).asIntBuffer();
    
    public static Matrix4f getModelViewMatrix(){
        floatBuffer.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX,floatBuffer);
//        floatBuffer.flip();
        Matrix4f m = new Matrix4f();
        m.load(floatBuffer);
        return m;
    }
    

    public static int[] getViewport(){
        intBuffer.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT,intBuffer);
        int[] v = new int[4];
        for(int i = 0; i < 4; i++){
            v[i] = intBuffer.get();
        }
        return v;
    }
    
    public static void glLightColour(int type, float r, float g, float b, float a){
    	floatBuffer.clear();
    	floatBuffer.put(r);
    	floatBuffer.put(g);
    	floatBuffer.put(b);
    	floatBuffer.put(a);
    	floatBuffer.flip();
    	GL11.glLight(GL11.GL_LIGHT0, type, floatBuffer);
    }
    
    public static void glLightModel(int model, float r, float g, float b, float a){
    	floatBuffer.clear();
    	floatBuffer.put(r);
    	floatBuffer.put(g);
    	floatBuffer.put(b);
    	floatBuffer.put(a);
    	floatBuffer.flip();
    	GL11.glLightModel(model, floatBuffer);
    }
    
    public static void glLightPos(float x, float y, float z){
    	floatBuffer.clear();
    	floatBuffer.put(x);
    	floatBuffer.put(y);
    	floatBuffer.put(z);
    	floatBuffer.put(0);
    	floatBuffer.flip();
    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, floatBuffer);
    }
    
    public static int glGenTexture(){
    	intBuffer.clear();
    	GL11.glGenTextures(intBuffer);
    	intBuffer.rewind();
    	return intBuffer.get();
    }
}
