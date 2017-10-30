package com.inapp.pdfbox.form;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.util.Matrix;

import com.inapp.pdfbox.model.ObjectData;

public class PDFForm {

	private PDDocument document = null;
	PDPage page = null;
	PDFont font = null;
	PDPageContentStream contentStream = null;
	PDAcroForm acroForm = null;

	public PDFForm() {
		createNewFormDocument(PDRectangle.A4, PDType1Font.HELVETICA);
	}

	public PDFForm(PDRectangle pageSize) {
		createNewFormDocument(pageSize, PDType1Font.HELVETICA);
	}

	public PDFForm(PDRectangle pageSize, PDFont font) {
		createNewFormDocument(pageSize, font);
	}

	/**
	 * Create a new document with an empty page.
	 */
	public void createNewFormDocument(PDRectangle rect, PDFont font) {
		try {
			if (document != null)
				document.close();

			document = new PDDocument();
			page = new PDPage(rect);

			document.addPage(page);

			PDResources resources = new PDResources();
			resources.put(COSName.getPDFName("Helv"), PDType1Font.HELVETICA);

			COSName fontName = resources.add(font);
			System.out.println("Added font :" + fontName);
			page.setResources(resources);

			COSDictionary acroFormDict = new COSDictionary();
			acroFormDict.setItem(COSName.FIELDS, new COSArray());
			acroFormDict.setItem(COSName.DA, new COSString("/Helv 0 Tf 0 g"));

			// Add a new AcroForm and add that to the document
			acroForm = new PDAcroForm(document);

			// Add and set the resources and default appearance at the form
			// level
			acroForm.setDefaultResources(resources);

			document.getDocumentCatalog().setAcroForm(acroForm);
			
//			float pageHeight = page.getMediaBox().getHeight();

//			try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
//            {
//				AffineTransform transform = new AffineTransform();
//
//				transform.scale(1, -1); // flip along the line y=0
//				transform.translate(0, -pageHeight); // move the page conet back up
//
////				double[] transformMatrix = new double[6];
////				transform.getMatrix(transformMatrix);
////				contentStream.concatCTM((float) transformMatrix[0], (float) transformMatrix[1], (float) transformMatrix[2], (float) transformMatrix[3], (float) transformMatrix[4], (float) transformMatrix[5]);
//
//				Matrix transformMatrix = new Matrix(transform);
//				contentStream.transform(transformMatrix);
//            }
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveForm(String filePath) throws IOException {
		document.save(filePath);
	}

	@Override
	protected void finalize() throws Throwable {
		if (document != null)
			document.close();
		super.finalize();
	}

	public void setPageBackground(ObjectData backgroundImage) {
		Overlay overlay = new Overlay();
		overlay.setInputFile(backgroundImage.getImageUrl());
		overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
		// TODO: not completed
	}

	public void addTextboxField(ObjectData objData) throws IOException {

		// COSDictionary cosDict = new COSDictionary();
		PDRectangle mediabox = page.getMediaBox();
		System.out.println("MediaBox = " + mediabox);

		COSArray rect = new COSArray();
		rect.add(new COSFloat(objData.getLeft())); // lower x boundary
		rect.add(new COSFloat(mediabox.getUpperRightY() - objData.getTop() )); // lower y boundary
		rect.add(new COSFloat(objData.getLeft() + objData.getWidth())); // upper x boundary
		rect.add(new COSFloat(mediabox.getUpperRightY() - (objData.getTop() + objData.getHeight()))); // upper y boundary

		String strREct = rect.toString();
		System.out.println(strREct);

		// cosDict.setItem(COSName.RECT, rect);
		// cosDict.setItem(COSName.FT, COSName.getPDFName("Tx")); // Field Type
		// cosDict.setItem(COSName.TYPE, COSName.ANNOT);
		// cosDict.setItem(COSName.SUBTYPE, COSName.getPDFName("Widget"));
		// cosDict.setItem(COSName.T, new COSString("yourFieldName"));

		// Add a form field to the form.
		PDTextField textBox = new PDTextField(acroForm);
		textBox.setPartialName(objData.getId());

		// Acrobat sets the font size to 12 as default
		// This is done by setting the font size to '12' on the
		// field level.
		// The text color is set to blue in this example.
		// To use black, replace "0 0 1 rg" with "0 0 0 rg" or "0 g".
		String defaultAppearanceString = "/Helv 7 Tf 0 0 1 rg";
		textBox.setDefaultAppearance(defaultAppearanceString);

		// add the field to the acroform
		acroForm.getFields().add(textBox);

		PDAnnotationWidget widget = textBox.getWidgets().get(0);
		PDRectangle rectangle = new PDRectangle(rect);
		widget.setRectangle(rectangle);
		widget.setPage(page);

		PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(
				new COSDictionary());
		fieldAppearance.setBorderColour(new PDColor(new float[] { 0, 1, 0 }, PDDeviceRGB.INSTANCE));
		fieldAppearance.setBackground(new PDColor(new float[] { 1, 1, 0 }, PDDeviceRGB.INSTANCE));
		widget.setAppearanceCharacteristics(fieldAppearance);

		widget.setPrinted(true);
		page.getAnnotations().add(widget);

		textBox.setValue(objData.getId());

	}

	public void addImageField(ObjectData objData) throws IOException {
		PDImageXObject pdImage = PDImageXObject.createFromFile(objData.getImageUrl(), document);
		PDRectangle mediabox = page.getMediaBox();
		System.out.println("MediaBox = " + mediabox);
		
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true,
				true)) {
			// contentStream.drawImage(pdImage, 20, 20 );
			float scale = 1f; // only if scaling is needed
			contentStream.drawImage(pdImage, mediabox.getLowerLeftX(), mediabox.getUpperRightY() - pdImage.getHeight(), 
					pdImage.getWidth() * scale, pdImage.getHeight() * scale);
		}
	}
}
