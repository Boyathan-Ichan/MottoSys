package com.technologiesdesesperation.mottosys

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.technologiesdesesperation.mottosys.databinding.ActivityConfigUserBinding
import com.technologiesdesesperation.mottosys.models.HardwareUpdateRequest
import com.technologiesdesesperation.mottosys.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigUser : AppCompatActivity() {
    private lateinit var binding: ActivityConfigUserBinding

    var email = intent.getStringExtra("email")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.btnNextSetup.setOnClickListener { view ->
            var isHandheld = binding.cbHandheld.isChecked

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val request = HardwareUpdateRequest(has_handheld = isHandheld)
                    val response = RetrofitClient.instance.updateUserHardware(
                        email = "eq.$email",
                        preference = request,
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val prefs = getSharedPreferences("user", MODE_PRIVATE)
                            prefs.edit().putBoolean("has_handheld", isHandheld).apply()

                            startActivity(
                                Intent(
                                    this@ConfigUser,
                                    TrialInfoActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@ConfigUser,
                                "Error al guardar preferencia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    // Manejo de errores de red
                }
            }
        }
    }
}