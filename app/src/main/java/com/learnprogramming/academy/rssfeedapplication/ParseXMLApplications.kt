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
            }

        } catch (e: Exception) {
            e.printStackTrace()
            isParseSuccessful = false
            Log.d(TAG, "XML Data Could Not Be Parsed Error = $XMLString")

        }


        return isParseSuccessful
    }

}