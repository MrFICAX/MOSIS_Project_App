package elfak.mosis.freelencelive

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.freelencelive.databinding.FragmentJobReviewBinding
import elfak.mosis.freelencelive.dialogs.ChooseDateFragmentDialog
import elfak.mosis.freelencelive.dialogs.ChooseTimeFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog

class JobReviewFragment : Fragment() {
    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private lateinit var binding: FragmentJobReviewBinding
    private var imageUri: Uri? = null

    var brojSlika = 0

    //lateinit var viewItem: View
    lateinit var gallery: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobReviewBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_job_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datePicker.setOnClickListener{
            val fragmentNovi = ChooseDateFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        binding.timePicker.setOnClickListener{
            val fragmentNovi = ChooseTimeFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        gallery =  requireActivity().findViewById(R.id.gallery) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for(i in 0..brojSlika - 1){
            val viewItem: View = inflater.inflate(R.layout.photo_item, gallery, false)
            val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
            fab.setOnClickListener {
                //BRISANJE SLIKE IZ LISTE SLIKA ZA OVAJ JOB
                (viewItem.getParent() as ViewGroup).removeView(viewItem)
                brojSlika--
            }

            val imageView:ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)
            gallery.addView(viewItem)
            brojSlika++
        }
        addNewPhoto()

    }


    private fun addNewPhoto(){
        val viewItem = inflater.inflate(R.layout.photo_item, gallery, false)
        val imageView:ImageView = viewItem.findViewById(R.id.imageView) as ImageView
        //imageView.setImageResource(R.drawable.add_photo)
        imageView.setImageResource(android.R.drawable.ic_input_add)
        //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500), android.graphics.PorterDuff.Mode.SRC_IN)
        imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500));
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(160, 160);
        imageView.setLayoutParams(params)
        imageView.minimumWidth = 100
        imageView.setBackgroundColor(R.color.menuColor)
        imageView.setBackgroundResource(R.drawable.rectangle_11_shape)
        val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
        fab.isVisible = false
        //imageView.background = R.drawable.rectangle_10_shape.toDrawable()
        imageView.setOnClickListener{
            Toast.makeText(context, "POZDRAV CLIKC!", Toast.LENGTH_LONG).show()
            OpenGalleryToAddPhoto()
        }
        gallery.addView(viewItem)
    }

    private fun OpenGalleryToAddPhoto() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            imageBitmap = data?.extras?.get("data") as Bitmap
//            formCheck[0] = true
//            binding.openCamera.setImageBitmap(null)
//            binding.imageCameraBackground.setImageBitmap(imageBitmap)
//            //imageView.setImageBitmap(imageBitmap)
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val viewItem = inflater.inflate(R.layout.photo_item, gallery, false)

            val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
            fab.setOnClickListener {
                //BRISANJE SLIKE IZ LISTE SLIKA ZA OVAJ JOB
                (viewItem.getParent() as ViewGroup).removeView(viewItem)
                brojSlika--
            }
            val imageView:ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            //imageView.setImageResource(R.drawable.add_photo)
            //imageView.setImageResource(android.R.drawable.ic_input_add)
            imageView.setImageURI(imageUri)
            //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500), android.graphics.PorterDuff.Mode.SRC_IN)
            //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500));
            imageView.minimumWidth = 100
            //imageView.setBackgroundColor(R.color.menuColor)
            imageView.setBackgroundResource(R.drawable.rectangle_11_shape)

            gallery.addView(viewItem, brojSlika)
            brojSlika++

        }
    }
}