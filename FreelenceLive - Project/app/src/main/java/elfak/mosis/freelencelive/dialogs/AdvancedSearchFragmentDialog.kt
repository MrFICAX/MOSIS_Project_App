package elfak.mosis.freelencelive.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogAdvancedSearchBinding


class AdvancedSearchFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogAdvancedSearchBinding

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

        binding.datePicker.setOnClickListener{
            val fragmentNovi = ChooseDateFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }
    }



}