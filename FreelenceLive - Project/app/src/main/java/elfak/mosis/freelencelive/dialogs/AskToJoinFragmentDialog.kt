package elfak.mosis.freelencelive.dialogs

import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.askToJoin
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentDialogAskToJoinBinding
import elfak.mosis.freelencelive.model.userViewModel
import java.util.HashMap


class AskToJoinFragmentDialog : DialogFragment() {

    var brojPrijatelja = 5

    lateinit var friendsView: LinearLayout
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    lateinit var pd: ProgressDialog

    lateinit var binding: FragmentDialogAskToJoinBinding
    private val userViewModel: userViewModel by activityViewModels()
    private lateinit var selectedEvent: Event


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogAskToJoinBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsView = binding.friendsView

        selectedEvent = userViewModel.selectedEvent.value!!

        val EventsObserver = Observer<Event>{

            selectedEvent = it
            addFriendsToFriendsView()
            fillAllViewsWithData()

        }

        userViewModel.selectedEvent.observe(viewLifecycleOwner, EventsObserver)


        inflater = LayoutInflater.from(requireContext())


        setButtonClickHandler()

    }

    private fun setButtonClickHandler() {


        val button: Button = binding.buttonAsk

        if (selectedEvent.organiser.equals(userViewModel.user.value?.id)) {
            button.setText("MY EVENT")
            button.setBackgroundTintList(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow
                    )
                )
            )
        } else {
            if (checkIfAskToJoinExists()) {
                button.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                )
                button.setText("REQUEST SENT")
                button.isClickable = false
            } else { if (checkIfYouAreAlreadyAccepted()){
                button.setText("I can't come!")
                button.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                )
                button.setOnClickListener{
                    pd.setMessage("SENDING...")
                    pd.show()
                    declineInvitation(selectedEvent, userViewModel)
                    dialog?.dismiss()
                }
            } else
                button.setOnClickListener {
                    pd.setMessage("SENDING...")
                    pd.show()
                    sendAskToJoinRequest(button)
                    dialog?.dismiss()

                }
            }
        }

    }

    private fun declineInvitation(
        singleEvent: Event,
        userViewModel: userViewModel
    ) {
        FirebaseHelper.declineJobInvitation(
            singleEvent,
            userViewModel,
            pd,
            null,
            null,
            requireContext()
        )
    }

    private fun checkIfAskToJoinExists(): Boolean {
        var flag = false
        val lista: List<askToJoin>? = userViewModel.askToJoin.value
        lista?.forEach {
            if (it.issuedBy.equals(userViewModel.user.value?.id) && it.joinToJob.equals(
                    selectedEvent.id
                )
            ) {
                return true
            }
        }
        return flag
    }

    private fun checkIfYouAreAlreadyAccepted(): Boolean {
        var myUserId : String? = userViewModel.user.value?.id
        var flag = false

        var tmpMapa: MutableMap<String, Boolean> =
            selectedEvent.listOfUsers as MutableMap<String, Boolean>
        val IsAccepted = tmpMapa[myUserId].toString()
            if (IsAccepted == "true")
                    return true
        return flag
    }

    private fun sendAskToJoinRequest(button: Button) {

        FirebaseHelper.sendAskToJoinRequest(
            selectedEvent.id,
            requireContext(),
            pd,
            button,
            userViewModel,
            requireContext()
        )
    }

    private fun fillAllViewsWithData() {
        binding.jobTitleText.setText(userViewModel.selectedEvent.value?.name)

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

        val issuedByUser: User? =
            userViewModel.users.value?.filter { it.id.equals(selectedEvent.organiser) }
                ?.firstOrNull()
            binding.organisedByText.setText(issuedByUser?.userName)
        if (selectedEvent.organiser.equals(userViewModel.user.value?.id))
            binding.organisedByTitle.setText("")
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

            if (userViewModel.user.value?.id.equals(keyValue.key)) {
                userViewModel.user.value?.let { listaKorisnika.add(it) }
            }
        }




        for (singleUser in listaKorisnika) {
            val viewItem: View = inflater.inflate(R.layout.user_item, friendsView, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val imageView2: ImageView = viewItem.findViewById(R.id.imageView2) as ImageView


            val IsAccepted = hashMapFriends[singleUser.id].toString()
            Log.d("IsAccepted", IsAccepted.toString())

            if (IsAccepted.equals("true")) {
                imageView2.setImageResource(R.drawable.accept)

            } else {
                imageView2.setImageResource(R.drawable.question)

            }

            Glide.with(this).load(singleUser?.imageUrl).into(imageView)

            val textView: TextView = viewItem.findViewById(R.id.userNameText) as TextView
            textView.setText(singleUser.userName)

            friendsView.addView(viewItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}