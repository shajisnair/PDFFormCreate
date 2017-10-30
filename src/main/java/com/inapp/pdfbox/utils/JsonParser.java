package com.inapp.pdfbox.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inapp.pdfbox.model.MainModel;
import com.inapp.pdfbox.model.ObjectData;

public class JsonParser {

	public static ObjectData parseObjectJSON(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectData modelObj = mapper.readValue(jsonString, ObjectData.class);
		return modelObj;
	}
	
	public static MainModel parseMainJSON(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		MainModel modelObj = mapper.readValue(jsonString, MainModel.class);
		return modelObj;
	}
}
