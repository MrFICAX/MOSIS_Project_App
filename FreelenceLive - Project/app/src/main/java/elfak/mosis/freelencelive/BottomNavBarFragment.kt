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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databinding.FragmentBottomNavBarBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel


class BottomNavBarFragment : Fragment() {

    private lateinit var binding: FragmentBottomNavBarBinding
    private var action: NavDirections? = null
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomNavBarBinding.inflate(layoutInflater)

        val FragmentObserver = Observer<Fragment> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            if (newValue != null)
                setColorsOnMapClicked()
            else
                setColorsOthersClicked()
        }
        fragmentViewModel.fragment.observe(viewLifecycleOwner, FragmentObserver)


        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

    }


    private fun setOnClickListeners() {

        val navController = requireActivity().findNavController(R.id.fragment_container)
        binding.navbarIconInvitations.setOnClickListener {
            action = null
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
                    userViewModel.restartAdvancedSearch()

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
                R.id.friendsProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsProfileFragmentDirections.actionFriendsProfileToInvitations()
                }
                R.id.helpFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = HelpFragmentDirections.actionHelpToInvitations()
                }
                R.id.settingsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = SettingsFragmentDirections.actionSettingsToInvitations()
                }
                else -> {
                    action = null
                }
            }
            if (action != null)
                NavHostFragment.findNavController(this).navigate(action!!)

        }

        binding.navbarIconMap.setOnClickListener {
            action = null
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
                    userViewModel.restartAdvancedSearch()

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
                R.id.friendsProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsProfileFragmentDirections.actionFriendsProfileToStartPage()
                }
                R.id.helpFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = HelpFragmentDirections.actionHelpToStartPage()
                }
                R.id.settingsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = SettingsFragmentDirections.actionSettingsToStartPage()
                }
                else -> {}
            }
            if (action != null)
                NavHostFragment.findNavController(this).navigate(action!!)

        }
        binding.navbarIconNotifications.setOnClickListener {
            action = null

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
                    userViewModel.restartAdvancedSearch()

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
                R.id.friendsProfileFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = FriendsProfileFragmentDirections.actionFriendsProfileToNotifications()
                }
                R.id.helpFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = HelpFragmentDirections.actionHelpToNotifications()
                }
                R.id.settingsFragment -> {
                    //navController.navigate(R.id.action_startpage_to_invitations)
                    action = SettingsFragmentDirections.actionSettingsToNotifications()
                }
                else -> {}
            }
            if (action != null)
                NavHostFragment.findNavController(this).navigate(action!!)

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

    private fun setColorsOthersClicked() {
        var slika: ImageView =  requireView().findViewById(R.id.navbar_icon_invitations)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

//        binding.navbarTitleMap.setTextColor(R.color.purple_500)
        slika =  requireView().findViewById(R.id.navbar_icon_map)
        DrawableCompat.setTint(slika.getDrawable(), ContextCompat.getColor(requireContext(), R.color.purple_200));

//        binding.navbarTitleNotifications.setTextColor(R.color.purple_200)
        //binding.navbarIconNotifications.setColorFilter(R.color.purple_500)

        slika = requireView().findViewById(R.id.navbar_icon_notifications)
        DrawableCompat.setTint(
            slika.getDrawable(),
            ContextCompat.getColor(requireContext(), R.color.purple_200)
        );    }

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


