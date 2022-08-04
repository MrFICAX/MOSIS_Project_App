package elfak.mosis.freelencelive.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogAdvancedSearchBinding
import elfak.mosis.freelencelive.model.userViewModel


class AdvancedSearchFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogAdvancedSearchBinding
    private var inputEventName: String = ""
    private var inputEventOrganiser: String = ""
    private var isFinished: Boolean = false
    private var isMyJob: Boolean = false
    private val userViewModel: userViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogAdvancedSearchBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        var rootView: View = inflater.inflate(AndroidR.layout.fragment_dialog_add_event, container, false)
//        return  rootView
//
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleText.setText(userViewModel.searchBarEventNameAdvancedSearch.value.toString())
        binding.userNameText.setText(userViewModel.searchBarEventOrganiserNameAdvancedSearch.value.toString())
        binding.checkBoxFinished.isChecked = userViewModel.searchBarEventFinishedAdvancedSearch.value == true
        binding.checkBoxMyJobs.isChecked = userViewModel.searchBarEventMyJobAdvancedSearch.value == true
        inputEventName = userViewModel.searchBarEventNameAdvancedSearch.value.toString()
        inputEventOrganiser = userViewModel.searchBarEventOrganiserNameAdvancedSearch.value.toString()
        isFinished = userViewModel.searchBarEventFinishedAdvancedSearch.value == true
        isMyJob = userViewModel.searchBarEventMyJobAdvancedSearch.value == true

        binding.titleText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                inputEventName = p0.toString()
            }
        })

        binding.userNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                inputEventOrganiser = p0.toString()
            }
        })

        binding.checkBoxFinished.setOnClickListener {
            isFinished = binding.checkBoxFinished.isChecked
        }

        binding.checkBoxMyJobs.setOnClickListener {
            isMyJob = binding.checkBoxMyJobs.isChecked
        }


        binding.buttonApplyChanges.setOnClickListener {

            userViewModel.setSearchBarEventNameAdvancedSearch(inputEventName)
            userViewModel.setSearchBarEventOrganiserNameAdvancedSearch(inputEventOrganiser)
            userViewModel.setSearchBarEventFinishedAdvancedSearch(isFinished)
            userViewModel.setSearchBarEventMyJobAdvancedSearch(isMyJob)
            dialog?.dismiss()
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