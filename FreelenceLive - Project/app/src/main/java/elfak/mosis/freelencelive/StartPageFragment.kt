package elfak.mosis.freelencelive

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import elfak.mosis.freelencelive.databinding.FragmentStartPageBinding
import elfak.mosis.freelencelive.dialogs.AskToJoinFragmentDialog
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel

class StartPageFragment : Fragment() {

    private lateinit var binding: FragmentStartPageBinding
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartPageBinding.inflate(layoutInflater)

        //setColorsOnMapClicked()

        fragmentViewModel.setFragment(this)

        return binding.root
    //return inflater.inflate(R.layout.fragment_start_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radius.setOnClickListener {
            val fragmentNovi = searchByRadiusFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        binding.fab.setOnClickListener {
            val fragmentNovi = addEventFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        binding.shapeableImageView.setOnClickListener{
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout) as DrawerLayout
            drawerLayout.open()
        }

        binding.searchBarText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                userViewModel.setSearchBarEventName(p0.toString())
            }
        })
    }

}