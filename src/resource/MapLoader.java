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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import map.GameMap;
import map.Tile;
import map.Tile.TileType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Shannon Smith
 */
public class MapLoader {

    public GameMap loadMap(Element ele, ResourceManager res) throws ResourceNotFoundException {
        GameMap map = new GameMap(ele.getAttributeValue("name"),Integer.parseInt(ele.getAttributeValue("size")));
        map.setGroundMaterial(res.getMaterial(ele.getAttributeValue("material")));
        map.setWaterMaterial(res.getMaterial("water"));
        loadLayout(map,ele.getChild("layout"));
        loadPlayers(map, ele.getChild("players"));
        loadBases(map,ele.getChild("bases"),res);
        map.generateMesh();
        return map;
    }
    
    private void loadLayout(GameMap map, Element layout){
    	String srcFile = "data/maps/" + layout.getAttributeValue("src");
    	Map<Integer, TileType> colourMap = new HashMap<Integer,TileType>();
    	colourMap.put(0x996600, TileType.HIGH_LAND);
    	colourMap.put(0x330000, TileType.LOW_LAND);
    	colourMap.put(0x339966, TileType.WATER);
    	try {
			BufferedImage src = ImageIO.read(new File(srcFile));
			for(int i = 0; i < src.getWidth(); i++){
				for(int j = 0; j < src.getHeight(); j++){
					TileType type = colourMap.get(src.getRGB(i,j) & 0xFFFFFF);
					if (type == null){
						throw new IllegalStateException("No such colour: " + Integer.toHexString(src.getRGB(i,j)));
					}
					map.getTile(i,j).setType(type);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//        String strLayout = layout.getText();
//        String[] rows = strLayout.split("\\s");
//        for(int i = 0; i < map.getSize();i++){
//            if (rows[i+1].length() == map.getSize()*2){
//                for(int j = 0; j < map.getSize(); j++){
//                    int typeIndex = Integer.parseInt(rows[i+1].substring(j*2,j*2+2)); 
//                    if (typeIndex == TileType.LOW_LAND.index){
//                        map.getTile(i,j).setType(TileType.LOW_LAND);
//                    } else if (typeIndex == TileType.HIGH_LAND.index){
//                        map.getTile(i,j).setType(TileType.HIGH_LAND);
//                    } else if (typeIndex == TileType.WATER.index){
//                        map.getTile(i,j).setType(TileType.WATER);
//                    }
//                }
//            } else {
//                System.out.println("Layout error on line: " + i);
//            }
//        }
//        
    }
    
    public void saveMap(GameMap map, File target) throws IOException {
        Document doc = new Document();
        Element root = new Element("map");
        root.setAttribute("name", map.getName());
        root.setAttribute("size", Integer.toString(map.getSize()));
        for(int i = 0; i < map.getSize(); i++){
            for(int j = 0; j < map.getSize(); j++){
                root.getChildren().add(toXML(map.getTile(i,j)));
            }
        }
        doc.setRootElement(root);
	    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
	    output.output(doc,new FileWriter(target));

    }
    
    private Element toXML(Tile tile){
        Element ele = new Element("tile");
        ele.setAttribute("x", Integer.toString(tile.getX()));
        ele.setAttribute("y", Integer.toString(tile.getY()));
//        ele.getChildren().add(new Element("material").setText(tile.getMaterial().getName()));
        Element height = new Element("height");
        ele.getChildren().add(height);
        return ele;
    }
    
//    private void loadTile(Element ele, Tile t, ResourceManager res) throws ResourceNotFoundException {
////        t.setMaterial(res.getMaterial(ele.getChildText("material")));
//        Element height = ele.getChild("height");
//        Element features = ele.getChild("features");
//        if (features != null){
//            Iterator i = features.getChildren().iterator();
//            while (i.hasNext()){
//                i.next();
//                t.addFeature(new WaterFeature(res));
//                t.setType(TileType.WATER);
//            }
//        }
//        
//    }

    private void loadBases(GameMap map, Element ele, ResourceManager res) throws ResourceNotFoundException {
//        Iterator i = ele.getChildren().iterator();
//        while (i.hasNext()){
//            Element child = (Element)i.next();
//            BaseType type = child.getName().equals("main_base")?BaseType.HOME_BASE:BaseType.MINI_BASE;
//            Base base = new Base(map.getTile(Integer.parseInt(child.getAttributeValue("x")),Integer.parseInt(child.getAttributeValue("y"))),type,res);
//            map.addBaseLocation(String name, base);
//        }
    }
    
    private void loadPlayers(GameMap map, Element ele) {
    	for(Element player : (List<Element>)ele.getChildren()){
    		String id = player.getAttributeValue("id");
    		map.addPlayerTemplate(id);
    		int x = Integer.parseInt(player.getChild("home_base").getAttributeValue("x"));
    		int y = Integer.parseInt(player.getChild("home_base").getAttributeValue("y"));
    		map.setHomeBaseLocation(id, x, y);
    	}
    }
    
    
}
