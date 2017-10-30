package com.inapp.pdfbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainModel {
	ObjectData backgroundImage;
	ObjectData []objects;
	
	public MainModel() {
		
	}
	
	public ObjectData getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(ObjectData backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public ObjectData[] getObjects() {
		return objects;
	}
	public void setObjects(ObjectData[] objects) {
		this.objects = objects;
	}

}
