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
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseDateBinding
import elfak.mosis.freelencelive.model.userViewModel
import java.util.*

class ChooseDateFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogChooseDateBinding
    private val userViewModel: userViewModel by activityViewModels()
    private var dateTmp : Date = Date()
    private var day: Int = 0
    private var flag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogChooseDateBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val it = userViewModel.selectedEvent?.value
        var date: String? = it?.date.toString()
        val lista = date?.split(" ")
        val dan = lista?.get(2)?.toInt()
        day = dan!!

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
                    flag = false
                }
                tmp < 0 -> {
                    dateTmp = Date(year, month, day)
                    this.day = day
                    flag = true

                }
                else -> {
                    dateTmp = Date(year, month, day)
                    this.day = day
                    flag = true
                }
            }
        }

        binding.buttonApplyChanges.setOnClickListener {
            if (flag){

            userViewModel.setSelectedEventDate(dateTmp.year, dateTmp.month, day)
            dialog?.dismiss()
            } else{
                Toast.makeText(
                    requireContext(),
                    "Date should be in future!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
        //return inflater.inflate(R.layout.fragment_dialog_choose_date, container, false)
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