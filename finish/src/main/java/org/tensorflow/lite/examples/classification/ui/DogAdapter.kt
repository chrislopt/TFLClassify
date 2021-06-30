package org.tensorflow.lite.examples.classification.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Dog
import java.text.SimpleDateFormat
import java.util.*

class DogAdapter(val clickListener:(Dog)-> Unit,  context: Context, dogs: List<Dog>) : ArrayAdapter<Dog>(context, 0, dogs) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = (context as Activity).layoutInflater.inflate(
                R.layout.item_dog,
                parent,
                false
            )
        }

        val dog: Dog = getItem(position) ?: return convertView!!

        convertView?.let {
            val dogTextView = convertView.findViewById<TextView>(R.id.dogText)
            val genderTextView = convertView.findViewById<TextView>(R.id.genderText)
            val dateTextView = convertView.findViewById<TextView>(R.id.dateText)
            val weightTextView = convertView.findViewById<TextView>(R.id.weightText)

            dogTextView.text = dog.name
            genderTextView.text = dog.sexo

            val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dog.fechaRescate?.let { dateTextView.text = formatDate.format(it.toDate()) }

            weightTextView.text = dog.peso

            if (dog.sexo.equals("Macho")) genderTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
            if (dog.sexo.equals("Hembra")) genderTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.pink
                )
            )
        }


        return convertView!!
    }
}