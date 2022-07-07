package elfak.mosis.freelencelive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        var textBox:TextView =  findViewById(R.id.textBox)

        var tekst:String? = null;
        if (firebaseAuth.currentUser != null)
            tekst = firebaseAuth.currentUser!!.email.toString();
        Toast.makeText(this, firebaseAuth.currentUser?.email.toString() + firebaseAuth.currentUser?.email.toString(), Toast.LENGTH_LONG )
        textBox.setText(tekst)

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