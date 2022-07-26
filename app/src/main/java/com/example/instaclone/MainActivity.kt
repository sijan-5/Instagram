package com.example.instaclone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class MainActivity : AppCompatActivity() {




    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFrag = HomeFragment()
        val profilefrag = profile()

        val notificationFrag = notification_fragment()
        val searchFragment = search()



        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,homeFrag)
            addToBackStack(null)
            commit()


            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    //R.id.nav_search ->startActivity(Intent(this@MainActivity,searchActivity::class.java))
                    R.id.nav_home -> {
                        setCurrentFragment(homeFrag)
                        finish()
                    }
                    R.id.nav_profile -> {
                        setCurrentFragment(profilefrag)
                        finish()
                    }

                    R.id.nav_add ->
                    {
                        it.isChecked = false
                        startActivity(Intent(this@MainActivity,addPostActivity::class.java))
                        finish()

                    }

                    R.id.nav_heart -> {
                        setCurrentFragment(notificationFrag)
                        finish()
                    }
                    R.id.nav_search -> {
                        setCurrentFragment(searchFragment)
                        finish()
                    }

                }
                true
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        Toast.makeText(this,"$title",Toast.LENGTH_LONG).show()
    }

    private fun setCurrentFragment(changeFragment:Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, changeFragment)
            addToBackStack(null)
            commit()

        }
    }
}
