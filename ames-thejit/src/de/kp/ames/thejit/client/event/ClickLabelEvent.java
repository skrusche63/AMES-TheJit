package de.kp.ames.thejit.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.kp.ames.thejit.client.handler.ClickLabelHandler;
import de.kp.ames.thejit.client.handler.HasClickLabelHandlers;

public class ClickLabelEvent extends GwtEvent<ClickLabelHandler> {

    private final boolean rightclick;
    private static Type<ClickLabelHandler> TYPE = new Type<ClickLabelHandler>();

    protected ClickLabelEvent(boolean rightclick) {
    	this.rightclick = rightclick;
    }

    public static Type<ClickLabelHandler> getType() {
    	return TYPE;
    }

    public static void fire(HasClickLabelHandlers source, boolean rightclick) {
    	ClickLabelEvent event = new ClickLabelEvent(rightclick);
    	source.fireEvent(event);
    }

    protected void dispatch(ClickLabelHandler handler) {
		if (rightclick) {	
		    handler.onRightClickLabel(this);
		} else {
		    handler.onClickLabel(this);
		}
    }

    public final Type<ClickLabelHandler> getAssociatedType() {
    	return TYPE;
    }

}