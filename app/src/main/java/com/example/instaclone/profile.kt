package com.example.instaclone

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.biooo
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File



class profile : Fragment() {
    val PICK_IMAGE = 10;
    val imageRef = Firebase.storage.reference
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    lateinit var itemuid: String
    lateinit var foolName: String
    var cureFile: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            //search garera profile page ma landing vaye pachi
        //for getting data from the recycler and set it to the profile fragment
        val pref = context?.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        if (pref != null) {
            this.itemuid = pref.getString("Unique_key", null).toString()
            this.foolName = pref.getString("Unique_key2", null).toString()

        }
        biooo.text = foolName

        // search gareko user afu nai ho vane edit_profile_button 'edit profile' nai basxa

        if (firebaseUser?.uid.toString() == itemuid) {
            edit_profile_button.text = "edit profile"

        }
        else {
            val ref = FirebaseDatabase.getInstance().reference.child("Follow")
                .child(firebaseUser?.uid.toString())
                .child("Following")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(foolName).exists()) {
                        edit_profile_button?.text = "Following"
                    } else {
                        edit_profile_button?.text = "Follow"
                    }

                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }


        /// yo segment sam search garera click garda hune kura haru thea

         //---------------------------------------------------------------------------//

     // below from here uses own function of profile

        // followed ch chaina check garne

        edit_profile_button.setOnClickListener {

            when (edit_profile_button.text.toString()) {
                "edit profile" -> {
                    startActivity(Intent(context, editprofileActivity::class.java))

                }
                // add follow user
                "Follow" -> {
                    firebaseUser?.uid.let {
                        FirebaseDatabase.getInstance().reference.child("Follow")
                            .child(it.toString())
                            .child("Following").child(foolName).setValue(true)
                            .addOnCompleteListener {

                                edit_profile_button.text = "Following"
                                FirebaseDatabase.getInstance().reference.child("Follow")
                                    .child(foolName).child("Follower")
                                    .child(firebaseUser?.uid.toString()).setValue(true)

                            }
                    }
                }
                //removes the user if do unfollow
                "Following" -> {
                    firebaseUser?.uid.let {
                        FirebaseDatabase.getInstance().getReference("Follow").child(it.toString())
                            .child("Following").child(foolName).removeValue()
                            .addOnCompleteListener {
                                edit_profile_button.text = "Follow"
                                FirebaseDatabase.getInstance().reference.child("Follow")
                                    .child(foolName)
                                    .child("Follower")
                                    .child(firebaseUser?.uid.toString()).removeValue()


                            }
                    }


                }
            }
        }
        // yea sam follow unfollow check garne

        follwerlist()
        followinglist()

        // for changing image
        mutable_image.setOnClickListener {
            Toast.makeText(view.context, "You can choose picture", Toast.LENGTH_LONG).show()
            pickimage()
        }

    }

    // kati jana following chan count garne
    private fun followinglist() {
        val followingRef =
            FirebaseDatabase.getInstance().reference.child("Follow")
                .child(itemuid)
                .child("Following")



        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                view?.following_count?.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
    // kati jana following chan count garne



    // kati jana follower chan count garne

    private fun follwerlist() {


        var followerRef = firebaseUser?.uid.let {
            FirebaseDatabase.getInstance().getReference("Follow").child(foolName)
                .child("Follower")
        }

        followerRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               view?.count_follow?.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    //  // kati jana follower chan count garne


    // yeha bta profile change garne functions

   // firebase or firestore ma store garne photo function
    private fun uploadImagetoStorage(filename:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            cureFile?.let {

                 imageRef.child("images/$filename").putFile(it).await()

                imageRef.child("\"images/$filename\")")
                val localFile = File.createTempFile("tempimage", "jpg")
                imageRef.getFile(localFile).addOnCompleteListener{
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    mutable_image.setImageBitmap(bitmap)
                }.addOnCanceledListener {

                    Toast.makeText(view?.context, "could not uploaded successfully", Toast.LENGTH_LONG).show()

                }



                Toast.makeText(view?.context, "uploaded successfully", Toast.LENGTH_LONG).show()


            }

        }
        catch(e:Exception) {
            withContext(Dispatchers.Main)
            {
                Toast.makeText(view?.context, "Upload successfully", Toast.LENGTH_LONG).show()
            }

        }

    }
    // firebase or firestore ma store garne photo function


//used to open up the gallery
    private fun pickimage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

// pick selected image and put it on the profile
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
          data?.data.let {
              cureFile = it

              mutable_image.setImageURI(data?.data)
          }

        }
        uploadImagetoStorage(cureFile.toString())
    }

}






