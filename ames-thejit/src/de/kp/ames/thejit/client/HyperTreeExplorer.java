package de.kp.ames.thejit.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import de.kp.ames.thejit.client.base.HyperTree;
import de.kp.ames.thejit.client.event.ClickLabelEvent;
import de.kp.ames.thejit.client.event.MouseOutLabelEvent;
import de.kp.ames.thejit.client.event.MouseOverLabelEvent;
import de.kp.ames.thejit.client.handler.ClickLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOutLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOverLabelHandler;

public class HyperTreeExplorer extends VLayout implements ClickLabelHandler, MouseOverLabelHandler, MouseOutLabelHandler {

	private static String HTREE_ID = "x-hypertree";
	private boolean initialized = false;
	
	private HyperTree tree;	
	private JavaScriptObject jsObject = null;
	
	public HyperTreeExplorer() {

		this.setShowEdges(false);
		/*
		 * Dimensions
		 */
//		this.setWidth100();
//		this.setHeight100();
		
		final HyperTreeExplorer self = this;
		
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

		tree = new HyperTree(HTREE_ID, getTreeConfig(HTREE_ID));
		
		// add handler
		tree.addClickLabelHandler(this);
		tree.addMouseOverLabelHandler(this);
		tree.addMouseOutLabelHandler(this);
		
		this.addMember(tree);
		
		/*
		 * this draw is unnecessary and will add the widget a second time to the window!
		 */
//		this.draw();

	}
	
	protected void afterResized(ResizedEvent event) {
		resizeTree();
	}

	protected void afterDraw(DrawEvent event) {
		if (this.initialized == false) this.loadDefault();
		this.initialized = true;
	}

	public void loadJTree(String tree) {

		jsObject = evaluate(tree);
		loadTree(HTREE_ID, jsObject);
		
		SC.logWarn("====> thejit.HT.loadJTree END");

	}
	
	public void resizeTree() {
		
		SC.logWarn("====> thejit.HT.resizeTree");

		double w = (double) this.getWidth();
		double h = (double) this.getHeight();
		
		tree.resize(HTREE_ID, w, h);
		
	}
	
	private native void loadTree(String name, JavaScriptObject json)  /*-{
 
       var widget = jitWrappedObject(name);
       if (widget == null) return;

	   // release previously created labels
	   var fx = widget.fx;
	   if (fx) {
	   	 for (var id in fx.labels) {
	   	   fx.disposeLabel(id); delete fx.labels[id];	
	   	 }
	   }
	   
       widget.loadJSON(json, 0);
       widget.refresh();

       widget.onClick(widget.root);

       widget.controller.onBeforeCompute(widget.graph.getNode(widget.root));
       widget.controller.onAfterCompute();

   	}-*/;

    private native JavaScriptObject evaluate(String stream) /*-{
		return eval('(' + stream + ')');
	}-*/;
	
    private native JavaScriptObject getTreeConfig(String name) /*-{
	    return {
	        Node:{'color': '#f00000','dim':8},
	        Edge:{'color': '#088088',lineWidth:2},
			
	        onBeforeCompute:function(node){},
	        
	        onAfterCompute: function(){},
	
	        onCreateLabel: function(domElement, node){
	            domElement.innerHTML = node.name;
	            
	            var forwardEvent = function(event) {
	            	jitWrapperForwardEvent(name, event || $wnd.event, domElement, node);
	            };
	            
	            domElement.onclick = function (event) {
	                jitWrappedObject(name).onClick(node.id, { hideLabels: false });
	                forwardEvent(event);
	            };
	            
	            // introduce context menu for radial nodes
	            domElement.oncontextmenu = forwardEvent;
	            
	        },
	
	        onPlaceLabel: function(domElement, node){
	            var style = domElement.style;
	            style.display = ''; style.cursor = 'pointer';
	            
	            if (node._depth <= 1) {
	            	style.fontSize = '10px'; style.color = '#ffffff'; // 0.8em
	
	            } else if (node._depth == 2) {
	            	style.fontSize = '9px'; style.color = '#f0f0f0';

	            } else if (node._depth == 3) {
	            	style.fontSize = '9px'; style.color = '#aaaaaa';
	
	            } else {
	            	style.display = 'none';
	            }	
	
	            var left = parseInt(style.left);
	            var w = domElement.offsetWidth;
	            style.left = (left - w / 2) + 'px';
	        }
	        
		}
	}-*/;

	/************************************************************************
	 * 
	 * VISUALIZATION     VISUALIZATION     VISUALIZATION     VISUALIZATION
	 * 
	 ***********************************************************************/
     
    private void showHypertree(JSONObject jHypertree) {
 
    	this.loadJTree(jHypertree.toString());
    	this.resizeTree();

    }

    public void loadDefault() {
    	
		SC.logWarn("====> thejit.HT.loadDefault");


    	//this.setTitle("Hypertree");
 
    	// initial loading of hypertree
		JSONObject jTree = new JSONObject();
		
		jTree.put("id",      new JSONString("de:kp:hypertree:initial"));
		jTree.put("name",    new JSONString("Hypertree"));
		jTree.put("children", new JSONArray());
		
		this.loadJTree(jTree.toString());
		this.resizeTree();
		
    }
    
    public void loadHypertree(String uid, String title) {
 
//    	this.setTitle("Hypertree: " + title);
//    	
//    	DataManager.getInstance().hypertree(uid, new CallbackImpl() {
//    		public void execute(JSONValue value) {    			
//    			showHypertree(value.isObject());
//    		}
//    	});
//   	
    }
	public void clearHypertree() {
		this.loadDefault();
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.thejit.client.handler.MouseOutLabelHandler#onMouseOutLabel(de.kp.ames.thejit.client.event.MouseOutLabelEvent)
	 */
	public void onMouseOutLabel(MouseOutLabelEvent event) {
		/*
		 * Must be overridden
		 */
	}

	public void onMouseOverLabel(MouseOverLabelEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickLabel(ClickLabelEvent event) {

//		JitWidget widget = (JitWidget) event.getSource();
//    	JSONObject jNode  = widget.getJSONData();
//   	
//    	String query = jNode.get("name").isString().stringValue();
//    	if (this.handler != null) this.handler.search(query);
		
	}

	public void onRightClickLabel(ClickLabelEvent event) {
		/*
		 * Must be overridden
		 */
		
	}
   
}
