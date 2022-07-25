package elfak.mosis.freelencelive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import elfak.mosis.freelencelive.R

class MainWindowActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

//        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_dashboard -> {
                    //Toast.makeText(this, "NAV HOME CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToDashboard()
                    Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
                    drawerLayout.close()

                }
                R.id.nav_myJobs -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToMyJobs()
                    Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_rankings -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToRating()
                    Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
                    drawerLayout.close()
                }
                R.id.nav_myFriends -> {
                    //Toast.makeText(this, "GALLERY CLICKED!", Toast.LENGTH_LONG).show()
                    val action = StartPageFragmentDirections.actionStartpageToFriends()
                    Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
                    drawerLayout.close()
                }
//                R.id.nav_slideshow -> {
//                    Toast.makeText(this, "SLIDESHOW!", Toast.LENGTH_LONG).show()
//                }
            }
            true
        }
        val headerView = navView.getHeaderView(0)
        var userNameText:TextView = headerView.findViewById(R.id.UserNameText)
        userNameText.setText("MrFICAX")

        val profileConstraint = headerView.findViewById<ConstraintLayout>(R.id.ToolbarConstraintUser)
        profileConstraint.setOnClickListener{
            val action = StartPageFragmentDirections.actionStartpageToMyprofile()
            Navigation.findNavController(findViewById(R.id.fragment_container)).navigate(action)
            drawerLayout.close()
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(toggle.onOptionsItemSelected(item)){
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//
//    }
}