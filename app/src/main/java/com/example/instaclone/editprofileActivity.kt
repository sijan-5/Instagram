package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_editprofile.bio
import kotlinx.android.synthetic.main.fragment_profile.*

class editprofileActivity : AppCompatActivity() {
    lateinit var firebaseReference: DatabaseReference
    lateinit var iamuser: FirebaseUser
    lateinit var pro:profile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)


        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@editprofileActivity, loginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        userInfo()
        save_info.setOnClickListener {
            work()
        }

    }


      // function to store the edited name to the firebase
    private fun work() {
        var  userMap = HashMap<String, Any>()

        //hashmap stores key value pair or arraylist also can also be used
        //to read and write data we use DatabaseReference with child

        fullname.text.let {

            userMap["fullname"] = fullname.text.toString()
            firebaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(iamuser.uid)

            firebaseReference.updateChildren(userMap)


           startActivity(Intent(this, MainActivity::class.java))

        }


    }
     //edited data to settext on the activity taneko from firebase
    private fun userInfo() {

        iamuser = FirebaseAuth.getInstance().currentUser!!
       firebaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(iamuser.uid)


        firebaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //all the data under last child is stored in snapshot so no need loop
                // last child vitra data matra ch vane no need of loop

                if (snapshot.exists())
                {
                    val dataobject = snapshot.getValue(DataContainer::class.java)
                    fullname.setText(dataobject?.fullname.toString())
                    username.setText(dataobject?.email.toString())

                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}