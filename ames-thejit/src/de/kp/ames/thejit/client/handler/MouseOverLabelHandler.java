package de.kp.ames.thejit.client.handler;

import com.google.gwt.event.shared.EventHandler;

import de.kp.ames.thejit.client.event.MouseOverLabelEvent;

public interface MouseOverLabelHandler extends EventHandler {

    void onMouseOverLabel(MouseOverLabelEvent event);

}

