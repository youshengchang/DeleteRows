package com.exploreca.tourfinder.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.content.Context;
import android.util.Log;

import com.exploreca.tourfinder.R;
import com.exploreca.tourfinder.model.Tour;

public class ToursJDOMParser {

	private static final String LOGTAG = "EXPLORECA";

	private static final String TOUR_TAG = "tour";
	private static final String TOUR_ID = "tourId";
	private static final String TOUR_TITLE = "tourTitle";
	private static final String TOUR_DESC = "description";
	private static final String TOUR_PRICE = "price";
	private static final String TOUR_IMAGE = "image";
	

	public List<Tour> parseXML(Context context) {

		InputStream stream = context.getResources().openRawResource(R.raw.tours);
		SAXBuilder builder = new SAXBuilder();
		List<Tour> tours = new ArrayList<Tour>();

		try {

			Document document = (Document) builder.build(stream);
			org.jdom2.Element rootNode = document.getRootElement();
			List<org.jdom2.Element> list = rootNode.getChildren(TOUR_TAG);

			for (Element node : list) {
				Tour tour = new Tour();
				tour.setId(Integer.parseInt(node.getChildText(TOUR_ID)));
				tour.setTitle(node.getChildText(TOUR_TITLE));
				tour.setDescription(node.getChildText(TOUR_DESC));
				tour.setPrice(Double.parseDouble(node.getChildText(TOUR_PRICE)));
				tour.setImage(node.getChildText(TOUR_IMAGE));
				tours.add(tour);
			}

		} catch (IOException e) {
			Log.i(LOGTAG, e.getMessage());
		} catch (JDOMException e) {
			Log.i(LOGTAG, e.getMessage());
		}
		return tours;
	}

}
