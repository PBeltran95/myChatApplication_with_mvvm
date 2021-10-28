package ar.com.example.chatExample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ar.com.example.chatExample.application.AppConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

//    How to subscribe to a topic for pushNotifications:

//    private fun subscribeToTopicNotifications() {
//        val myUid = FirebaseAuth.getInstance().currentUser?.uid
//        FirebaseMessaging.getInstance().subscribeToTopic("${AppConstants.TOPIC}/${myUid}")
//    }


    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}