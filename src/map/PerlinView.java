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
package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import util.Perlin;

/**
 * @author Shannon Smith
 */
public class PerlinView extends JFrame{

    private Perlin perlin;
    private BufferedImage img;
    
    public PerlinView(){
        perlin = new Perlin(1024,354,3,0.24f);
        img = new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 400;i++){
            for(int j = 0; j < 400;j++){
                img.setRGB(i,j,Color.HSBtoRGB(1.0f,1.0f,0.5f + 0.5f*perlin.fractalSum((float)i/100f,(float)j/100f)));
            }
        }
        
    }
    
    public static void main(String[] args){
        PerlinView pview = new PerlinView();
        pview.setSize(400,400);
        pview.setVisible(true);
        pview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public void paint(Graphics g){
        this.getContentPane().getGraphics().drawImage(img,0,0,null);
        
        
    }
    
}
