package org.tensorflow.lite.examples.classification.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dog.*
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import java.text.SimpleDateFormat
import java.util.*

class DogActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val myCalendar = Calendar.getInstance()
    private lateinit var sdf: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog)

        val razaLabel = "Raza 🐶: " + intent.getStringExtra("raza")
        razaPerro.text = razaLabel
        val dateInput = findViewById<EditText>(R.id.dateInput)

        val myFormat = "dd/MM/yy"
        sdf = SimpleDateFormat(myFormat, Locale.US)
        initControls(dateInput)
        dateInput.setText(sdf.format(myCalendar.time))

        backButton.setOnClickListener { finish() }
        checkButton.setOnClickListener {
            if (ValidadorCampos()) {
                var optionGender = "Macho"

                if (femaleOption.isChecked && !maleOption.isChecked) {
                    optionGender = "Hembra"
                } else if (!femaleOption.isChecked && maleOption.isChecked) {
                    optionGender = "Macho"
                }

                db.collection("Perros").add(
                    Dog(
                        idRaza = intent.getStringExtra("idRaza"),
                        name = nombreInput.text.toString(),
                        fechaRescate = Timestamp(myCalendar.time),
                        peso = pesoInput.text.toString(),
                        sexo = optionGender
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Creado con exito", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Campos deben estar llenos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ValidadorCampos(): Boolean {
        return nombreInput.text!!.isNotEmpty() && pesoInput.text!!.isNotEmpty() && dateInput.text!!.isNotEmpty() && (femaleOption.isChecked || maleOption.isChecked)
    }


    private fun initControls(dateInput: EditText) {

        val dogDate: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                myCalendar.set(Calendar.HOUR_OF_DAY, 23)
                myCalendar.set(Calendar.MINUTE, 59)
                myCalendar.set(Calendar.SECOND, 0)
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

    }
}