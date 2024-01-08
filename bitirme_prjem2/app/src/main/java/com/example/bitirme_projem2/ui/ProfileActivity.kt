package com.example.bitirme_projem2.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {

    private lateinit var lName:TextView
   // private lateinit var name: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val auth = FirebaseAuth.getInstance()



       // name = findViewById(R.id.profile_userName)
        lName = findViewById(R.id.profile_userLName)

        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        fetchUserData()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Menü öğelerine tıklama olaylarını ele al

        when (item.itemId) {
            R.id.back -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.profile_menu_botton -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)


                return true
            }
            R.id.addNote -> {
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.subitem1 -> {
                // subitem1'e tıklandığında yapılacak işlemler (item_3 altındaki öğe)
                return true
            }
            R.id.subitem2 -> {
                // subitem2'ye tıklandığında yapılacak işlemler (item_3 altındaki öğe)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun fetchUserData() {
        val auth =FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val email = dataSnapshot.child("email").getValue(String::class.java)
                        val name = dataSnapshot.child("name").getValue(String::class.java)
                        val lName = dataSnapshot.child("lastName").getValue(String::class.java)

                        // Kullanıcı verilerini kullanarak yapılacak işlemleri burada gerçekleştirin

                        // Örneğin, kullanıcı adını ve soyadını bir TextView'e yerleştirme
                        val userName: TextView = findViewById(R.id.profile_userName)
                        userName.text = name
                        val userLName:TextView = findViewById(R.id.profile_userLName)
                        userLName.text = lName

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Hata durumu
                    Toast.makeText(this@ProfileActivity, "Veri çekme hatası: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



}