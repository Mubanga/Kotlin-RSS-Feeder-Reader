package com.learnprogramming.academy.rssfeedapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

enum class XML_DATA_READER_PERMISSIONS(InternalCode: Int){
    INTERNET_PERMISSION_REQUEST(1),
    LOCATION_HARDWARE_PERMISSION_REQUEST(2)
}



class MainActivity : AppCompatActivity() {

    companion object
    {
        val TAG = "MainActivity"
    }

    /**
     *  Layout Widget Declarations
     */
    private lateinit var _RecyclerView: RecyclerView
    private lateinit var _RSSMusicViewAdapter: RecyclerView.Adapter<*>
    private lateinit var _RSSMusicViewManager: RecyclerView.LayoutManager
    private var _RSSFeedEntries = ArrayList<FeedEntry>()
    private val _DownloadData by lazy { DownloadData() }


    /**
     *  RSS XML URL Feed Declarations
     */
    private var FEEDURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=100/xml"
    private var FEEDURL_2 = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=75/xml"
    private var FEEDURL_3 = "https://rss.itunes.apple.com/api/v1/us/itunes-music/new-music/all/75/explicit.atom"
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

    override fun onDestroy() {
        super.onDestroy()
        _DownloadData.cancel(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //_RecyclerView = findViewById(R.id.recyclerView_ListMain)
        _RSSMusicViewManager = LinearLayoutManager(this)


        Log.d(MainActivity.TAG, "OnCreate Called")
        val _DownloadData = DownloadData()

        _DownloadData.execute(FEEDURL)

        Log.d(MainActivity.TAG, "OnCreate Finished")

    }

    /**
     *  I changed DownloadData From A CompanionObject Because Our Implementation Of It In The onCreate
     *  We Are Making A Concrete Instance Of It, So I See No Benefit To Having It As Pseudo-Static Class
     */
        internal inner class DownloadData : AsyncTask<String, Void, ArrayList<FeedEntry> >() {
            private val TAG = "DownloadData"
            private var isParseSuccessful = false
            override fun onPostExecute(result: ArrayList<FeedEntry>?) {
                super.onPostExecute(result)
                Log.d(TAG,"onPostExecute Returns Nyols")
                if (result != null) {
                    _RSSFeedEntries = result
                  // _RSSMusicViewManager = LinearLayoutManager(this@MainActivity)
                    _RSSMusicViewAdapter = RSSMusicAdapter(_RSSFeedEntries)

                    _RecyclerView = findViewById<RecyclerView>(R.id.recyclerView_ListMain).apply {
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                       // setHasFixedSize(true)

                        // use a linear layout manager
                        layoutManager = _RSSMusicViewManager

                        // specify an viewAdapter (see also next example)
                        adapter = _RSSMusicViewAdapter

                    }

                }



            }

            fun DownloadXML(URLPath: String?): String {
                var XMLResult = StringBuilder()

                val _XMLDataReader = XMLDataReader(URLPath!!, 1024)
                XMLResult = _XMLDataReader.DownloadXMLData()
                //       Log.d(TAG, "$TAG: XML RESULT = $XMLResult")

                return XMLResult.toString()
            }

            override fun doInBackground(vararg url: String?): ArrayList<FeedEntry> {
                var ParsedXMLEntries = ArrayList<FeedEntry>()
                Log.d(TAG, "doInBackground Called With Parameter: ${url[0]}")
                val RSSFeed = DownloadXML(url[0])
                //    Log.d(TAG,"$TAG: RSS FEED = $RSSFeed")
                val _ParseXMLApplications = ParseXMLApplications()

                if(_ParseXMLApplications.Parse(RSSFeed))
                {
                    ParsedXMLEntries = _ParseXMLApplications.getParsedData()
                    isParseSuccessful = true
                }

                return ParsedXMLEntries
            }

        }

}
