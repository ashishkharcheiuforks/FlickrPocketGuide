package com.piotrek1543.example.flickrpocketguide.ui.photos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.piotrek1543.example.flickrpocketguide.Constants
import com.piotrek1543.example.flickrpocketguide.R
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.model.PhotoModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_photo.view.*
import timber.log.Timber

class PhotosAdapter(
    var items: List<PhotoEntity> = listOf(),
    private val context: Context
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_photo,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = items[position]
        val photoUrl = getPhotoUrl(photo.id!!, photo.secret!!)
        Timber.d("photossssss $photoUrl")
        Picasso.get().load(photoUrl).into(holder.photoTv)
    }

    private fun getPhotoUrl(id: String, secret: String) = "${Constants.PHOTO_URL}/${id}_$secret.jpg}"

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoTv: AppCompatImageView = view.image_photo
    }
}