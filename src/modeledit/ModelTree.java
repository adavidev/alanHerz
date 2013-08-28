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

import java.awt.GridBagLayout;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Model;
import model.ModelSection;

public class ModelTree extends JTree {

    private final AnimationEditor parent;
    private DefaultTreeModel tree;
    
    public ModelTree(AnimationEditor parent){
    	tree = new DefaultTreeModel(null);
    	this.setModel(tree);
        this.parent = parent;
        this.setLayout( new GridBagLayout() );
    }
    
    public void setModel(Model model){
    	ModelSection rootSection = model.getRootSection();
    	DefaultMutableTreeNode rootNode = recurseSections(rootSection);
    	tree.setRoot(rootNode);
    }
    
    public DefaultMutableTreeNode recurseSections(ModelSection curSection){
    	DefaultMutableTreeNode node = new DefaultMutableTreeNode(curSection);
        for (ModelSection section : curSection.getChildren()){
        	node.add(recurseSections(section));
        }
        return node;
    }
    
    public ModelSection getSelectedSection(){
    	return (ModelSection)((DefaultMutableTreeNode)getSelectionPath().getLastPathComponent()).getUserObject();
    }
}
