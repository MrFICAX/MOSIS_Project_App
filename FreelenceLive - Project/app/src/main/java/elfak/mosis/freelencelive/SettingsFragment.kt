package elfak.mosis.freelencelive

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentSettingsBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel


class SettingsFragment : Fragment() {
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()

    lateinit var binding: FragmentSettingsBinding
    var status: Boolean = false
    var online: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.serviceSwitch.isEnabled = false;
            binding.onlineSwitch.isEnabled = false;
        }
        binding.serviceSwitch.isChecked = userViewModel.backGroundServiceActivated.value == true
        binding.onlineSwitch.isChecked = userViewModel.onlineActivated.value == true
        binding.shapeableImageView.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.serviceSwitch.setOnCheckedChangeListener { _, isChecked ->
            FirebaseHelper.postMyServiceValue(isChecked, userViewModel, requireContext())
        }

        binding.onlineSwitch.setOnCheckedChangeListener{ _, isCheched ->
            FirebaseHelper.postMyOnlineValue(isCheched, userViewModel, requireContext())

        }

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