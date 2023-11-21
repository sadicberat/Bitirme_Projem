package com.example.bitirme_projem2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.LoginActivity
import com.example.bitirme_projem2.MainActivity
import com.example.bitirme_projem2.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userPassword: TextView
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_signup)

        userName = findViewById(R.id.signUp_name)
        userPassword = findViewById(R.id.signUp_password)
        register = findViewById(R.id.button_signUp)

        auth = FirebaseAuth.getInstance()

        val name = userName.text.toString()
        val password = userPassword.text.toString()

        register.setOnClickListener {
            onRegisterButtonClick(it)


        }


        val buttonGeriSignUp: Button = findViewById(R.id.button_geri_signUp)
        buttonGeriSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun onRegisterButtonClick(view: View) {
        val email = userName.text.toString()
        val password = userPassword.text.toString()

        signUn(email, password)
    }
    private fun signUn(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılıysa yapılacak işlemler
                    Toast.makeText(this, "kayıt oluşturuldu giriş yapılıyor", Toast.LENGTH_SHORT).show()
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
