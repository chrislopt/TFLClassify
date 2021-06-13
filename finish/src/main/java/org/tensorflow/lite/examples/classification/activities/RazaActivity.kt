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

        getData(intent.getStringExtra("raza"))
    }

    private fun getData(raza: String?) {
        val db = Firebase.firestore
        db.collection("razas")
            .whereEqualTo("name", raza)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    Log.d(tag, "DocumentSnapshot data: ${documents.documents[0].data}")
                    val razaDoc = documents.documents[0].toObject(Raza::class.java)
                    razaDoc?.let {
                        razaDoc.id = documents.documents[0].id

                        Glide
                            .with(this)
                            .load(razaDoc.img)
                            .into(razaImage)

                        razaText.text = razaDoc.name!!.toUpperCase(Locale.getDefault())
                        descText.text = razaDoc.desc
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