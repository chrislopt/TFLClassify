package org.tensorflow.lite.examples.classification.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
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

    val db=FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog)

        val razaLabel = "Raza 🐶: " + intent.getStringExtra("raza")
        razaPerro.text = razaLabel
        val dateInput=findViewById<EditText>(R.id.dateInput)
        
        initControls(dateInput)


        //val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
       //val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        //val string: String = selectedRadioButton.text.toString()


        checkButton.setOnClickListener {
            //TODO: NOT QUITE WORKING
            var optionGender: String = "Macho"

            if (femaleOption.isSelected && !maleOption.isSelected) {
                optionGender = "Hembra";
            } else if (!femaleOption.isSelected && maleOption.isSelected) {
                optionGender = "Macho"
            };

            db.collection("Perros").add(Dog(idRaza = intent.getStringExtra("idRaza") ,name = nombreInput.text.toString(),fechaRescate = Timestamp(Date()),peso = pesoInput.text.toString(), sexo = optionGender  )).addOnSuccessListener { newDoc ->

                Toast.makeText(this, "Creado con exito", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
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
