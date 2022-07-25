package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentDashboardBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DashboardFragment : Fragment() {

    var brojKomentara = 10
    lateinit var CommentsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommentsLayout =  requireActivity().findViewById(R.id.CommentsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

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