package org.tensorflow.lite.examples.classification.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_raza.*
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Cat
import org.tensorflow.lite.examples.classification.model.Raza
import org.tensorflow.lite.examples.classification.ui.CatAdapter
import java.util.*
import kotlin.collections.ArrayList

class RazaActivity : AppCompatActivity() {
    private val tag = "RazaActivity"
    private lateinit var db: FirebaseFirestore
    private var idRaza: String = ""
    private var catsListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raza)
        val raza = intent.getStringExtra("raza")
        db = Firebase.firestore
        getData(raza)
        addGato.setOnClickListener {
            val intent = Intent(this, CatActivity::class.java)
            intent.putExtra("raza", raza)
            intent.putExtra("idRaza", idRaza)
            startActivity(intent)
        }

        catsList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItemText: Cat = parent.getItemAtPosition(position) as Cat
                Log.d("click", selectedItemText.name.toString())

                val intent = Intent(this, PerfilCatActivity::class.java)
                intent.putExtra("cat", Gson().toJson(selectedItemText))
                startActivity(intent)
            }
    }

    override fun onDestroy() {
        catsListener?.remove()
        super.onDestroy()
    }

    private fun getData(raza: String?) {
        db.collection("razasG")
            .whereEqualTo("name", raza)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    Log.d(tag, "DocumentSnapshot data: ${documents.documents[0].data}")
                    val razaDoc = documents.documents[0].toObject(Raza::class.java)
                    razaDoc?.let {
                        razaDoc.id = documents.documents[0].id
                        idRaza = razaDoc.id.toString()
                        Glide
                            .with(this)
                            .load(razaDoc.img)
                            .into(razaImage)

                        razaText.text = razaDoc.name!!.toUpperCase(Locale.getDefault())
                        descText.text = razaDoc.desc

                        getCats(documents.documents[0].id)
                    }

                } else {
                    Log.d(tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(tag, "get failed with ", exception)
            }
    }

    private fun getCats(idRaza: String) {
        catsListener = db.collection("Gatos")
            .whereEqualTo("idRaza", idRaza)
            .orderBy("name")
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.d(tag, "get failed with ${it.message}")
                    return@addSnapshotListener
                }

                val listCats: MutableList<Cat> = ArrayList()
                Log.d(tag, "DocumentSnapshot GATITOS: ${value?.size()}")
                for (document in value!!) {
                    val catObjet = document.toObject(Cat::class.java)
                    catObjet.let {
                        catObjet.id = document.id
                        listCats.add(catObjet)
                    }
                }

                catsList.adapter = CatAdapter(context = this, cats = listCats)
            }
    }

}