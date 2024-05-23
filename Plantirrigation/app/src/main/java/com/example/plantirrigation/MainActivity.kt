package com.example.plantirrigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val scan_Btn = findViewById<Button>(R.id.scan_btn)
        scan_Btn.setOnClickListener {
            val Intent = Intent(this, ScanActivity::class.java)
            startActivity(Intent)
        }

        val irrinow_Btn = findViewById<Button>(R.id.irrinow_but)
        irrinow_Btn.setOnClickListener {
            val Intent = Intent(this, irrigation_now::class.java)
            startActivity(Intent)
        }
    }
}