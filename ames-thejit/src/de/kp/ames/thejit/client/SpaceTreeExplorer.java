package de.kp.ames.thejit.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import de.kp.ames.thejit.client.base.SpaceTree;
import de.kp.ames.thejit.client.event.ClickLabelEvent;
import de.kp.ames.thejit.client.event.MouseOutLabelEvent;
import de.kp.ames.thejit.client.event.MouseOverLabelEvent;
import de.kp.ames.thejit.client.handler.ClickLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOutLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOverLabelHandler;

public class SpaceTreeExplorer extends VLayout implements ClickLabelHandler, MouseOverLabelHandler, MouseOutLabelHandler {

	private SpaceTree tree;
	private static String SPACETREE_ID = " x-spacetree";
	
	JavaScriptObject jsObject = null;

	public SpaceTreeExplorer() {

		this.setShowEdges(false);
		/*
		 * Dimensions
		 */
		this.setWidth100();
		this.setHeight100();
		
		final SpaceTreeExplorer self = this;
		
		this.addDrawHandler(new DrawHandler(){
			public void onDraw(DrawEvent event) {
				self.afterDraw(event);				
			}
			
		});

		this.addResizedHandler(new ResizedHandler() {
			public void onResized(ResizedEvent event) {
				self.afterResized(event);				
			}
			
		});

		tree = new SpaceTree(SPACETREE_ID, getTreeConfig(SPACETREE_ID));
		
		// add handler
		tree.addClickLabelHandler(this);
		tree.addMouseOverLabelHandler(this);
		tree.addMouseOutLabelHandler(this);
		
		this.addMember(tree);
		this.draw();
		
	}
	
	protected void afterResized(ResizedEvent event) {
		resizeTree();
	}

	protected void afterDraw(DrawEvent event) {
	}

    private native JavaScriptObject evaluate(String stream) /*-{
		return eval('(' + stream + ')');
	}-*/;

	public void loadTree(String tree) {

		jsObject = evaluate(tree);
		loadTree(SPACETREE_ID, jsObject);
				
	}

	private native void loadTree(String name, JavaScriptObject json)  /*-{
   	   var widget = jitWrappedObject(name);

	   // release previously created labels
	   var fx = widget.fx;
	   if (fx) {
	   	 for (var id in fx.labels) {
	   	   fx.disposeLabel(id); delete fx.labels[id];	
	   	 }
	   }
    	
       widget.loadJSON(json);
       widget.compute();
    	
       widget.onClick(widget.root);
	}-*/;
	
	public void resizeTree() {
		
		double w = (double) this.getWidth();
		double h = (double) this.getHeight();
		
		tree.resize(SPACETREE_ID, w, h);		
	}

	private native JavaScriptObject getTreeConfig(String name) /*-{
		return {
	    	orientation: 'top',
	    	duration: 333,
	    	fps: 25,
	    	levelDistance:48,
	    	levelsToShow:5,
	    	Node: {
		        width: 120,
		        height: 60,
		        type: 'rectangle',
		        color: '#d2e2f4',
		        overridable: true
		    },
	    	Edge: {
	        	type: 'arrow',
	        	overridable: true
	        },
	    	onCreateLabel: function(label, node) {
	        	label.id = node.id;
		        
		        if (0 < node.data.childcount && !node.selected) {
		            label.innerHTML = node.name + "  " + node.data.childcount;
		            node.data.$color = "#ffffcc";
		        } else {
		            label.innerHTML = node.name;
		            delete node.data.$color;
		        }
		        
	        	var forwardEvent = function(event) {
	            	jitWrapperForwardEvent(name, event || $wnd.event, label, node); 
	            };
	            
	        	label.onclick = function(event) {
	            	jitWrappedObject(name).onClick(node.id);
	            	forwardEvent(event); 
	            };
	        
	        	label.oncontextmenu = forwardEvent;
	        	label.onmouseover = forwardEvent;
	        	label.onmouseout = forwardEvent;
	        
	        	var style = label.style;
	        	style.display = 'block';
	        	style.width = '120px';
	        	style.height ='60px';
	        	style.cursor = 'pointer';
	        	style.color = '#566d99';
	        	style.fontSize = '11px';
	        	style.textAlign= 'left';
	        	style.paddingTop = '4px';
	        	style.paddingLeft = '4px';
	    	},

	    	onBeforePlotNode: function(node){
	        	if (node.selected) {
	            	node.data.$color = "#ffffcc";
	        	} else {
	            	delete node.data.$color;
	        	}
	    	},
	    	onBeforePlotLine: function(adj){
	        	if (adj.nodeFrom.selected && adj.nodeTo.selected) {
	            	adj.data.$color = "#566d99";
	            	adj.data.$lineWidth = 1.2;
	        	} else {
	            	delete adj.data.$color;
	            	delete adj.data.$lineWidth;
	        	}
	    	}
	    }
	}-*/;
	
	public void onMouseOverLabel(MouseOverLabelEvent event) {
		/*
		 * Must be overridden
		 */
    }

    public void onMouseOutLabel(MouseOutLabelEvent event) {
		/*
		 * Must be overridden
		 */
    }

    public void onClickLabel(ClickLabelEvent event) {
		/*
		 * Must be overridden
		 */
    }

    public void onRightClickLabel(ClickLabelEvent event) {
		/*
		 * Must be overridden
		 */
    }
	
}
