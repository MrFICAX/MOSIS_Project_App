package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backToLogin.setOnClickListener{
            findNavController().navigate(R.id.action_signup_goto_login)
            //            val intent = Intent(this, LogInActivity::class.java)
//            startActivity(intent)
        }
        binding.buttonSignUp.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if( it.isSuccessful){
//                        val intent = Intent(this, LogInActivity::class.java)
//                        startActivity(intent)
                        Toast.makeText(activity, "UserCreation successful!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(activity, "UserCreation not successful!", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else{
                Toast.makeText(activity, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }


}