package de.kp.ames.thejit.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.layout.VLayout;

public class main implements EntryPoint {

	public void onModuleLoad() {
		
		VLayout layout = new VLayout();
		
		layout.setWidth(800);
		layout.setHeight(800);

		HyperTreeExplorer hypertree = new HyperTreeExplorer();
		layout.addMember(hypertree);
		
		layout.draw();
		
	}

}
