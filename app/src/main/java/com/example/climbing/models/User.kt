package com.example.climbing.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

data class User (

    var userId       : String?,
    var name     : String?,
    var email        : String?,
    var photoFilename: String?,
){

    fun toHashMap() : HashMap<String,Any?> {
        return hashMapOf(
            "userId"        to userId       ,
            "name"      to name     ,
            "email"         to email        ,
            "photoFilename" to photoFilename,
        )
    }



    fun sendUser(callback: (error:String?)->Unit) {
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val db = Firebase.firestore
        db.collection("users").document(uid)
            .set(toHashMap())
            .addOnSuccessListener {
                callback(null)
            }
            .addOnFailureListener { e->
                callback(e.toString())
            }
    }


    companion object {

        fun postField(name:String, field:String){
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = Firebase.firestore
            db.collection("users")
                .document(uid )
                .update(field,name)


        }

        fun fromDoc(doc:DocumentSnapshot) : User {
            return User(

                doc.getString("userId"       ),
                doc.getString("name"     ),
                doc.getString("email"        ),
                doc.getString("photoFilename")
            )
        }
    }
}
