package com.learnprogramming.academy.rssfeedapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 *  Adapter Class For iTunes RSS Feed Data To Be Used With A RecyclerView
 */
class RSSMusicAdapter(private val DataSet : ArrayList<FeedEntry>) : RecyclerView.Adapter<RSSMusicAdapter.RSSMusicViewHolder>() {

    class RSSMusicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        // Declaration
        var _albumart : ImageView? = null
        var _title: TextView? = null
        var _artist: TextView? = null

        // Initialisation
        init {
            _albumart = itemView?.findViewById(R.id.imgView_listItem_AlbumArt)
            _title = itemView?.findViewById(R.id.txtView_listItem_Title)
            _artist = itemView?.findViewById(R.id.txtView_listItem_Artist)
        }
    }
    /**
     *  When Creating The ViewHolder We Are Looking To Do Mainly The Following
     *  Create A View Of A ListItem To Be Used In The RecyclerView.
     *  This Is Achieved By " Inflating " The View For A Specific Item
     *
     *  // Create new views (invoked by the layout manager)

     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSSMusicViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_2,parent,false)
        return RSSMusicViewHolder(itemView)
    }

    /**
     *  Returns The Total Count Of Items Available In Your DataSet
     */

    override fun getItemCount(): Int {
      return DataSet.size
    }

    /**
     *  // Replace the contents of a view (invoked by the layout manager)
     */

    override fun onBindViewHolder(holder: RSSMusicViewHolder, position: Int) {

        Picasso.get().load(DataSet[position].Image).into(holder._albumart)
        holder._title?.text = DataSet[position].Title
        holder._artist?.text = DataSet[position].Artist

    }



}