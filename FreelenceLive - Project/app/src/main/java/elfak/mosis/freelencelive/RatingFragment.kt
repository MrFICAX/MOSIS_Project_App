package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import elfak.mosis.freelencelive.databinding.FragmentDashboardBinding
import elfak.mosis.freelencelive.databinding.FragmentRatingBinding
import org.w3c.dom.Text

class RatingFragment : Fragment() {

    var brojRatings = 10
    lateinit var ratingLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    lateinit var binding: FragmentRatingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_rating, container, false)
        binding = FragmentRatingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratingLayout =  requireActivity().findViewById(R.id.RatingLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener{
            val action = DashboardFragmentDirections.actionDashboardToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        addRatingsToLinearLayout()
    }

    private fun addRatingsToLinearLayout() {
        for(i in 0..brojRatings - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_ranking_item, ratingLayout, false)

            val numberSortText: TextView = viewItem.findViewById(R.id.NumberSortText) as TextView
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.userNameText)
            val ratingText: TextView = viewItem.findViewById(R.id.ratingText)

            numberSortText.setText("#"+i.toString())
            imageView.setImageResource(R.drawable.img_0944)
            usernameView.setText("UserName "+i.toString())
            ratingText.setText("Rating"+"4.5")

            ratingLayout.addView(viewItem)
        }
    }

}