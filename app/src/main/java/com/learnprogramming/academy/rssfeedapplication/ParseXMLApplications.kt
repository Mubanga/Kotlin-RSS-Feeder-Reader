package com.learnprogramming.academy.rssfeedapplication

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseXMLApplications {
    private val TAG = "ParseXMLApplications"
    val _FeedEntries = ArrayList<FeedEntry>() // Array List Of All The Relevant Feed Entries From The XML
    var isParseSuccessful = true // True By Default Will Be Set To False If An Exception Or No Parsed Data Can Be Read
    private var _gotCorrectImageSize: Boolean = false

    /**
     *  Parses The XML Data From A Valid XML String
     *  @param XMLString
     *  @return True If XML Data is successfully Parsed Or False If XML Data Cannot Be Parsed An Exception Is Also Thrown
     */
    fun Parse(XMLString: String): Boolean {
        //  Log.d(TAG, "Parse: Called With XML String = $XMLString")
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
                // Log.d(TAG,"$TAG: XML Pull Parser Name: ${xmlPullParser.name.toLowerCase()}")
                var currentTagName = xmlPullParser.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "Parse: Starting Tag = $currentTagName")
                        if (currentTagName == "entry") {
                            isInEntryTag = true
                        }
                        if(isInEntryTag && currentTagName == "image")
                        {
                            val imageResolution = xmlPullParser?.getAttributeValue(null,"height")
                            if (imageResolution != null) {
                                if(imageResolution.isNotEmpty()) {
                                    _gotCorrectImageSize = imageResolution == "170"
                                }
                            }

                        }
                    }

                    XmlPullParser.TEXT -> textValue = xmlPullParser.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "Parse: Ending Tag = $currentTagName")
                        if (isInEntryTag) {
                            when (currentTagName) {
                                "entry" -> {
                                    _FeedEntries.add(currentRecord)
                                    isInEntryTag = false
                                    currentRecord = FeedEntry()
                                }
                                // <im:name>The Git Up</im:name>
                                "name" -> currentRecord.Title = textValue
                                // <title>The Git Up - Blanco Brown</title>
                                "artist" -> currentRecord.Artist = textValue
                                // <im:image height="170">https://....png</im:image>
                                "image" -> {
                                    if(_gotCorrectImageSize) {
                                        val ImageURI = textValue
                                        Log.d(TAG, "Image URI = ${ImageURI}")
                                        currentRecord.Image = ImageURI
                                    }
                                    else
                                    {
                                        currentRecord.Image = textValue // Take Whatever Image You Can Get
                                    }
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
 //           Log.d(TAG,"$TAG Feed Entries Count = ${_FeedEntries.count()}")
            for (x in _FeedEntries) {
                Log.d(TAG, "**********************************\n")
                Log.d(TAG, "${x.toString()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            isParseSuccessful = false
            Log.d(TAG, "XML Data Could Not Be Parsed Error = ${e.message}")

        }


        return isParseSuccessful
    }

    /**
     *  Returns A List Of The FeedEntries From The RSSFeed
     *  @return ArrayList<FeedEntry>
     */
    fun getParsedData() : ArrayList<FeedEntry>
    {
        return _FeedEntries
    }

}