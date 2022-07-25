package elfak.mosis.freelencelive

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import elfak.mosis.freelencelive.databinding.FragmentFriendsBinding
import elfak.mosis.freelencelive.databinding.FragmentRatingBinding

class FriendsFragment : Fragment() {

    var brojKorisnika = 10
    lateinit var friendsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

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

        addFriendsToLinearLayout()
    }

    private fun addFriendsToLinearLayout() {
        for(i in 0..brojKorisnika - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_user_item, friendsLayout, false)

            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.userNameText)
            val phoneView: TextView = viewItem.findViewById(R.id.phoneText)
            var sendRequestButton: Button = viewItem.findViewById(R.id.send_request_button) as MaterialButton

            if(i < 2) {// == prijatelj)
                sendRequestButton.setText("FRIENDS")
                sendRequestButton.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_dark
                        )
                    )
                )
            }
            else{
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
                    Toast.makeText(requireContext(), "SALJEM ZAHTEV ZA PRIJATELJSTVO!", Toast.LENGTH_LONG).show()
                }
            }
            imageView.setImageResource(R.drawable.img_0944)
            usernameView.setText("UserName "+i.toString())
            phoneView.setText("061 644 50 67")

            friendsLayout.addView(viewItem)
        }
    }

}
