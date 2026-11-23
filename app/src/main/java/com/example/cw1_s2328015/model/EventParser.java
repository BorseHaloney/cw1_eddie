package com.example.cw1_s2328015.model;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class EventParser
{

    public ArrayList<Event> parseData(String dataToParse, Event.Category category)
    {
        ArrayList<Event> allEvents = new ArrayList<>();
        Event anEvent = null;
        try
        {
            boolean insideAnEvent = false;
            boolean insideComment = false;
            boolean insideFrom = false;
            boolean insideTo = false;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {   // Check which start tag
                    String tag = xpp.getName();
                    if (tag.equalsIgnoreCase("situationRecord"))
                    {
                        insideAnEvent = true;
                        anEvent = new Event();
                        anEvent.setCategory(category);
                        anEvent.setRecordType(xpp.getAttributeValue(0));
                        anEvent.setId(xpp.getAttributeValue(1));
                        Log.d("MyTag","New Event found! Type: " + anEvent.getRecordType());
                    }
                    else if (insideAnEvent) //only look at tags inside a an event
                    {
                        if (tag.equalsIgnoreCase("generalPublicComment"))
                        {
                            insideComment = true;
                        }
                        else if (tag.equalsIgnoreCase("from"))
                        {
                            insideFrom = true;
                        }
                        else if (tag.equalsIgnoreCase("to"))
                        {
                            insideTo = true;
                        }
                        else if (tag.equalsIgnoreCase("value"))
                        {
                            String temp = xpp.nextText();
                            if (insideComment) {
                                anEvent.setComment(temp);
                                Log.d("MyTag","Comment: " + temp);
                            }
                            else if (insideFrom) {
                                anEvent.setFromName(temp);
                            }
                            else if (insideTo) {
                                anEvent.setToName(temp);
                            }
                        }
                        else if (tag.equalsIgnoreCase("overallStartTime"))
                        {
                            anEvent.setStartTime(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("overallEndTime"))
                        {
                            anEvent.setEndTime(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("tpegDirection"))
                        {
                            anEvent.setDirection(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("latitude"))
                        {
                            String temp = xpp.nextText();
                            if (insideFrom) {
                                anEvent.setFromLatitude(temp);
                            } else {
                                anEvent.setToLatitude(temp);
                            }
                        }
                        else if (tag.equalsIgnoreCase("longitude"))
                        {
                            String temp = xpp.nextText();
                            if (insideFrom) {
                                anEvent.setFromLongitude(temp);
                            } else {
                                anEvent.setToLongitude(temp);
                            }
                        }
                        else if (tag.equalsIgnoreCase("numberOfOperationalLanes"))
                        {
                            anEvent.setOperationalLanes(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("numberOfLanesRestricted"))
                        {
                            anEvent.setLanesRestricted(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("roadOrCarriagewayOrLaneManagementType"))
                        {
                            anEvent.setManagementType(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("abnormalTrafficType"))
                        {
                            anEvent.setAbnormalTrafficType(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("delaysType"))
                        {
                            anEvent.setDelaysType(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("roadMaintenanceType"))
                        {
                            anEvent.setMaintenanceType(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("situationRecordVersionTime"))
                        {
                            anEvent.setVersionTime(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("probabilityOfOccurrence"))
                        {
                            anEvent.setProbability(xpp.nextText());
                        }
                        else if (tag.equalsIgnoreCase("sourceType"))
                        {
                            anEvent.setSourceType(xpp.nextText());
                        }
                        // any other tags are ignored
                    }
                }
                else if(eventType == XmlPullParser.END_TAG)
                {
                    String tag = xpp.getName();
                    if (tag.equalsIgnoreCase("situationRecord"))
                    {
                        allEvents.add(anEvent); //add to collection
                        insideAnEvent = false;
                        Log.d("MyTag","Event parsing completed! " + anEvent);
                    }
                    else if (tag.equalsIgnoreCase("generalPublicComment"))
                    {
                        insideComment = false;
                    }
                    else if (tag.equalsIgnoreCase("from"))
                    {
                        insideFrom = false;
                    }
                    else if (tag.equalsIgnoreCase("to"))
                    {
                        insideTo = false;
                    }
                }
                eventType = xpp.next(); // Get the next event  before looping again
            }
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.d("MyTag","End of document reached, " + allEvents.size() + " events");
        return allEvents;
    }

}
