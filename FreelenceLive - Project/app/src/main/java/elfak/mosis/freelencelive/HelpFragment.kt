package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.databinding.FragmentHelpBinding
import elfak.mosis.freelencelive.model.fragmentViewModel


class HelpFragment : Fragment() {

    private val fragmentViewModel: fragmentViewModel by activityViewModels()

    lateinit var binding: FragmentHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHelpBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener{
            val action = HelpFragmentDirections.actionHelpToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

}