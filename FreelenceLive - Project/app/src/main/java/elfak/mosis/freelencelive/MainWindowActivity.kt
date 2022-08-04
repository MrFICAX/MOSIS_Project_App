package elfak.mosis.freelencelive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.model.userViewModel
import elfak.mosis.freelencelive.services.NotificationsService
import java.io.Serializable

class MainWindowActivity : AppCompatActivity() {

    private val userViewModel: userViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout


    //val object = intent.extras.get("extra_object") as Object
    //val user = intent.getSerializableExtra("user") as? User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)

        val BackGroundServiceObserver = Observer<Boolean> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            StartBackgroundService(newValue)
        }
        userViewModel.backGroundServiceActivated.observe(this, BackGroundServiceObserver)


        val newUser: User = this.intent.getSerializableExtra("user") as User
        userViewModel.setNewUser(newUser)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    //Toast.makeText(this, "NAV HOME CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToDashboard()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()

                }
                R.id.nav_myJobs -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToMyJobs()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_rankings -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToRating()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_myFriends -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToFriends()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_help -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToHelp()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_settings -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToSettings()
                    Navigation.findNavController(findViewById(R.id.fragment_container))
                        .navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                    drawerLayout.close()

                    FirebaseAuth.getInstance().signOut()
                }
//                R.id.nav_slideshow -> {
//                    Toast.makeText(this, "SLIDESHOW!", Toast.LENGTH_LONG).show()
//                }
            }
            true
        }
        val headerView = navView.getHeaderView(0)
        var userNameText: TextView = headerView.findViewById(R.id.UserNameText)
        //userNameText.setText("MrFICAX")

        val UserObserver = Observer<User> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            userNameText.setText(newValue.userName)

        }
        userViewModel.user.observe(this, UserObserver)


        val shapeableView: ImageView = headerView.findViewById(R.id.shapeableImageView)
        shapeableView.setOnClickListener {
            drawerLayout.close()
        }

        val profilePicture: ImageView = headerView.findViewById(R.id.shapeableImageViewUser)

        Glide.with(this).load(userViewModel.user.value?.imageUrl).into(profilePicture)

        val profileConstraint =
            headerView.findViewById<ConstraintLayout>(R.id.ToolbarConstraintUser)
        profileConstraint.setOnClickListener {
            val action = StartPageFragmentDirections.actionStartpageToMyprofile()
            Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
            drawerLayout.close()
        }

    }

    private fun StartBackgroundService(newValue: Boolean?) {
        if (newValue == true) {
            Intent(
                applicationContext,
                NotificationsService::class.java
            ).also { intent ->
                stopService(intent)
                startService(intent)

            }
        } else {
            Intent(
                applicationContext,
                NotificationsService::class.java
            ).also { intent ->
                stopService(intent)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}