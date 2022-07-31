package elfak.mosis.freelencelive.dialogs

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.StartPageFragmentDirections
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.R as AndroidR
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.model.addEventViewModel
import elfak.mosis.freelencelive.model.userViewModel
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class addEventFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogAddEventBinding
    private val addEventViewModel: addEventViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    lateinit var friendsView: LinearLayout
    lateinit var pd: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pd = ProgressDialog(context)
        pd.setCancelable(false)

        if (userViewModel.users.value?.isEmpty() == true)
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)



        binding = FragmentDialogAddEventBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        friendsView = binding.friendsLinearLayout
        val it = userViewModel.newEvent?.value
        binding.JobName.setText(userViewModel.newEvent?.value?.name)

        var date: String? = it?.date.toString()
        val lista = date?.split(" ")
        val dan = lista?.get(2)?.toInt()


        binding.datePicker.init(
            it?.date?.year!!,
            it.date!!.month,
            dan!!
        ) { view, year, month, day ->

            val potentialDate: Date = Date(year, month, day)
            val today = Calendar.getInstance()
            var date = Date(
                today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            )

            val tmp = date.compareTo(potentialDate)
            when {
                tmp > 0 -> {
                    Toast.makeText(
                        requireContext(),
                        "Date should be in future!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                tmp < 0 -> {
                    userViewModel.setNewEventDate(year, month, day)
                }
                else -> {
                    userViewModel.setNewEventDate(year, month, day)
                }
            }
//            if (date.compareTo(potentialDate))
//                userViewModel.setNewEventDate(year, month, day)
//            else{
//                Toast.makeText(requireContext(), "Date should be in future!", Toast.LENGTH_SHORT).show()
//            }
        }

        binding.timePicker.hour = it.date?.hours!!
        binding.timePicker.minute = it.date?.minutes!!

        userViewModel.newEvent.observe(viewLifecycleOwner, Observer<Event> {
//            Toast.makeText(requireContext(), it.date.toString()+" "+it.time.toString(),Toast.LENGTH_LONG).show()
//            Toast.makeText(
//                requireContext(),
//                it.latitude.toString() + " " + it.longitude.toString(),
//                Toast.LENGTH_LONG
//            ).show()
//
//            val today = Calendar.getInstance()


        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflater = LayoutInflater.from(requireContext())
        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<User> = newValue
            addFriendsToFriendsView(lista)

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)


        binding.JobName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //addEventViewModel.setEventName(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.setNewEventName(s.toString())
            }
        })

        val datePicker = binding.datePicker
        val today = Calendar.getInstance()

//        datePicker.init(
//            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
//            today.get(Calendar.DAY_OF_MONTH)
//        ){
//                view, year, month, day ->
//                val month = month + 1
//                userViewModel.setNewEventDate(year, month, day)
//        }

        binding.timePicker.setIs24HourView(true)
        binding.setTimeButton.setOnClickListener {
            var hour = binding.timePicker.hour
            var minute = binding.timePicker.minute
            //var am_pm = binding.timePicker.
            var am_pm = ""
            // AM_PM decider logic
//            when {
//                hour == 0 -> {
//                    hour += 12
//                    am_pm = "AM"
//                }
//                hour == 12 -> am_pm = "PM"
//                hour > 12 -> {
//                    hour -= 12
//                    am_pm = "PM"
//                }
//                else -> am_pm = "AM"
//            }
            userViewModel.setNewEventTime(hour, minute)
        }
//        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
//            var hour = hour
//            var am_pm = ""
//            // AM_PM decider logic
//            when {
//                hour == 0 -> {
//                    hour += 12
//                    am_pm = "AM"
//                }
//                hour == 12 -> am_pm = "PM"
//                hour > 12 -> {
//                    hour -= 12
//                    am_pm = "PM"
//                }
//                else -> am_pm = "AM"
//            }
//            userViewModel.setNewEventTime(hour, minute)
//        }


        binding.buttonCreateJob.setOnClickListener {
//            addEventViewModel.setEventName("PROBNI TEKST")
//            addEventViewModel.setLocation("proba1", "proba2")

            val eventTmp: Event? = userViewModel.newEvent.value
            eventTmp?.organiser = userViewModel.user.value?.id!!
            if (CheckIfFieldsAreFilledCorrectly(eventTmp)) {
                pd.setMessage("POSTING...")
                pd.show()

                FirebaseHelper.createEvent(eventTmp, requireContext(), pd, userViewModel)
                dialog?.dismiss()
                userViewModel.setNewEvent()

            }

//            Toast.makeText(
//                requireContext(),
//                addEventViewModel.event.value?.name.toString(),
//                Toast.LENGTH_LONG
//            ).show()
        }


        //val ikonaMapa = requireDialog().findViewById(AndroidR.id.icon_map)
        binding.IconMapInvitations.setOnClickListener {
            //findNavController().navigate(AndroidR.id.action_startpage_to_setLocation)
            val action = StartPageFragmentDirections.actionStartpageToSetLocation()
            findNavController().navigate(action)

            dismiss()
        }
    }

    private fun CheckIfFieldsAreFilledCorrectly(eventTmp: Event?): Boolean {

        var flag = false
        if (eventTmp?.name?.trim().toString().equals("")) {
            Toast.makeText(requireContext(), "Name of event not filled!", Toast.LENGTH_SHORT).show()
            return flag
        }
        val time1 = LocalDateTime.now()
        val time = Time(time1.hour, time1.minute, time1.second)
        val cal = Calendar.getInstance()
        //cal.add(Calendar.DATE, -1)
        val yesterday = cal
        var date = Date(
            yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH),
            yesterday.get(Calendar.DAY_OF_MONTH), time1.hour, time1.minute
        )
//        if(!(eventTmp?.date?.after(date) == true)){
//            Toast.makeText(requireContext(), "Date is not correct!", Toast.LENGTH_SHORT).show()
//            return flag
//        }
        val tmp = eventTmp?.date!!.compareTo(date)
        when {
            tmp > 0 -> {

            }
            tmp < 0 -> {
                Toast.makeText(requireContext(), "Date is not correct!", Toast.LENGTH_SHORT).show()
                return flag
            }
            else -> {
                Toast.makeText(requireContext(), "Date is not correct!", Toast.LENGTH_SHORT).show()
                return flag
            }
        }

        if (eventTmp.latitude.equals(0.0) && eventTmp.longitude.equals(0.0)) {

            Toast.makeText(requireContext(), "Location is not correct!", Toast.LENGTH_SHORT).show()
            return flag
        }
        return true
        //if(eventTmp.date.)
    }

    private fun addFriendsToFriendsView(lista: List<User>) {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        var listaKorisnika: List<User> = lista
        friendsView.removeAllViewsInLayout()


        listaKorisnika = filterByFriends(listaKorisnika)

        for (singleUser in listaKorisnika) {
            val viewItem: View =
                inflater.inflate(elfak.mosis.freelencelive.R.layout.user_item, friendsView, false)
            val imageView: ImageView =
                viewItem.findViewById(elfak.mosis.freelencelive.R.id.imageView) as ImageView

            imageView.setOnClickListener {
                //appViewModel.selectedUser = nizKorisnika[i]
//                val action = StartPageFragmentDirections.actionStartpageToFriendsProfile()
//                NavHostFragment.findNavController(this).navigate(action)
//                dialog?.dismiss()
                if (userViewModel.newEvent.value?.listOfUsers?.containsKey(singleUser.id) == true) {
//                    userViewModel.newEvent.value?.listOfUsers?.remove(singleUser.id)

                    userViewModel.setNewEventUserRemove(singleUser.id)
                    imageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            elfak.mosis.freelencelive.R.color.transparent
                        )
                    )
                } else {
//                    userViewModel.newEvent.value?.listOfUsers?.put(singleUser.id, true)
                    userViewModel.setNewEventUserAdd(singleUser.id)
                    imageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            elfak.mosis.freelencelive.R.color.transparent_grey
                        )
                    )
                }
            }

//            if (i < 2)
//                imageView.setImageResource(elfak.mosis.freelencelive.R.drawable.img_0944)
//            else
//                imageView.setImageResource(elfak.mosis.freelencelive.R.drawable.img_0950)


            Glide.with(this).load(singleUser.imageUrl).into(imageView)

            val textView: TextView =
                viewItem.findViewById(elfak.mosis.freelencelive.R.id.userNameText) as TextView
            textView.setText(singleUser.userName)

            friendsView.addView(viewItem)
        }
    }

    private fun filterByFriends(listaKorisnika: List<User>): List<User> {

        return listaKorisnika.filter { it.friendsList.containsKey(FirebaseAuth.getInstance().currentUser?.uid) }
    }
}