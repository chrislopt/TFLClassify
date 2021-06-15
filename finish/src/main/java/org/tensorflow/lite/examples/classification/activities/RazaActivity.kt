package org.tensorflow.lite.examples.classification.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_raza.*
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import org.tensorflow.lite.examples.classification.model.Raza
import org.tensorflow.lite.examples.classification.ui.DogAdapter
import java.util.*

class RazaActivity : AppCompatActivity() {

    private val tag = "RazaActivity"
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raza)
        val raza = intent.getStringExtra("raza")
        db = Firebase.firestore
        getData(raza)
        addPerro.setOnClickListener {
            val intent = Intent(this, DogActivity::class.java)
            intent.putExtra("raza", raza)
            startActivity(intent)
        }
    }

    private fun getData(raza: String?) {
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

                        getDogs(documents.documents[0].id)
                    }

                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }
    }

    private fun getDogs(idRaza: String) {
        db.collection("Perros")
            .whereEqualTo("idRaza", idRaza)
            //.whereEqualTo("idRefugio", idRefugio)
            .get()
            .addOnSuccessListener { documents ->
                val listDogs: MutableList<Dog> = ArrayList()

                Log.d(tag, "DocumentSnapshot PERRITOS: ${documents.documents.size}")
                for (document in documents.documents) {
                    val dogObjet = document.toObject(Dog::class.java)
                    dogObjet?.let {
                        dogObjet.id = document.id
                        listDogs.add(dogObjet)
                    }
                }

                dogsList.adapter = DogAdapter(context = this, dogs = listDogs)
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }
    }
}