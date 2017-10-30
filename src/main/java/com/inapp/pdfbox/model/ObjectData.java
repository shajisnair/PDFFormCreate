package com.inapp.pdfbox.model;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectData {

	private OBJECT_TYPE objectType = OBJECT_TYPE.BACKGROUND;
	private String type;
	private COSArray rect;
	private String originX;
	private String originY;
	private float left;
	private float top;
	private float width;
	private float height;
	private String src;
	private String id;
	
	public ObjectData(){
		
	}

	public ObjectData(String type, String originX, String originY, float left, float top, float width, float height) {
		
		switch(type){
			case "image":
				objectType = OBJECT_TYPE.IMAGE;
			case "i-text":
				objectType = OBJECT_TYPE.TEXTFIELD;
			default:
				objectType = OBJECT_TYPE.BACKGROUND;
		}
		
		this.originX = originX;
		this.originY = originY;
		
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
	}
	
	public OBJECT_TYPE getObjectType() {
		return objectType;
	}

	public void setObjectType(OBJECT_TYPE objectType) {
		this.objectType = objectType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public COSArray getRect() {
		if(rect != null)
			rect.clear();
		else
			rect = new COSArray();
		rect.add(new COSFloat(left));
		rect.add(new COSFloat(top));
		rect.add(new COSFloat(width));
		rect.add(new COSFloat(height));
		return rect;
	}
	
	public COSArray getPDFRect(PDRectangle outerRect) {
		if(rect != null)
			rect.clear();
		else
			rect = new COSArray();
		//MediaBox [0.0,0.0,595.27563,841.8898]
				
		rect.add(new COSFloat(left));
		rect.add(new COSFloat(outerRect.getHeight() - top));
		rect.add(new COSFloat(width));
		rect.add(new COSFloat(-height));
		
		
		System.out.println("Drawing co-ordinates : " + rect);
		return rect;
	}

	public void setRect(COSArray rect) {
		this.rect = rect;
	}

	public String getOriginX() {
		return originX;
	}

	public void setOriginX(String originX) {
		this.originX = originX;
	}

	public String getOriginY() {
		return originY;
	}

	public void setOriginY(String originY) {
		this.originY = originY;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	public String getImageUrl() {
		return getSrc();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
