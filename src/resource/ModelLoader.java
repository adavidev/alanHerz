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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import model.Connector;
import model.KeyFrame;
import model.KeyFrameAnimation;
import model.Model;
import model.ModelJoint;
import model.ModelSection;
import model.ModelState;

import org.jdom.Element;

import util.Vector2f;
import util.Vector3f;


/**
 * @author Shannon Smith
 */
public class ModelLoader {

	private ObjMeshLoader meshLoader;
	
    public ModelLoader(){
    	meshLoader = new ObjMeshLoader();
    }
    

    public Model loadModel(Element ele, ResourceManager res) throws ResourceNotFoundException {
        Model model = new Model(ele.getAttributeValue("name"));
        model.setSkinName(ele.getAttributeValue("material"));
        Iterator i = ele.getChildren().iterator();
        while (i.hasNext()){
            Element child = (Element)i.next();
            if (child.getName().equals("section")){
                loadSections(child,model,null);
            } else if (child.getName().equals("animation")){
                KeyFrameAnimation ani = getAnimation(child);
                model.addAnimation(ani);
            }
        }
        return model;
    }
    
    public KeyFrameAnimation getAnimation(Element ele){
        KeyFrameAnimation ani = new KeyFrameAnimation(ele.getAttributeValue("name"),Float.parseFloat(ele.getAttributeValue("length")));
        Iterator keyFrames = ele.getChildren().iterator();
        while (keyFrames.hasNext()){
            Element child = (Element)keyFrames.next();
            ModelState state = new ModelState();
            KeyFrame frame = new KeyFrame(Float.parseFloat(child.getAttributeValue("time")),state);
            Iterator joints = child.getChildren().iterator();
            while (joints.hasNext()){
                Element joint = (Element)joints.next();
                state.addJoint(new ModelJoint(joint.getAttributeValue("section"),Float.parseFloat(joint.getText())));
            }
            ani.addKeyFrame(frame);
        }
        
        return ani;
    }
    
    public Element toXML(Model model){
        Element root = new Element("articulated_model");
        root.setAttribute("name", model.getName());
        root.addContent(toXML(model.getRootSection()));
        for (KeyFrameAnimation anim : model.getAnimations()){
            root.addContent(toXML(anim));
        }
        return root;
    }
    
    public Element toXML(KeyFrameAnimation ani){
        Element ele = new Element ("animation");
        ele.setAttribute("name", ani.getName());
        ele.setAttribute("length", Float.toString(ani.getLength()));
        Iterator i = ani.getKeyFrames();
        while (i.hasNext()){
            KeyFrame keyFrame = (KeyFrame)i.next();
            Element key = new Element("key_frame");
            ele.addContent(key);
            key.setAttribute("time", Float.toString(keyFrame.getTime()));
            Iterator joints = keyFrame.getState().getJoints();
            while (joints.hasNext()){
                ModelJoint joint = (ModelJoint)joints.next();
                key.addContent(new Element("joint").setAttribute("section",joint.getSection()).setText(Float.toString(joint.getAngle())));
            }
        }
        return ele;
        
    }
    
    public Element toXML(ModelSection section){
        final Element ele = new Element("section");
        ele.setAttribute("name",section.getName());

        if (section.getMesh() != null){
	        Element mesh = new Element("mesh");
	        mesh.setText(section.getMesh().getSource().getName());
	        ele.addContent(mesh);
        }
        
        Element con = new Element("connector");
        if (section.getParent() != null){
            con.setAttribute("parent",section.getParent().getName());
        }
        Element p = new Element("p");
        Element a = new Element("a");
        p.addContent(toXML(section.getConnector().getPosition()));
        a.addContent(toXML(section.getConnector().getAxis()));
        con.addContent(p);
        con.addContent(a);
        ele.addContent(con);
        
        for(ModelSection sec : section.getChildren()){
        	ele.addContent(toXML(sec));
        }
        
//        final Element mesh = new Element("mesh");
//        ele.getChildren().add(mesh);
//        
//        Element verts = new Element("points");
//        mesh.getChildren().add(verts);
//        for(int i = 0; i < section.getVertexCount();i++){
//            verts.getChildren().add(toXML(section.getVertex(i)));
//        }
//        
//        Element norms = new Element("normals");
//        mesh.getChildren().add(norms);
//        for(int i = 0; i < section.getNormalCount();i++){
//            norms.getChildren().add(toXML(section.getNormal(i)));
//        }
//        
//        Element texCoords = new Element("texture_coords");
//        mesh.getChildren().add(texCoords);
//        for(int i = 0; i < section.getTextureCoordCount();i++){
//            texCoords.getChildren().add(toXML(section.getTextureCoord(i)));
//        }
//        
//        Element polys = new Element("polygons");
//        mesh.getChildren().add(polys);
//        for(int i = 0; i < section.getTriangleCount();i++){
//            polys.getChildren().add(toXML(section.getTriangle(i),section));
//        }
        
        return ele;
    }
    
    public Element toXML(Vector3f v){
        Element vec = new Element("v");
        vec.getChildren().add(new Element("x").setText(Float.toString(v.x)));
        vec.getChildren().add(new Element("y").setText(Float.toString(v.y)));
        vec.getChildren().add(new Element("z").setText(Float.toString(v.z)));
        return vec;
    }
    
    public Element toXML(Vector2f v){
        Element vec = new Element("v");
        vec.getChildren().add(new Element("x").setText(Float.toString(v.x)));
        vec.getChildren().add(new Element("y").setText(Float.toString(v.y)));
        return vec;
    }
    
//    public Element toXML(Triangle t, ModelSection mod){
//        Element vertex,point,norm,tex;
//        Element tri = new Element("t");
//        for(int i = 0; i < 3;i++){
//            vertex = new Element("v");
//	        point = new Element("p").setText(Integer.toString(mod.indexOfPoint(t.pos[i])));
//	        norm = new Element("n").setText(Integer.toString(mod.indexOfNorm(t.norm[i])));
//	        tex = new Element("t").setText(Integer.toString(mod.indexOfTex(t.tex[i])));
//	        vertex.getChildren().add(point);
//	        vertex.getChildren().add(norm);
//	        vertex.getChildren().add(tex);
//	        tri.getChildren().add(vertex);
//        }
//        return tri;
//    }
    
    public void loadSections(Element ele, Model model, ModelSection parent) throws ResourceNotFoundException {
        ModelSection section = new ModelSection(ele.getAttributeValue("name"));
        model.addSection(section,parent);
        Iterator i = ele.getChildren().iterator();
        while (i.hasNext()){
            Element child = (Element)i.next();
            if (child.getName().equals("connector")){
                Connector c = new Connector(getVector3D(child.getChild("p").getChild("v")),getVector3D(child.getChild("a").getChild("v")));
                section.setConnector(c);
            } else if (child.getName().equals("mesh")){
//                if (child.getAttribute("material") != null){
//                    section.setMaterial(res.getMaterial(child.getAttributeValue("material")));
//                }
            	try {
					section.setMesh(meshLoader.loadMesh(new File("data/meshes/" + child.getText())));
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else if (child.getName().equals("section")){
            	loadSections(child,model,section);
            }
        }
        
    }
    
    private static Vector3f getVector3D(Element ele){
        
        Vector3f v = new Vector3f(
                Float.parseFloat(ele.getChildText("x")),
                Float.parseFloat(ele.getChildText("y")),
                Float.parseFloat(ele.getChildText("z")));
        return v;
    }
    
    private static Vector2f getVector2D(Element ele){
        Vector2f v = new Vector2f(
                Float.parseFloat(ele.getChildText("x")),
                Float.parseFloat(ele.getChildText("y")));
        return v;
    }
    
}
