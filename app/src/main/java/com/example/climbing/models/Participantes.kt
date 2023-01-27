package com.example.climbing.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap
import java.util.UUID

class Participantes (

    var participanteId       : String?,
    var name     : String?,
    var idade     : String?,
    var nacionalidade      : String?,
    var photoParticipante: String?,
    ){



    fun toHashMap() : HashMap<String, Any?> {
        return hashMapOf(

            "participanteId"        to participanteId       ,
            "name"      to name     ,
            "idade"         to idade       ,
            "nacionalidade" to nacionalidade,
            "photoParticipante" to photoParticipante,
        )
    }



    fun sendParticipantes(callback: (error:String?)->Unit) {
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val db = Firebase.firestore
        db.collection("participantes")
            .document(UUID.randomUUID().toString())
            .set(toHashMap())
            .addOnSuccessListener {
                callback(null)
            }.addOnFailureListener {
                callback(it.toString())
            }
    }

    companion object {

        fun postField(name:String, field:String){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("participantes")
                .document(uid )
                .update(field,name)


        }

        fun fromDoc(doc: DocumentSnapshot) : Participantes {
            return Participantes(
                doc.getString("participanteId"       ),
                doc.getString("name"     ),
                doc.getString("idade"        ),
                doc.getString("nacionalidade"        ),
                doc.getString("photoParticipante"      )
            )
        }
    }

}
