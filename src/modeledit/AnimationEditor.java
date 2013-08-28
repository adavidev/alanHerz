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
package modeledit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.KeyFrame;
import model.KeyFrameAnimation;
import model.Model;
import model.ModelJoint;
import model.ModelSection;
import model.ModelState;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import resource.ModelLoader;
import resource.ResourceLoader;
import resource.ResourceNotFoundException;

/**
 * @author Shannon Smith
 */
public class AnimationEditor extends JFrame implements ActionListener, TreeSelectionListener, ChangeListener {

    public static final float KEY_FRAME_SNAP = 0.05f;

    private XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
    private ModelLoader loader = new ModelLoader();
    private SAXBuilder xmlLoader = new SAXBuilder();

    private float aniPos;
    private KeyFrameAnimation curAni;
    private ModifySectionDialog sectionModDialog;
    
    // The model to display
    private File src;
    private Model model;
    private ModelState modelState;
    
    private ModelView3D modelView = new ModelView3D();
    private JPanel controlView = new JPanel(new BorderLayout());
    private ModelTree modelTree = new ModelTree(this);
    private JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,controlView,modelView);
    private JComboBox animationSelect = new JComboBox();
    private AnimationControl aniControl= new AnimationControl(this);
    private ModelSection curSection;
    
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem open = new JMenuItem("Open");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem importMesh = new JMenuItem("Import Mesh");
    private JMenu editMenu = new JMenu("Edit");
    private JMenuItem editJoint = new JMenuItem("Edit Joint");
    private JFileChooser chooser = new JFileChooser(new File("data"));
    private JSlider jointControl = new JSlider(-180,180);
    
    private boolean ignoreJointControl;
    
    public AnimationEditor() throws Exception {
        
    	jointControl.setMajorTickSpacing(45);
    	jointControl.setMinorTickSpacing(5);
    	jointControl.setPaintTicks(true);
    	jointControl.setPaintLabels(true);
    	jointControl.setSnapToTicks(true);
    	
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(importMesh);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        editMenu.add(editJoint);
        this.setJMenuBar(menuBar);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(aniControl,BorderLayout.SOUTH);
        this.getContentPane().add(split,BorderLayout.CENTER);
        
        controlView.add(animationSelect,BorderLayout.NORTH);
        controlView.add(modelTree, BorderLayout.CENTER);
        controlView.add(jointControl,BorderLayout.SOUTH);
        animationSelect.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        importMesh.addActionListener(this);
        modelTree.addTreeSelectionListener(this);
        jointControl.addChangeListener(this);
        editJoint.addActionListener(this);
        
        ignoreJointControl = false;
    }
    
    public void loadModel(File src) throws JDOMException, IOException {
        aniPos = 0;
        this.src = src;
        try {
            model = loader.loadModel(xmlLoader.build(src).getRootElement(),null);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (KeyFrameAnimation anim : model.getAnimations()){
            animationSelect.addItem(anim.getName());
        }
        modelState = new ModelState(model);

        modelTree.setModel(model);
        if (curAni != null){
        	aniControl.setAnimation(curAni);
        }
        modelView.setModel(model,modelState);
        
        split.setDividerLocation(0.5);
        
        sectionModDialog = new ModifySectionDialog(model,this);
        this.repaint();
        
    }
    
    public void saveModel(File src) throws IOException {
        this.src = src;
        Document doc = new Document(loader.toXML(model));
        output.output(doc,new FileOutputStream(src));
    }

    public void setAniPos(float pos){
        this.aniPos = pos;
        if (curAni != null && modelState != null){
            curAni.setState(aniPos,modelState);
            if (curSection != null){
            	ignoreJointControl = true;
            	jointControl.setValue((int)(modelState.getAngle(curSection.getName())*180/Math.PI));
            	ignoreJointControl = false;
            }
        }
        modelView.repaint();
    }
    
    public void modifySection(ModelSection section){
        sectionModDialog.setSection(section);
        sectionModDialog.setVisible(true);
    }
    
    public void sectionChanged(ModelSection section){
        section.getConnector().set(
                sectionModDialog.getConnectPosX(),
                sectionModDialog.getConnectPosY(),
                sectionModDialog.getConnectPosZ(),
                sectionModDialog.getConnectAxisX(),
                sectionModDialog.getConnectAxisY(),
                sectionModDialog.getConnectAxisZ()
        );
        modelView.repaint();
    }
    
    public void setJointAngle(ModelSection section, float angle){
        KeyFrame frame = curAni.getKeyFrame(aniPos, KEY_FRAME_SNAP);
        if (frame == null){
            System.out.println("Adding key Frame!");
            frame = new KeyFrame(aniPos, new ModelState());
            curAni.addKeyFrame(frame);
        }
        if (!frame.getState().hasJoint(section.getName())){
            frame.getState().addJoint(new ModelJoint(section.getName(),angle));
        } else { 
            frame.getState().setAngle(section.getName(),angle);
        }
        curAni.setState(aniPos,modelState);
        modelView.repaint();
        
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == open){
            chooser.showOpenDialog(this);
            if (chooser.getSelectedFile() != null){
                try {
                    loadModel(chooser.getSelectedFile());
                } catch (IOException ioe){
                    System.out.println("Error reading file!");
                } catch (JDOMException je){
                    System.out.println("Error in file!");
                    je.printStackTrace();
                }
            }
        } else if (ae.getSource() == save){
            chooser.showSaveDialog(this);
            if (chooser.getSelectedFile() != null){
                try {
                    saveModel(chooser.getSelectedFile());
                } catch (IOException ioe){
                    System.out.println("Error writing file!");
                }
            }
        } else if (ae.getSource() == animationSelect && model != null){
            this.curAni = model.getAnimation((String)animationSelect.getSelectedItem());
            aniControl.setAnimation(curAni);
            aniPos = 0;
            this.repaint();
        } else if (ae.getSource() == editJoint && curSection != null){
        	sectionModDialog.setSection(curSection);
        	sectionModDialog.setVisible(true);
        }
    }

    
    public static void main(String[] args) throws Exception {
        ResourceLoader loader = new ResourceLoader();
        //ResourceManager res = loader.loadResources(new File("data/resources.xml"));
        AnimationEditor view = new AnimationEditor();
//      res.bindTextures();
        view.setSize(800,600);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setVisible(true);
        if (args.length == 1){
            view.loadModel(new File(args[0]));
        }
    }

	public void valueChanged(TreeSelectionEvent arg0) {
		curSection = modelTree.getSelectedSection();
	}

	public void stateChanged(ChangeEvent arg0) {
		if (curSection != null && !ignoreJointControl){
			setJointAngle(curSection, (float)((jointControl.getValue()*Math.PI)/180.0));
		}
	}
    
    
}
