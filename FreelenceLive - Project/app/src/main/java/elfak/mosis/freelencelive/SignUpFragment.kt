package elfak.mosis.freelencelive

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var imageUri: Uri? = null
    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

//        val spinnerLanguages: Spinner = requireView().findViewById<Spinner>(R.id.spinnerPhoneCodes)
//        val languages: ArrayList<String> = arrayListOf("asd", "qwer", "zxv","asdasd","assadfsda","asdasdfasdf","sadf","wqeqw")
//        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
//            requireContext(), android.R.layout.simple_spinner_item,
//            languages
//        )
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerLanguages.adapter = spinnerArrayAdapter
//        spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                Toast.makeText(requireActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
//            }
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }
        val butLoadFromGallery = binding.openGallery
        butLoadFromGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        val butCam = binding.openCamera

        //otvaranje kamere
        butCam.setOnClickListener {

            dispatchTakePictureIntent()

        }
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            formCheck[0] = true
            binding.openCamera.setImageBitmap(null)
            binding.imageCameraBackground.setImageBitmap(imageBitmap)
            //imageView.setImageBitmap(imageBitmap)
        }
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.openCamera.setImageBitmap(null)
            binding.imageCameraBackground.setImageURI(imageUri)
        }
    }


}