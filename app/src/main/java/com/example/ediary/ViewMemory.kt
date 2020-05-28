package com.example.ediary

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import com.example.ediary.models.MemoriesStore
import com.example.ediary.models.Memory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ViewMemory : AppCompatActivity() {
    var currentFirebaseUser: String = FirebaseAuth.getInstance().currentUser!!.uid
    var currentMemory: Memory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_memory)
        setSupportActionBar(findViewById(R.id.toolbar3))

        val index = intent.getIntExtra("ITEM_ID",0)
        currentMemory = MemoriesStore.GetList()[index-1]
        val titlememo = findViewById<TextView>(R.id.add_title)
        titlememo.setText(currentMemory?.title)

        val datememo = findViewById<TextView>(R.id.tv_date)
        datememo.setText(currentMemory?.date.toString())

       val description = findViewById<TextView>(R.id.tv_description)
        description.setText(currentMemory?.description)
       //
        //val bitmap:Bitmap
      //  bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,currentMemory?.imageURL!!.toUri())
        val image = findViewById<ImageView>(R.id.pic_memory).also {
                Picasso.get().load(currentMemory?.imageURL).into(it)
           // it.setImageBitmap(bitmap)
           // it.setImageURI(currentMemory?.imageURL!!.toUri())

        }
        Toast.makeText(this,currentMemory?.IDdb!!, Toast.LENGTH_LONG).show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.remove_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)  {

        R.id.removeMemory ->
        {
            MemoriesStore.removeItem(currentMemory!!.id-1)
            //val intent = Intent(this, MemoryActivity::class.java)
            //startActivity(intent)
            val ref =FirebaseDatabase.getInstance().getReference().child(currentFirebaseUser).child("Memories")
            ref.child(currentMemory?.IDdb!!).removeValue()
            finish()
            true
        }
        else -> {

            super.onOptionsItemSelected(item)
        }
    }
}
