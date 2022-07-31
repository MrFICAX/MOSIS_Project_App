package elfak.mosis.freelencelive

import android.R.attr.button
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.friendRequest
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentJobViewBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel
import java.util.HashMap


class JobViewFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private lateinit var binding: FragmentJobViewBinding
    private var imageUri: Uri? = null
    private lateinit var selectedEvent: Event

    var brojSlika = 3
    var brojPrijatelja = 5

    //lateinit var viewItem: View
    lateinit var checkBox: CheckBox
    lateinit var galleryView: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var friendsView: LinearLayout
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()
    lateinit var pd: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fragmentViewModel.setFragment(null)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobViewBinding.inflate(inflater)

        if (userViewModel.friendReqeusts.value?.isEmpty() == true)
            FirebaseHelper.getAllFriendRequests(requireContext(), userViewModel)

        val friendRequestObserver = Observer<List<friendRequest>> { newValue ->
//            //binding.buttonCreateJob.setText(newValue)
//            val lista: List<User> = newValue
//            addFriendsToLinearLayout(lista, false, "")
            addFriendsToFriendsView()

        }
        userViewModel.friendReqeusts.observe(viewLifecycleOwner, friendRequestObserver)

        return binding.root
        //return inflater.inflate(R.layout.fragment_job_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedEvent = userViewModel.selectedEvent.value!!

        checkBox = requireActivity().findViewById(R.id.checkBox)
        galleryView = requireActivity().findViewById(R.id.galleryView) //binding.gallery
        friendsView = requireActivity().findViewById(R.id.FriendsView)
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = JobViewFragmentDirections.actionJobViewToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        addPhotosToGalleryView()
        addFriendsToFriendsView()
        setClickListeners()

        fillViewsWithData()
    }

    private fun fillViewsWithData() {
        checkCheckBox()
        binding.JobText.setText(selectedEvent.name)

        val issuedByUser: User? =
            userViewModel.users.value?.filter { it.id.equals(selectedEvent.organiser) }
                ?.firstOrNull()
        binding.organisatorUsername.setText(issuedByUser?.userName)

        var date: String? = selectedEvent?.date.toString()
        val lista = date?.split(" ")
        val year = selectedEvent.date.year
        val month = lista?.get(1)
        val day = lista?.get(2)//lista?.get(2)?.toInt()
        val hourMinute = lista?.get(3)?.split(":")
        var hour: String? = hourMinute?.get(0)//event.date.hours.toString()//lista?.get(3)?.toInt()
        var minute: String? =
            hourMinute?.get(1)//event.date.minutes.toString()//lista?.get(4)?.toInt()
//            var noviHour: String = hour
//            var noviMinute: String = minute
//            if (hour.toInt() < 10){
//                noviHour = "0"+hour
//            }
//            if (minute.toInt() < 10){
//                noviMinute = "0"+minute
//            }
        val dateString: String = day.toString() + "/" + month + "/" + year.toString()
        val timeString: String = hour + ":" + minute + "h"

        binding.dateText.setText(dateString)
        binding.timeText.setText(timeString)

        addPhotosToGalleryView()
        addFriendsToFriendsView()


    }

    private fun setClickListeners() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addFriendsToFriendsView() {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB

        friendsView.removeAllViewsInLayout()

        var hashMapFriends: HashMap<String, Boolean> = selectedEvent.listOfUsers
        var listaKorisnika: MutableList<User> = mutableListOf()

        hashMapFriends.forEach { keyValue ->

            val userTmp =
                userViewModel.users.value?.filter { it.id.equals(keyValue.key) }?.firstOrNull()
            userTmp?.let { listaKorisnika.add(it) }
        }

        for (singleUser in listaKorisnika) {
            val viewItem: View = inflater.inflate(R.layout.friend_item, friendsView, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val imageView2: ImageView = viewItem.findViewById(R.id.imageView2) as ImageView
            var sendRequestButton: Button =
                viewItem.findViewById(R.id.send_request_button) as MaterialButton

            val IsAccepted = hashMapFriends[singleUser.id].toString()
            Log.d("IsAccepted", IsAccepted.toString())

            if (IsAccepted.equals("true")) {
                imageView2.setImageResource(R.drawable.accept)

            } else {
                imageView2.setImageResource(R.drawable.question)

            }

            if (userViewModel.user.value?.friendsList?.containsKey(singleUser.id) == true) {

                sendRequestButton.setText("FRIENDS")
                sendRequestButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                )
            } else {
                if (!checkIfRequestExists(singleUser.id)) {
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
                        Toast.makeText(
                            requireContext(),
                            "SALJEM ZAHTEV ZA PRIJATELJSTVO!",
                            Toast.LENGTH_LONG
                        ).show()
                        pd.setMessage("REGISTRATING...")
                        pd.show()
                        sendfriendRequest(singleUser.id, sendRequestButton)
                    }
                } else {
                    sendRequestButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.yellow
                            )
                        )
                    )
                    sendRequestButton.setText("REQUEST SENT")
                    sendRequestButton.isClickable = false
                }
            }

            Glide.with(this).load(singleUser?.imageUrl).into(imageView)


            val textView: TextView = viewItem.findViewById(R.id.userNameText) as TextView
            textView.setText(singleUser.userName)


            friendsView.addView(viewItem)
        }
    }

    private fun checkIfRequestExists(friendId: String): Boolean {
        var flag = false
        val lista: List<friendRequest>? = userViewModel.friendReqeusts.value
        lista?.forEach {
            if (it.issuedBy.equals(FirebaseAuth.getInstance().currentUser?.uid)
                && it.requestTo.equals(friendId)
            ) {
                return true
            }
        }
        return flag
    }

    private fun sendfriendRequest(userId: String, sendRequestButton: Button) {
        FirebaseHelper.sendFriendsRequest(
            userId,
            requireContext(),
            pd,
            sendRequestButton,
            userViewModel
        )
    }

    private fun addPhotosToGalleryView() {
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for (i in 0..brojSlika - 1) {
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
        if (brojSlika == 0) {
            addPhotoIfNoPhotosInGallery()
        }
    }

    private fun checkCheckBox() {
        checkBox.isChecked = selectedEvent.finished
        checkBox.isClickable = false

    }

    private fun addPhotoIfNoPhotosInGallery() {
        val viewItem = inflater.inflate(R.layout.photo_item, galleryView, false)
        val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
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