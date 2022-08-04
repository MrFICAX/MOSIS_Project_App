package elfak.mosis.freelencelive

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding
import org.w3c.dom.Text


class SignUpFragment : Fragment() {
    private var imageUri: Uri? = null
    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    lateinit var pd : ProgressDialog

    private lateinit var imageBitmap: Bitmap

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)

        imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.kravatica)
    }

    fun setViews(){
        binding.email.setText("ficax00@gmail.com")
        binding.password.setText("Aa1234.")
        binding.username.setText("ficax99")
        binding.firstName.setText("Filip")
        binding.lastName.setText("Trajkovic")
        binding.phoneNumber.setText("0616440562")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        setViews()


        val butLoadFromGallery = binding.openGallery
        butLoadFromGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            gallery.setType("image/*")
            startActivityForResult(gallery, pickImage)
        }

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

        //otvaranje kamere
        binding.imageCameraBackground.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.backToLogin.setOnClickListener{
            //findNavController().navigate(R.id.action_signup_goto_login)
            val action = SignUpFragmentDirections.actionSignupGotoLogin()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.buttonSignUp.setOnClickListener{


            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val username = binding.username.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()
//            if(!isValidPassword(password))
//                binding.password.error = "Password is not valid!"
//            else
//                binding.password.error = null

            if (CheckEmptyFields() && checkEmailStyle() && isValidPassword(password)){

                pd.setMessage("REGISTRATING...")
                pd.show()

                //FirebaseHelper.probniCloudFirestore(requireContext())
                FirebaseHelper.registerUser(
                email,
                password,
                    username,
                    firstName,
                    lastName,
                    phoneNumber,
                binding,
                imageBitmap,
                pd,
                this)
            }
            else{
                Toast.makeText(activity, "Empty fields not allowed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun CheckEmptyFields(): Boolean {
        return (   binding.email.text.toString().trim().isNotEmpty()
                && binding.password.text.toString().trim().isNotEmpty()
                && binding.username.text.toString().trim().isNotEmpty()
                && binding.firstName.text.toString().trim().isNotEmpty()
                && binding.lastName.text.toString().trim().isNotEmpty()
                && binding.phoneNumber.text.toString().trim().isNotEmpty())
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK ){//&& data != null && data.data != null) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageCameraBackground.setImageBitmap(imageBitmap)
            //imageView.setImageBitmap(imageBitmap)
        }
        if (resultCode == RESULT_OK && requestCode == pickImage && data != null && data.data != null) {
            imageUri = data?.data
            imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            //use the bitmap as you like
            binding.imageCameraBackground.setImageBitmap(imageBitmap);

            //binding.imageCameraBackground.setImageURI(imageUri)
        }
    }

    private fun getFileExtension(uri: Uri) : String? {
        val cR: ContentResolver? = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR?.getType(uri))
    }


}