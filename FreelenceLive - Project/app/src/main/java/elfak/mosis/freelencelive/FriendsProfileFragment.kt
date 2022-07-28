package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databinding.FragmentFriendsProfileBinding
import elfak.mosis.freelencelive.model.userViewModel


class FriendsProfileFragment : Fragment() {

    var brojKomentara = 10
    lateinit var CommentsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()

    lateinit var binding: FragmentFriendsProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_friends_profile, container, false)
        binding = FragmentFriendsProfileBinding.inflate(inflater)

        val FriendObserver = Observer<User> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            Glide.with(this).load(newValue.imageUrl).into(binding.imageCameraBackground)
            binding.userNameText.setText(newValue.userName)
            binding.phoneNumberText.setText(newValue.phoneNumber)
            binding.EmailText.setText(newValue.email)
            setRating()
        }
        userViewModel.selectedUser.observe(viewLifecycleOwner, FriendObserver)

        return binding.root
    }

    private fun setRating() {
        val totalScore = userViewModel.selectedUser.value?.totalScore?.toFloat()
        val numOfRatings = userViewModel.selectedUser.value?.numOfRatings
        val ratingResult = totalScore?.div(numOfRatings!!)

        binding.Rating.rating = ratingResult!!
        binding.ScoreText.text = ratingResult.toString()!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommentsLayout =  requireActivity().findViewById(R.id.CommentsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener{
            val action = DashboardFragmentDirections.actionDashboardToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        addCommentsToLinearLayout()
    }

    private fun addCommentsToLinearLayout() {
        for(i in 0..brojKomentara - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_comment_item, CommentsLayout, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageCameraBackground) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.username)
            val commentField: TextView = viewItem.findViewById(R.id.commentField)

            imageView.setImageResource(R.drawable.img_0944)
            usernameView.setText("UserName "+i.toString())
            commentField.setText("ASFASDSADFSADLKFJS;LKFJSA;KLFJSAKL;FJLKASJFDKASL;WKQL;ERJKL")

            CommentsLayout.addView(viewItem)
        }
    }

}