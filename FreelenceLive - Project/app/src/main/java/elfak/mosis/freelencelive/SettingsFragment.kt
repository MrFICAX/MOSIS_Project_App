package elfak.mosis.freelencelive

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    var status: Boolean = false
    var online: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater)
        return  binding.root
        //return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener{
            val action = SettingsFragmentDirections.actionSettingsToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }

//        binding.toggleService.setOnClickListener {
//            if(!status){
//                binding.toggleService.setImageResource(R.drawable.toggle_on)
//                binding.toggleService.setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.green),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//                status = true
//            } else{
//                binding.toggleService.setImageResource(R.drawable.toggle_off)
//                binding.toggleService.setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.red),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//                status = false
//            }
//        }
//        binding.toggleOnline.setOnClickListener {
//            if(!online){
//                binding.toggleOnline.setImageResource(R.drawable.toggle_on)
//                binding.toggleOnline.setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.green),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//                online = true
//            } else{
//                binding.toggleOnline.setImageResource(R.drawable.toggle_off)
//                binding.toggleOnline.setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.red),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//                online = false
//            }
//        }


//        if (it) {
//            binding.imageProfileNotificationToggle
//                .setImageResource(R.drawable.ic_baseline_toggle_on)
//            binding.imageProfileNotificationToggle
//                .setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.blue_medium),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//
//        } else {
//            binding.imageProfileNotificationToggle
//                .setImageResource(R.drawable.ic_baseline_toggle_off)
//            binding.imageProfileNotificationToggle
//                .setColorFilter(
//                    ContextCompat.getColor(requireContext(), R.color.blue_pale),
//                    android.graphics.PorterDuff.Mode.SRC_IN)
//            Intent(requireActivity(), BackgroundCommunicationService::class.java).also { intent ->
//                requireActivity().stopService(intent)
//            }
//        }
    }

}