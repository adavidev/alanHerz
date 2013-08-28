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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import unit.UnitFactory;
import unit.UnitStats;
import ai.UnitAIFactory;
import effects.ParticleModel;

/**
 * @author Shannon Smith
 */
public class ResourceLoader {

    private ModelLoader modelLoader;
    private MapLoader mapLoader;
    private SAXBuilder xmlLoader;
    
    public ResourceLoader(){
	    modelLoader = new ModelLoader();
	    mapLoader = new MapLoader();
		xmlLoader = new SAXBuilder();
    }
    
    public ResourceManager loadResources(File resourceManifest) throws IOException, JDOMException, ResourceNotFoundException {
        ResourceManager resManager = new ResourceManager();
		Element root = xmlLoader.build(resourceManifest).getRootElement();
		for(Element ele : (List<Element>)root.getChild("heightmap_images").getChildren()){
		    resManager.addHeightMapTexture(loadHeightMapImage(resManager,ele));
		}
		for(Element ele : (List<Element>)root.getChild("materials").getChildren()){
		    resManager.addMaterial(loadMaterial(ele,resManager));
		}
		for(Element ele : (List<Element>)root.getChild("models").getChildren()){
	        File src = new File("data/models/",ele.getAttributeValue("src"));
	        resManager.addModel(modelLoader.loadModel(xmlLoader.build(src).getRootElement(),resManager));
		}
		for(Element ele : (List<Element>)root.getChild("maps").getChildren()){
		    File src = new File("data/maps/",ele.getAttributeValue("src"));
		    resManager.addMap(mapLoader.loadMap(xmlLoader.build(src).getRootElement(),resManager));
		}
		for(Element ele : (List<Element>)root.getChild("songs").getChildren()){
            try {
                resManager.addSong(ele.getAttributeValue("name"),MidiSystem.getSequence(new File("data/music/",ele.getAttributeValue("src"))));
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		for(Element ele : (List<Element>)root.getChild("particle_models").getChildren()){
	    	ParticleModel particle = new ParticleModel(ele.getAttributeValue("name"),resManager.getMaterial(ele.getChildText("material")));
	    	particle.setLifeTime(Float.parseFloat(ele.getChildText("life_time")));
	    	particle.setDensity(Float.parseFloat(ele.getChildText("density")));
	    	particle.setGravity(Float.parseFloat(ele.getChildText("gravity")));
	    	particle.setForce(Float.parseFloat(ele.getChildText("force")));
	    	particle.setDispersion(Float.parseFloat(ele.getChildText("dispersion")));
	    	particle.setParticleSize(Float.parseFloat(ele.getChildText("size")));
	    	resManager.addParticleModel(particle);
		}
		UnitFactory unitFactory = new UnitFactory(resManager);
		for(Element ele : (List<Element>)root.getChild("units").getChildren()){
			unitFactory.addUnit(
					ele.getAttributeValue("name"),
					ele.getAttributeValue("class"),
					loadStats(ele));
		}
		resManager.setUnitFactory(unitFactory);
		UnitAIFactory aiFactory = new UnitAIFactory(resManager);
		for(Element ele : (List<Element>)root.getChild("orders").getChildren()){
			aiFactory.addUnitAI(
					ele.getAttributeValue("name"),
					ele.getAttributeValue("class"),
					Integer.parseInt(ele.getChildText("cost")));
		}
		resManager.setUnitAIFactory(aiFactory);
        return resManager;
    }
    
    private HeightMapTexture loadHeightMapImage(ResourceManager resManager, Element ele) throws IOException, ResourceNotFoundException{
        HeightMapTexture hmapTex = new HeightMapTexture(ele.getAttributeValue("name"));
        for(Element layer : (List<Element>)(ele.getChild("layers").getChildren())){
            String imgName = layer.getText();
            File imgFile = new File("data/images/" + imgName);
            if (!imgFile.exists()){
                throw new ResourceNotFoundException(imgFile.getAbsolutePath());
            }
            BufferedImage img = loadImage(imgFile);
            hmapTex.addTexture(img,Float.parseFloat(layer.getAttributeValue("height")));
        }
        return hmapTex;
    }
    
    private UnitStats loadStats(Element ele){
    	UnitStats stats = new UnitStats(
    			Integer.parseInt(ele.getChildText("cost")),
    			Integer.parseInt(ele.getChildText("build_time")),
    			Integer.parseInt(ele.getChildText("armour")),
    		    Integer.parseInt(ele.getChildText("energy")),
    		    Integer.parseInt(ele.getChildText("ammo")),
    		    Integer.parseInt(ele.getChildText("missiles")),
    			Integer.parseInt(ele.getChildText("speed")),
    		    Integer.parseInt(ele.getChildText("turn_speed")),
    		    Integer.parseInt(ele.getChildText("dammage")));
    	return stats;
    }
    
    private Material loadMaterial(Element ele, ResourceManager res){
        Material mat = new Material(ele.getAttributeValue("name"));
        for(Element child : (List<Element>)ele.getChildren()){
	    	if (child.getName().equals("diffuse")){
	            mat.setDiffuse(loadColour(child));
	    	} else if (child.getName().equals("specular")){
	            mat.setSpecular(loadColour(child));
        	} else if (child.getName().equals("shininess")){
	            mat.setShininess(Float.parseFloat(child.getText()));
	    	} else if (child.getName().equals("texture")){
	    		Texture tex = null;
	    		if (child.getChild("img") != null){
	                File texFile = new File("data/images/",child.getChildText("img"));
	                tex = new Texture(child.getChildText("img"));
	                tex.setImage(loadImage(texFile));
	    		} else {
	    			String hmap = child.getChildText("hmap_img");
	    			tex = new Texture(hmap);
	    			res.addTexture(tex);
	    		}
	    		res.addTexture(tex);
                if (child.getAttribute("unit") != null){
                	mat.setTexture(Integer.parseInt(child.getAttributeValue("unit")),tex);
                } else {
                	mat.setTexture(0,res.getTexture(tex.getName()));
                }
	    	}
        }
        return mat;
    }
    
    public BufferedImage loadImage(File src){
    	if (!src.exists()){
    		throw new ResourceNotFoundException(src.getName());
    	}
    	
    	try {
			BufferedImage img = ImageIO.read(src);
			BufferedImage newImg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = newImg.getGraphics();
			g.drawImage(img,0,0,null);
			g.dispose();
			return newImg;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    private float[] loadColour(Element ele){
        float[] col = new float[4];
        col[0] = Float.parseFloat(ele.getChildText("r"));
        col[1] = Float.parseFloat(ele.getChildText("g"));
        col[2] = Float.parseFloat(ele.getChildText("b"));
        col[3] = Float.parseFloat(ele.getChildText("a"));
        return col;
    }
    
}
