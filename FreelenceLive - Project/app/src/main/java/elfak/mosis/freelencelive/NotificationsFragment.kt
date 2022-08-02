package elfak.mosis.freelencelive

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.askToJoin
import elfak.mosis.freelencelive.data.friendRequest
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentInvitationsBinding
import elfak.mosis.freelencelive.databinding.FragmentNotificationsBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationsFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null

    var brojInvitationsa = 3
    lateinit var notificationsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()
    var usersDownloaded = false
    var askToJoinsDownloaded = false
    var eventsDownloaded = false


    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userViewModel.users.value?.isEmpty() == true) {
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
        }

        if (userViewModel.askToJoin.value?.isEmpty() == true) {
            FirebaseHelper.getAllAskToJoins(requireContext(), userViewModel)
        }

        if (userViewModel.events.value?.isEmpty() == true)
            FirebaseHelper.getAllEvents(requireContext(), userViewModel)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater)

        val EventsObserver = Observer<List<Event>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<Event> = newValue
            //addFriendsToLinearLayout(lista, false, "")
            //writeAllEventsOverlays(lista)
            if (newValue.isNotEmpty()) {
                eventsDownloaded = true
                addNotificationsToLinearLayout()
            }

        }
        userViewModel.events.observe(viewLifecycleOwner, EventsObserver)

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)

            if (!newValue.isEmpty()) {

                usersDownloaded = true
                addNotificationsToLinearLayout()
            }

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)

        val AskToJoinsObserver = Observer<List<askToJoin>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
//            val lista: List<User> = newValue

            if (!newValue.isEmpty()) {
                askToJoinsDownloaded = true
                addNotificationsToLinearLayout()

            }


        }
        userViewModel.askToJoin.observe(viewLifecycleOwner, AskToJoinsObserver)

        return binding.root
        //return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsLayout =
            requireActivity().findViewById(R.id.NotificationsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = NotificationsFragmentDirections.actionNotificationsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        //addNotificationsToLinearLayout()
    }

    private fun addNotificationsToLinearLayout() {
        val myId: String = userViewModel.user.value?.id.toString()

        notificationsLayout.removeAllViewsInLayout()

        if (usersDownloaded && eventsDownloaded && askToJoinsDownloaded) {

            val listaKorisnika: List<User>? = userViewModel.users.value
            val listaEventa: List<Event>? = userViewModel.events.value
            val listaAskToJoins: List<askToJoin>? = userViewModel.askToJoin.value

            if (listaAskToJoins != null) {
                for (singleAskToJoin in listaAskToJoins) {
                    val joinToJob: Event? =
                        listaEventa?.filter { it.id.equals(singleAskToJoin.joinToJob) }
                            ?.firstOrNull()

                    if (joinToJob?.organiser.equals(myId)) {

                        val viewItem: View =
                            inflater.inflate(
                                R.layout.fragment_notification_item,
                                notificationsLayout,
                                false
                            )

                        val issuedByUser: User? =
                            listaKorisnika?.filter { it.id.equals(singleAskToJoin.issuedBy) }
                                ?.firstOrNull()


                        val imageView: ImageView =
                            viewItem.findViewById(R.id.imageCameraBackground) as ImageView
                        val usernameView: TextView = viewItem.findViewById(R.id.username)
                        val jobTitle: TextView = viewItem.findViewById(R.id.JobTitleInvitations)
                        val dateTitle: TextView = viewItem.findViewById(R.id.DateTextInvitations)
                        val itemMap: ImageView =
                            viewItem.findViewById(R.id.IconMapInvitations) as ImageView
                        val acceptButton: LinearLayout =
                            viewItem.findViewById(R.id.linearLayoutAccept) as LinearLayout
                        val declineButton: LinearLayout =
                            viewItem.findViewById(R.id.linearLayoutDecline) as LinearLayout

                        itemMap.setOnClickListener {
                            if (joinToJob != null) {
                                userViewModel.setSelectedEvent(joinToJob)
                                Toast.makeText(
                                    requireContext(),
                                    "KLIK NA VIEW LOCATION!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                val action =
                                    NotificationsFragmentDirections.actionNotificationsToViewLocationOnMap()
                                findNavController().navigate(action)
                            }
                        }
                        acceptButton.setOnClickListener {
                            Toast.makeText(requireContext(), "ACCEPT BUTTON!", Toast.LENGTH_LONG)
                                .show()

                            if (joinToJob != null) {
                                FirebaseHelper.acceptAskToJoin(
                                    singleAskToJoin,
                                    joinToJob,
                                    requireContext(),
                                    userViewModel,
                                    viewItem,
                                    notificationsLayout
                                )
                            }
                        }
                        declineButton.setOnClickListener {
                            Toast.makeText(requireContext(), "DECLINE BUTTON!", Toast.LENGTH_LONG)
                                .show()
                            FirebaseHelper.declineAskToJoin(
                                singleAskToJoin,
                                requireContext(),
                                userViewModel,
                                viewItem,
                                notificationsLayout
                            )
                        }

                        if (issuedByUser != null) {

                            Glide.with(this).load(issuedByUser?.imageUrl).into(imageView)

                            usernameView.setText(issuedByUser?.userName)
                        }

                        jobTitle.setText(joinToJob?.name)
                        var date: String? = joinToJob?.date.toString()
                        val lista = date?.split(" ")
                        val year = joinToJob?.date?.year
                        val month = lista?.get(1)
                        val day = lista?.get(2)//lista?.get(2)?.toInt()
                        val hourMinute = lista?.get(3)?.split(":")
                        var hour: String? =
                            hourMinute?.get(0)
                        var minute: String? =
                            hourMinute?.get(1)

                        val dateString: String =
                            day.toString() + "/" + month + "/" + year.toString() + " " + hour + ":" + minute + "h"
                        dateTitle.setText(dateString)


                        notificationsLayout.addView(viewItem)
                    }
                }
            }
        }

    }

}