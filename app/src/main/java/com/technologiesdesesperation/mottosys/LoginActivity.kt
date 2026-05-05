package com.technologiesdesesperation.mottosys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.technologiesdesesperation.mottosys.databinding.ActivityLoginBinding
import com.technologiesdesesperation.mottosys.dbhelter.InventoryDbHelper
import com.technologiesdesesperation.mottosys.models.LicenseResponse
import com.technologiesdesesperation.mottosys.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnMain = binding.btnLogin
        val dontCount = binding.tvRegisterLink

        var licencia = intent.getBooleanExtra("Licencia", false)

        if (!licencia) {
            showLicenseBlockedDialog()
        }

        var successSe : Boolean? = null

        btnMain.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    successSe = isLoggin(email, password)

                    when (successSe) {
                        true -> {
                            val latestDate = withContext(Dispatchers.IO) {
                                var latestDate = getLatestLicenseDate(email)

                                var licencia = latestDate?.licencia

                                when (licencia){
                                    true -> {
                                        val prefs = getSharedPreferences("user", MODE_PRIVATE)
                                        prefs.edit().putString("email", email).apply()

                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra("email", email)
                                        startActivity(intent)

                                        finish()
                                    }
                                    false -> {
                                        showLicenseBlockedDialog()
                                    }
                                    null -> {
                                        withContext(Dispatchers.IO) {
                                            InventoryDbHelper(this@LoginActivity).nukeDatabase()
                                        }
                                        Toast.makeText(this@LoginActivity, "Error de sincronización (0x882)", Toast.LENGTH_LONG).show()
                                        finishAffinity()
                                    }
                                }
                            }
                        }
                        false -> {
                            Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                        }
                        null -> {
                            Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        dontCount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private suspend fun isLoggin(email: String, password: String): Boolean? {
        return try {
            val response = RetrofitClient.instance.getUser(email)

            if (!response.isSuccessful) return null

            val users = response.body() ?: return null

            for (user in users) {
                if (user.password == password) {
                    return true
                } else {
                    return false
                }
            }

            false
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getLatestLicenseDate(email: String): LicenseResponse? {
        return try {
            val response = RetrofitClient.instance.checkSecurityStatus(email)

            if (response.isSuccessful) {
                val list = response.body() ?: return null

                if (list.isEmpty()) return null

                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val sorted = list.sortedByDescending {
                    format.parse(it.pago_date)
                }

                sorted.firstOrNull()

            } else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

    fun showLicenseBlockedDialog() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Licencia requerida")
            .setMessage("Debe pagar su licencia para continuar.")
            .setCancelable(false)
            .setPositiveButton("Salir") { _, _ ->
                finishAffinity()
            }
            .create()

        dialog.show()
    }
}