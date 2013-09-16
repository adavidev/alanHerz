package game.hz.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import map.Tile.TileType;

public class TileMapLoader {

	public TileMapLoader() {
		// TODO Auto-generated constructor stub
	}

	public static void loadFromXML()
	{
		
//		String srcFile = "data/maps/" + layout.getAttributeValue("src");
//    	Map<Integer, TileType> colourMap = new HashMap<Integer,TileType>();
//    	colourMap.put(0x996600, TileType.HIGH_LAND);
//    	colourMap.put(0x330000, TileType.LOW_LAND);
//    	colourMap.put(0x339966, TileType.WATER);
//    	try {
//			BufferedImage src = ImageIO.read(new File(srcFile));
//			for(int i = 0; i < src.getWidth(); i++){
//				for(int j = 0; j < src.getHeight(); j++){
//					TileType type = colourMap.get(src.getRGB(i,j) & 0xFFFFFF);
//					if (type == null){
//						throw new IllegalStateException("No such colour: " + Integer.toHexString(src.getRGB(i,j)));
//					}
//					map.getTile(i,j).setType(type);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
