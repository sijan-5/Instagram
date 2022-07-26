package com.example.instaclone

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.fragment_profile.*

class addPostActivity : AppCompatActivity() {

    val PICK_IMAGE = 10
    var cureFile: Uri? = null

    private var myUrl = ""
    private var poststorage : StorageReference?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        poststorage = FirebaseStorage.getInstance().reference.child("Posts Pictures")

        save_post.setOnClickListener {
            pickimage()

        }

    }

    override fun onStart() {
        super.onStart()
    }
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

               image_post.setImageURI(data?.data)
            }

        }

    }

}