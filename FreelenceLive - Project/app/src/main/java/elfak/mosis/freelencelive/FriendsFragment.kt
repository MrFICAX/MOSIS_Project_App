package elfak.mosis.freelencelive

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentFriendsBinding
import elfak.mosis.freelencelive.databinding.FragmentRatingBinding
import elfak.mosis.freelencelive.model.userViewModel

class FriendsFragment : Fragment() {

    var brojKorisnika = 10
    lateinit var friendsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()


    lateinit var binding: FragmentFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_friends, container, false)
        binding = FragmentFriendsBinding.inflate(inflater)

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            addFriendsToLinearLayout()

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsLayout =  requireActivity().findViewById(R.id.FriendsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener{
            val action = FriendsFragmentDirections.actionFriendsToStartPage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        if(userViewModel.users.value?.isEmpty() == true)
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)

        //addFriendsToLinearLayout()
    }

    private fun addFriendsToLinearLayout() {
        val listaKorisnika = userViewModel.users

        for(singleUser in listaKorisnika.value!!){
            val viewItem: View = inflater.inflate(R.layout.fragment_user_item, friendsLayout, false)

            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.userNameText)
            val phoneView: TextView = viewItem.findViewById(R.id.phoneText)
            var sendRequestButton: Button = viewItem.findViewById(R.id.send_request_button) as MaterialButton

//            if(i < 2) {// == prijatelj)
//                sendRequestButton.setText("FRIENDS")
//                sendRequestButton.setBackgroundTintList(
//                    ColorStateList.valueOf(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            android.R.color.holo_red_dark
//                        )
//                    )
//                )
//            }
//            else{
//                sendRequestButton.setBackgroundTintList(
//                    ColorStateList.valueOf(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.green
//                        )
//                    )
//                )
//                sendRequestButton.setOnClickListener {
//                    //SLANJE ZAHTEVA ZA PRIJATELJSTVO
//                    Toast.makeText(requireContext(), "SALJEM ZAHTEV ZA PRIJATELJSTVO!", Toast.LENGTH_LONG).show()
//                }
//            }

            viewItem.setOnClickListener{

                userViewModel.setSelectedUser(singleUser)

                val action = FriendsFragmentDirections.actionFriendsToFriendsProfile()
                NavHostFragment.findNavController(this).navigate(action)
            }

            //imageView.setImageResource(R.drawable.img_0944)

            Log.d("100", singleUser.toString())
            Glide.with(this).load(singleUser.imageUrl).into(imageView)

            usernameView.setText(singleUser.userName)
            phoneView.setText(singleUser.phoneNumber)

            friendsLayout.addView(viewItem)
        }
    }

}
