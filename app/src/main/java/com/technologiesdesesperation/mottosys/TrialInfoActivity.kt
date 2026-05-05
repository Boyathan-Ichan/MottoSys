package com.technologiesdesesperation.mottosys

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.technologiesdesesperation.mottosys.databinding.ActivityTrialInfoBinding
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class TrialInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrialInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrialInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7)

        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val nuevaFecha = formato.format(calendar.time)

        binding.tvExpireDate.text = nuevaFecha

        binding.btnFinishSetup.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}