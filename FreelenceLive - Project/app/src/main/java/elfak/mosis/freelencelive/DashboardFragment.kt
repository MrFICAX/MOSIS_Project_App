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
import elfak.mosis.freelencelive.data.Comment
import elfak.mosis.freelencelive.data.Rating
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentDashboardBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel


class DashboardFragment : Fragment() {

    lateinit var CommentsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()
    private val fragmentViewModel: fragmentViewModel by activityViewModels()

    private lateinit var binding: FragmentDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.inflate(inflater)
        fragmentViewModel.setFragment(null)

        val UsersObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            //val lista: List<Comment> = newValue
            val filtriranaLista: List<Comment> =
                userViewModel.comments.value!!.filter { it.issuedFor.equals(userViewModel.user.value?.id) }
                    .toList()//userViewModel.comments.value!!

            addCommentsToLinearLayout(filtriranaLista)

        }
        userViewModel.users.observe(viewLifecycleOwner, UsersObserver)


        val CommentsObserver = Observer<List<Comment>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            //val lista: List<Comment> = newValue
            val filtriranaLista: List<Comment> =
                userViewModel.comments.value!!.filter { it.issuedFor.equals(userViewModel.user.value?.id) }
                    .toList()//userViewModel.comments.value!!

            addCommentsToLinearLayout(filtriranaLista)

        }
        userViewModel.comments.observe(viewLifecycleOwner, CommentsObserver)


        val listaKomentara: List<Comment> = userViewModel.comments.value!!

        if (userViewModel.users.value?.isEmpty() == true) {

            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
        }
        if (listaKomentara.filter { it.issuedFor.equals(userViewModel.user.value?.id) }.isEmpty())
            userViewModel.user.value?.id?.let {
                FirebaseHelper.getAllCommentsForSingleUser(
                    it,
                    userViewModel,
                    requireContext()
                )
            }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommentsLayout = requireActivity().findViewById(R.id.CommentsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        val RatingsObserver = Observer<List<Rating>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val filtriranaLista: List<Rating> =
                userViewModel.ratings.value!!.filter { it.issuedFor.equals(userViewModel.user.value?.id) }
                    .toList()//userViewModel.comments.value!!
            setRating(filtriranaLista)

        }
        userViewModel.ratings.observe(viewLifecycleOwner, RatingsObserver)

//        val totalScore = userViewModel.user.value?.totalScore?.toFloat()
//        val numOfRatings = userViewModel.user.value?.numOfRatings
//        var ratingResult = 0f
//        if (numOfRatings!!.equals(0))
//            ratingResult = totalScore?.div(numOfRatings!!)!!
//
//        binding.Rating.rating = ratingResult!!
//        binding.ScoreNumber.text = ratingResult.toString()!!
        //addCommentsToLinearLayout()
    }

    private fun setRating(lista: List<Rating>) {

        var total: Float = 0f
        for (element in lista) {
            total += element.score
        }


        val totalScore = total//userViewModel.selectedUser.value?.totalScore?.toFloat()
        val numOfRatings = lista.size //userViewModel.selectedUser.value?.numOfRatings

        var ratingResult = 0f

        if (!numOfRatings!!.equals(0)) {
            ratingResult = totalScore?.div(numOfRatings!!)!!
            binding.Rating.rating = ratingResult!!
            binding.ScoreText.text = ratingResult.toString()!! +"/"+ numOfRatings.toString() + " reviews"
        }
    }

    private fun addCommentsToLinearLayout(lista: List<Comment>) {
        CommentsLayout.removeAllViewsInLayout()
        for (singleComment in lista) {

            val viewItem: View =
                inflater.inflate(R.layout.fragment_comment_item, CommentsLayout, false)

            val imageView: ImageView =
                viewItem.findViewById(R.id.imageCameraBackground) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.username)
            val commentField: TextView = viewItem.findViewById(R.id.commentField)


//            imageView.setImageResource(R.drawable.img_0944)
//            usernameView.setText("UserName "+i.toString())
//            commentField.setText("ASFASDSADFSADLKFJS;LKFJSA;KLFJSAKL;FJLKASJFDKASL;WKQL;ERJKL")

            var issuedBy: User? =
                userViewModel.users.value?.filter { it.id.equals(singleComment.issuedBy) }
                    ?.firstOrNull()

            if (userViewModel.user.value?.id.equals(singleComment.issuedBy))
                issuedBy = userViewModel.user.value

            Glide.with(this).load(issuedBy?.imageUrl).into(imageView)

            //imageView.setImageResource(R.drawable.img_0944)
            usernameView.setText(issuedBy?.userName)
            commentField.setText(singleComment.text)

            CommentsLayout.addView(viewItem)
        }
    }
}