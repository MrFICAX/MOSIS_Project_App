package elfak.mosis.freelencelive

import android.os.Bundle
import android.text.Layout
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import elfak.mosis.freelencelive.data.Rating
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentRatingBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel

class RatingFragment : Fragment() {

    var brojRatings = 10
    lateinit var ratingLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val fragmentViewModel: fragmentViewModel by activityViewModels()
    private val userViewModel: userViewModel by activityViewModels()

    lateinit var binding: FragmentRatingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_rating, container, false)
        binding = FragmentRatingBinding.inflate(inflater)

        if (userViewModel.users.value?.isEmpty() == true)
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)

        //if (userViewModel.ratings.value?.isEmpty() == true)
        FirebaseHelper.getAllRatings(userViewModel, requireContext())

        val RatingsObserver = Observer<List<Rating>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)

            userViewModel.users.value?.let { addRatingsToLinearLayout(newValue, it) }
        }

        userViewModel.ratings.observe(viewLifecycleOwner, RatingsObserver)

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<User> = newValue
            addRatingsToLinearLayout(userViewModel.ratings.value, newValue)


        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)

        binding.checkBox.setOnClickListener {
            if( binding.checkBox.isChecked){
                val filteredList: List<User> = userViewModel.users.value?.filter { userViewModel.user.value?.friendsList?.containsKey(it.id) == true }!!
                addRatingsToLinearLayout(userViewModel.ratings.value, filteredList)

            } else{
                userViewModel.users.value?.let { it1 ->
                    addRatingsToLinearLayout(userViewModel.ratings.value,
                        it1
                    )
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratingLayout = requireActivity().findViewById(R.id.RatingLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = RatingFragmentDirections.actionRatingToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        //addRatingsToLinearLayout()
    }

    private fun addRatingsToLinearLayout(ratingList: List<Rating>?, userList: List<User>) {

        ratingLayout.removeAllViewsInLayout()

        val userLista: List<User> = userList

        if (ratingList?.isNotEmpty() == true && userLista.isNotEmpty()) {

            val tmpLista: MutableList<User> = mutableListOf()

            for (singleUser in userLista) {

                val filtriranaLista: List<Rating> =
                    userViewModel.ratings.value!!.filter { it.issuedFor.equals(singleUser.id) }
                        .toList()//userViewModel.comments.value!!

                var totalScore: Float = 0f
                filtriranaLista.forEach { totalScore += it.score }

                val numOfRatings =
                    filtriranaLista.size //userViewModel.selectedUser.value?.numOfRatings

                var ratingResult = 0f

                if (!numOfRatings!!.equals(0)) {
                    ratingResult = totalScore?.div(numOfRatings!!)!!

                } else {
                    ratingResult = 0f
                }
                singleUser.totalScore = ratingResult
                singleUser.numOfRatings = filtriranaLista.size
                tmpLista.add(singleUser)
            }

            tmpLista.sortWith( compareByDescending<User> { it.totalScore}.thenByDescending { it.numOfRatings } )

            for (sortedUser in tmpLista) {

                val viewItem: View =
                    inflater.inflate(R.layout.fragment_ranking_item, ratingLayout, false)



                val userDataLayout: View? = viewItem.findViewById(R.id.userDataLayout)
                userDataLayout?.setOnClickListener{
                    userViewModel.setSelectedUser(sortedUser)
                    val action = RatingFragmentDirections.actionRatingToFriendsProfile()
                    findNavController().navigate(action)
                }

                val numberSortText: TextView =
                    viewItem.findViewById(R.id.NumberSortText) as TextView
                val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
                val usernameView: TextView = viewItem.findViewById(R.id.userNameText)
                val ratingText: TextView = viewItem.findViewById(R.id.ratingText)

            numberSortText.setText("#" + (tmpLista.indexOf(sortedUser) + 1).toString())
            //imageView.setImageResource(R.drawable.img_0944)
                Glide.with(this).load(sortedUser.imageUrl).into(imageView)


            usernameView.setText(sortedUser.userName)

                if(sortedUser.numOfRatings.equals(0)){
                    ratingText.setText("No ratings!")
                } else
                    ratingText.setText(sortedUser.totalScore.toString())

                ratingLayout.addView(viewItem)
            }

        }
    }

}