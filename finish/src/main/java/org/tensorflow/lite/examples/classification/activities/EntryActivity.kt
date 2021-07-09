package org.tensorflow.lite.examples.classification.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_entry.*
import org.tensorflow.lite.examples.classification.MainActivity
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import java.util.*

//Primera actividad que se muestra con la actividad de los perros y el boton de escanear
class EntryActivity : AppCompatActivity() {

    private var db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        scanButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        getData()
    }

    private fun getData() {
        db.collection("razas")
            .orderBy("name")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    Log.d("peditos", "DocumentSnapshot data: ${documents.documents}")
                    val list: MutableList<String> = ArrayList()

                    for (document in documents!!) {
                        list.add(document.data["name"].toString())
                    }
                    val itemsAdapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
                    razasList.adapter = itemsAdapter

                    razasList.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, _, position, _ ->
                            val selectedItemText: String = parent.getItemAtPosition(position) as String
                            Log.d("click", selectedItemText)

                            val intent = Intent(this, RazaActivity::class.java)
                            intent.putExtra("raza", selectedItemText)

                            startActivity(intent)
                        }
                } else {
                    Log.d("tag", "No such document")
                }
            }.addOnFailureListener { exception ->
                Log.d("tag", "get failed with ", exception)
            }
    }

}