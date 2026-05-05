package com.technologiesdesesperation.mottosys

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.technologiesdesesperation.mottosys.databinding.ActivityRegisterBinding
import com.technologiesdesesperation.mottosys.models.LicenseResponse
import com.technologiesdesesperation.mottosys.models.UserResponse
import kotlinx.coroutines.launch
import com.technologiesdesesperation.mottosys.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val btnRegister = binding.btnRegister
        val tvBackToLogin = binding.tvBackToLogin

        binding.regPasswordConfirm.doOnTextChanged { text, _, _, _ ->
            binding.regPasswordConfirm.error = if (text.toString() != binding.regPassword.text.toString()) {
                "Las contraseñas no coinciden"
            } else {
                null
            }
        }

        btnRegister.setOnClickListener {
            val email = binding.regEmail.text.toString()
            val password = binding.regPassword.text.toString()
            val passwordConfirm = binding.regPasswordConfirm.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                lifecycleScope.launch {
                    registerNewAccount(email, password)
                }
            }
        }

        tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerNewAccount(email: String, pass: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userResponse = RetrofitClient.instance.createUser(
                    UserResponse(email, pass)
                )

                if (userResponse.isSuccessful) {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    val licenseResponse = RetrofitClient.instance.createLicense(
                        LicenseResponse(email = email, licencia = true, pago_date = today)
                    )

                    withContext(Dispatchers.Main) {
                        if (licenseResponse.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Cuenta creada. Espere activación.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}