package de.kp.ames.thejit.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import de.kp.ames.thejit.client.base.RGraph;
import de.kp.ames.thejit.client.event.ClickLabelEvent;
import de.kp.ames.thejit.client.event.MouseOutLabelEvent;
import de.kp.ames.thejit.client.event.MouseOverLabelEvent;
import de.kp.ames.thejit.client.handler.ClickLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOutLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOverLabelHandler;

public class RadialExplorer extends VLayout implements ClickLabelHandler, MouseOverLabelHandler, MouseOutLabelHandler {

	private RGraph graph;

	private static String RGRAPH_ID = "x-rgraph";
	private boolean initialized = false;
	
	private JavaScriptObject jsObject = null;		

	public RadialExplorer() {
		

		this.setShowEdges(false);
		/*
		 * Dimensions
		 */
		this.setWidth100();
		this.setHeight100();
		
		final RadialExplorer self = this;
		
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

		graph = new RGraph(RGRAPH_ID, getGraphConfig(RGRAPH_ID));
		
		// add handler
		graph.addClickLabelHandler(this);
		graph.addMouseOverLabelHandler(this);
		graph.addMouseOutLabelHandler(this);
		
		this.addMember(graph);
		this.draw();
		
	}
	
	protected void afterResized(ResizedEvent event) {
		resizeRadial();
	}

	protected void afterDraw(DrawEvent event) {
		if (this.initialized == false) this.loadDefault();
		this.initialized = true;		
	}

	public void loadJRadial(String graph) {

		jsObject = evaluate(graph);
		loadRadial(RGRAPH_ID, jsObject);

	}
	
	public void resizeRadial() {
		
		double w = (double) this.getWidth();
		double h = (double) this.getHeight();
		
		graph.resize(RGRAPH_ID, w, h);
		
	}
	
	private native void loadRadial(String name, JavaScriptObject json)  /*-{
 
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
	
    private native JavaScriptObject getGraphConfig(String name) /*-{
	    return {
	        Node:{'color': '#ccddee'},
	        Edge:{'color': '#088088',lineWidth:2},
	
			interpolation:'linear',
	
			levelDistance:100,
			
	        onBeforeCompute:function(node){
	        },
	        
	        onAfterCompute: function(){
	        },
	
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
	            	style.fontSize = '11px'; style.color = '#ffffff'; // 0.8em
	
	            } else if (node._depth == 2) {
	            	style.fontSize = '10px'; style.color = '#fcfcfc';
	
	            } else {
	            	style.display = 'none';
	            }	
	
	            var left = parseInt(style.left);
	            var w = domElement.offsetWidth;
	            style.left = (left - w / 2) + 'px';
	        }
	        
		}
	}-*/;
	
	public void onMouseOverLabel(MouseOverLabelEvent event) {
	}

    public void onMouseOutLabel(MouseOutLabelEvent event) {
    }

    /* (non-Javadoc)
     * @see de.kp.ames.thejit.client.handler.ClickLabelHandler#onClickLabel(de.kp.ames.thejit.client.event.ClickLabelEvent)
     */
    public void onClickLabel(ClickLabelEvent event) {

//    	JitWidget widget = (JitWidget) event.getSource();
//    	JSONObject jNode  = widget.getJSONData();
//   	
//    	String query = jNode.get("name").isString().stringValue();
//    	if (this.handler != null) this.handler.search(query);

    }

    public void onRightClickLabel(ClickLabelEvent event) {
 
    	/**
    	JitWidget widget = (JitWidget) event.getSource();
 
        int[]xy = widget.getClickedXY();
        JSONObject jFeature  = widget.getJSONData();

        Menu menu = XMenuFactory.getVisualizationMenu(jFeature);		
    	menu.showAt(xy);
        **/
    
    }

	/************************************************************************
	 * 
	 * VISUALIZATION     VISUALIZATION     VISUALIZATION     VISUALIZATION
	 * 
	 ***********************************************************************/
     
    /**
     * @param jRadial
     */
    public void showRadial(JSONObject jRadial) {
 
    	this.loadJRadial(jRadial.toString());
    	this.resizeRadial();

    }

    public void loadDefault() {

    	this.setTitle("Radial Explorer");
		JSONObject jTree = new JSONObject();
		
		jTree.put("id",       new JSONString("de:kp:radial:initial"));
		jTree.put("name",     new JSONString("Radial Explorer"));
		jTree.put("children", new JSONArray());
		
		this.loadJRadial(jTree.toString());
		this.resizeRadial();
		
    }
   
    public void loadRadial(final String uid, final String title) {
    }


    public void clearRadial() {
		this.loadDefault();
	}

}
