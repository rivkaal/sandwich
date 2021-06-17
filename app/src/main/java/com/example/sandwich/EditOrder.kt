package com.example.sandwich

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.NumberFormatException

class EditOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order)
        val db = FirebaseFirestore.getInstance()
        val name = findViewById<EditText>(R.id.customerName)
        val pickles = findViewById<EditText>(R.id.pickles)
        val comment = findViewById<EditText>(R.id.comment)
        val hummus = findViewById<CheckBox>(R.id.hummus)
        val tahini = findViewById<CheckBox>(R.id.tahini)
        val buttonSaveOrder = findViewById<Button>(R.id.saveOrder)
        pickles.addTextChangedListener {

            var p = pickles.text.toString().toIntOrNull() ?: 0
                    buttonSaveOrder.setEnabled(p >= 0 && p <= 10)
                }

        buttonSaveOrder.setOnClickListener {

            var order = Order(
                customer_name = name.text.toString(),
                pickles = pickles.text.toString().toInt(), //todo check if input is OK
                hummus = hummus.isChecked,
                tahini = tahini.isChecked,
                comment = comment.text.toString()
            )
            db.collection("orders").document(order.id)
                .set(order)
                .addOnSuccessListener {
                    Log.d("logs","success")
                }
                .addOnFailureListener {
                    Log.d("logs", "failed")
                }
            buttonSaveOrder.setEnabled(false)
        }
    }
}