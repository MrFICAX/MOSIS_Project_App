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
import elfak.mosis.freelencelive.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_login_goto_signup)

            //            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if( it.isSuccessful){
//                        val intent = Intent(this, TestDatabaseActivity::class.java)
//                        startActivity(intent)
                        Toast.makeText(activity, "UserLogin successful!", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(activity, "Login not successful!", Toast.LENGTH_LONG).show()

                    }

                }
            }
            else{
                Toast.makeText(activity, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }
    }

}