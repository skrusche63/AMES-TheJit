package de.kp.ames.thejit.client.handler;

import com.google.gwt.event.shared.EventHandler;

import de.kp.ames.thejit.client.event.MouseOutLabelEvent;

public interface MouseOutLabelHandler extends EventHandler {

    void onMouseOutLabel(MouseOutLabelEvent event);
    
}
