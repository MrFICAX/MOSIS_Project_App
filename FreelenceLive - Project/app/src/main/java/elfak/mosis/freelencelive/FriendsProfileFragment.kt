package elfak.mosis.freelencelive

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
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
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.Comment
import elfak.mosis.freelencelive.data.Rating
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentFriendsProfileBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel


class FriendsProfileFragment : Fragment() {

    lateinit var CommentsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()
    private val fragmentViewModel: fragmentViewModel by activityViewModels()

    lateinit var pd: ProgressDialog

    lateinit var binding: FragmentFriendsProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)
        fragmentViewModel.setFragment(null)

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
            //setRating()
        }
        userViewModel.selectedUser.observe(viewLifecycleOwner, FriendObserver)

        val CommentsObserver = Observer<List<Comment>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val filtriranaLista: List<Comment> =
                userViewModel.comments.value!!.filter { it.issuedFor.equals(userViewModel.selectedUser.value?.id) }
                    .toList()//userViewModel.comments.value!!
            addCommentsToLinearLayout(filtriranaLista)

        }
        userViewModel.comments.observe(viewLifecycleOwner, CommentsObserver)

        val RatingsObserver = Observer<List<Rating>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val filtriranaLista: List<Rating> =
                userViewModel.ratings.value!!.filter { it.issuedFor.equals(userViewModel.selectedUser.value?.id) }
                    .toList()//userViewModel.comments.value!!
            setRating(filtriranaLista)

        }
        userViewModel.ratings.observe(viewLifecycleOwner, RatingsObserver)



        if (userViewModel.users.value?.isEmpty() == true) {
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
        }

        val listaKomentara: List<Comment> = userViewModel.comments.value!!
        if (listaKomentara.filter { it.issuedFor.equals(userViewModel.selectedUser.value?.id) }
                .isEmpty())
            userViewModel.selectedUser.value?.id?.let {
                FirebaseHelper.getAllCommentsForSingleUser(
                    it,
                    userViewModel,
                    requireContext()
                )
            }

        val listaRatinga: List<Rating> = userViewModel.ratings.value!!
        if (listaRatinga.filter { it.issuedFor.equals(userViewModel.selectedUser.value?.id) }
                .isEmpty())
            userViewModel.selectedUser.value?.id?.let {
                FirebaseHelper.getAllRatingsForSingleUser(
                    it,
                    userViewModel,
                    requireContext()
                )
            }
        return binding.root
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
            binding.ScoreText.text = ratingResult.toString()!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommentsLayout = requireActivity().findViewById(R.id.CommentsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = FriendsProfileFragmentDirections.actionFriendsProfileToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.commentText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty())

                    binding.leaveAComment.isSelected = true
                binding.leaveAComment.isEnabled = true
                binding.leaveAComment.isClickable = true
            }

        })

        binding.rateButton.setOnClickListener {
            rateSelectedUser()
        }

        //addCommentsToLinearLayout(lista)

        binding.leaveAComment.setOnClickListener {
            PostAComment()
        }

    }

    private fun rateSelectedUser() {
        val score: Float = binding.Rating.rating
        val issuedFor: String = userViewModel.selectedUser.value?.id.toString()
        val issuedBy: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val newRating: Rating = Rating("", issuedBy, issuedFor, score)
        pd.setMessage("Posting...")
        pd.show()
        FirebaseHelper.postRating(
            newRating,
            requireContext(),
            userViewModel,
            pd,
            binding.commentText
        )
    }

    private fun PostAComment() {
        val text: String = binding.commentText.text.toString()
        val issuedFor: String = userViewModel.selectedUser.value?.id.toString()
        val issuedBy: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val newComment: Comment = Comment(issuedBy, issuedFor, text)
        pd.setMessage("Posting...")
        pd.show()
        FirebaseHelper.postComment(
            newComment,
            requireContext(),
            userViewModel,
            pd,
            binding.commentText
        )


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