package org.tensorflow.lite.examples.classification.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dog.*
import org.tensorflow.lite.examples.classification.R
import java.text.SimpleDateFormat
import java.util.*

class DogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog)

        val razaLabel = "Raza 🐶: " + intent.getStringExtra("raza")
        razaPerro.text = razaLabel
        val dateInput=findViewById<EditText>(R.id.dateInput)
        
        initControls(dateInput)
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
