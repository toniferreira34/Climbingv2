package com.example.climbing.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap
import java.util.UUID

class Measurement (
    var id_Participante:        String?,
    var id_Percurso:            String?,
    var Oxygen:                 String?,
    var hearthbeat:             String?,
    var tired:                  Boolean?,
    var insomnia:               Boolean?,
    var dizziness:              Boolean?,
    var headaches:              Boolean?,

    ){

    fun sendmeasurement(callback: (error:String?)->Unit) {
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val db = Firebase.firestore
        db.collection("Measurements").document(UUID.randomUUID().toString())
            .set(toHashMap())
            .addOnSuccessListener {
                callback(null)
            }
            .addOnFailureListener { e->
                callback(e.toString())
            }
    }

    fun toHashMap() : HashMap<String, Any?> {
        return hashMapOf(

            "id_Participante"        to id_Participante       ,
            "id_Percurso"      to id_Percurso     ,
            "Oxygen"         to Oxygen       ,
            "hearthbeat" to hearthbeat,
            "tired" to tired,
            "insomnia" to insomnia,
            "dizziness" to dizziness,
            "headaches" to headaches,

            )
    }

    companion object {

        fun postField(name:String, field:String){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("Measurements")
                .document(uid )
                .update(field,name)


        }

        fun fromDoc(doc: DocumentSnapshot) : Measurement {
            return Measurement(
                doc.getString("id_Participante",       ),
                doc.getString("id_Percurso"     ),
                doc.getString("Oxygen"        ),
                doc.getString("hearthbeat"        ),
                doc.getBoolean("tired"  ),
                doc.getBoolean("insomnia"  ),
                doc.getBoolean("dizziness"  ),
                doc.getBoolean("headaches"   )
            )
        }
    }



}

