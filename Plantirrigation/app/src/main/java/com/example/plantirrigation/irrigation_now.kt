package com.example.plantirrigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class irrigation_now : AppCompatActivity() {
   // private lateinit var binding: ActivityIrrigationNowBinding
    private lateinit var tvSoil : TextView
    private lateinit var now_btn : Button

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityIrrigationNowBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_irrigation_now)

        tvSoil = findViewById(R.id.soil_tv)
        now_btn = findViewById(R.id.now_btn)

       // tvSoil.setText("tfoo")
        // Firebase setting
        database = Firebase.database.reference


        database.child("sensors").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val soil_level = snapshot.child("soil_sensor").value.toString()
                tvSoil.setText(soil_level)
               // Toast.makeText(applicationContext,"Successfuly Read", Toast.LENGTH_SHORT).show()

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        now_btn.setOnClickListener {
            database.child("sensors").child("pump_state").setValue(1)
        }



    }

}