package elfak.mosis.freelencelive.dialogs

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
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentDialogInviteFriendBinding


class InviteFriendFragmentDialog : DialogFragment() {

    lateinit var selectedOption: String
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

        val nizPoslova = listOf<String>("Posao 0", "Posao 1", "Posao 2", "Posao 3", "Posao 4")
        //val feelings = resources.getStringArray(R.array.feelings)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nizPoslova)

        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundDrawable(ColorDrawable(R.color.light_pink))

        binding.buttonInvite.setOnClickListener {

            var flag = false

            if (binding.autoCompleteTextView.text.isNotEmpty()){

                //KOD ZA SLANJE INVITE-A PRIJATELJU
                dialog?.dismiss()
            } else
            {
                Toast.makeText(requireContext(), "No option selected!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val nizPoslova = listOf<String>("Posao 0", "Posao 1", "Posao 2", "Posao 3", "Posao 4", "Posao 0", "Posao 1", "Posao 2", "Posao 3", "Posao 4")
        //val feelings = resources.getStringArray(R.array.feelings)
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

}