package org.tensorflow.lite.examples.classification.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_dog.*
import kotlinx.android.synthetic.main.activity_perfil_dog.*
import kotlinx.android.synthetic.main.activity_perfil_dog.backButton
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import java.text.SimpleDateFormat
import java.util.*


class PerfilDogActivity : AppCompatActivity() {

    private val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
    private lateinit var db: FirebaseFirestore
    private lateinit var dog: Dog
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_dog)

        db = Firebase.firestore
        context = this

        // Dog information
        dog = Gson().fromJson(intent.getStringExtra("dog"), Dog::class.java)

        dogText.text = dog.name
        genderText.text = dog.sexo

        val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dog.fechaRescate?.let { dateText.text = formatDate.format(it.toDate()) }

        weightText.text = dog.peso

        if (dog.sexo.equals("Macho")) genderText.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.blue
            )
        )
        if (dog.sexo.equals("Hembra")) genderText.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.pink
            )
        )

        // Back button
        backButton.setOnClickListener { finish() }

        addVacuna.setOnClickListener { dialogVacuna() }
    }


    private fun dialogVacuna() {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_vacuna, null)
        val nombreInput = dialogView.findViewById<TextInputEditText>(R.id.nombreInput)
        val dateInput = dialogView.findViewById<TextInputEditText>(R.id.dateInput)
        val responsableInput = dialogView.findViewById<TextInputEditText>(R.id.responsableInput)
        val myCalendar = Calendar.getInstance()
        myCalendar.set(Calendar.HOUR_OF_DAY, 0)
        myCalendar.set(Calendar.MINUTE, 1)
        myCalendar.set(Calendar.SECOND, 0)

        val dogDate: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                dateInput.setText(sdf.format(myCalendar.time))
            }

        dateInput.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                dogDate,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )

            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "CANCEL"
            ) { _: DialogInterface?, _: Int ->
                dateInput.setText("")
            }
            dialog.show()
        }

        dateInput.setText(sdf.format(myCalendar.time))

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Agregar una nueva vacuna")
            .setView(dialogView)
            .setNegativeButton("CANCELAR", null)
            .setPositiveButton("AGREGAR") { _, _ ->
                if (nombreInput.text!!.isEmpty() || dateInput.text!!.isEmpty() || responsableInput.text!!.isEmpty()) {
                    Toast.makeText(this, "Campos deben estar llenos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val vacuna = hashMapOf(
                    "name" to nombreInput.text.toString(),
                    "date" to Timestamp(myCalendar.time),
                    "by" to responsableInput.text.toString()
                )

                db.collection("Perros")
                    .document(dog.id!!)
                    .collection("vacunas")
                    .add(vacuna)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Creada con exito", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
            }

        dialog.show()
    }
}