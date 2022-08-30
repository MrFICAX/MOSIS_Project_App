package elfak.mosis.freelencelive

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentLogInBinding
import elfak.mosis.freelencelive.model.userViewModel


class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val userViewModel: userViewModel by activityViewModels()
    lateinit var pd : ProgressDialog

    fun setViews(){
        binding.email.setText("ficax00@gmail.com")
        binding.password.setText("Aa1234.")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        pd = ProgressDialog(context)
        pd.setCancelable(false)


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

        setViews()

        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkEmailStyle()
            }
        })

        binding.password.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(!isValidPassword(p0.toString()))
                {
                    binding.passwordLayout.isErrorEnabled = true
                    binding.passwordLayout.error = "Password is not valid!"
                }
                else
                {
                    binding.passwordLayout.error = null
                    binding.passwordLayout.isErrorEnabled = false
                }
            }

        })

        binding.register.setOnClickListener {
            //findNavController().navigate(R.id.action_login_goto_signup)
            val action = LogInFragmentDirections.actionLoginGotoSignup()
            NavHostFragment.findNavController(this).navigate(action)

            //            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener{
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (CheckEmptyFields() && checkEmailStyle() && isValidPassword(password)){

                pd.setMessage("REGISTRATING...")
                pd.show()

//                val intent = Intent(requireActivity(), MainWindowActivity::class.java)
//                //intent.putExtra("user", user as Serializable)
//
//                startActivity(intent)
//
                FirebaseHelper.logInUser(
                    requireContext(),
                    email,
                    password,
                    pd,
                    this,
                    requireActivity(),
                    userViewModel
                     )


//                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
//                    if( it.isSuccessful){
////                        val intent = Intent(this, TestDatabaseActivity::class.java)
////                        startActivity(intent)
//                        Toast.makeText(activity, "UserLogin successful!", Toast.LENGTH_LONG).show()
//
//                        val intent = Intent(context, MainWindowActivity::class.java)
//                        // To pass any data to next activity
//                        //intent.putExtra("keyIdentifier", value)
//                        // start your next activity
//                        startActivity(intent)
//                    }
//                    else{
//                        Toast.makeText(activity, "Login not successful!", Toast.LENGTH_LONG).show()
//
//                    }
//
//                }
            }
            else{
                Toast.makeText(activity, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun CheckEmptyFields(): Boolean {
        return (   binding.email.text.toString().trim().isNotEmpty()
                && binding.password.text.toString().trim().isNotEmpty())
    }

    private fun checkEmailStyle(): Boolean{
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches())
        {
            binding.emailLayout.isErrorEnabled = false
            binding.emailLayout.error = null
            return true
        }
        else{

            binding.emailLayout.isErrorEnabled = true
            binding.emailLayout.error = "Format of the email is not allowed!"
            return false
        }
    }

    internal fun isValidPassword(password: String): Boolean {
        if (password.length < 5) return false //minimum 5 karaktera
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false //mora da sadrzi cifru, ali ne na prvom mestu
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false // mora da sadrzi veliko slovo
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false // mora da sadrzi malo slovo
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false // mora da sadrzi samo brojeve ili slova

        return true
    }

}