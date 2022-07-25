package elfak.mosis.freelencelive

import android.R.attr.button
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.freelencelive.databinding.FragmentJobViewBinding


class JobViewFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private lateinit var binding: FragmentJobViewBinding
    private var imageUri: Uri? = null

    var brojSlika = 3
    var brojPrijatelja = 5
    //lateinit var viewItem: View
    lateinit var checkBox: CheckBox
    lateinit var galleryView: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var friendsView: LinearLayout
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobViewBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_job_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBox = requireActivity().findViewById(R.id.checkBox)
        galleryView =  requireActivity().findViewById(R.id.galleryView) //binding.gallery
        friendsView = requireActivity().findViewById(R.id.FriendsView)
        inflater = LayoutInflater.from(requireContext())

        checkCheckBox()
        addPhotosToGalleryView()
        addFriendsToFriendsView()
        setClickListeners()

    }

    private fun setClickListeners() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addFriendsToFriendsView() {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for(i in 0..brojPrijatelja - 1){
            val viewItem: View = inflater.inflate(R.layout.friend_item, friendsView, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            var sendRequestButton: Button = viewItem.findViewById(R.id.send_request_button) as MaterialButton

            if(i < 2) {// == prijatelj)
                sendRequestButton.setText("FRIENDS")
                sendRequestButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                )
            }
            else{
                sendRequestButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                )
                sendRequestButton.setOnClickListener {
                //SLANJE ZAHTEVA ZA PRIJATELJSTVO
                    Toast.makeText(requireContext(), "SALJEM ZAHTEV ZA PRIJATELJSTVO!", Toast.LENGTH_LONG).show()
                }
            }


            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)

            val textView: TextView = viewItem.findViewById(R.id.userNameText) as TextView
            textView.setText("Prijatelj" + i.toString())


            friendsView.addView(viewItem)
        }
    }

    private fun addPhotosToGalleryView() {
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for(i in 0..brojSlika - 1){
            val viewItem: View = inflater.inflate(R.layout.photo_item, galleryView, false)
            val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
            fab.isVisible = false

            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)

            galleryView.addView(viewItem)
        }
        if(brojSlika == 0){
            addPhotoIfNoPhotosInGallery()
        }    }

    private fun checkCheckBox() {
        checkBox.isChecked = true
        checkBox.isClickable = false

    }

    private fun addPhotoIfNoPhotosInGallery(){
        val viewItem = inflater.inflate(R.layout.photo_item, galleryView, false)
        val imageView:ImageView = viewItem.findViewById(R.id.imageView) as ImageView
        imageView.setImageResource(R.drawable.no_photos)
        //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500));
      imageView.minimumWidth = 100
        //imageView.setBackgroundColor(R.color.menuColor)
        //imageView.setBackgroundResource(R.drawable.rectangle_11_shape)
        val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
        fab.isVisible = false

        galleryView.addView(viewItem)
    }
}