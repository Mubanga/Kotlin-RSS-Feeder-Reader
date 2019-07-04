package com.learnprogramming.academy.rssfeedapplication

class FeedEntry
{

    var Title:String =""
    var Artist:String = ""
    var  Image:String = ""
    var PageURL:String = ""
    var PublicationDate:String = ""

    override fun toString(): String {
        var Nyolz:String = ""
        return """
            Title: $Title
            Artist: $Artist
            ImageURL: $Image
            PageURL: $PageURL
            PublicationDate: $PublicationDate
            """.trimIndent()
    }
}