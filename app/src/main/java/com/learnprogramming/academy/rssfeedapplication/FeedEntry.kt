package com.learnprogramming.academy.rssfeedapplication

class FeedEntry
{

    var Title:String =""
    var  Image:String = ""
    var Summary:String = ""
    var PageURL:String = ""
    var PublicationDate:String = ""

    override fun toString(): String {
        var Nyolz:String = ""
        return """
            Title: $Title
            ImageURL: $Image
            Summary: $Summary
            PageURL: $PageURL
            PublicationDate: $PublicationDate
            """.trimIndent()
    }
}