package com.example.geradorsenhas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var cbUppercase: CheckBox
    private lateinit var cbLowercase: CheckBox
    private lateinit var cbNumbers: CheckBox
    private lateinit var cbExcludeSimilar: CheckBox
    private lateinit var seekBarLength: SeekBar
    private lateinit var tvLengthValue: TextView
    private lateinit var tvPassword: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnCopy: Button

    private val similarChars = "il1Lo0O"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cbUppercase = findViewById(R.id.cbUppercase)
        cbLowercase = findViewById(R.id.cbLowercase)
        cbNumbers = findViewById(R.id.cbNumbers)
        cbExcludeSimilar = findViewById(R.id.cbExcludeSimilar)
        seekBarLength = findViewById(R.id.seekBarLength)
        tvLengthValue = findViewById(R.id.tvLengthValue)
        tvPassword = findViewById(R.id.tvPassword)
        btnGenerate = findViewById(R.id.btnGenerate)
        btnCopy = findViewById(R.id.btnCopy)

        seekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvLengthValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        btnGenerate.setOnClickListener {
            val length = seekBarLength.progress
            val password = generatePassword(length)
            tvPassword.text = password
        }

        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("senha", tvPassword.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Senha copiada!", Toast.LENGTH_SHORT).show()
        }

        seekBarLength.progress = 12
        btnGenerate.performClick()
    }

    private fun generatePassword(length: Int): String {
        val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lower = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"

        var charPool = ""

        if (cbUppercase.isChecked) charPool += upper
        if (cbLowercase.isChecked) charPool += lower
        if (cbNumbers.isChecked) charPool += numbers

        if (cbExcludeSimilar.isChecked) {
            charPool = charPool.filterNot { similarChars.contains(it) }
        }

        if (charPool.isEmpty()) return "Selecione ao menos uma opção!"

        return (1..length)
            .map { charPool.random() }
            .joinToString("")
    }
}
