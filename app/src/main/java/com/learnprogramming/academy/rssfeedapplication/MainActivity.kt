package com.learnprogramming.academy.rssfeedapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

enum class XML_DATA_READER_PERMISSIONS(InternalCode: Int){
    INTERNET_PERMISSION_REQUEST(1),
    LOCATION_HARDWARE_PERMISSION_REQUEST(2)
}

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var FEEDURL = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-songs/all/10/explicit.rss"
    private var FEEDURL_2 = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml"
    private var _LOCATION_HARDWARE_PERMISSION_GRANTED = false


    // Check To See If We Have The Required Permission For INTERNET ACCESS Before Continuing
    private fun AskForPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.LOCATION_HARDWARE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            //  Toast.makeText(this,"Location Hardware Permission Is Required To Download The RSS Feed",Toast.LENGTH_LONG).show()
            _LOCATION_HARDWARE_PERMISSION_GRANTED = false
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.LOCATION_HARDWARE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                /// TODO: Show An AlertDialogue About Why The Use Of The Internet Is Important For Downloading The XML
                Toast.makeText(this,"Internet Permission Is Required To Download The RSS Feed",Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.LOCATION_HARDWARE),2)
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.LOCATION_HARDWARE),
                    2)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("MainActivity","MainActivity Permissions: INTERNET_PERMISSION ALREADY GRANTED")
            _LOCATION_HARDWARE_PERMISSION_GRANTED = true

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        for(x in grantResults)
        {
            Log.d("MainActivity","onRequestPermissionResult: RequestCode = ${requestCode}")
            when(requestCode){
                // INTERNET
                2 ->{
                    if ((grantResults.size!=0) && (x == PackageManager.PERMISSION_GRANTED)) {
                        // XML_DATA_READER_PERMISSIONS(1)
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        /// TODO: Permission Granted Everything Is Cauchy
                        _LOCATION_HARDWARE_PERMISSION_GRANTED = true
                        Log.d("MainActivity","onRequestPermissionResult: LOCATION HARDWARE PERMISSION GRANTED")

                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        /// TODO: Permission Denied You Essentially Can't Continue
                        Log.d("MainActivity","onRequestPermissionResult: LOCATION HARDWARE PERMISSION DENIED")

                    }

                }
                else -> {
                    // Ignore all other requests.
                    return
                }


            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "OnCreate Called")
        val _DownloadData = DownloadData()

        _DownloadData.execute(FEEDURL)

        Log.d(TAG, "OnCreate Finished")
        // Wait Till I Get My Money Right
    }

    // Companion Objects Are Kotlin's Equivalents To Static
    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "OnPostExecute Called With Parameter: ${result}")
            }

            fun DownloadXML(URLPath: String?): String {
                var XMLResult = StringBuilder()

                val _XMLDataReader = XMLDataReader(URLPath!!, 1024)
                XMLResult = _XMLDataReader.DownloadXMLData()
         //       Log.d(TAG, "$TAG: XML RESULT = $XMLResult")

                return XMLResult.toString()
            }

            override fun doInBackground(vararg url: String?): String? {
                // TODO("not implemented")To change body of created functions use File | Settings | File Templates.
                Log.d(TAG, "doInBackground Called With Parameter: ${url[0]}")
                val RSSFeed = DownloadXML(url[0])
                Log.d(TAG,"$TAG: RSS FEED = $RSSFeed")
                val _ParseXMLApplications = ParseXMLApplications()
                _ParseXMLApplications.Parse(RSSFeed)
//                if(_ParseXMLApplications.Parse(RSSFeed))
//                {
//
//                }
//                else
//                {
//                    Log.e(TAG, "doInBackground: Error Downloading XML")
//                }
//                if (RSSFeed == "") {
//                    Log.e(TAG, "doInBackground: Error Downloading XML")
//                }
                return RSSFeed
            }

        }
    }
}
