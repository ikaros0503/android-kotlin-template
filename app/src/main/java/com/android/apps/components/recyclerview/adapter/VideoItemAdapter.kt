package com.android.apps.components.recyclerview.adapter

import android.content.Intent
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.apps.R
import com.android.apps.content.FBVideoContent
import com.android.apps.extensions.px
import kotlinx.android.synthetic.main.item_video.view.*
import java.io.File


class VideoItemAdapter : RecyclerView.Adapter<VideoItemAdapter.VideoItemViewHolder>() {

    private var items: MutableList<FBVideoContent> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        return VideoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        val content = items[adapterPosition]
        holder.itemView.text_video_title.text = content.getTitleFromURL()
        val thumb = ThumbnailUtils.createVideoThumbnail(content.url,
                MediaStore.Images.Thumbnails.MINI_KIND)
        holder.itemView.image_video_thumbnail.setImageBitmap(thumb)
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content.url))
            intent.setDataAndType(Uri.parse(content.url), "video/mp4")
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.image_button_more.setOnClickListener { onMoreButtonPressed(it, adapterPosition) }
    }

    private fun onMoreButtonPressed(view: View, position: Int) {
        val content = items[position]
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_item_video, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { handleOnMenuItemClick(view, it, content) }
        popupMenu.show()
    }

    private fun handleOnMenuItemClick(view: View, menuItem: MenuItem, content: FBVideoContent): Boolean {
        when (menuItem.itemId) {
            R.id.menu_action_rename -> {
                showDialogRename(view, content)
            }
            R.id.menu_action_delete -> {
                removeContent(content)
            }
        }
        return true
    }

    private fun removeContent(content: FBVideoContent) {
        File(content.url).takeIf { it.exists() }?.delete()
        val position = items.indexOf(content)
        items.remove(content)
        notifyItemRemoved(position)
    }

    private fun showDialogRename(anchorView: View, content: FBVideoContent) {
        val editText = EditText(anchorView.context)
        editText.hint = "Video name"
        editText.setText(content.getTitleFromURL())
        AlertDialog.Builder(anchorView.context)
                .setTitle("Rename video")
                .setMessage("Enter video's new name")
                .setPositiveButton("Rename") { _, _ ->
                    val newName = editText.text.toString().let {
                        if (!it.endsWith(".mp4")) it.plus(".mp4")
                        else it
                    }
                    renameContent(content, newName)
                }
                .setNegativeButton(android.R.string.cancel) { di, _ ->
                    di.dismiss()
                }
                .setView(editText)
                .show()
        (editText.layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(20.px.toInt(), 0, 20.px.toInt(), 0)
            editText.requestLayout()
        }
    }

    fun renameContent(content: FBVideoContent, newName: String) {
        File(content.url).takeIf { it.exists() }?.let { old ->
            val newDest = File(old.parent, newName)
            old.renameTo(newDest)
            val index = items.indexOf(content)
            items.set(index, FBVideoContent("", newDest.absolutePath))
            notifyItemChanged(index)
        }
    }

    fun addContent(fbVideoContent: FBVideoContent) {
        items.add(fbVideoContent)
    }

    fun clear() {
        items.clear()
    }

    class VideoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}