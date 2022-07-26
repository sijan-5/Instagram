package com.example.instaclone

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class registerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        alreadyAccount.setOnClickListener {
            startActivity(Intent(this@registerActivity, loginActivity::class.java))

        }
        registerButton.setOnClickListener {

            when {
                TextUtils.isEmpty(fname.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@registerActivity,
                        "please enter full name",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(uname.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@registerActivity,
                        "please enter user name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(registerEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@registerActivity, "please enter Email", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(registerpassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@registerActivity,
                        "please enter Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val progressDialog = ProgressDialog(this@registerActivity)
                    progressDialog.setTitle("Please Sign up")
                    progressDialog.setMessage("please wait,this may take a while")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()
                    val fullname = fname.text.toString().trim { it <= ' ' }
                    val username = uname.text.toString().trim { it <= ' ' }
                    val email = registerEmail.text.toString().trim { it <= ' ' }
                    val password = registerpassword.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {

                                    saveUserInfo(fullname,username,email,progressDialog)
                                    //progressDialog.dismiss()

                                }
                                else
                                {
                                    Toast.makeText(
                                        this@registerActivity,
                                        task.exception!!.message.toString(), Toast.LENGTH_LONG
                                    ).show()
                                    //progressDialog.dismiss()
                                    FirebaseAuth.getInstance().signOut()
                                }

                            })

                }
            }


        }
    }

    private fun saveUserInfo(fullname: String, username: String, email: String,progressDialog:ProgressDialog) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        var  userMap = HashMap<String, Any>()

        //hashmap stores key value pair or arraylist also can also be used
        //to read and write data we use DatabaseReference with child
        userMap["username"] = username.lowercase()
        userMap["uid"] = currentUserId
        userMap["fullname"] = fullname.lowercase()
        userMap["email"] = email
        userMap["bio"] = "hey i am sijan and welcome to my clone app"
        userMap["image"]= "gs://instagram-clone-180ba.appspot.com/Default Image/profile.png"
        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@registerActivity, "You are Registered up  successfully", Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(
                        this@registerActivity,
                        MainActivity::class.java)

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                }else
                {
                    Toast.makeText(
                        this@registerActivity,
                        task.exception!!.message.toString(), Toast.LENGTH_LONG
                    ).show()
                    //progressDialog.dismiss()
                    FirebaseAuth.getInstance().signOut()

                }
            }

                }

            }


