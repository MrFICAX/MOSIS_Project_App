package elfak.mosis.freelencelive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.databinding.ActivityLogInBinding


class LogInActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if( it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "UserLogin successful!", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(this, "Login not successful!", Toast.LENGTH_LONG).show()

                    }

                }
            }
            else{
                Toast.makeText(this, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        firebaseAuth.signOut()
    }
}