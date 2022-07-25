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
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentBottomNavBarBinding


class BottomNavBarFragment : Fragment() {

    private lateinit var binding: FragmentBottomNavBarBinding
    private lateinit var action: NavDirections
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
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = StartPageFragmentDirections.actionStartpageToInvitations()
                    //NavHostFragment.findNavController(this).navigate(action)

                }
                R.id.notificationsFragment -> {
                    //navController.navigate(R.id.action_notifications_to_invitations)
                    action = NotificationsFragmentDirections.actionNotificationsToInvitations()
                    //NavHostFragment.findNavController(this).navigate(action)
                }
                R.id.jobReviewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobReviewFragmentDirections.actionJobReviewToInvitations()
                }
                R.id.jobViewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobViewFragmentDirections.actionJobViewToInvitations()
                }
                R.id.myProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyProfileFragmentDirections.actionMyProfileToInvitations()
                }
                R.id.myJobsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyJobsFragmentDirections.actionMyJobsToInvitations()
                }
                R.id.dashboardFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = DashboardFragmentDirections.actionDashboardToInvitations()
                }
                R.id.friendsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsFragmentDirections.actionFriendsToInvitations()
                }
                R.id.ratingFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = RatingFragmentDirections.actionRatingToInvitations()
                }
                else -> {}
            }
            NavHostFragment.findNavController(this).navigate(action)

        }

        binding.navbarIconMap.setOnClickListener {
            setColorsOnMapClicked();
            when (navController.currentDestination?.id) {
                R.id.notificationsFragment -> {
                    //navController.navigate(R.id.action_notifications_to_startpage)
                    action = NotificationsFragmentDirections.actionNotificationsToStartpage()
                    //NavHostFragment.findNavController(this).navigate(action)
                }
                R.id.invitationsFragment -> {
                    //navController.navigate(R.id.action_invitations_to_startpage)
                    action = InvitationsFragmentDirections.actionInvitationsToStartpage()
                }
                R.id.jobReviewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobReviewFragmentDirections.actionJobReviewToStartpage()
                }
                R.id.jobViewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobViewFragmentDirections.actionJobViewToStartpage()
                }
                R.id.myProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyProfileFragmentDirections.actionMyProfileToStartpage()
                }
                R.id.myJobsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyJobsFragmentDirections.actionMyJobsToStartpage()
                }
                R.id.dashboardFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = DashboardFragmentDirections.actionDashboardToStartpage()
                }
                R.id.friendsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsFragmentDirections.actionFriendsToStartPage()
                }
                R.id.ratingFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = RatingFragmentDirections.actionRatingToStartPage()
                }
                else -> {}
            }
            NavHostFragment.findNavController(this).navigate(action)

        }
        binding.navbarIconNotifications.setOnClickListener {
            setColorsOnNotificationsClicked();
            when (navController.currentDestination?.id) {
                R.id.startPageFragment -> {
                    //navController.navigate(R.id.action_starpage_to_notifications)
                    action = StartPageFragmentDirections.actionStarpageToNotifications()
                }
                R.id.invitationsFragment -> {
                    //navController.navigate(R.id.action_invitations_to_notifications)
                    action = InvitationsFragmentDirections.actionInvitationsToNotifications()
                    //NavHostFragment.findNavController(this).navigate(action)
                }
                R.id.jobReviewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobReviewFragmentDirections.actionJobReviewToNotifications()
                }
                R.id.jobViewFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = JobViewFragmentDirections.actionJobViewToNotifications()
                }
                R.id.myProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyProfileFragmentDirections.actionMyProfileToNotifications()
                }
                R.id.myJobsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = MyJobsFragmentDirections.actionMyJobsToNotifications()
                }
                R.id.dashboardFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = DashboardFragmentDirections.actionDashboardToNotifications()
                }
                R.id.friendsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsFragmentDirections.actionFriendsToNotifications()
                }
                R.id.ratingFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = RatingFragmentDirections.actionRatingToNotifications()
                }
                else -> {}
            }
            NavHostFragment.findNavController(this).navigate(action)

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


