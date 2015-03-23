package com.exploreca.tourfinder.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.exploreca.tourfinder.R;
import com.exploreca.tourfinder.model.Tour;

public class ToursPullParser {

	private static final String LOGTAG = "EXPLORECA";
	
	private static final String TOUR_ID = "tourId";
	private static final String TOUR_TITLE = "tourTitle";
	private static final String TOUR_DESC = "description";
	private static final String TOUR_PRICE = "price";
	private static final String TOUR_IMAGE = "image";
	
	private Tour currentTour  = null;
	private String currentTag = null;
	List<Tour> tours = new ArrayList<Tour>();

	public List<Tour> parseXML(Context context) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			InputStream stream = context.getResources().openRawResource(R.raw.tours);
			xpp.setInput(stream, null);

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if (eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText());
				}
				eventType = xpp.next();
			}

		} catch (NotFoundException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (XmlPullParserException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (IOException e) {
			Log.d(LOGTAG, e.getMessage());
		}

		return tours;
	}

	private void handleText(String text) {
		String xmlText = text;
		if (currentTour != null && currentTag != null) {
			if (currentTag.equals(TOUR_ID)) {
				Integer id = Integer.parseInt(xmlText);
				currentTour.setId(id);
			} 
			else if (currentTag.equals(TOUR_TITLE)) {
				currentTour.setTitle(xmlText);
			}
			else if (currentTag.equals(TOUR_DESC)) {
				currentTour.setDescription(xmlText);
			}
			else if (currentTag.equals(TOUR_IMAGE)) {
				currentTour.setImage(xmlText);
			}
			else if (currentTag.equals(TOUR_PRICE)) {
				double price = Double.parseDouble(xmlText);
				currentTour.setPrice(price);
			}
		}
	}

	private void handleStartTag(String name) {
		if (name.equals("tour")) {
			currentTour = new Tour();
			tours.add(currentTour);
		}
		else {
			currentTag = name;
		}
	}
}
