package elfak.mosis.freelencelive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val TAG = "101"
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var myRef: DatabaseReference
    private lateinit var textBox: TextView
    private lateinit var textLoad: TextView
    private lateinit var buttonPost: Button
    private lateinit var buttonLoad: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val database = Firebase.database
        myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        textBox =  findViewById(R.id.textComment)
        textLoad = findViewById(R.id.textViewLoad)
        buttonPost = findViewById(R.id.buttonPost)
        buttonLoad = findViewById(R.id.buttonLoad)

        var tekst:String? = null;
        if (firebaseAuth.currentUser != null)
            tekst = firebaseAuth.currentUser!!.email.toString();
        Toast.makeText(this, firebaseAuth.currentUser?.email.toString(), Toast.LENGTH_LONG )
        //textBox.setText(tekst)

        buttonPost.setOnClickListener {
            val tekst: String = textBox.text.toString()
            if (tekst.isNotEmpty())
            {
                myRef.setValue(tekst)
            }
        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                textLoad.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        buttonLoad.setOnClickListener {

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        firebaseAuth.signOut()
    }

    override fun onDestroy() {
        super.onDestroy()

        firebaseAuth.signOut()
    }
}