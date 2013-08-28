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
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Sequence;

import map.GameMap;
import model.Model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import unit.UnitFactory;
import ai.UnitAIFactory;
import effects.ParticleModel;

/**
 * @author Shannon Smith
 */
public class ResourceManager {

    /**
     * This stores the heightmapped texures,
     * each of these has a corresponding material as
     * well as a corresponding image. The image is generated
     * and bound to the material when bindTextures is called.
     */
    private final Map<String,HeightMapTexture> heightMapTextures;
    /** Materials */
    private final Map<String,Material> materials;
    /** Models. */
    private final Map<String,Model> models;
    /** Maps. */
    private final Map<String,map.GameMap> maps;
    /** Textures. */
    private final Map<String,Texture> textures;
    /** Music. */
    private final Map<String,Sequence> songs;
    
    private final Map<String,ParticleModel> particleModels;
    
    private UnitFactory unitFactory;
    private UnitAIFactory unitAIFactory;
    
    public ResourceManager(){
    	heightMapTextures = new HashMap<String,HeightMapTexture>();
        models = new HashMap<String,Model>();
        materials = new HashMap<String,Material>();
        maps = new HashMap<String,map.GameMap>();
        songs = new HashMap<String,Sequence>();
        particleModels = new HashMap<String,ParticleModel>();
        textures = new HashMap<String,Texture>();
    }
    
    public void setUnitFactory(UnitFactory unitFactory){
    	this.unitFactory = unitFactory;
    }
    
    public void setUnitAIFactory(UnitAIFactory unitAIFactory){
    	this.unitAIFactory = unitAIFactory; 
    	
    }

    public UnitFactory getUnitFactory(){
    	return unitFactory;
    }
    
    public UnitAIFactory getUnitAIFactory(){
    	return unitAIFactory;
    }
    
    public void addHeightMapTexture(HeightMapTexture hmapTex){
    	heightMapTextures.put(hmapTex.getName(), hmapTex);
    }
    
    public void addParticleModel(ParticleModel model){
    	particleModels.put(model.getName(),model);
    }
    
    public void addMap(map.GameMap map){
        System.out.println("Adding map: " + map.getName());
        maps.put(map.getName(),map);
    }
    
    public void addModel(Model model){
        models.put(model.getName(),model);
    }
    
    public Model getModel(String name) {
        if (!models.containsKey(name)){
            System.out.println("No such model: " + name);
        }
        return models.get(name);
    }

    public ParticleModel getParticleModel(String name){
    	return particleModels.get(name);
    }
    
    public map.GameMap getMap(String name) throws ResourceNotFoundException {
        if (!maps.containsKey(name)){
            throw new ResourceNotFoundException(name);
        }
        return maps.get(name);
    }
    
    public Texture getTexture(String name) throws ResourceNotFoundException {
        if(!textures.containsKey(name)){
            throw new ResourceNotFoundException(name);
        }
        return textures.get(name);
    }
    
    public void addTexture(Texture tex){
        textures.put(tex.getName(), tex);
    }
    
    public Material getMaterial(String name) {
//        if (!materials.containsKey(name)){
//            throw new ResourceNotFoundException(name);
//        }
        return materials.get(name);
    }

    public Collection<Material> getMaterials(){
        return  materials.values();
    }
    
    public void addMaterial(Material mat){
        materials.put(mat.getName(),mat);
    }
    
    public void addSong(String name, Sequence sequence){
        songs.put(name,sequence);
    }
    
    public Sequence getSong(String name){
        return songs.get(name);
    }
    
    public Collection<String> getAllSongNames(){
        return songs.keySet();
    }
    
    public int getSongCount(){
        return songs.size();
    }
    
    /**
     * Bind all textures to GL.
     * 
     * @param map Needed for binding height mapped textures.
     */
    public void bindTextures() {
        for(GameMap map : maps.values()){
        	HeightMapTexture hmap = heightMapTextures.get(map.getGroundMaterial().getTexture(0).getName());
        	map.getGroundMaterial().getTexture(0).setImage(hmap.genTexture(map));
        }
        for(Texture tex : textures.values()){
        	if (tex.getID() == -1){
        		bindTexture(tex);
        	}
        }
        for(GameMap map : maps.values()){
        	// force a minimap render.
        	map.getMinimap();
        }
    }
    
//	private int loadTexture(String name) throws ResourceNotFoundException {
//	    BufferedImage tex = images.get(name);
//	    
//        // Flip Image
//        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
//        tx.translate(0, -tex.getHeight(null));
//        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//        tex = op.filter(tex, null);
//        return bindTexture(tex);
//	}
    
	public void bindTexture(Texture tex){

		// Put Image In Memory
        ByteBuffer scratch = ByteBuffer.allocateDirect(4 * tex.getSize() * tex.getSize());

        byte data[] = (byte[]) tex.getImage().getRaster().getDataElements(0, 0, tex.getSize(), tex.getSize(), null);
        scratch.clear();
        scratch.put(data);
        scratch.rewind();

        IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL11.glGenTextures(buf); // Create a texure ID
        tex.setID(buf.get(0));
        
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getID());
  
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        // Generate The Texture
        GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D,GL11.GL_RGBA, tex.getSize(),tex.getSize(),
        					  GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, scratch);
	}
    
}
