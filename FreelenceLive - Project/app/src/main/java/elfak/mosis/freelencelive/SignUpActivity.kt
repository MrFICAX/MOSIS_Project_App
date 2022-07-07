package elfak.mosis.freelencelive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.databinding.ActivityLogInBinding
import elfak.mosis.freelencelive.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.backToLogin.setOnClickListener{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSignUp.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if( it.isSuccessful){
                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "UserCreation successful!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this, "UserCreation not successful!", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else{
                Toast.makeText(this, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }

    }
}