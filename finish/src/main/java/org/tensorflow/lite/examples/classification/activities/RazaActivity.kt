package org.tensorflow.lite.examples.classification.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_raza.*
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Raza
import java.util.*

class RazaActivity : AppCompatActivity() {

    private val tag = "RazaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raza)

        getData()
    }

    private fun getData() {
        val db = Firebase.firestore
        db.collection("razas")
            .document("pug")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(tag, "DocumentSnapshot data: ${document.data}")
                    val raza = document.toObject(Raza::class.java)
                    raza?.let {
                        raza.id = document.id

                        Glide
                            .with(this)
                            .load(raza.img)
                            .into(razaImage)

                        razaText.text = raza.id!!.toUpperCase(Locale.getDefault())
                        descText.text = raza.desc
                    }

                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }
    }
}