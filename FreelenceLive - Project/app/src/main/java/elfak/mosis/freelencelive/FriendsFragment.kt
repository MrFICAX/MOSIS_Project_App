package elfak.mosis.freelencelive

import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.friendRequest
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentFriendsBinding
import elfak.mosis.freelencelive.model.fragmentViewModel
import elfak.mosis.freelencelive.model.userViewModel

class FriendsFragment : Fragment() {

    var brojKorisnika = 10
    lateinit var friendsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val userViewModel: userViewModel by activityViewModels()
    lateinit var pd: ProgressDialog


    lateinit var binding: FragmentFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(context)
        pd.setCancelable(false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_friends, container, false)
        binding = FragmentFriendsBinding.inflate(inflater)

        if (userViewModel.users.value?.isEmpty() == true){

            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
            FirebaseHelper.getAllFriendRequests(requireContext(), userViewModel)
        }

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<User> = newValue
            addFriendsToLinearLayout(lista, false, "")

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsLayout = requireActivity().findViewById(R.id.FriendsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener {
            val action = FriendsFragmentDirections.actionFriendsToStartPage()
            NavHostFragment.findNavController(this).navigate(action)

        }

        binding.searchFriendsText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                addFriendsToLinearLayout(userViewModel.users.value!!, true, p0.toString())
            }
        })
        //addFriendsToLinearLayout()
    }

    private fun filterByString(listaKorisnika: List<User>, filterString: String): List<User>{

        return listaKorisnika.filter { it.userName.startsWith(filterString) }
    }

    private fun addFriendsToLinearLayout(lista: List<User>, filterFlag: Boolean, filterString: String) {
        var listaKorisnika: List<User> = lista

        if( filterFlag){
            listaKorisnika = filterByString(listaKorisnika, filterString)//listaKorisnika.filter { it.userName.startsWith(filterString) }
        }

        friendsLayout.removeAllViewsInLayout()

        for (singleUser in listaKorisnika) {
            val viewItem: View = inflater.inflate(R.layout.fragment_user_item, friendsLayout, false)

            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.userNameText)
            val phoneView: TextView = viewItem.findViewById(R.id.phoneText)
            var sendRequestButton: Button =
                viewItem.findViewById(R.id.send_request_button) as MaterialButton


            if (singleUser.friendsList.containsKey(FirebaseAuth.getInstance().currentUser?.uid)) {
                sendRequestButton.setText("FRIENDS")
                sendRequestButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                )
            } else {
                if (!checkIfRequestExists(singleUser.id)) {

                    sendRequestButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    )
                    sendRequestButton.setOnClickListener {
                        //SLANJE ZAHTEVA ZA PRIJATELJSTVO
                        //Toast.makeText(requireContext(), "SALJEM ZAHTEV ZA PRIJATELJSTVO!", Toast.LENGTH_LONG).show()
                        pd.setMessage("REGISTRATING...")
                        pd.show()
                        sendfriendRequest(singleUser.id, sendRequestButton)


                    }
                }
                else{
                    sendRequestButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.yellow
                            )
                        )
                    )
                    sendRequestButton.setText("REQUEST SENT")
                    sendRequestButton.isClickable = false
                }
            }

            viewItem.setOnClickListener {

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

    private fun checkIfRequestExists(friendId: String): Boolean {
        var flag = false
        val lista: List<friendRequest>? = userViewModel.friendReqeusts.value
        lista?.forEach {
            if (it.issuedBy.equals(FirebaseAuth.getInstance().currentUser?.uid)
                && it.requestTo.equals(friendId)
            ) {
                return true
            }
        }
        return flag
    }

    private fun sendfriendRequest(userId: String, sendRequestButton: Button) {
        FirebaseHelper.sendFriendsRequest(
            userId,
            requireContext(),
            pd,
            sendRequestButton,
            userViewModel
        )
    }

}
