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
import androidx.navigation.NavAction
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentMyJobsBinding
import elfak.mosis.freelencelive.dialogs.AdvancedSearchFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyJobsFragment : Fragment() {
    var flag: Boolean = false

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null

    var brojJobsa = 10
    lateinit var jobsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()


    private lateinit var binding: FragmentMyJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userViewModel.restartAdvancedSearch()
        if (userViewModel.users.value?.isEmpty() == true)
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)

        if (userViewModel.events.value?.isEmpty() == true)
            FirebaseHelper.getAllEvents(requireContext(), userViewModel)

        val EventsObserver = Observer<List<Event>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<Event> = newValue
            //addFriendsToLinearLayout(lista, false, "")
            addJobsToLinearLayout(lista)
        }
        userViewModel.events.observe(viewLifecycleOwner, EventsObserver)

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            //val lista: List<User> = newValue
            userViewModel.events.value?.let { addJobsToLinearLayout(it) }

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)

        val searchFieldsSetAdvancedSearchObserver = Observer<List<Boolean>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            if (userViewModel.events.value?.isNotEmpty() == true) {
                writeFilteredEventsOverlays(userViewModel.events.value!!, newValue)
            }
        }
        userViewModel.searchFieldsSetAdvancedSearch.observe(viewLifecycleOwner, searchFieldsSetAdvancedSearchObserver)


        // Inflate the layout for this fragment
        binding = FragmentMyJobsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_my_jobs, container, false)
    }

    private fun filterByStringEventName(listaEventa: List<Event>, filterString: String): List<Event> {

        return listaEventa.filter { it.name.contains(filterString) }
    }

    private fun filterByStringEventOrganiserName(listaEventa: List<Event>, filterString: String): List<Event> {
        val listaKorisnika: List<User>? = userViewModel.users.value

        var tmpLista: MutableList<Event> = mutableListOf()

        listaEventa.forEach { event ->
            val issuedByUser: User? = listaKorisnika?.filter { it.id.equals(event.organiser) }?.firstOrNull()
            if (issuedByUser != null)
                tmpLista.add(event)
        }
        return tmpLista
    }

    private fun filterByFinished(listaEventa: List<Event>, value: Boolean): List<Event> {
        return listaEventa.filter { it.finished.equals(value) }
    }

    private fun filterByMyJobs(listaEventa: List<Event>, value: Boolean): List<Event> {
        return listaEventa.filter { it.organiser.equals(FirebaseAuth.getInstance().currentUser?.uid) }
    }

    private fun writeFilteredEventsOverlays(lista: List<Event>, listOfBooleans: List<Boolean>?) {

        var listOfFilteredEvents: List<Event> = lista
        if (listOfBooleans?.get(0) == true)
            listOfFilteredEvents = filterByStringEventName(
                listOfFilteredEvents,
                userViewModel.searchBarEventNameAdvancedSearch.value!!
            )
        if (listOfBooleans?.get(1) == true) {
            listOfFilteredEvents = filterByStringEventOrganiserName(
                listOfFilteredEvents,
                userViewModel.searchBarEventOrganiserNameAdvancedSearch.value!!
            )
        }
        if (listOfBooleans?.get(2) == true) {
            listOfFilteredEvents = filterByFinished(listOfFilteredEvents, userViewModel.searchBarEventFinishedAdvancedSearch.value!!)
        }
        if (listOfBooleans?.get(3) == true) {
            listOfFilteredEvents = filterByMyJobs(listOfFilteredEvents, userViewModel.searchBarEventFinishedAdvancedSearch.value!!)
        }

        addJobsToLinearLayout(listOfFilteredEvents)


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener{
            val action = MyJobsFragmentDirections.actionMyJobsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.buttonAdvancedSearch.setOnClickListener{

            val fragmentNovi = AdvancedSearchFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        jobsLayout =  requireActivity().findViewById(R.id.MyJobsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

       // addJobsToLinearLayout()

    }

    private fun addJobsToLinearLayout(lista: List<Event>) {

        jobsLayout.removeAllViewsInLayout()

        val listaKorisnika: List<User>? = userViewModel.users.value

        for(event in lista){
            //this.flag = event.organiser == FirebaseAuth.getInstance().currentUser?.uid

            val viewItem: View = inflater.inflate(R.layout.fragment_job_item, jobsLayout, false)

            val imageView: ImageView = viewItem.findViewById(R.id.imageCameraBackground) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.username)
            val jobTitle: TextView = viewItem.findViewById(R.id.JobTitleInvitations)
            val dateTitle: TextView = viewItem.findViewById(R.id.DateTextInvitations)
            val finishedTitle: TextView = viewItem.findViewById(R.id.finished)
            val imageViewFinished: ImageView = viewItem.findViewById(R.id.imageCameraBackground2) as ImageView

            val issuedByUser: User? =
                listaKorisnika?.filter { it.id.equals(event.organiser) }?.firstOrNull()

            if (issuedByUser != null) {

                Glide.with(this).load(issuedByUser?.imageUrl).into(imageView)

                usernameView.setText(issuedByUser?.userName)
            }

            if (event.organiser == FirebaseAuth.getInstance().currentUser?.uid){
                Glide.with(this).load(userViewModel.user.value?.imageUrl).into(imageView)

                usernameView.setText(userViewModel.user.value?.userName)
            }

            if (!event.finished) {
                finishedTitle.setText("Not finished")
                imageViewFinished.setImageResource(R.drawable.not_finished)
            }

            jobTitle.setText(event.name)

            var date: String? = event?.date.toString()
            val lista = date?.split(" ")
            val year = event.date.year
            val month = lista?.get(1)
            val day = lista?.get(2)//lista?.get(2)?.toInt()
            val hourMinute = lista?.get(3)?.split(":")
            var hour: String? = hourMinute?.get(0)//event.date.hours.toString()//lista?.get(3)?.toInt()
            var minute: String? = hourMinute?.get(1)//event.date.minutes.toString()//lista?.get(4)?.toInt()
//            var noviHour: String = hour
//            var noviMinute: String = minute
//            if (hour.toInt() < 10){
//                noviHour = "0"+hour
//            }
//            if (minute.toInt() < 10){
//                noviMinute = "0"+minute
//            }

            val timeString: String = day.toString() + "/" + month + "/" + year.toString() + " " + hour + ":" + minute + "h"

            dateTitle.setText(timeString)    //.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            viewItem.setOnClickListener{
                //ako sam ja organizator
                val action: NavDirections
                userViewModel.setSelectedEvent(event)

                if (event.organiser == FirebaseAuth.getInstance().currentUser?.uid){
                    action = MyJobsFragmentDirections.actionMyJobsToJobReview()
                }
                else{
                    action = MyJobsFragmentDirections.actionMyJobsToJobView()
                }

                NavHostFragment.findNavController(this).navigate(action)
            }

            jobsLayout.addView(viewItem)
        }
    }

}