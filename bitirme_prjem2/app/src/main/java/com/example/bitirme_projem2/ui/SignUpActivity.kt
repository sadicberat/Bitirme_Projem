package com.example.bitirme_projem2.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var userMail: TextView
    private lateinit var userPassword: TextView
    private lateinit var userName: TextView
    private lateinit var userLastName: TextView
    private lateinit var register: Button

    private lateinit var auth: FirebaseAuth
  //  private lateinit var database: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_signup)

        val database = FirebaseDatabase.getInstance()
        //val database = FirebaseDatabase.getInstance()
        val reference = database.reference
        FirebaseApp.initializeApp(this)



        userName = findViewById(R.id.signUp_name)
        userLastName = findViewById(R.id.signUp_secondName)
        //val userLastName = findViewById<TextView>(R.id.signUp_secondName)

        userMail = findViewById(R.id.signUp_mail)
        userPassword = findViewById(R.id.signUp_password)
        register = findViewById(R.id.button_signUp)

        auth = FirebaseAuth.getInstance()

        val name = userMail.text.toString().trim()
        var password = userPassword.text.toString().trim()

        register.setOnClickListener {
            onRegisterButtonClick(it)



        }
    /*
        fun onActivityCreated(savedInstanceState: Bundle?){

            onActivityCreated(savedInstanceState)

            register.setOnClickListener{
                if (name.isEmpty()){
                    // = getString(R.string.error_field_required)
                }
            }
        }
*/
        val buttonGeriSignUp: Button = findViewById(R.id.button_geri_signUp)
        buttonGeriSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
       // getInstance().reference.child("ilk child").child("ikinci child").setValue("set value")
    }
    private fun onRegisterButtonClick(view: View) {
        val email = userMail.text.toString()
        val password = userPassword.text.toString()
        val name = userName.text.toString()
        val lastName = userLastName.text.toString()


        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        signUp(email, password)



    }

    private fun saveUserData() {
        val user = auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            val userData = mapOf(
               // "name" to userName.text.toString(), // Burada kullanıcı adını almalısınız
                "Uıd" to user.uid,
                "email" to user.email,
                "lastName" to userLastName.text.toString(),
                "name" to userName.text.toString()
                // Diğer kullanıcı verilerini ekleyebilirsiniz
            )
            val database = FirebaseDatabase.getInstance()
            //database.reference.child("users").child(userId).setValue(userData)
            userId.let {
                //database.reference.child("users").child(it).setValue(userData)
                database.reference.child("users").child(userId).setValue(userData)
            }
        }
    }



    /* private fun saveUserData(userId: String?, displayName: String, additionalData: String) {
        val userData = mapOf(
            "displayName" to displayName,
            "additionalData" to additionalData
        )

        // Realtime Database kullanımı
        userId?.let {
            database.child("users").child(it).setValue(userData)
        }
    }*/
    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılıysa yapılacak işlemler
                    Toast.makeText(this, "kayıt oluşturuldu giriş yapılıyor", Toast.LENGTH_SHORT).show()

                    //val userLastName = R.id.signUp_secondName
                    //val user = auth.currentUser


                    saveUserData()
                    startMainActivity()
                } else {
                    // Giriş başarısızsa kullanıcıya uyarı ver
                    Toast.makeText(this, "Giriş Başarısız. Kullanıcı adı veya şifre kısımlarını doğru doldurun", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
       finish()
    }
    companion object {
        private const val TAG = "SignUpActivity"
    }
}