package com.example.ediary

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.ediary.models.Memory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*

class MemoriesAdapter(internal var dataSet : List<Memory>) : RecyclerView.Adapter<MemoriesAdapter.ItemViewHolder>  () {

    class ItemViewHolder(val cellview: View) : RecyclerView.ViewHolder(cellview)

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.rv_child_number, parent, false) as View
        val viewHolder = ItemViewHolder(cellView)
        cellView.setOnClickListener {
            //Toast.makeText(parent.context, "I have been clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(parent.context, ViewMemory::class.java)
            val pos = viewHolder.adapterPosition //mn hon
            // if (pos== id)
            val index = dataSet[pos].id //mhl pos 3mlena ha
            intent.putExtra("ITEM_ID", index)//la hon aam bfarji wein hatit mon index

            parent.context.startActivity(intent)
        }

        return viewHolder
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val titleView = holder.cellview.findViewById<TextView>(R.id.MemoryTitle)
        val dateView = holder.cellview.findViewById<TextView>(R.id.Date_memory)
        val imageView = holder.cellview.findViewById<ImageView>(R.id.imageMemory)
        val index = position
        val item = dataSet[index]
        titleView.text = item.title
        dateView.text = item.date
    Picasso.get().load(item.imageURL.toUri()).into(imageView)// to use picasso i added implementation 'com.squareup.picasso:picasso:2.71828' in gradle app
       // RemoteService.getImage(item.imageURL, imageView)
        // /* var bitmap:Bitmap
        //
        //        bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,item.imageURL!!.toUri())
        //        imageView.setImageBitmap(bitmap) //ha l asliye (item.imageURL.toUri()!!).drawToBitmap()
        //        imageView.setImageURI(item.imageURL.toUri())
        //         bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,currentMemory?.imageURL!!.toUri())
        //        */
    }


    override fun getItemCount(): Int = dataSet.size

    fun updateData(list:List<Memory>) {
        dataSet = list
        notifyDataSetChanged()
    }
}
