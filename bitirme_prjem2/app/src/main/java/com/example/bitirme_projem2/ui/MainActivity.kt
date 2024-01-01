package com.example.bitirme_projem2.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)





        val button_main_geri: Button = this.findViewById(R.id.button_main_geri)
        button_main_geri.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       menuInflater.inflate(R.menu.main_menu,menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Menü öğelerine tıklama olaylarını ele al

        when (item.itemId) {
            R.id.item_1 -> {
                // item_1'e tıklandığında yapılacak işlemler
                return true
            }
            R.id.profile_menu_botton -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)


                return true
            }
            R.id.item_3 -> {
                // item_3'e tıklandığında yapılacak işlemler
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
}