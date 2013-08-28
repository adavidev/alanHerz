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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import model.Mesh;
import model.Triangle;
import util.Vector2f;
import util.Vector3f;

public class ObjMeshLoader {

	public ObjMeshLoader(){
		
	}

    public Mesh loadMesh(File obj) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(obj));
        String line;
        Mesh mesh = new Mesh(obj);
        while((line = reader.readLine()) != null){
            line = line.trim();
            if (line.length() == 0 || line.startsWith("#")){
                // comment
                continue;
            }
            StringTokenizer tokens = new StringTokenizer(line," ");
            String command = tokens.nextToken();
            if (command.equalsIgnoreCase("v")){
                mesh.addPoint(loadVector3D(tokens));
            } else if (command.equalsIgnoreCase("f")){
                mesh.addTriangle(loadFace(tokens,mesh));
            } else if (command.equalsIgnoreCase("vn")){
                mesh.addNormal(loadVector3D(tokens));
            } else if (command.equalsIgnoreCase("vt")){
                mesh.addTextureCoord(loadVector2D(tokens));
            }
        }
        
        return mesh;
    }

    public static Triangle loadFace(StringTokenizer tokens, Mesh mesh){
        Triangle t = new Triangle();
        for(int i = 0; i < 3;i++){
            String vertexString = tokens.nextToken();
	        String[] vertexFields = vertexString.split("\\/");
	        t.pos[i] = mesh.getVertex(Integer.parseInt(vertexFields[0])-1);
	        if (vertexFields[1].length() > 0){
	        	t.tex[i] = mesh.getTextureCoord(Integer.parseInt(vertexFields[1])-1);
	        }
	        t.norm[i] = mesh.getNormal(Integer.parseInt(vertexFields[2])-1);
	        
        }
        if (tokens.hasMoreTokens()){
            throw new IllegalStateException();
            
        }

        return t;
    }
    
    public static Vector3f loadVector3D(StringTokenizer tokens){
        float x = Float.parseFloat(tokens.nextToken());
        float y = Float.parseFloat(tokens.nextToken());
        float z = Float.parseFloat(tokens.nextToken());
        return new Vector3f(x,y,z);
    }
    
    public static Vector2f loadVector2D(StringTokenizer tokens){
        float x = Float.parseFloat(tokens.nextToken());
        float y = 1.0f - Float.parseFloat(tokens.nextToken());
        return new Vector2f(x,y);
    }
	
	
	
}
