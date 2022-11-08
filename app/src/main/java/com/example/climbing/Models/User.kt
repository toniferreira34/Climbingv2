package com.example.climbing.Models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class User
    (
    var username     : String?,
    var userId       : String?,
    var email        : String?,
    var photoFilename: String?,
){

    fun toHashMap() : HashMap<String, Any?> {
        return hashMapOf(
            "username"      to username     ,
            "userId"        to userId       ,
            "email"         to email        ,
            "photoFilename" to photoFilename,
        )
    }



    fun sendUser(callback: (error:String?)->Unit) {
        val db = Firebase.firestore
        db.collection("users")
            .add(toHashMap())
            .addOnSuccessListener { documentReference ->
                callback(null)
            }
            .addOnFailureListener { e ->
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

        fun fromDoc(doc: DocumentSnapshot) : User {
            return User(
                doc.getString("username"     ),
                doc.getString("userId"       ),
                doc.getString("email"        ),
                doc.getString("photoFilename")
            )
        }
    }
}