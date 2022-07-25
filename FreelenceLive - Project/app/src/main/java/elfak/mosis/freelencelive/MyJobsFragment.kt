package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.databinding.FragmentMyJobsBinding
import elfak.mosis.freelencelive.dialogs.AdvancedSearchFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog

class MyJobsFragment : Fragment() {

    private lateinit var binding: FragmentMyJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyJobsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_my_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener{
            val action = MyJobsFragmentDirections.actionMyJobsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.buttonAdvancedSearch.setOnClickListener{

            val fragmentNovi = AdvancedSearchFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

    }

}