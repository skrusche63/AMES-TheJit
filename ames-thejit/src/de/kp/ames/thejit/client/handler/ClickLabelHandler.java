package de.kp.ames.thejit.client.handler;

import com.google.gwt.event.shared.EventHandler;

import de.kp.ames.thejit.client.event.ClickLabelEvent;

public interface ClickLabelHandler extends EventHandler {

	public void onClickLabel(ClickLabelEvent event);
	
	public void onRightClickLabel(ClickLabelEvent event);
	
}
