package com.example.ediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        btn_signup2.setOnClickListener{
        signUpUser()
        }
    }
     private fun signUpUser()
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
        if(password1.text.toString().isEmpty()) // if the user didnt type anything in password put this error
        {
            password1.error="Please add a password"
            password1.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(Username.text.toString(), password1.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent( this, MainActivity::class.java))
                                finish()
                            }

                        }
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed , please try again later  ",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }
}
