package com.example.ediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        btn_signup1.setOnClickListener {
            startActivity(Intent(this, SignUpActivity ::class.java))
            finish()
        }
        LogInButton.setOnClickListener{
            doLogin()
        }

    }

    private fun doLogin()
    {
        if(Username.text.toString().isEmpty()) // if the user didnt type anything in email put this error
        {
             Username.error="Please enter email"
            Username.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Username.text.toString()).matches()) // if the user added an unvalid email adress
        {
            Username.error="Please enter a valid email"
           Username.requestFocus()
            return
        }
        if(Password.text.toString().isEmpty()) // if the user didnt type anything in password put this error
        {
           Password.error="Please add a password"
            Password.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(Username.text.toString(), Password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    // to check if user is already logged in or not if their is they will give me the details of current user if not they will return null
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser:FirebaseUser?)
    {
     if (currentUser != null)
     {
         if (currentUser.isEmailVerified)

         {
             startActivity( Intent(this ,MemoryActivity::class.java))
             finish()
         }
         else
         {
             Toast.makeText(baseContext, "Please verify your email adress.",
                 Toast.LENGTH_SHORT).show()
         }
     }
        else
     {
         Toast.makeText(baseContext, " Please Login.",
             Toast.LENGTH_SHORT).show()
     }
    }
}
