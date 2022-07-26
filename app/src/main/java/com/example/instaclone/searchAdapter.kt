package com.example.instaclone

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class SearchAdapter(val array: ArrayList<DataContainer>,private var mcontext:Context) :RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {


     val dataContainer = DataContainer()
     val obj = profile()



    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

     class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val name: TextView = view.findViewById(R.id.show_name)
         val id: TextView = view.findViewById(R.id.put_id)
         val dp: ImageView = view.findViewById(R.id.profile_image)
         //val followbutton: Button = view.findViewById(R.id.followButton)


     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.forrecyclerview, parent, false)

         return MyViewHolder(view)
     }
     override fun getItemCount(): Int {
         return array.size
     }

     @SuppressLint("SetTextI18n", "ResourceType", "CommitPrefEdits")
     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


         val items = array[position]
         holder.name.text = items.fullname
         holder.id.text = items.email
         Picasso.get().load(dataContainer.image).placeholder(R.drawable.tom).into(holder.dp)

         holder.itemView.setOnClickListener(object :View.OnClickListener{
             override fun onClick(v: View?) {
                 val sharedPreferences = mcontext.getSharedPreferences("Prefs",
                     Context.MODE_PRIVATE).edit()

                 sharedPreferences.putString("Unique_key", items.uid)
                 sharedPreferences.putString("Unique_key2", items.fullname)
                 sharedPreferences.apply()


               val activity = v!!.context as AppCompatActivity
                 activity.supportFragmentManager.beginTransaction().replace(R.id.flFragment,profile())
                     .addToBackStack(null).commit()
             }


         })

         }


     }



