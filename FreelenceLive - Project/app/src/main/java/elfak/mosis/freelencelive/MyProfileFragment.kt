package elfak.mosis.freelencelive

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentDashboardBinding
import elfak.mosis.freelencelive.databinding.FragmentMyProfileBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel


class MyProfileFragment : Fragment() {

    private val userViewModel: userViewModel by activityViewModels()
    lateinit var pd : ProgressDialog
    private val fragmentViewModel: fragmentViewModel by activityViewModels()

    private var imageUri: Uri? = null
    private val pickImage = 100
    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val butLoadFromGallery = binding.changePhoto
        butLoadFromGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        val cancelButton = binding.cancelButton
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val applyButton = binding.ApplyChanges
        applyButton.setOnClickListener {

            if (CheckEmptyFields())
            {
                updateUserInDatabase()
            } else{
                Toast.makeText(requireContext(), "Polja ne smeju biti prazna", Toast.LENGTH_LONG).show()
            }
        }

        binding.shapeableImageView.setOnClickListener{
            val action = MyProfileFragmentDirections.actionMyProfileToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }


            fillViewsWithData()
    }

    fun fillViewsWithData(){
        Glide.with(this).load(userViewModel.user.value?.imageUrl).into(binding.imageCameraBackground)
        binding.usernameText.setText(userViewModel.user.value?.userName)

        binding.phoneNumberText.setText(userViewModel.user.value?.phoneNumber)
        binding.firstNameText.setText(userViewModel.user.value?.firstName)
        binding.lastNameText.setText(userViewModel.user.value?.lastName)
        binding.EmailText.setText(userViewModel.user.value?.email)

    }

    fun CheckEmptyFields(): Boolean{
        return (   binding.usernameText.text.toString().trim().isNotEmpty()
                && binding.firstNameText.text.toString().trim().isNotEmpty()
                && binding.lastNameText.text.toString().trim().isNotEmpty()
                && binding.phoneNumberText.text.toString().trim().isNotEmpty())
    }
    fun updateUserInDatabase(){
        val inputUsername =  binding.usernameText.text.toString()
        val inputFirstName = binding.firstNameText.text.toString()
        val inputLastName = binding.lastNameText.text.toString()
        val inputPhoneNumber = binding.phoneNumberText.text.toString()


        var mapa: HashMap<String, String> = hashMapOf()
        if(!inputUsername.equals(userViewModel.user.value?.userName))
            mapa.put("userName", inputUsername)
        if(!inputFirstName.equals(userViewModel.user.value?.firstName))
            mapa.put("firstName", inputFirstName)
        if(!inputLastName.equals(userViewModel.user.value?.lastName))
            mapa.put("lastName", inputLastName)
        if(!inputPhoneNumber.equals(userViewModel.user.value?.phoneNumber))
            mapa.put("phoneNumber", inputPhoneNumber)

        FirebaseHelper.updateUserData(mapa, userViewModel, pd, requireContext(), findNavController())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {

            pd.show()
            pd.setTitle("UPDATING PHOTO..")
            pd.setMessage("WAIT A SECOND..")

            imageUri = data?.data
            val bitmapa:Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

            FirebaseHelper.updateProfilePhoto(pd, bitmapa, userViewModel, binding, imageUri)

        }
    }


}