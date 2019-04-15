package com.android.apps.components.content.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.apps.R
import com.android.apps.api.model.event.Event
import com.android.apps.extensions.OnEventItemClickListener
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_video.view.*

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val draweeViewImageThumbnail: SimpleDraweeView = itemView.draweeview_image_thumbnail
    val textViewTitle: TextView = itemView.textview_title
}

class VideoAdapter : RecyclerView.Adapter<VideoViewHolder>() {

    private var events: MutableList<Event> = mutableListOf()
    private var itemClickListener: OnEventItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.adapterPosition.let { events[it] }.let { data ->
            holder.draweeViewImageThumbnail.setImageURI(data.image)
            holder.textViewTitle.text = data.title
            holder.itemView.setOnClickListener { itemClickListener?.invoke(data, holder.adapterPosition) }
        }
    }

    fun registerOnItemClickListener(action: OnEventItemClickListener) {
        itemClickListener = action
    }

    fun add(vararg event: Event) {
        events.addAll(event)
        notifyItemRangeInserted(itemCount - event.size, event.size)
    }
}