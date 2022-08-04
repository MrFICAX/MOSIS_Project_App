package elfak.mosis.freelencelive.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentDialogInviteFriendBinding
import elfak.mosis.freelencelive.model.userViewModel


class InviteFriendFragmentDialog : DialogFragment() {


    private val userViewModel: userViewModel by activityViewModels()
    private var selectedUser: User? = null
    private var listaMyJobsa: List<Event>? = null
    lateinit var binding: FragmentDialogInviteFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogInviteFriendBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "POZDRAV IZ DIALOG FRAGMENTA!", Toast.LENGTH_LONG).show()

        selectedUser = userViewModel.selectedUser.value
        binding.userNameText.setText(selectedUser?.userName)

        Glide.with(this).load(selectedUser?.imageUrl).into(binding.imageCameraBackground)

        listaMyJobsa =
            userViewModel.events.value?.filter { it.organiser.equals(FirebaseAuth.getInstance().currentUser?.uid) }

        listaMyJobsa = listaMyJobsa?.filter { !it.listOfUsers.containsKey(selectedUser?.id) }

        val listaEventsString: MutableList<String> = mutableListOf()
        listaMyJobsa?.forEach {
            listaEventsString.add(it.name)
        }

        val nizPoslova = listaEventsString // listOf<String>("Posao 0", "Posao 1", "Posao 2", "Posao 3", "Posao 4")
        //val feelings = resources.getStringArray(R.array.feelings)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nizPoslova)

        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundDrawable(ColorDrawable(R.color.light_pink))



        binding.buttonInvite.setOnClickListener {

            if (binding.autoCompleteTextView.text.isNotEmpty()) {

//                Toast.makeText(requireContext(), binding.autoCompleteTextView.text, Toast.LENGTH_LONG).show()
                val selectedInput: String = binding.autoCompleteTextView.text.toString()
                val selectedEvent: Event? = listaMyJobsa?.filter { it.name.equals(selectedInput) }?.firstOrNull()
                if(selectedEvent != null){
                    Toast.makeText(requireContext(), "Sending invite!", Toast.LENGTH_LONG).show()
                    FirebaseHelper.sendInviteFriend(selectedUser, selectedEvent, userViewModel, dialog, requireContext())
//                    dialog?.dismiss()
                }


                //KOD ZA SLANJE INVITE-A PRIJATELJU
            } else {
                Toast.makeText(requireContext(), "No option selected!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val listaEventsString: MutableList<String> = mutableListOf()
        listaMyJobsa?.forEach {
            listaEventsString.add(it.name)
        }

        val nizPoslova =
            listaEventsString// listOf<String>("Posao 0", "Posao 1", "Posao 2", "Posao 3", "Posao 4")

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nizPoslova)

        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundDrawable(ColorDrawable(R.color.light_pink))

//        binding.autoCompleteTextView.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    selectedOption = position.toString()
//                    Toast.makeText(requireContext(), selectedOption, Toast.LENGTH_LONG).show()
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    //... your stuff
//                }
//            }

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

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}