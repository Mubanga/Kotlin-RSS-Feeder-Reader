package com.learnprogramming.academy.rssfeedapplication

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

// Buffered XML Data Reader
/** Buffered XML Data Reader
 *  Reads DATA From Specified CharBuffer And Builds The _XMLFeedData string
 *  @param urlFeedPath The URL Of The RSS Feed As A " String "
 *  @param ReaderBufferSize The Size Of The Buffer Used For Reading Data As An "Int"
 */
class XMLDataReader (urlFeedPath: String, ReaderBufferSize: Int) {

    private val TAG = "XmlDataReader"

    //********* Initialisation Variables **************************
    private val _urlFeedPath = urlFeedPath
    private var _ReaderBufferSize = ReaderBufferSize
    private val _InputBuffer = CharArray(_ReaderBufferSize)

    //********* Connection and Reader Variables *******************
    private val _XMLFeedData = StringBuilder()
    private lateinit var _RSS_URL: URL
    private lateinit var _Buffered_RSS_Reader: BufferedReader

    /**
     * Reads DATA From Specified CharBuffer And Builds The _XMLFeedData string
     *
     */
    private fun ReadBufferedData()
    {
        // Reading Buffered Data
        var charRead = 0
        while(charRead>=0)
        {
            charRead = _Buffered_RSS_Reader.read(_InputBuffer) // Populate The InputBuffer With Characters Read From The Buffered RSS (Input) Reader Stream
            if(charRead>0)
            {
                _XMLFeedData.append(_InputBuffer,0,charRead) // Build Up The XMLFeedData String From The Characters Read From The Buffered RSS Feed
            }
        }
        _Buffered_RSS_Reader.close() // *** N.B *** Always Remeber To Close The Connection ***
    }

    /** Setup The URL Connection
     * Initialises A URL Connection And Reads The DATA With A BufferedReader
     *
     */
    private fun InitialiseConnection()
    {
        try{
            _RSS_URL = URL(_urlFeedPath)
            val Active_Connection = _RSS_URL.openConnection() as HttpURLConnection
            val Response_Code = Active_Connection.responseCode
            Log.d(TAG,"$TAG: Server Response Code = ${Response_Code}")
            _Buffered_RSS_Reader = BufferedReader(InputStreamReader(Active_Connection.inputStream))
            Active_Connection.inputStream.buffered().reader().use { _XMLFeedData.append(it.readText())}
            Active_Connection.inputStream.buffered(_ReaderBufferSize).reader().use { _XMLFeedData.append(it.readText()) }
            //ReadBufferedData() // TODO: There Is A Idiomatic Kotlin Expression To Read The Data ALL In One So If We Want To Do That We Can Implement That, However It's Not Advisable For Large DATA Sets
        }
        catch(e: Exception)
        {
            var ErrorMessage:String
            when(e)
            {
                is MalformedURLException -> {ErrorMessage = "$TAG: Invalid URL ${e.message}"
                    Log.e(TAG,ErrorMessage)
                }
                is IOException -> { ErrorMessage = "$TAG: IOException ${e.message}"
                    Log.e(TAG,ErrorMessage)
                }
                is SecurityException -> { ErrorMessage ="$TAG: Missing Permissions ? INTERNET ${e.message}"
                    Log.e(TAG,ErrorMessage)
                }
                is Exception -> { ErrorMessage ="$TAG: Unknown Exception ${e.message}"
                    Log.e(TAG,ErrorMessage)
                }

            }
        }
//        catch (e: MalformedURLException)
//        {
//            Log.e(TAG,"$TAG: Invalid URL ${e.message} ")
//        }catch(e: IOException){
//            Log.e(TAG,"$TAG: IOException ${e.message} ")
//
//        } catch(e: SecurityException){
//            Log.e(TAG,"$TAG: Missing Permissions ? INTERNET ${e.message}")
//        }
//        catch(e: Exception){
//            Log.e(TAG,"$TAG: Unknown Exception ${e.message}")
//        }
    }

    /**
     * Downloads And Returns The Read XML Data
     * @return  The Downloaded RSS Feed In XML Format As A String Of Type "StringBuilder"
     *
     */
    fun DownloadXMLData() : StringBuilder
    {

        InitialiseConnection()
        return _XMLFeedData
    }

}