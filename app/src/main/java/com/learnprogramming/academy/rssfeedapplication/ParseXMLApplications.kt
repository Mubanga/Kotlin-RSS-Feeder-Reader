package com.learnprogramming.academy.rssfeedapplication

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseXMLApplications {
    private val TAG = "ParseXMLApplications"
    val _FeedEntries = ArrayList<FeedEntry>() // Array List Of All The Relevant Feed Entries From The XML
    var isParseSuccessful = true // True By Default Will Be Set To False If An Exception Or No Parsed Data Can Be Read

    fun Parse(XMLString: String): Boolean {
        Log.d(TAG, "Parse: Called With XML String = $XMLString")
        var isInSpecifiedTag = false
        var textValue = ""

        try {
            // Generate An XML Factory From The " XmlPullParserFactory " Found In Java
            val xmlFactory = XmlPullParserFactory.newInstance()
            // Ensure That The Factory Is Aware Of The Particular XML Namespace(s)
            xmlFactory.isNamespaceAware = true

            // Create An XML Pull Parser Using The XML Factory
            val xmlPullParser = xmlFactory.newPullParser()
            // The Input From The XML Pull Parser Needs To Be Set As The Input Coming In From The XMLString
            xmlPullParser.setInput(XMLString.reader())
            var eventType = xmlPullParser.eventType
            var currentRecord = FeedEntry()
            var isInEntryTag = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val currentTagName = xmlPullParser.name.toLowerCase()
                 when(eventType)
                 {
                     XmlPullParser.START_TAG -> {
                         Log.d(TAG,"Parse: Starting Tag = $currentTagName")
                         if(currentTagName == "entry")
                         {
                             isInEntryTag = true
                         }
                     }

                     XmlPullParser.TEXT -> textValue = xmlPullParser.text

                     XmlPullParser.END_TAG -> {
                         Log.d(TAG,"Parse: Ending Tag = $currentTagName")
                         if(isInEntryTag)
                         {
                             when(currentTagName)
                             {
                                 "entry" -> {
                                     _FeedEntries.add(currentRecord)
                                     isInEntryTag = false
                                     currentRecord = FeedEntry()
                                 }
                                 // <im:name>The Git Up</im:name>
                                 "name" ->  currentRecord.Title = textValue
                                 // <title>The Git Up - Blanco Brown</title>
                                 "artist" -> currentRecord.Artist = textValue
                                 // <im:image height="170">https://....png</im:image>
                                 "image" -> {
                                   var ImageURI =  xmlPullParser.getAttributeValue(170)
                                    Log.d(TAG,"Image URI = ${ImageURI?.toString()}")
                                     currentRecord.Image = textValue
                                 }
                                 // <im:releaseDate label="May 3, 2019">2019-05-03T00:00:00-07:00</im:releaseDate>
                                 "releasedate" -> currentRecord.PublicationDate = textValue
                                 // <id im:id="1461880347">https:// </id>
                                 "id" -> currentRecord.PageURL = textValue

                             }

                         }

                     }
                 }
                // Proceed To The Next XML Tag
                eventType = xmlPullParser.next()
            }
            for(x in _FeedEntries)
            {
                Log.d(TAG,"**********************************")
                Log.d(TAG,"${x.toString()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            isParseSuccessful = false
            Log.d(TAG, "XML Data Could Not Be Parsed Error = $XMLString")

        }


        return isParseSuccessful
    }

}