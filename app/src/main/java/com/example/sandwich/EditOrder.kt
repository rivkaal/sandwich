package com.example.sandwich

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.model.Document


class EditOrder : AppCompatActivity() {
    var order: Order = Order()
    var status = "waiting"
    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("orders").document(order.id)

    private fun changeToProgressScreen() {
        val name = findViewById<EditText>(R.id.customerName)
        val pickles = findViewById<EditText>(R.id.pickles)
        val comment = findViewById<EditText>(R.id.comment)
        val hummus = findViewById<CheckBox>(R.id.hummus)
        val tahini = findViewById<CheckBox>(R.id.tahini)
        val buttonChangeOrder = findViewById<Button>(R.id.change_order)
        val buttonSaveChanges = findViewById<Button>(R.id.save_changes)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        name.visibility = View.GONE
        pickles.visibility = View.GONE
        comment.visibility = View.GONE
        hummus.visibility = View.GONE
        tahini.visibility = View.GONE
        buttonChangeOrder.visibility = View.GONE
        buttonSaveChanges.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

    }

    private fun orderChangedListener(id: String) {
        /**
         * sets on order changed listener for an order that corresponds to current id
         */
        ref.addSnapshotListener { snapshot, e ->
            if (snapshot == null) {
                //error or no value
            } else if (!snapshot.exists()) {
                //deleted
            } else {
                val order = snapshot.toObject(Order::class.java)
                if (order != null) {
                    status = order.status
                    if(status == "in progress")
                    {
                        changeToProgressScreen()
                    }
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
        val buttonSaveChanges = findViewById<Button>(R.id.save_changes)
        orderChangedListener(order.id)
        pickles.addTextChangedListener {

            var p = pickles.text.toString().toIntOrNull() ?: 0
            buttonSaveOrder.isEnabled = p in 0..10
            buttonSaveChanges.isEnabled = p in 0..10
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
        }

        buttonChangeOrder.setOnClickListener {
            name.isEnabled = true
            pickles.isEnabled = true
            comment.isEnabled = true
            hummus.isEnabled = true
            tahini.isEnabled = true
            buttonChangeOrder.visibility = View.GONE
            buttonSaveChanges.visibility = View.VISIBLE
        }

        buttonSaveChanges.setOnClickListener {
            order.customer_name = name.text.toString()
            order.pickles = pickles.text.toString().toInt()
            order.hummus = hummus.isChecked
            order.tahini = tahini.isChecked
            order.comment = comment.text.toString()
            db.collection("orders").document(order.id).set(order)
            name.isEnabled = false
            pickles.isEnabled = false
            comment.isEnabled = false
            hummus.isEnabled = false
            tahini.isEnabled = false
            buttonChangeOrder.visibility = View.VISIBLE
            buttonSaveChanges.visibility = View.GONE
        }
    }
}