package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentHomeScreenBinding


class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener{
            val action = HomeScreenFragmentDirections.actionHommescreenGotoLogin()
            NavHostFragment.findNavController(this).navigate(action)
            //findNavController().navigate(R.id.action_hommescreen_goto_login)
        }

        binding.buttonSignUp.setOnClickListener{
            val action = HomeScreenFragmentDirections.actionHomescreenGotoSignup()
            NavHostFragment.findNavController(this).navigate(action)

            //findNavController().navigate(R.id.action_homescreen_goto_signup)
        }
    }

}