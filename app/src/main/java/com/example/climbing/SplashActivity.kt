package com.example.climbing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.climbing.databinding.ActivitySplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firestore = Firebase.firestore
        firestore.setFirestoreSettings(FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build())

        lifecycleScope.launch (Dispatchers.IO){
            delay(2000)
            lifecycleScope.launch (Dispatchers.Main) {
                val auth = Firebase.auth
                val currentUser = auth.currentUser
                if (currentUser != null){
                    startActivity(
                        Intent(this@SplashActivity,
                            MainActivity::class.java)
                    )

                }else{
                    startActivity(
                        Intent(this@SplashActivity,
                            LoginActivity::class.java)
                    )
                }
                finish()
            }

        }

    }
}
