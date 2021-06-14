package org.tensorflow.lite.examples.classification.model

import com.google.firebase.Timestamp

data class Vacunas(
    var id: String? = null,
    var producto: String? = null,
    var fecha: Timestamp? = null,
    var encargado: String? = null
)
