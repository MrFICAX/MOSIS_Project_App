package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.findNavController
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentBottomNavBarBinding


class BottomNavBarFragment : Fragment() {

    private lateinit var binding: FragmentBottomNavBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomNavBarBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

    }


    private fun setOnClickListeners() {

        val navController = requireActivity().findNavController(R.id.fragment_container)

        binding.navbarIconInvitations.setOnClickListener {
            setColorsOnInvitationsClicked();
            when (navController.currentDestination?.id) {

                R.id.startPageFragment -> {
                    navController.navigate(R.id.action_startpage_to_invitations)

                }
                R.id.notificationsFragment -> {
                    navController.navigate(R.id.action_notifications_to_invitations)
                }
                else -> {}
            }
        }

        binding.navbarIconMap.setOnClickListener {
            setColorsOnMapClicked();
            when (navController.currentDestination?.id) {
                R.id.notificationsFragment -> {
                    navController.navigate(R.id.action_notifications_to_startpage)
                }
                R.id.invitationsFragment -> {
                    navController.navigate(R.id.action_invitations_to_startpage)
                }
                else -> {}
            }
        }
        binding.navbarIconNotifications.setOnClickListener {
            setColorsOnNotificationsClicked();
            when (navController.currentDestination?.id) {
                R.id.startPageFragment -> {
                    navController.navigate(R.id.action_starpage_to_notifications)
                }
                R.id.invitationsFragment -> {
                    navController.navigate(R.id.action_invitations_to_notifications)
                }
                else -> {}
            }
        }

    }

    private fun setColorsOnInvitationsClicked(){
//        binding.navbarTitleInvitations.setTextColor(R.color.purple_500)
        //binding.navbarIconInvitations.setColorFilter(R.color.purple_500)
        var slika: ImageView =  requireView().findViewById(R.id.navbar_icon_invitations)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_500));

//        binding.navbarTitleMap.setTextColor(R.color.white)

//        var mapTekst: TextView = requireView().findViewById(R.id.navbar_title_map)
//        mapTekst.setTextColor(R.color.white)

        slika =  requireView().findViewById(R.id.navbar_icon_map)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

        //binding.navbarTitleNotifications.setTextColor(R.color.purple_200)
        //binding.navbarIconNotifications.setColorFilter(R.color.purple_500)

         slika = requireView().findViewById(R.id.navbar_icon_notifications)
        DrawableCompat.setTint(
            slika.getDrawable(),
            ContextCompat.getColor(requireContext(), R.color.purple_200)
        );
    }

    private fun setColorsOnMapClicked(){
//        binding.navbarTitleInvitations.setTextColor(R.color.purple_200)
        //binding.navbarIconInvitations.setColorFilter(R.color.purple_500)
        var slika: ImageView =  requireView().findViewById(R.id.navbar_icon_invitations)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

//        binding.navbarTitleMap.setTextColor(R.color.purple_500)
        slika =  requireView().findViewById(R.id.navbar_icon_map)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_500));

//        binding.navbarTitleNotifications.setTextColor(R.color.purple_200)
        //binding.navbarIconNotifications.setColorFilter(R.color.purple_500)

        slika = requireView().findViewById(R.id.navbar_icon_notifications)
        DrawableCompat.setTint(
            slika.getDrawable(),
            ContextCompat.getColor(requireContext(), R.color.purple_200)
        );
    }

    private fun setColorsOnNotificationsClicked(){
//        binding.navbarTitleInvitations.setTextColor(R.color.purple_200)
        //binding.navbarIconInvitations.setColorFilter(R.color.purple_500)
        var slika: ImageView =  requireView().findViewById(R.id.navbar_icon_invitations)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

//        binding.navbarTitleMap.setTextColor(R.color.purple_200)
        slika =  requireView().findViewById(R.id.navbar_icon_map)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

//        binding.navbarTitleNotifications.setTextColor(R.color.purple_500)
        //binding.navbarIconNotifications.setColorFilter(R.color.purple_500)

        slika = requireView().findViewById(R.id.navbar_icon_notifications)
        DrawableCompat.setTint(
            slika.getDrawable(),
            ContextCompat.getColor(requireContext(), R.color.purple_500)
        );
    }
}


