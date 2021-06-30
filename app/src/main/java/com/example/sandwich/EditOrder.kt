package com.example.sandwich

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException


class EditOrder : AppCompatActivity() {
    var order: Order? = null
    var status = "waiting"


    private fun orderChangedListener(id: String) {
        /**
         * sets on order changed listener for an order that corresponds to current id
         */
        val firebase = FirebaseFirestore.getInstance()
        firebase.collection("orders").document(id).addSnapshotListener { snapshot, e ->
            if (snapshot == null) {
                //error or no value
            } else if (!snapshot.exists()) {
                //deleted
            } else {
                val order = snapshot.toObject(Order::class.java)
                if (order != null) {
                    status = order.status
                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order)
        val db = FirebaseFirestore.getInstance()
        val name = findViewById<EditText>(R.id.customerName)
        val pickles = findViewById<EditText>(R.id.pickles)
        val comment = findViewById<EditText>(R.id.comment)
        val hummus = findViewById<CheckBox>(R.id.hummus)
        val tahini = findViewById<CheckBox>(R.id.tahini)
        val buttonSaveOrder = findViewById<Button>(R.id.createOrder)
        val buttonChangeOrder = findViewById<Button>(R.id.change_order)
        val order = Order()
        pickles.addTextChangedListener {

            var p = pickles.text.toString().toIntOrNull() ?: 0
                    buttonSaveOrder.setEnabled(p >= 0 && p <= 10)
                }

        buttonSaveOrder.setOnClickListener {
            order.customer_name = name.text.toString()
            order.pickles = pickles.text.toString().toInt()
            order.hummus = hummus.isChecked
            order.tahini = tahini.isChecked
            order.comment = comment.text.toString()
            db.collection("orders").document(order.id)
                .set(order)
                .addOnSuccessListener {
                    Log.d("logs","success")
                }
                .addOnFailureListener {
                    Log.d("logs", "failed")
                }
            buttonSaveOrder.setEnabled(false)
            name.isEnabled = false
            pickles.isEnabled = false
            comment.isEnabled = false
            hummus.isEnabled = false
            tahini.isEnabled = false
            buttonSaveOrder.visibility = View.GONE
            buttonChangeOrder.visibility = View.VISIBLE
            orderChangedListener(order.id)
        }

        buttonChangeOrder.setOnClickListener {
            name.isEnabled = true
            pickles.isEnabled = true
            comment.isEnabled = true
            hummus.isEnabled = true
            tahini.isEnabled = true
        }
    }
}
//todo add screen for processing order, for editing order and for order is ready
// todo in the edit order add part where you can delete this order
//todo add reaction to changes from cloud (status of the order)