package com.example.bitirme_projem2.ui



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projem2.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var register: Button
    private lateinit var login: Button
    private lateinit var username: TextView
    private lateinit var password: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        auth = FirebaseAuth.getInstance()

        register = findViewById(R.id.Button_register)
        login = findViewById(R.id.login_button)
        username = findViewById(R.id.user_name)
        password = findViewById(R.id.user_password)

        // Oturum açıkken MainActivity'e yönlendir
        if (auth.currentUser != null) {
            startMainActivity()
        }

        register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            onLoginButtonClick(it)
        }
    }

    private fun onLoginButtonClick(view: View) {
        val email = username.text.toString()
        val password = password.text.toString()

        signIn(email, password)
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılıysa yapılacak işlemler
                    Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                } else {
                    // Giriş başarısızsa kullanıcıya uyarı ver
                    Toast.makeText(this, "Giriş Başarısız. Kullanıcı adı veya şifre yanlış.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Giriş başarısız", task.exception)

                }
            }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
