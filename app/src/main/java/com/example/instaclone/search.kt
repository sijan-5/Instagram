package com.example.instaclone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*


class search : Fragment() {

    val array = ArrayList<DataContainer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recycler_view_search.layoutManager = LinearLayoutManager(activity)
        recycler_view_search.setHasFixedSize(true)
        searchtext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               val khojnuparne:String = searchtext.text.toString()

                if(khojnuparne=="")
                {
                    recycler_view_search.visibility = View.INVISIBLE
                    array.clear()
                }
                else
                {
                    array.clear()
                    recycler_view_search.visibility = View.VISIBLE
                    searchUser(khojnuparne)
                }
            }

        })

    }

    private fun searchUser(khojnuparne: String) {

        val database = FirebaseDatabase.getInstance().getReference("Users")
            .orderByChild("fullname").startAt(khojnuparne)
            .endAt("$khojnuparne\uf8ff")


        //add listner gives the call back when there is change is value of firebase
        //also gives the value that are within the node and right now that node if referenced by database
        // so gives the value under that node
        database.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}



            // this ondata change contain all the data that are under the node
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists())
                {
                    for (usersnapshot in snapshot.children)
                    {
                        val user = usersnapshot.getValue(DataContainer::class.java)
                        array.add(user!!)
                    }
                }
                recycler_view_search.adapter = SearchAdapter(array,context!!)

            }

        })


   }

}
