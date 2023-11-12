package com.example.bitirme_projem2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        val signUp: TextView = findViewById(R.id.signUp)
        signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val loginButton: TextView = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            onLoginButtonClick(it) // Butona tıklanınca onLoginButtonClick fonksiyonunu çağır
        }
    }

    private fun onLoginButtonClick(view: View) {
        // EditText'leri al
        val usernameEditText: TextView = findViewById(R.id.user_name)
        val passwordEditText: TextView = findViewById(R.id.user_password)

        // Girdileri al
        val enteredUsername = usernameEditText.text.toString()
        val enteredPassword = passwordEditText.text.toString()

        // Kontrol et ve mesaj göster
        if (checkCredentials(enteredUsername, enteredPassword)) {
            Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
            startMainActivity()
        } else {
            Toast.makeText(this, "Giriş Başarısız. Kullanıcı adı veya şifre yanlış.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCredentials(username: String, password: String): Boolean {
        // Kullanıcı adı ve şifreyi kontrol et
        val correctUsername = "admin"
        val correctPassword = "admin"

        return username == correctUsername && password == correctPassword
    }
    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
