package de.kp.ames.thejit.client.base;

import com.google.gwt.core.client.JavaScriptObject;


public class HyperTree extends JitWidget {

	public HyperTree(String name, JavaScriptObject config) {
		super(name, config);
	}

    protected native JavaScriptObject init(JavaScriptObject canvas, JavaScriptObject config) /*-{
	    return new $wnd.Hypertree(canvas, config);
	}-*/;

	protected native JavaScriptObject initCanvas(String name, double w, double h) /*-{
		return new $wnd.Canvas(name + '_canvas', {
			'injectInto': name, 
			'width': w, 
			'height': h,
			'backgroundColor': '#f2f2f4'
		});
	}-*/;

}
