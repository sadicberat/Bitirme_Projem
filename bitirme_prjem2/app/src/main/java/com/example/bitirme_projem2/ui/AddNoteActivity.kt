package com.example.bitirme_projem2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddNoteActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var tableLayoutNotes: TableLayout
    private lateinit var buttonSaveNote: Button
    private var selectedCourse: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val editTextStudentName = findViewById<EditText>(R.id.editTextStudentName)
        val editTextCourse = findViewById<Spinner>(R.id.spinnerCourse)
        val editTextGrade = findViewById<EditText>(R.id.editTextGrade)
        val editTextExtraInfo = findViewById<EditText>(R.id.editTextExtraInfo)
        val buttonSaveNote = findViewById<Button>(R.id.buttonSaveNote)

        tableLayoutNotes = findViewById(R.id.tableLayoutNotes)
        //buttonSaveNote = findViewById(R.id.buttonSaveNote)

        buttonSaveNote.setOnClickListener {
            //addNewRow()
            addOrUpdateNoteForCourse(
                editTextStudentName.text.toString(),
                editTextCourse.toString(),
                editTextGrade.text.toString(),
                editTextExtraInfo.text.toString())
        }
        initSpinner()
        checkAndAddExistingRows()

    }

    private fun checkAndAddExistingRows() {
        val user = auth.currentUser
        if (user != null) {
            // Firestore'dan kullanıcının notlarını getir
            db.collection("notes")
                .document(user.uid)
                .collection("courses")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        // Firestore'dan gelen her ders için satır ekleyin
                        val courseName = document.id
                        val grade = document.getString("grade") ?: ""
                        val extraInfo = document.getString("extraInfo") ?: ""
                        addOrUpdateNote(user.displayName ?: "", courseName, user.uid, grade, extraInfo)

                        val selectedTableRowId = resources.getIdentifier(courseName, "id", packageName)
                        val selectedTableRow = findViewById<TableRow>(selectedTableRowId)

                        updateRow(selectedTableRow, courseName, grade, extraInfo)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Derslerin getirilmesi başarısız oldu.", exception)
                }
        }
    }

    private fun initSpinner() {
        val spinnerCourse = findViewById<Spinner>(R.id.spinnerCourse)

        // Spinner'a statik verileri eklemek için bir ArrayAdapter kullanabilirsin
        //val courses = arrayOf("Ders 1", "Ders 2", "Ders 3") // Değiştirilecek ders adlarını buraya ekleyebilirsin
        //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinnerCourse.adapter = adapter

        // Kullanıcının seçimini dinlemek için bir OnItemSelectedListener ekleyebilirsin
        spinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Seçilen dersi değişkene ata
                selectedCourse = parent?.getItemAtPosition(position).toString()
                // Diğer işlemleri yapabilirsin
                // Toast.makeText(this@AddNoteActivity, "Seçilen Ders: $selectedCourse", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Kullanıcı hiçbir şey seçmezse bu kısım çalışır
            }
        }
    }

    private fun addOrUpdateNoteForCourse(studentName: String, courseName: String, grade: String, extraInfo: String) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser
        val spinnerCourse = findViewById<Spinner>(R.id.spinnerCourse)

        spinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Seçilen dersi değişkene ata
                selectedCourse = parent?.getItemAtPosition(position).toString()
                // Diğer işlemleri yapabilirsin
                // Toast.makeText(this@AddNoteActivity, "Seçilen Ders: $selectedCourse", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Kullanıcı hiçbir şey seçmezse bu kısım çalışır
            }
        }



        if (user != null) {
            val selectedTableRowId = resources.getIdentifier(selectedCourse, "id", packageName)
            val selectedTableRow = findViewById<TableRow>(selectedTableRowId)

            if (selectedTableRow != null) {
                // Ders adı değişti mi kontrol et
                if (selectedTableRow.tag != selectedCourse) {
                    // Ders adı değişti, yeni satır ekle
                    //addNoteToFirestore(studentName, selectedCourse, user.uid, grade, extraInfo)
                    addOrUpdateNote(studentName, selectedCourse, user.uid, grade, extraInfo)
                    addNewRow(selectedCourse,grade, extraInfo)
                    Toast.makeText(this, "Yeni satır eklendi", Toast.LENGTH_SHORT).show()
                } else {
                    // Ders daha önce kaydedilmiş, satırı güncelle
                    val gradeTextView = getOrCreateTextView(selectedTableRow, 1, grade)
                    val extraInfoTextView = getOrCreateTextView(selectedTableRow, 2, extraInfo)
                    // Güncelleme yap
                    updateRow(selectedTableRow, selectedCourse, grade, extraInfo)
                    // Firestore'da da güncelleme yap
                    //updateNoteInFirestore(selectedCourse, user.uid, grade, extraInfo)
                    addOrUpdateNote(studentName, selectedCourse, user.uid, grade, extraInfo)
                }
            } else {
                // Ders adıyla uyumlu TableRow bulunamadı
                Toast.makeText(this, "Hata: Ders adıyla uyumlu TableRow bulunamadı", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Kullanıcı null ise
            Toast.makeText(this, "Hata: Kullanıcı null", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addOrUpdateNote(studentName: String, courseName: String, userId: String, grade: String, extraInfo: String) {
        val db = FirebaseFirestore.getInstance()

        // Firestore'dan belirli bir dersin bilgilerini getir
        db.collection("notes")
            .document(userId)
            .collection("courses")
            .document(courseName)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                // Ders zaten varsa güncelle, yoksa ekle
                if (documentSnapshot.exists()) {
                    // Ders var, güncelle
                    updateNoteInFirestore(courseName, userId, grade, extraInfo)
                } else {
                    // Ders yok, ekle
                    addNoteToFirestore(studentName, courseName, userId, grade, extraInfo)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Ders kontrol hatası: ${exception.message}", exception)
                Toast.makeText(this, "Ders kontrol hatası: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateNoteInFirestore(courseName: String, userId: String, grade: String, extraInfo: String) {
        val db = FirebaseFirestore.getInstance()

        // Firestore'da notu güncelle
        db.collection("notes")
            .document(userId)
            .collection("courses")
            .document(courseName)
            .update(
                mapOf(
                    "grade" to grade,
                    "extraInfo" to extraInfo
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Not güncellendi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Not güncelleme hatası: ${e.message}", e)
                Toast.makeText(this, "Not güncelleme hatası: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addNoteToFirestore(studentName: String, courseName: String, userId: String, grade: String, extraInfo: String) {
        val db = FirebaseFirestore.getInstance()

        // Firestore'a yeni not ekle
        val note = hashMapOf(
            "userId" to userId,
            "studentName" to studentName,
            "courseName" to courseName,
            "grade" to grade,
            "extraInfo" to extraInfo
        )

        db.collection("notes")
            .document(userId)
            .collection("courses")
            .document(courseName)
            .set(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Not kaydedildi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Not kaydetme hatası: ${e.message}", e)
                Toast.makeText(this, "Not kaydetme hatası: ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }
//denem bitiş

    private fun addNewRow(course: String, grade: String, extraInfo: String) {
        // Tablonun ilk satırını al (başlık satırı olduğunu varsayıyoruz)
        val titleRow = tableLayoutNotes.getChildAt(0) as? TableRow

        // Eğer başlık satırı varsa devam et
        if (titleRow != null) {
            // Tablonun ikinci satırını al (ilk satır başlık olduğu için)
            val existingRow = tableLayoutNotes.getChildAt(1) as? TableRow

            // Eğer ikinci satır varsa, mevcut satırı güncelle
            if (existingRow != null) {
                updateRow(existingRow, course, grade, extraInfo)
            } else {
                // Eğer ikinci satır yoksa, yeni bir satır oluştur ve tabloya ekle
                val newRow = TableRow(this)
                newRow.addView(createTextView(course))
                newRow.addView(createTextView(grade))
                newRow.addView(createTextView(extraInfo))
                tableLayoutNotes.addView(newRow)
            }
        }
    }


    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text.takeIf { it.isNotBlank() } ?: "N/A"
        textView.setPadding(8, 8, 8, 8)
        return textView
    }

    private fun updateRow(existingRow: TableRow, course: String, grade: String, extraInfo: String) {
        // Ders adını gösteren TextView
        val courseTextView = getOrCreateTextView(existingRow, 0, course)
        courseTextView.text = course

        // Notu gösteren TextView
        val gradeTextView = getOrCreateTextView(existingRow, 1, grade)
        gradeTextView.text = grade

        // Ekstra bilgiyi gösteren TextView
        val extraInfoTextView = getOrCreateTextView(existingRow, 2, extraInfo)
        extraInfoTextView.text = extraInfo
    }

    private fun getOrCreateTextView(existingRow: TableRow, index: Int, text: String): TextView {
        val textView: TextView
        if (existingRow.childCount > index && existingRow.getChildAt(index) is TextView) {
            textView = existingRow.getChildAt(index) as TextView
        } else {
            textView = createTextView(text)
            existingRow.addView(textView)
        }
        return textView
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Menü öğelerine tıklama olaylarını ele al
        when (item.itemId) {
            R.id.back -> {
                val intent = Intent(this, MainActivity::class.java)
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

}
