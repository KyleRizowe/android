package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.request.GunZone
import com.fuel4media.carrythistoo.utils.GlideUtil
import kotlinx.android.synthetic.main.row_gun_zone.view.*

class GunZoneAdapter(val context: Context, val gunZones: ArrayList<GunZone>, val listener: GunZoneAdapter.GunZoneClickListener) : RecyclerView.Adapter<GunZoneAdapter.ViewHolder>() {

    interface GunZoneClickListener {
        fun onGunZoneClick(index: Int, event: GunZone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_gun_zone, parent, false)

        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gunZone = this.gunZones[position]

        holder.tv_name.text = gunZone.name
        holder.tv_type.text = UserManager.getInstance().getGunZoneName(gunZone.type.toString())
        holder.tv_type.setBackgroundColor(context.resources.getColor(R.color.colorGray))
        holder.tv_distance.text = "" + gunZone.distance.toString() + " km away"
        holder.tv_address.text = gunZone.address


        GlideUtil.loadImage(context!!, holder.ivGunZone, gunZone.icon)
        ImageViewCompat.setImageTintList(holder.ivGunZone, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTextBlack)));
        holder.ivGunZone.background.setTint(context.resources.getColor(R.color.colorGray))

    }

    override fun getItemCount(): Int {
        return gunZones.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGunZone = view!!.iv_gun_zone
        val tv_name = view!!.tv_gun_zone_name
        val tv_type = view!!.tv_zone_type
        val tv_distance = view!!.tv_zone_distance
        val tv_address = view!!.tv_address

        init {
            view!!.setOnClickListener {
                listener.onGunZoneClick(adapterPosition, gunZones.get(adapterPosition))
            }
        }

    }
}