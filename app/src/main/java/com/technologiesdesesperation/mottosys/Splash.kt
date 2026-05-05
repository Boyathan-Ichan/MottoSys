package com.technologiesdesesperation.mottosys

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.technologiesdesesperation.mottosys.models.LicenseResponse
import com.technologiesdesesperation.mottosys.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import com.technologiesdesesperation.mottosys.dbhelter.InventoryDbHelper

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startFlow()
        }, 1000)
    }

    private fun startFlow() {
        lifecycleScope.launch {

            val hasInternet = withContext(Dispatchers.IO) {
                isInternetAvailable(this@Splash)
            }

            if (!hasInternet) {
                Toast.makeText(this@Splash, "Sin conexión a internet", Toast.LENGTH_LONG).show()
                return@launch
            }

            val prefs = getSharedPreferences("user", MODE_PRIVATE)
            val email = prefs.getString("email", null) ?: ""

            if (email.isEmpty()) {
                goToLogin(true)
                return@launch
            }

            val latestDate = withContext(Dispatchers.IO) {
                var latestDate = getLatestLicenseDate(email)

                var licencia = latestDate?.licencia

                when (licencia){
                    true -> {
                        goToMain(email)
                    }
                    false -> {
                        goToLogin(licencia)
                    }
                    null -> {
                        InventoryDbHelper(this@Splash).nukeDatabase()
                    }
                }
            }
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

    private fun goToMain(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun goToLogin(licencia: Boolean) {
        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        prefs.edit().remove("email").apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("Licencia", licencia)
        startActivity(intent)
        finish()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}