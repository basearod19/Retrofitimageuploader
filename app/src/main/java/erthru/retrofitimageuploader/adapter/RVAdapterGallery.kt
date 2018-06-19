package erthru.retrofitimageuploader.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import erthru.retrofitimageuploader.GalleryActivity
import erthru.retrofitimageuploader.R
import erthru.retrofitimageuploader.network.ApiConfig
import erthru.retrofitimageuploader.network.datamodel.Image
import erthru.retrofitimageuploader.network.responsemodel.Gallery
import kotlinx.android.synthetic.main.list_gallery.view.*

class RVAdapterGallery(private val context: GalleryActivity, private val arrayList: ArrayList<Image>) : RecyclerView.Adapter<RVAdapterGallery.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_gallery,parent,false))
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        Glide.with(context).load(ApiConfig.IMAGE_URL+arrayList.get(position).imagename).into(holder.view.imgList)
        holder.view.imgList.setOnClickListener {

            holder.view.frameClickList.visibility = View.VISIBLE

        }

        holder.view.lbNoList.setOnClickListener {

            holder.view.frameClickList.visibility = View.GONE

        }

        holder.view.lbYesList.setOnClickListener {

            context.delete(arrayList.get(position).imageid)

        }

    }

    class Holder(val view:View) : RecyclerView.ViewHolder(view)

}