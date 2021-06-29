package org.tensorflow.lite.examples.classification.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dog.*
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import java.text.SimpleDateFormat
import java.util.*

class DogActivity : AppCompatActivity() {

    val db=FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog)

        val razaLabel = "Raza 🐶: " + intent.getStringExtra("raza")
        razaPerro.text = razaLabel
        val dateInput=findViewById<EditText>(R.id.dateInput)
        
        initControls(dateInput)


        val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
       val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val string: String = selectedRadioButton.text.toString()

        checkButton.setOnClickListener{
            db.collection("Perros").add(Dog(name = nombreInput.text.toString(),fechaRescate = firebase.firestore.Timestamp.fromDate(Date()),peso = pesoInput.text.toString(),sexo =string )).addOnSuccessListener { newDoc ->
                Log.d(
                    "resultado","EXITO!"
                )

            }
                .addOnFailureListener { e ->
                    Log.d(
                        "resultado","FRACASO!"
                    )
                }
        }

    }


    private fun initControls(dateInput:EditText) {
        val myCalendar = Calendar.getInstance()

        val myFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

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

            dialog.datePicker.minDate = Date().time
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "CANCEL"
            ) { _: DialogInterface?, _: Int ->
                dateInput.setText("")
            }
            dialog.show()
        }

    }
}
