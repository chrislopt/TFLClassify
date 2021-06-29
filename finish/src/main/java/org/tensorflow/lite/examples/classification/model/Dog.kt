package org.tensorflow.lite.examples.classification.model

import com.google.firebase.Timestamp

data class Dog(
    var id: String? = null,
    var idRaza: String? = null,
    var idRefugio: String? = null,
    var name: String? = null,
    var peso: String? = null,
    var sexo: String? = null,
    var fechaRescate: Timestamp? = null,
    var vacunas: List<Vacunas>? = null
)