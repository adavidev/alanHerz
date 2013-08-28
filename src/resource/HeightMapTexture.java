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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import map.GameMap;
import util.GameMath;

public class HeightMapTexture {

    private static final int RED_MASK = 0xFF<<16;
    private static final int GREEN_MASK = 0xFF<<8;
    private static final int BLUE_MASK = 0xFF;
    
    private String name;
    private List<TextureLayer> textures;
    private int texSize;
    
    public HeightMapTexture(String name){
        this.name = name;
        this.texSize = 0;
        textures = new ArrayList<TextureLayer>();
    }
    
    public String getName(){
        return name;
    }
    
    public void addTexture(BufferedImage tex, float height){
        int index = 0;
        texSize = tex.getWidth();
        for(int i = 0; i < textures.size();i++){
            if (textures.get(i).height < height){
                index = i+1;
            } else {
                break;
            }
        }
        textures.add(index,new TextureLayer(tex,height));
    }
    
    public BufferedImage genTexture(GameMap map){
        BufferedImage img = new BufferedImage(texSize,texSize,BufferedImage.TYPE_4BYTE_ABGR);
        float scale = (float)map.getSize()/(float)texSize;
        for(int i = 0; i < texSize;i++){
            for(int j = 0; j < texSize;j++){
                img.setRGB(i,j,getWeightedTextureValue(map.getHeightAt(i*scale,j*scale),i,j));
            }
        }
        
//        try {
//			ImageIO.write(img,"png",new File("test.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        return img;
    }
    
    public int getWeightedTextureValue(float height, int x, int y){
        TextureLayer bottom = textures.get(0);
        TextureLayer top = textures.get(textures.size()-1);
        for(int i = 0; i < textures.size(); i++){
            if (textures.get(i).height <= height){
                bottom = textures.get(i);
            }
        }
        for(int i = textures.size()-1; i >= 0; i--){
            if (textures.get(i).height > height){
                top = textures.get(i);
            }
        }
        height = GameMath.clamp(height,bottom.height,top.height);
        float dh = top.height - bottom.height;
        float bWeight,tWeight;
        if (top != bottom){
            tWeight = (height - bottom.height)/dh;
            bWeight = (top.height - height)/dh;
        } else {
            bWeight = 0;
            tWeight = 1.0f;
        }
        int red = (int)(bWeight*bottom.getRed(x,y) + tWeight*top.getRed(x,y));
        int green = (int)(bWeight*bottom.getGreen(x,y) + tWeight*top.getGreen(x,y));
        int blue = (int)(bWeight*bottom.getBlue(x,y) + tWeight*top.getBlue(x,y));
        return (red<<16 & RED_MASK) | (green<<8 & GREEN_MASK) | (blue & BLUE_MASK);
    }
    
    private class TextureLayer {
        
        public BufferedImage texture;
        public float height;
        
        public TextureLayer(BufferedImage texture, float height){
            this.texture = texture;
            this.height = height;
        }
        
        public int getRed(int x, int y){
            return (texture.getRGB(x,y) & RED_MASK)>>16;
        }

        public int getGreen(int x, int y){
            return (texture.getRGB(x,y) & GREEN_MASK)>>8;
        }

        public int getBlue(int x, int y){
            return  texture.getRGB(x,y) & BLUE_MASK;
        }
    }
}
