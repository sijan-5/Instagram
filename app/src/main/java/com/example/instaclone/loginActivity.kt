package com.example.instaclone

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signupButton.setOnClickListener{
            startActivity(Intent(this@loginActivity,registerActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            when{
                TextUtils.isEmpty(inputEmail.text.toString().trim { it <= ' '}) ->{
                    Toast.makeText(this@loginActivity, "please enter Email", Toast.LENGTH_SHORT)
                        .show()
                }

                TextUtils.isEmpty(inputPassword.text.toString().trim { it<=' '})->
                {
                    Toast.makeText(this@loginActivity, "please enter Password", Toast.LENGTH_SHORT)
                        .show()
                }
                else->{
                    val email = inputEmail.text.toString().trim { it<=' '}
                    val password = inputPassword.text.toString().trim { it<=' '}
                    val progressDialog = ProgressDialog(this@loginActivity)
                    progressDialog.setTitle("Logging In")
                    progressDialog.setMessage("please wait,this may take a while")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                         progressDialog.dismiss()
                            Toast.makeText(this@loginActivity, "You are logged in successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(
                                this@loginActivity,
                                MainActivity::class.java)

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            Toast.makeText(
                                this@loginActivity,
                                task.exception!!.message.toString(), Toast.LENGTH_LONG
                            ).show()
                            //progressDialog.dismiss()
                            FirebaseAuth.getInstance().signOut()
                        }
                    }
                }

            }
        }
    }
//check if the login user has already data in firebase or not
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            val intent = Intent(
                this@loginActivity,
                MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}