package elfak.mosis.freelencelive.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.R as AndroidR
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.model.addEventViewModel

class addEventFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogAddEventBinding
    private val addEventViewModel: addEventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogAddEventBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        var rootView: View = inflater.inflate(AndroidR.layout.fragment_dialog_add_event, container, false)
//        return  rootView
//
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.JobName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addEventViewModel.setEventName(s as Editable?)

            }

        })

        val EventObserver = Observer<Event> { newValue ->
            binding.JobName.setText(newValue.name.toString())

        }
        addEventViewModel.event.observe(viewLifecycleOwner, EventObserver)




        //val ikonaMapa = requireDialog().findViewById(AndroidR.id.icon_map)
        binding.iconMap.setOnClickListener {
            Toast.makeText(context, "POZDRAV", Toast.LENGTH_LONG).show()
            findNavController().navigate(AndroidR.id.action_startpage_to_setLocation)
            dismiss()

        }
    }
}