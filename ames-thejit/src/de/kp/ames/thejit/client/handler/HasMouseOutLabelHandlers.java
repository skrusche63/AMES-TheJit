package de.kp.ames.thejit.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasMouseOutLabelHandlers extends HasHandlers {

    HandlerRegistration addMouseOutLabelHandler(MouseOutLabelHandler handler);
    
}
