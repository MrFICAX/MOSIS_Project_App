package elfak.mosis.freelencelive

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentDashboardBinding
import elfak.mosis.freelencelive.databinding.FragmentMyProfileBinding


class MyProfileFragment : Fragment() {

    private var imageUri: Uri? = null
    private val pickImage = 100
    private lateinit var binding: FragmentMyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            //UPDATE USER IN DATABASE

            findNavController().popBackStack()
        }

        binding.shapeableImageView.setOnClickListener{
            val action = MyProfileFragmentDirections.actionMyProfileToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.imageCameraBackground.setImageBitmap(null)
            binding.imageCameraBackground.setImageURI(imageUri)
        }
    }


}