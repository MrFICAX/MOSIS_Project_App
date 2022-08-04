package elfak.mosis.freelencelive.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseTimeBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogSearchByRadiusBinding
import elfak.mosis.freelencelive.model.userViewModel

class searchByRadiusFragmentDialog : DialogFragment() {

    private val userViewModel: userViewModel by activityViewModels()
    private lateinit var binding: FragmentDialogSearchByRadiusBinding
    private var inputFloatValue: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogSearchByRadiusBinding.inflate(inflater)

        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root

//        var rootView: View = inflater.inflate(R.layout.fragment_dialog_search_by_radius, container, false)
//        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radiusInMetersText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                inputFloatValue = p0.toString().toDouble()
            }
        })

        binding.buttonSetSearch.setOnClickListener {
            userViewModel.setRadiusValue(inputFloatValue)
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