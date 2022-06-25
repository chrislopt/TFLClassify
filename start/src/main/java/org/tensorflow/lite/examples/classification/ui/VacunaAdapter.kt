package org.tensorflow.lite.examples.classification.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.tensorflow.lite.examples.classification.R
import org.tensorflow.lite.examples.classification.model.Vacuna
import java.text.SimpleDateFormat
import java.util.*



//Con esta clase se maneja el adaptador para cada elemento del list view de las vacunas

class VacunaAdapter(context: Context, vacunas: List<Vacuna>) :
    ArrayAdapter<Vacuna>(context, 0, vacunas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = (context as Activity).layoutInflater.inflate(
                R.layout.item_vacuna,
                parent,
                false
            )
        }

        val vacuna: Vacuna = getItem(position) ?: return convertView!!

        convertView?.let {
            val vacunaTextView = convertView.findViewById<TextView>(R.id.vacunaText)
            val encargadoTextView = convertView.findViewById<TextView>(R.id.encargadoText)
            val dateTextView = convertView.findViewById<TextView>(R.id.dateText)

            vacunaTextView.text = vacuna.producto
            encargadoTextView.text = vacuna.encargado

            val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            vacuna.fecha?.let { dateTextView.text = formatDate.format(it.toDate()) }
        }


        return convertView!!
    }
}