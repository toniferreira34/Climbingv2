package com.example.climbing.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

data class percurso(
    var percursoId       : String?,
    var name     : String?,
    var duracao       : String?,
    var photoPercurso: String?,
) {

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "percursoId" to percursoId,
            "name" to name,
            "duracao" to duracao,
            "photoPercurso" to photoPercurso,
        )
    }

    companion object {

        fun postField(name: String, field: String) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("percurso")
                .document(uid)
                .update(field, name)


        }

        fun fromDoc(doc: DocumentSnapshot): percurso {
            return percurso(

                doc.getString("percursoId"),
                doc.getString("name"),
                doc.getString("duracao"),
                doc.getString("photoPercurso")
            )
        }
    }
}


