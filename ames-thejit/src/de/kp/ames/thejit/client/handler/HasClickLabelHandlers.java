package de.kp.ames.thejit.client.handler;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasClickLabelHandlers extends HasHandlers {

	HandlerRegistration addClickLabelHandler(ClickLabelHandler handler);

}
