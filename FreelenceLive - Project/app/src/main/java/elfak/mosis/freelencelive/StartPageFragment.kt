package elfak.mosis.freelencelive

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding
import elfak.mosis.freelencelive.databinding.FragmentStartPageBinding
import elfak.mosis.freelencelive.dialogs.InviteFriendFragmentDialog
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog

class StartPageFragment : Fragment() {

    private lateinit var binding: FragmentStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartPageBinding.inflate(layoutInflater)
        return binding.root
    //return inflater.inflate(R.layout.fragment_start_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radius.setOnClickListener {
            //Toast.makeText(context, "POZDRAV", Toast.LENGTH_LONG).show()

            val fragmentNovi = searchByRadiusFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
//          val fragmentNovi = InviteFriendFragmentDialog()
//          fragmentNovi.show(parentFragmentManager, "customString")

        }

        binding.fab.setOnClickListener {
        //ISPRAVAN KOD JE U KOMENTARU
            val fragmentNovi = addEventFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")

//            val fragmentNovi = InviteFriendFragmentDialog()
//            fragmentNovi.show(parentFragmentManager, "customString")


        }

        binding.shapeableImageView.setOnClickListener{
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout) as DrawerLayout
            drawerLayout.open()
        }
    }

}