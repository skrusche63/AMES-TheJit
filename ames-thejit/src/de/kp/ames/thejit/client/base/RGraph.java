package de.kp.ames.thejit.client.base;

import com.google.gwt.core.client.JavaScriptObject;


public class RGraph extends JitWidget {

	public RGraph(String name, JavaScriptObject config) {
		super(name, config);
	}

    @Override
    protected native JavaScriptObject init(JavaScriptObject canvas, JavaScriptObject config) /*-{
        return new $wnd.RGraph(canvas, config);
    }-*/;
	
    @Override
	protected native JavaScriptObject initCanvas(String name, double w, double h) /*-{
		return new $wnd.Canvas(name + '_canvas', {
			'injectInto': name, 
			'width': w, 
			'height': h, 
			'backgroundColor':'#f2f2f4',
			'backgroundCanvas': {
				'styles': {'strokeStyle': '#555555'},
				'impl': {
					'init': function() {},
					'plot': function(env,ctx){
						var times = 6, d = 100;
						var pi2 = Math.PI * 2;
						for (var i=1; i <= times; i++) {
							ctx.beginPath();
							ctx.arc(0, 0, i * d, 0, pi2, true);
							ctx.stroke();
							ctx.closePath();
						}
					}
				}
			}
		});
	}-*/;

}

