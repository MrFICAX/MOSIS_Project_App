package elfak.mosis.freelencelive

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.friendRequest
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentInvitationsBinding
import elfak.mosis.freelencelive.model.userViewModel

class InvitationsFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null
    lateinit var pd: ProgressDialog

    var brojInvitationsa = 3
    lateinit var invitationsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()
    var usersDownloaded = false
    var friendRequestsDownloaded = false

    private lateinit var binding: FragmentInvitationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)

        if (userViewModel.users.value?.isEmpty() == true) {
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
        }

        if (userViewModel.friendReqeusts.value?.isEmpty() == true) {
            FirebaseHelper.getAllFriendRequests(requireContext(), userViewModel)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invitationsLayout = requireActivity().findViewById(R.id.InvitationsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = InvitationsFragmentDirections.actionInvitationsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvitationsBinding.inflate(inflater)

//        if (userViewModel.users.value?.isEmpty() == true) {
//            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
//        }
//
//        if (userViewModel.friendReqeusts.value?.isEmpty() == true) {
//            FirebaseHelper.getAllFriendRequests(requireContext(), userViewModel)
//        }
        if (userViewModel.invitations.value?.isEmpty() == true) {
            //FirebaseHelper.getAllInvitations(requireContext(), userViewModel)
        }

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)

            if (!newValue.isEmpty()) {

                usersDownloaded = true
                invitationsLayout.removeAllViewsInLayout()
                userViewModel.events.value?.let { addInvitationsToLinearLayout(it) }
                addFriendsRequestsToLinearLayout()
            }

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)

        val FriendsRequestsObserver = Observer<List<friendRequest>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
//            val lista: List<User> = newValue

            if (!newValue.isEmpty()) {
                friendRequestsDownloaded = true
                invitationsLayout.removeAllViewsInLayout()
                userViewModel.events.value?.let { addInvitationsToLinearLayout(it) }
                addFriendsRequestsToLinearLayout()

            }


        }
        userViewModel.friendReqeusts.observe(viewLifecycleOwner, FriendsRequestsObserver)

        val EventObserver = Observer<List<Event>> { newValue ->

//            addInvitationsToLinearLayout(newValue)

            invitationsLayout.removeAllViewsInLayout()
            addInvitationsToLinearLayout(newValue)
            addFriendsRequestsToLinearLayout()


        }
        userViewModel.events.observe(viewLifecycleOwner, EventObserver)


        return binding.root
        //return inflater.inflate(R.layout.fragment_invitations, container, false)
    }

    private fun refreshInvitationsLayout() {

    }


    private fun addInvitationsToLinearLayout(listOfEvents: List<Event>) {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB

        val listaKorisnika: List<User>? = userViewModel.users.value
        val myId: String = userViewModel.user.value?.id.toString()
        for (singleEvent in listOfEvents) {

            var hashMapFriends: HashMap<String, Boolean> = singleEvent.listOfUsers
            val IsAccepted = hashMapFriends[myId].toString()

            if (IsAccepted.equals("false")) {

                val viewItem: View =
                    inflater.inflate(R.layout.fragment_invitation_item, invitationsLayout, false)
                val imageView: ImageView =
                    viewItem.findViewById(R.id.userProfilePictureInvitations) as ImageView
                val usernameView: TextView = viewItem.findViewById(R.id.usernameTextInvitations)
                val jobTitle: TextView = viewItem.findViewById(R.id.JobTitleInvitations)
                val dateTitle: TextView = viewItem.findViewById(R.id.DateTextInvitations)
                val itemMap: ImageView = viewItem.findViewById(R.id.IconMapInvitations) as ImageView
                val acceptButton: LinearLayout =
                    viewItem.findViewById(R.id.linearLayoutAccept) as LinearLayout
                val declineButton: LinearLayout =
                    viewItem.findViewById(R.id.linearLayoutDecline) as LinearLayout


                val issuedByUser: User? =
                    listaKorisnika?.filter { it.id.equals(singleEvent.organiser) }?.firstOrNull()

                if (issuedByUser != null) {

                    Glide.with(this).load(issuedByUser?.imageUrl).into(imageView)

                    usernameView.setText(issuedByUser?.userName)
                }

                itemMap.setOnClickListener {
                    userViewModel.setSelectedEvent(singleEvent)
//                    Toast.makeText(requireContext(), "KLIK NA VIEW LOCATION!", Toast.LENGTH_LONG)
//                        .show()
                    val action =
                        InvitationsFragmentDirections.actionInvitationsToViewLocationOnMap()
                    findNavController().navigate(action)
                }
                acceptButton.setOnClickListener {
                    //Toast.makeText(requireContext(), "ACCEPT BUTTON!", Toast.LENGTH_LONG).show()
                    pd.show()
                    acceptInvitation(singleEvent, userViewModel, viewItem, invitationsLayout)

                }
                declineButton.setOnClickListener {
                    //Toast.makeText(requireContext(), "DECLINE BUTTON!", Toast.LENGTH_LONG).show()
                    pd.show()
                    declineInvitation(singleEvent, userViewModel, viewItem, invitationsLayout)
                }

                jobTitle.setText(singleEvent.name)


                var date: String? = singleEvent?.date.toString()
                val lista = date?.split(" ")
                val year = singleEvent.date.year
                val month = lista?.get(1)
                val day = lista?.get(2)//lista?.get(2)?.toInt()
                val hourMinute = lista?.get(3)?.split(":")
                var hour: String? =
                    hourMinute?.get(0)//event.date.hours.toString()//lista?.get(3)?.toInt()
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
                val dateString: String =
                    day.toString() + "/" + month + "/" + year.toString() + " " + hour + ":" + minute + "h"
                dateTitle.setText(dateString)

                invitationsLayout.addView(viewItem)
            }
        }
    }

    public fun addFriendsRequestsToLinearLayout() {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB

        //invitationsLayout.removeAllViewsInLayout()

        if (usersDownloaded && friendRequestsDownloaded) {


            var listaKorisnika: List<User> = userViewModel.users.value!!
            var listaRequesta: List<friendRequest> = userViewModel.friendReqeusts.value!!


            for (singleRequest in listaRequesta) {

                if (singleRequest.requestTo.equals(FirebaseAuth.getInstance().currentUser?.uid)) {


                    val viewItem: View = inflater.inflate(
                        R.layout.fragment_friend_request_item,
                        invitationsLayout,
                        false
                    )
                    val imageView: ImageView =
                        viewItem.findViewById(R.id.userProfilePictureInvitations) as ImageView
                    val usernameView: TextView = viewItem.findViewById(R.id.usernameTextInvitations)

                    val acceptButton: LinearLayout =
                        viewItem.findViewById(R.id.linearLayoutAccept) as LinearLayout
                    val declineButton: LinearLayout =
                        viewItem.findViewById(R.id.linearLayoutDecline) as LinearLayout

                    acceptButton.setOnClickListener {
                        Toast.makeText(requireContext(), "ACCEPT BUTTON!", Toast.LENGTH_LONG).show()
                        acceptRequest(singleRequest, viewItem, invitationsLayout)
                    }
                    declineButton.setOnClickListener {
                        Toast.makeText(requireContext(), "DECLINE BUTTON!", Toast.LENGTH_LONG)
                            .show()
                        declineRequest(singleRequest, viewItem, invitationsLayout)
                    }

                    val issuedByUser: User? =
                        listaKorisnika.filter { it.id.equals(singleRequest.issuedBy) }.firstOrNull()

                    if (issuedByUser != null) {

                        Glide.with(this).load(issuedByUser?.imageUrl).into(imageView)
//                    if (i < 2)
//                        imageView.setImageResource(R.drawable.img_0944)
//                    else
//                        imageView.setImageResource(R.drawable.img_0950)

                        usernameView.setText(issuedByUser?.userName)//"Prijatelj" + i.toString())

                        invitationsLayout.addView(viewItem)
                    }
                }
            }
        }

    }

    private fun declineRequest(
        singleRequest: friendRequest,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {
        FirebaseHelper.declineRequest(
            singleRequest, requireContext(), userViewModel, viewItem,
            invitationsLayout
        )

    }

    private fun acceptRequest(
        singleRequest: friendRequest,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {

        FirebaseHelper.acceptRequest(
            singleRequest,
            requireContext(),
            userViewModel,
            viewItem,
            invitationsLayout
        )
    }

    private fun acceptInvitation(
        event: Event,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {

        FirebaseHelper.acceptJobInvitation(
            event,
            userViewModel,
            pd,
            viewItem,
            invitationsLayout,
            requireContext()
        )
    }

    private fun declineInvitation(
        singleEvent: Event,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {
        FirebaseHelper.declineJobInvitation(
            singleEvent,
            userViewModel,
            pd,
            viewItem,
            invitationsLayout,
            requireContext()
        )
    }
}