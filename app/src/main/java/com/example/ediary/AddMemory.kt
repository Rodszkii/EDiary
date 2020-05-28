package com.example.ediary

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView

import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ediary.models.MemoriesStore
import com.example.ediary.models.Memory
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_memory.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private lateinit var MemoriesList:List<Memory>

class AddMemory : AppCompatActivity() {
    val IMAGE_REQUEST_CODE=24
    val Permition_REQUEST_CODE=75
    val speach_REQUEST_CODE=25

   // var usersReference:DatabaseReference?=null
var storage:FirebaseStorage?=null// i added implementation 'com.google.firebase:firebase-storage:17.0.0' 16.0.5 gives an error to gradle module app to be able to use FirebaseStorage
    var photoUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memory)
        setSupportActionBar(findViewById(R.id.toolbar2))
       MemoriesList = MemoriesStore.GetList()
        //
        btn_voice.setOnClickListener{
            speak()
        }



        //
        btn_saveMemory.setOnClickListener{SaveData()}
        //initiate Storage
       storage= FirebaseStorage.getInstance()
       // usersReference = FirebaseDatabase.getInstance().reference.child("user").child(firebaseuser!!.uid)
    }

    private fun speak() {
        val mIntent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())//.getDefault()
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"hello please narrate your memory")
        if(intent.resolveActivity(getPackageManager())!=null)
        {
        startActivityForResult(mIntent,speach_REQUEST_CODE)
        }
        else   //catch (e:Exception)
        {
        Toast.makeText(this,"their was an error",Toast.LENGTH_SHORT).show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }
        override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)  {

       R.id.addImgmenu ->
       {
           Toast.makeText(this,"Please select Image", Toast.LENGTH_LONG).show()

           //get permistion for storage
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
         {
             if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
             {
                val permissions= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                 requestPermissions(permissions,Permition_REQUEST_CODE)
             }
             else
             {
                 pickPhotoFromGallery()
             }
         }
           else
         {
            pickPhotoFromGallery()
         }


           true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    public fun SaveData()
    {
        var id: Int
        var title: String
        var description: String
        var date :String
        var imageURL:ImageView // String
        var lastMemory:Int

        lastMemory = MemoriesList.size
        id=lastMemory+1
        title= add_title.text.toString()
        if (title.trim().isEmpty())
        {
            add_title.error="Please add a title"
            return
        }
        date=add_date.text.toString()
        if (date.trim().isEmpty())
        {
            add_date.error="Please add a date"
            return
        }
        description=add_description2.text.toString()
        if (description.trim().isEmpty())
        {
            add_description2.error="Please add a description "
            return
        }
        // imageURL=iv_addImage //.toString() //

        val MemoriesID= MemoriesStore.ref.push().key.toString()
        //make filename
        var timestamp=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())//I added time stamp to try to make the photo unique
        var imageFileName="Image_"+timestamp+"_.jpg"
       var storageRef =storage?.reference?.child("images")?.child(imageFileName) //file upload/*  storageRef?.putFile(photoUri!!) */
        var uploadTask:StorageTask<*>
        uploadTask=storageRef!!.putFile(photoUri!!)
        uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot,Task<Uri>>{ task->
            if (!task.isSuccessful)
            {
                task.exception?.let {
                    throw it
                }

            }
            return@Continuation storageRef.downloadUrl
        }).addOnCompleteListener{task->
            if (task.isSuccessful)
            {
                val downloadurl =task.result
                val url=downloadurl.toString()
                val Memoryx = Memory(MemoriesID,id,title,description ,date, url)//mhl photouri imageURL
                MemoriesStore.ref.child(MemoriesID).setValue(Memoryx)//lesim tkoun Memoryx bas bade zabit l images

               // MemoriesStore.addItem(Memoryx)
                val mapMemoImg=HashMap<String,Any>()
                mapMemoImg["Memory"]=url
               // usersReference!!.updateChildren(mapMemoImg)
               // val intent = Intent(this, MemoryActivity::class.java)
                //startActivity(intent)
                finish()
            }
        }

    }
private fun pickPhotoFromGallery()
{
   val intent=Intent(Intent.ACTION_PICK)
  intent.type="image/*"
    startActivityForResult(intent,IMAGE_REQUEST_CODE)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if(resultCode== Activity.RESULT_OK&& requestCode==IMAGE_REQUEST_CODE)
        {
            photoUri=data?.data
            iv_addImage.setImageURI(photoUri)
        }
      if(requestCode==speach_REQUEST_CODE) {
                     var result:ArrayList<String>
                     result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    var texttospeach:String= result.get(0)
                    add_description2.setText(texttospeach)
                    //String newString = userName1.getText().toString();
                   // Editable newTxt=(Editable)userName1.getText();
        }

        else{
            finish()
        }*/
        when(requestCode){
            IMAGE_REQUEST_CODE->{photoUri=data?.data
            iv_addImage.setImageURI(photoUri)}
            speach_REQUEST_CODE->{var result:ArrayList<String>
                result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var texttospeach:String= result.get(0)
                add_description2.setText(texttospeach)}

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permition_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickPhotoFromGallery()
                } else {
                    Toast.makeText(this, " PERMITION DENIED", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    }

