package com.inapp.pdfbox.formeditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import com.inapp.pdfbox.form.PDFForm;
import com.inapp.pdfbox.model.MainModel;
import com.inapp.pdfbox.model.ObjectData;
import com.inapp.pdfbox.utils.JsonParser;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Starting editor !!!");
		// new MainPanel();

		try {
			String data = new App().readJsonFile();

			MainModel mainObj = JsonParser.parseMainJSON(data);
			System.out.println(data);

			PDFForm pdfForm = new PDFForm();
			pdfForm.addImageField(mainObj.getBackgroundImage());
			System.out.println("Background set");

			for (ObjectData objData : mainObj.getObjects()) {
				System.out.println("Setting fields..." + objData.getId());
				pdfForm.addTextboxField(objData);
			}

			pdfForm.saveForm("./output/Test.pdf");
			System.out.println("Done !!!");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This will only work from IDE
	private String readJsonFile() throws IOException {
		File file = null;
		String resource = "/sample.json";
		URL res = getClass().getResource(resource);

		file = new File(res.getFile());
		if (file != null && !file.exists()) {
			throw new RuntimeException("Error: File " + file + " not found!");
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read;
		byte[] bytes = new byte[1024];
		FileInputStream inputStream = new FileInputStream(file);
		while ((read = inputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		inputStream.close();

		return out.toString("UTF-8");
	}
}
