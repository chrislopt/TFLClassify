package org.tensorflow.lite.examples.classification.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Cat
import java.text.SimpleDateFormat
import java.util.*

//Con esta clase se maneja el adaptador para cada elemento del list view de los perros

class CatAdapter(context: Context, cats: List<Cat>) : ArrayAdapter<Cat>(context, 0, cats) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    var convertView = convertView
    if (convertView == null)
    {
        convertView = (context as Activity).layoutInflater.inflate(
            R.layout.item_cat,
            parent,
            false
        )
    }

    val cat: Cat = getItem(position) ?: return convertView!!

    convertView?.let{
        val catTextView = convertView.findViewById<TextView>(R.id.catText)
        val genderTextView = convertView.findViewById<TextView>(R.id.genderText)
        val dateTextView = convertView.findViewById<TextView>(R.id.dateText)
        val weightTextView = convertView.findViewById<TextView>(R.id.weightText)

        catTextView.text = cat.name
        genderTextView.text = cat.sexo

        val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        cat.fechaRescate?.let { dateTextView.text = formatDate.format(it.toDate()) }

        weightTextView.text = cat.peso

        if (cat.sexo.equals("Macho")) genderTextView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.blue
            )
        )
        if (cat.sexo.equals("Hembra")) genderTextView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.pink
            )
        )
    }


    return convertView!!

    }
}