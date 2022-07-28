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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.StartPageFragmentDirections
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.R as AndroidR
import elfak.mosis.freelencelive.databinding.FragmentDialogAddEventBinding
import elfak.mosis.freelencelive.model.addEventViewModel
import elfak.mosis.freelencelive.model.userViewModel

class addEventFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentDialogAddEventBinding
    private val addEventViewModel: addEventViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()


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

        val EventObserver = Observer<Event> { newValue ->
            binding.creatingJob.setText(newValue.name)

        }
        addEventViewModel.event.observe(viewLifecycleOwner, Observer<Event> {
            binding.creatingJob.setText(it.name)

        })

        val LongitudeObserver = Observer<String> { newValue ->
            binding.buttonCreateJob.setText(newValue)

        }
        addEventViewModel.longitude.observe(viewLifecycleOwner, LongitudeObserver)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.JobName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                addEventViewModel.setEventName(s.toString())

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //addEventViewModel.setEventName(s.toString())

            }

        })


        binding.buttonCreateJob.setOnClickListener {
            addEventViewModel.setEventName("PROBNI TEKST")
            addEventViewModel.setLocation("proba1", "proba2")

            Toast.makeText(requireContext(), addEventViewModel.event.value?.name.toString(), Toast.LENGTH_LONG).show()
        }


        //val ikonaMapa = requireDialog().findViewById(AndroidR.id.icon_map)
        binding.IconMapInvitations.setOnClickListener {
            Toast.makeText(context, "POZDRAV", Toast.LENGTH_LONG).show()
            //findNavController().navigate(AndroidR.id.action_startpage_to_setLocation)
            val action = StartPageFragmentDirections.actionStartpageToSetLocation()
            findNavController().navigate(action)

            dismiss()

        }
    }
}