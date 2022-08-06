package elfak.mosis.freelencelive

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentJobReviewBinding
import elfak.mosis.freelencelive.dialogs.ChooseDateFragmentDialog
import elfak.mosis.freelencelive.dialogs.ChooseTimeFragmentDialog
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel

class JobReviewFragment : Fragment() {
    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private lateinit var binding: FragmentJobReviewBinding
    private var imageUri: Uri? = null
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()
    lateinit var pd: ProgressDialog
    private lateinit var currentJobName: String
    private lateinit var photosListTmp: MutableList<Bitmap>


    var brojSlika = 0

    //lateinit var viewItem: View
    lateinit var gallery: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        photosListTmp = mutableListOf()

        //if (userViewModel.selectedEvent.value?.photosList?.isEmpty() == true)
            FirebaseHelper.getPhotosForSingleEventFromDatabase(
                userViewModel.selectedEvent.value!!,
                requireContext(),
                userViewModel
            )


        binding = FragmentJobReviewBinding.inflate(inflater)
        userViewModel.InitialSetSelectedEvent()

        currentJobName = userViewModel.selectedEvent.value?.name.toString()

        val EventObserver = Observer<Event> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val event: Event = newValue
            //addFriendsToLinearLayout(lista, false, "")
            //addJobsToLinearLayout(lista)
            //fillControls(event)
            addPhotosOfThisEvent(event.photosList)
        }
        userViewModel.selectedEvent.observe(viewLifecycleOwner, EventObserver)


        return binding.root
        //return inflater.inflate(R.layout.fragment_job_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.jobTitleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //addEventViewModel.setEventName(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.setSelectedEventName(s.toString())
            }
        })

        binding.datePicker.setOnClickListener {
            val fragmentNovi = ChooseDateFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }
        binding.cancelButton.setOnClickListener {
            //findNavController().popBackStack()
            navigateBackToJobFragment()
        }

        binding.ApplyChanges.setOnClickListener {

            addPhotosToDb()
            updateUserInDatabase()
            navigateBackToJobFragment()
        }

        binding.timePicker.setOnClickListener {
            val fragmentNovi = ChooseTimeFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }
        binding.shapeableImageView.setOnClickListener {
            val action = JobReviewFragmentDirections.actionJobReviewToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        gallery = requireActivity().findViewById(R.id.gallery) //binding.gallery
        gallery.removeAllViews()
        gallery.removeAllViewsInLayout()

        inflater = LayoutInflater.from(requireContext())


        fillViewsWithData(userViewModel.selectedEvent.value!!)
        // addPhotosOfThisEvent(event.photosList)
    }

    private fun navigateBackToJobFragment(){
        userViewModel.deletePhotosOfSelectedEvent()
        val action = JobReviewFragmentDirections.actionJobReviewToJobs()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun addPhotosToDb() {

        if (photosListTmp.isNotEmpty()) {
            Toast.makeText(requireContext(), "Photos uploading..", Toast.LENGTH_SHORT).show()
            FirebaseHelper.addPhotosToDatabase(
                userViewModel.selectedEvent.value!!,
                photosListTmp,
                requireContext(),
                findNavController()
            )
        }
    }

    fun addPhotosOfThisEvent(photosList: MutableList<String>) {
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB

        gallery.removeAllViewsInLayout()
        gallery.removeAllViews()

        for (photo in photosList) {
            val viewItem: View = inflater.inflate(R.layout.photo_item, gallery, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
            fab.setOnClickListener {
                //BRISANJE SLIKE IZ LISTE SLIKA ZA OVAJ JOB
                FirebaseHelper.deleteSinglePhotoForSelectedEventFromDatabase(
                    viewItem,
                    photo,
                    userViewModel.selectedEvent.value!!,
                    requireContext(),
                    userViewModel.gsPhotosList.value!!,
                    userViewModel
                )

            }


            Glide.with(this).load(photo).into(imageView)
            imageView.setImageResource(R.drawable.no_photos)
            gallery.addView(viewItem)

        }
        addNewPhoto()
    }

    private fun addNewPhoto() {
        val viewItem = inflater.inflate(R.layout.photo_item, gallery, false)
        val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
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
        imageView.setOnClickListener {
            //Toast.makeText(context, "POZDRAV CLIKC!", Toast.LENGTH_LONG).show()
            OpenGalleryToAddPhoto()
        }
        gallery.addView(viewItem)
    }

    private fun OpenGalleryToAddPhoto() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    fun fillViewsWithData(event: Event) {

        binding.jobTitleText.setText(event.name)
        binding.organisatorUsername.setText(userViewModel.user.value?.userName)
        binding.checkBox.isChecked = event.finished

        Toast.makeText(requireContext(), event.date.toString(), Toast.LENGTH_SHORT).show()


    }

    fun CheckEmptyFields(): Boolean {
        return (binding.jobTitleText.text.toString().trim().isNotEmpty())
    }

    fun updateUserInDatabase() {
        val inputJobTitle = binding.jobTitleText.text.toString()

        var mapa: HashMap<String, Any> = hashMapOf()
        if (!inputJobTitle.equals(currentJobName))
            mapa.put("name", inputJobTitle)

        if (!binding.checkBox.isChecked.equals(userViewModel.selectedEvent.value?.finished))
            mapa.put("finished", binding.checkBox.isChecked)

        if (userViewModel.dateChanged) {

            var date: String? = userViewModel.selectedEvent.value?.date.toString()
            val lista = date?.split(" ")
            val dan = lista?.get(2)?.toInt()

            val dateHashMap = hashMapOf<String, Int>(
                "year" to userViewModel.selectedEvent.value?.date?.year!!,
                "month" to userViewModel.selectedEvent.value?.date?.month!!,
                "day" to dan!!,
                "hours" to userViewModel.selectedEvent.value?.date?.hours!!,
                "minutes" to userViewModel.selectedEvent.value?.date?.minutes!!
            )
            mapa.put("dateHashMap", dateHashMap)
        }

        if (mapa.isNotEmpty()) {
            pd.show()
            pd.setMessage("Updating event..")
            userViewModel.selectedEvent.value?.let {
                FirebaseHelper.updateEventData(
                    it,
                    mapa,
                    pd,
                    requireContext(),
                    findNavController()
                )
            }

        } else {
            Toast.makeText(requireContext(), "You didn't edit any data!", Toast.LENGTH_SHORT).show()
        }
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

            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            photosListTmp.add(bitmap)

            val fab: FloatingActionButton = viewItem.findViewById(R.id.fab)
            fab.setOnClickListener {
                //BRISANJE SLIKE IZ LISTE SLIKA ZA OVAJ JOB
                (viewItem.getParent() as ViewGroup).removeView(viewItem)
                photosListTmp.remove(bitmap)
            }
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            //imageView.setImageResource(R.drawable.add_photo)
            //imageView.setImageResource(android.R.drawable.ic_input_add)



            imageView.setImageURI(imageUri)
            //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500), android.graphics.PorterDuff.Mode.SRC_IN)
            //imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_500));
            imageView.minimumWidth = 100
            //imageView.setBackgroundColor(R.color.menuColor)
            imageView.setBackgroundResource(R.drawable.rectangle_11_shape)


            //userViewModel.setPhotoUrlToSelectedEvent(imageUri!!.toString())
            gallery.addView(viewItem, userViewModel.selectedEvent.value?.photosList?.size!!)

        }
    }
}