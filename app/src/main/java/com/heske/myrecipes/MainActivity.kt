package com.heske.myrecipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.heske.myrecipes.databinding.ActivityMainBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

//class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
//    @Inject
//    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//
//    override fun supportFragmentInjector()
//            = dispatchingAndroidInjector
//}
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        //This app has an AppTheme.Launcher theme, so if there's a delay
        //while the app loads, the user will see something nicer than
        //a plain white screen. Now change back to the original AppTheme.
        //   setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")

        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // Set up ActionBar with navigation.
        navController = Navigation.findNavController(this, R.id.navigation_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun supportFragmentInjector()
            = dispatchingAndroidInjector
}

