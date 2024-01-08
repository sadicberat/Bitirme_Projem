package com.example.bitirme_projem2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Seçilen tarihi global olarak saklamak için bir değişken
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val calendar = findViewById<CalendarView>(R.id.calendarView)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendarload = Calendar.getInstance()
            calendarload.set(year, month, dayOfMonth)
            val formatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
            selectedDate = formatter.format(calendarload.time)
            Toast.makeText(this, "$selectedDate", Toast.LENGTH_SHORT).show()
            loadTasksForDate(selectedDate)
        }

        val listViewTasks = findViewById<ListView>(R.id.listViewTasks)
        val addMission: Button = findViewById(R.id.addMission)

        val button_main_geri: Button = findViewById(R.id.button_main_geri)
        button_main_geri.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        addMission.setOnClickListener {
            // Eğer bir tarih seçilmediyse kullanıcıyı uyarabilirsiniz
            if (selectedDate.isNullOrEmpty()) {
                Toast.makeText(this, "Lütfen bir tarih seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskNameEditText: EditText = findViewById(R.id.taskNameEditText)
            val taskDescriptionEditText: EditText = findViewById(R.id.taskDescriptionEditText)
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()

            // Seçilen tarihle görev ekleme
            addTaskForDate(taskName, taskDescription)
        }
    }

    private fun addTaskForDate(taskName: String, taskDescription: String) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // Firestore'ya görev ekleme
            val task = hashMapOf(
                "userId" to user.uid,
                "date" to selectedDate,
                "taskName" to taskName,
                "taskDescription" to taskDescription
            )
            db.collection("tasks")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "görev kayıt edildi", Toast.LENGTH_SHORT).show()
                    loadTasksForDate(selectedDate)
                }
                .addOnFailureListener { e ->
                    // Hata durumunda yapılacak işlemler
                    Log.e("Firestore", "Error adding task: ${e.message}", e)
                }
        }
    }

    private fun loadTasksForDate(selectedDate: String?) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user != null && !selectedDate.isNullOrEmpty()) {
            // Firestore sorgusu ile tarihe göre filtreleme yapılacak
            db.collection("tasks")
                .whereEqualTo("userId", user.uid)
                .whereEqualTo("date", selectedDate)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    // Firestore'dan çekilen verileri işleme
                    val listViewChanges = mutableListOf<String>()

                    for (document in documents) {
                        val taskName = document.getString("taskName")
                        val taskDescription = document.getString("taskDescription")
                        listViewChanges.add("$taskName - $taskDescription")
                    }

                    // Görev listesini ListView içinde göstermek için bir adapter kullanın
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, listViewChanges)
                    val listViewTasks = findViewById<ListView>(R.id.listViewTasks)
                    listViewTasks.adapter = adapter
                }
                .addOnFailureListener { e ->
                    // Hata durumunda yapılacak işlemler
                    Log.e("Firestore", "Error fetching tasks for date: $selectedDate", e)
                    runOnUiThread {
                        Toast.makeText(this, "Veri çekme hatası: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Menü öğelerine tıklama olaylarını ele al
        when (item.itemId) {
            R.id.back -> {
                // Oturumu kapat
                FirebaseAuth.getInstance().signOut()

                // LoginActivity'e yönlendir
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Aktiviteyi kapat
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
}
