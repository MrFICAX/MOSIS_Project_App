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
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseDateBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogChooseTimeBinding


class ChooseTimeFragmentDialog : DialogFragment() {
    private lateinit var binding: FragmentDialogChooseTimeBinding

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

        return binding.root
        //return inflater.inflate(R.layout.fragment_dialog_choose_time, container, false)
    }

}