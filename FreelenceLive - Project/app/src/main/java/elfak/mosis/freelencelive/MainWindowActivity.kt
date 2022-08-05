package elfak.mosis.freelencelive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.dialogs.InviteFriendFragmentDialog
import elfak.mosis.freelencelive.model.userViewModel
import elfak.mosis.freelencelive.services.NotificationsService

class MainWindowActivity : AppCompatActivity() {

    private val userViewModel: userViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private var notificationIntentFlag = false

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

        if (this.intent.hasExtra("user")) {
            notificationIntentFlag = false
            val newUser: User = this.intent.getSerializableExtra("user") as User
            userViewModel.setNewUser(newUser)
            FirebaseHelper.getMyServiceValue(userViewModel, this)
            FirebaseHelper.setOnlineValueEventListener(userViewModel, this)
        } else if (this.intent.hasExtra("currentUserId") && this.intent.hasExtra("nearUserId")) {
//            Toast.makeText(
//                this,
//                this.intent.getStringExtra("currentUserId") + this.intent.getStringExtra("nearUserId"),
//                Toast.LENGTH_SHORT
//            ).show()
            FirebaseHelper.getUserData(this, this, userViewModel, this)
            notificationIntentFlag = true

        } else {
            startActivity(Intent(this, MainActivity::class.java))
            notificationIntentFlag = false
        }

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
                    FirebaseHelper.postMyOnlineValue(false, userViewModel, this)
                    userViewModel.setNewUser(null)

                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                    drawerLayout.close()

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
        val profilePicture: ImageView = headerView.findViewById(R.id.shapeableImageViewUser)

        val UserObserver = Observer<User> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            userNameText.setText(newValue?.userName)
            Glide.with(this).load(userViewModel.user.value?.imageUrl).into(profilePicture)
        }
        userViewModel.user.observe(this, UserObserver)

        val OtherUsersObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)

            val nearUserId: String? = this.intent.getStringExtra("nearUserId")
            val listOfUsers: List<User> = newValue
            var selectedUser: User? = listOfUsers.filter { it.id.equals(nearUserId) }.firstOrNull()

            if (notificationIntentFlag && selectedUser?.imageUrl?.isNotEmpty() == true)
                setAndOpenSelectedUser(nearUserId)


        }
        userViewModel.users.observe(this, OtherUsersObserver)


        val shapeableView: ImageView = headerView.findViewById(R.id.shapeableImageView)
        shapeableView.setOnClickListener {
            drawerLayout.close()
        }


        val profileConstraint =
            headerView.findViewById<ConstraintLayout>(R.id.ToolbarConstraintUser)
        profileConstraint.setOnClickListener {
            val action = StartPageFragmentDirections.actionStartpageToMyprofile()
            Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
            drawerLayout.close()
        }

    }

    private fun setAndOpenSelectedUser(nearUserId: String?) {
        var selectedUser: User? =
            userViewModel.users.value?.filter { it.id.equals(nearUserId) }?.firstOrNull()

        if (selectedUser != null) {
            userViewModel.setSelectedUser(selectedUser)
        }

        val fragmentNovi = InviteFriendFragmentDialog()
        fragmentNovi.show(supportFragmentManager, "customString")
        notificationIntentFlag = false
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
        Intent(
            applicationContext,
            NotificationsService::class.java
        )
        stopService(intent)

        FirebaseHelper.postMyOnlineValue(false, userViewModel, this)
    }


}