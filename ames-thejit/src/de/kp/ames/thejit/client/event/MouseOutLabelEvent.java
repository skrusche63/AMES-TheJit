package de.kp.ames.thejit.client.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.HasNativeEvent;
import com.google.gwt.event.shared.GwtEvent;

import de.kp.ames.thejit.client.handler.HasMouseOutLabelHandlers;
import de.kp.ames.thejit.client.handler.MouseOutLabelHandler;

public class MouseOutLabelEvent extends GwtEvent<MouseOutLabelHandler> implements HasNativeEvent {
    
    private static Type<MouseOutLabelHandler> TYPE = new Type<MouseOutLabelHandler>();
    private final NativeEvent nativeevent;

	protected MouseOutLabelEvent(NativeEvent nativeevent) {
		this.nativeevent = nativeevent;
	}

    public static Type<MouseOutLabelHandler> getType() {
		return TYPE;
    }

    public static void fire(HasMouseOutLabelHandlers source, NativeEvent nativeevent) {
		source.fireEvent(new MouseOutLabelEvent(nativeevent));
    }

    protected void dispatch(MouseOutLabelHandler handler) {
		handler.onMouseOutLabel(this);
    }

    public final Type<MouseOutLabelHandler> getAssociatedType() {
		return TYPE;
    }

    public NativeEvent getNativeEvent() {
		return nativeevent;
    }

}
