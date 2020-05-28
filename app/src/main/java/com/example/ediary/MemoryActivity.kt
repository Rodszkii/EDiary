package com.example.ediary

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ediary.models.MemoriesStore
import com.example.ediary.models.Memory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_memory.*
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import java.util.Calendar


class MemoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    public var recyclerAdapter:MemoriesAdapter?= null
    private lateinit var MemoriesList:List<Memory>

  /*  lateinit var notificationManager:NotificationManager
    lateinit var notificationchannel:NotificationChannel
    lateinit var builder :Notification.Builder
    private val channelId="com.example.ediary"
    private val desc="Daily Notifications"*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)
        setSupportActionBar(findViewById(R.id.toolbar))
        auth = FirebaseAuth.getInstance()
        val ct = this
        MemoriesList = MemoriesStore.GetList()

        recyclerAdapter = MemoriesAdapter(MemoriesList)
        var recyclerView = findViewById<RecyclerView>(R.id.memoryGrid)
        var recyclerLayout = GridLayoutManager(this, 1)
        recyclerView!!.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
          //  adapter!!.notifyDataSetChanged()
            //notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }


            btn_addMemory.setOnClickListener{GoToAddActivity()}
      /*  var swipelayout:SwipeRefreshLayout=findViewById(R.id.swipe)
        swipelayout.setColorSchemeResources(R.color.ic_launcher_background)
        swipelayout.setOnRefreshListener{

            val intent = Intent(this, MemoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
            swipelayout.isRefreshing=false
        }*/



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu,menu)

        val searchItem = menu.findItem(R.id.item1)
        val searchView = searchItem?.actionView as SearchView

        val ctl = this
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filterItemsByTitle(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(ctl,"Text Submitted"+query, Toast.LENGTH_SHORT).show()
                return false
            }
        })
            return true
    }

   override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)  {
        R.id.logOut -> {
           SignOut()
            true
        }
       R.id.addImgmenu ->
       {
           Toast.makeText(this,"Been Clicked", Toast.LENGTH_LONG).show()
           true
       }
       R.id.item1 ->{
           true
       }
       else -> {
               // If we got here, the user's action was not recognized.
               // Invoke the superclass to handle it.
               super.onOptionsItemSelected(item)
       }
    }

    private fun filterItemsByTitle(title: String) {
        MemoriesList = MemoriesStore.SearchName(title)
        recyclerAdapter!!.updateData(MemoriesList)
    }
    fun GoToAddActivity()
    {
        val intent = Intent(this, AddMemory::class.java)
        startActivity(intent)
    }

    fun SignOut()
    {
        auth.signOut()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
   /* fun Notification()
    {
        val intent=Intent(this,LauncherActivity::class.java)
        val pendingintent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationchannel= NotificationChannel(channelId,desc,NotificationManager.IMPORTANCE_HIGH)
            notificationchannel.enableLights(true)
            notificationchannel.lightColor=Color.GREEN
            notificationchannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationchannel)

            builder= Notification.Builder(this,channelId)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.splashscreen_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splashscreen_foreground))
                .setContentIntent(pendingintent)

        }
        else{
            builder= Notification.Builder(this)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.splashscreen_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.splashscreen_foreground))
                .setContentIntent(pendingintent)
        }
        notificationManager.notify(1234,builder.build())
    }*/

}
