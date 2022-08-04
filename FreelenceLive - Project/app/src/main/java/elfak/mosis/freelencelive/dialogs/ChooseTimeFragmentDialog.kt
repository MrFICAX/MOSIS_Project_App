package elfak.mosis.freelencelive.dialogs

import android.content.Context
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
import elfak.mosis.freelencelive.data.Event
import java.util.*
import kotlin.math.min

class ChooseTimeFragmentDialog : DialogFragment() {
    private lateinit var binding: FragmentDialogChooseTimeBinding
    private val userViewModel: userViewModel by activityViewModels()
    private lateinit var selectedEvent: Event

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
        selectedEvent = userViewModel.selectedEvent?.value!!


        binding.timePicker.hour = selectedEvent?.date?.hours!!
        binding.timePicker.minute = selectedEvent?.date?.minutes!!

        binding.ApplyChanges.setOnClickListener {
            var hour = binding.timePicker.hour
            var minute = binding.timePicker.minute

            if( hour.equals(selectedEvent?.date?.hours) && minute.equals(selectedEvent?.date?.minutes)){
                Toast.makeText(requireContext(), "You didn't change anything!", Toast.LENGTH_SHORT).show()
            } else{

                if (checkIfTimeIsNotInThePast(hour, minute)){
                    userViewModel.setSelectedEventTime(hour, minute)
                    dialog?.dismiss()
                } else{
                    Toast.makeText(requireContext(), "Hours and minutes must be in future!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
        //return inflater.inflate(R.layout.fragment_dialog_choose_time, container, false)
    }

    private fun checkIfTimeIsNotInThePast(hour: Int, minute: Int): Boolean {

        var returnFlag = false
        var flag = true
        val potentialDate: Date = Date(selectedEvent.date.year, selectedEvent.date.month, selectedEvent.date.day - 1)
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
                flag = true
                returnFlag = true
            }
            tmp < 0 -> {
                flag = true
                returnFlag = true

            }
            else -> {
                flag = false
            }
        }

        if (! flag){

            val tmp = today.time.hours.compareTo(hour)
            when {
                tmp > 0 -> {
                    Toast.makeText(
                        requireContext(),
                        "Date should be in future!",
                        Toast.LENGTH_SHORT
                    ).show()
                    returnFlag = false
                }
                tmp < 0 -> {
                    returnFlag = true
                }
                else -> {
                    val tmp = today.time.minutes.compareTo(minute)
                    when {
                        tmp > 0 -> {
                            Toast.makeText(
                                requireContext(),
                                "Date should be in future!",
                                Toast.LENGTH_SHORT
                            ).show()
                            returnFlag = false
                        }
                        tmp < 0 -> {
                            returnFlag = true
                        }
                        else -> {
                            returnFlag = true
                        }
                    }
                }
            }

        }
        return returnFlag
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