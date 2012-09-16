package de.kp.ames.thejit.client.base;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;

import de.kp.ames.thejit.client.event.ClickLabelEvent;
import de.kp.ames.thejit.client.event.MouseOutLabelEvent;
import de.kp.ames.thejit.client.event.MouseOverLabelEvent;
import de.kp.ames.thejit.client.handler.ClickLabelHandler;
import de.kp.ames.thejit.client.handler.HasClickLabelHandlers;
import de.kp.ames.thejit.client.handler.HasMouseOutLabelHandlers;
import de.kp.ames.thejit.client.handler.HasMouseOverLabelHandlers;
import de.kp.ames.thejit.client.handler.MouseOutLabelHandler;
import de.kp.ames.thejit.client.handler.MouseOverLabelHandler;

public abstract class JitWidget extends Widget implements ResizeHandler, HasClickLabelHandlers, HasMouseOverLabelHandlers, HasMouseOutLabelHandlers {

	private final String name;

	private final String backgroundColor;

	private final JavaScriptObject widgetConfig;
	
	private JavaScriptObject node;
	private JavaScriptObject label;

	// this is a JSON representation of the data of a clicked node
	private JSONObject data;
	
	// this is the event mouse position
	private int[] xy;
	
	private static final String defaultBackgroundColor = "#333";

	private static final Map<String, JavaScriptObject> jitwrappedobjects = new LinkedHashMap<String, JavaScriptObject>();
	private static final Map<String, JitWidget> jitgwtwrappers = new LinkedHashMap<String, JitWidget>();

	private static double oldWidth;
	private static double oldHeight;

	static {
		exportToJavaScript();

		oldWidth  = Window.getClientWidth();
		oldHeight = Window.getClientHeight();
	}

	public JitWidget(String name, JavaScriptObject widgetConfig) {

		this.name = name;
		this.widgetConfig = widgetConfig;		

		final Element domelement = DOM.createElement("div");
		DOM.setElementProperty(domelement, "id", name);
		setElement(domelement);

		backgroundColor = 0 < DOM.getStyleAttribute(domelement,	"backgroundColor").length() ? DOM.getStyleAttribute(getElement(), "backgroundColor") : defaultBackgroundColor;
		DOM.setStyleAttribute(domelement, "backgroundColor", backgroundColor);

		jitgwtwrappers.put(name, this);
	
	}

	public String getName() {
		return name;
	}

	public JavaScriptObject getClickedNode() {
		return node;
	}

	public JavaScriptObject getClickedLabel() {
		return label;
	}

	public JSONObject getJSONData() {
		return this.data;
	}
	
	public int[] getClickedXY() {
		return xy;
	}
	
	protected void onAttach() {
		
		SC.logWarn("====> thejit.JitWidget.onAttach name: " + name + " offset w/h: "+ getOffsetWidth() + "/" + getOffsetHeight());

		
		setSize("100%", "100%");

		JavaScriptObject canvas = initCanvas(name, getOffsetWidth(), getOffsetHeight());
		JavaScriptObject widget = init(canvas, this.widgetConfig);
		
		jitwrappedobjects.put(name, widget);
		super.onAttach();
	
	}

	public void onResize(ResizeEvent event) {
		
		double width = event.getWidth();
		double height = event.getHeight();
		double percentagex = width / oldWidth;
		double percentagey = height / oldHeight;
		double w = getOffsetWidth() * percentagex;
		double h = getOffsetHeight() * percentagey;
		
		setPixelSize((int) w, (int) h);
		resize(name, w, h);
	
	}

	protected abstract JavaScriptObject init(JavaScriptObject canvas, JavaScriptObject config);
	
	protected native JavaScriptObject initCanvas(String name, double w, double h) /*-{
	}-*/;
	
	public final native void resize(String name, double w, double h) /*-{
		var widget = jitWrappedObject(name);
		if (widget == null) return;
		
		widget.canvas.resize(w, h);
		widget.refresh();
		widget.controller.onAfterCompute();

	}-*/;
	
	public final static void resizeAll(ResizeEvent event) {

		for (JitWidget jit : jitgwtwrappers.values()) {
			jit.onResize(event);
		}

		oldWidth = event.getWidth();
		oldHeight = event.getHeight();

	}

	public HandlerRegistration addClickLabelHandler(ClickLabelHandler handler) {
		return addHandler(handler, ClickLabelEvent.getType());
	}

	public HandlerRegistration addMouseOverLabelHandler(MouseOverLabelHandler handler) {
		return addHandler(handler, MouseOverLabelEvent.getType());
	}

	public HandlerRegistration addMouseOutLabelHandler(MouseOutLabelHandler handler) {
		return addHandler(handler, MouseOutLabelEvent.getType());
	}

	private final void setJSONData(JSONObject data) {
		this.data = data;
	}
	
	private final void setClickedXY(int x, int y ) {
		int[]xy = {x,y};
		this.xy = xy;
	}
		
	private final static JavaScriptObject getWrappedObject(String name) {
		return jitwrappedobjects.get(name);
	}

	@SuppressWarnings("unused")
	private final static void forwardEvent(String name, JavaScriptObject event, JavaScriptObject label, JavaScriptObject node) {
			
			NativeEvent received = (NativeEvent) (null == event ? null : event.cast());
			Document document = Document.get();

			JitWidget jit = jitgwtwrappers.get(name);

			// register event mouse position
			int x = received.getClientX();
			int y = received.getClientY();
			
			jit.setClickedXY(x,y);
				
			String type = null == received ? "click" : received.getType();
			if ("click".equals(type)) {
				
				JSONObject jObject = new JSONObject(node);
				jit.setJSONData(jObject);
				
				String nodeid = jObject.get("id").toString();
				nodeid = nodeid.substring(1, nodeid.length() - 1);
				
				History.newItem(name + ";" + nodeid);
				ClickLabelEvent.fire(jit, false);
				
			} else if ("contextmenu".equals(type)) {

				JSONObject jObject = new JSONObject(node);				
				if (jObject.containsKey("data")) jit.setJSONData(jObject.get("data").isObject());				
				
				ClickLabelEvent.fire(jit, true);

			} else if ("mouseover".equals(type)) {
				MouseOverLabelEvent.fire(jit, received);

			} else if ("mouseout".equals(type)) {
				MouseOutLabelEvent.fire(jit, received);

			} else {
				GWT.log("not handled: " + type, null);
			}
			
			// this avoids further browser events
			received.preventDefault();
			received.stopPropagation();

	}

	private final static native void exportToJavaScript() /*-{
		
		jitWrappedObject = @de.kp.ames.thejit.client.base.JitWidget::getWrappedObject(Ljava/lang/String;);
		jitWrapperForwardEvent = @de.kp.ames.thejit.client.base.JitWidget::forwardEvent(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;);

	}-*/;
}
