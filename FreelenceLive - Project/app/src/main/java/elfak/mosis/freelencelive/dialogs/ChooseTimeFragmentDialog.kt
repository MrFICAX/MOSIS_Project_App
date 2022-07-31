package elfak.mosis.freelencelive.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseDateBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseTimeBinding
import elfak.mosis.freelencelive.model.userViewModel


class ChooseTimeFragmentDialog : DialogFragment() {
    private lateinit var binding: FragmentDialogChooseTimeBinding
    private val userViewModel: userViewModel by activityViewModels()
    val selectedEvent = userViewModel.selectedEvent?.value


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogChooseTimeBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        binding.timePicker.hour = selectedEvent?.date?.hours!!
        binding.timePicker.minute = selectedEvent?.date?.minutes!!

        binding.ApplyChanges.setOnClickListener {
            var hour = binding.timePicker.hour
            var minute = binding.timePicker.minute

            if( hour.equals(selectedEvent.date.hours) && minute.equals(selectedEvent.date.minutes)){
                Toast.makeText(requireContext(), "You didn't change anything!", Toast.LENGTH_SHORT).show()
            } else{
                userViewModel.setSelectedEventTime(hour, minute)
                dialog?.dismiss()
            }
        }


        return binding.root
        //return inflater.inflate(R.layout.fragment_dialog_choose_time, container, false)
    }

}