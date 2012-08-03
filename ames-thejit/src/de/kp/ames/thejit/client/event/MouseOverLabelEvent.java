package de.kp.ames.thejit.client.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.HasNativeEvent;
import com.google.gwt.event.shared.GwtEvent;

import de.kp.ames.thejit.client.handler.HasMouseOverLabelHandlers;
import de.kp.ames.thejit.client.handler.MouseOverLabelHandler;

public class MouseOverLabelEvent extends GwtEvent<MouseOverLabelHandler> implements HasNativeEvent {

    private static Type<MouseOverLabelHandler> TYPE = new Type<MouseOverLabelHandler>();
    private final NativeEvent nativeevent;

	protected MouseOverLabelEvent(NativeEvent nativeevent) {
		this.nativeevent = nativeevent;
    }

    public static Type<MouseOverLabelHandler> getType() {
		return TYPE;
    }

    public static void fire(HasMouseOverLabelHandlers source, NativeEvent nativeevent) {
		source.fireEvent(new MouseOverLabelEvent(nativeevent));
    }

    protected void dispatch(MouseOverLabelHandler handler) {
		handler.onMouseOverLabel(this);
    }

    public final Type<MouseOverLabelHandler> getAssociatedType() {
		return TYPE;
    }

    public NativeEvent getNativeEvent() {
		return nativeevent;
    }

}
