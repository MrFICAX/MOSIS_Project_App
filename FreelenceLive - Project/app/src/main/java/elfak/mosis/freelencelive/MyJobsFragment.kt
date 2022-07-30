package elfak.mosis.freelencelive

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavAction
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentMyJobsBinding
import elfak.mosis.freelencelive.dialogs.AdvancedSearchFragmentDialog
import elfak.mosis.freelencelive.dialogs.searchByRadiusFragmentDialog
import elfak.mosis.freelencelive.model.fragmentViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyJobsFragment : Fragment() {
    var flag: Boolean = false

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null

    var brojJobsa = 10
    lateinit var jobsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private val fragmentViewModel: fragmentViewModel by activityViewModels()

    private lateinit var binding: FragmentMyJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.setFragment(null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyJobsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_my_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shapeableImageView.setOnClickListener{
            val action = MyJobsFragmentDirections.actionMyJobsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.buttonAdvancedSearch.setOnClickListener{

            val fragmentNovi = AdvancedSearchFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }

        jobsLayout =  requireActivity().findViewById(R.id.MyJobsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        addJobsToLinearLayout()

    }

    private fun addJobsToLinearLayout() {

        for(i in 0..brojJobsa - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_job_item, jobsLayout, false)

            val imageView: ImageView = viewItem.findViewById(R.id.imageCameraBackground) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.username)
            val jobTitle: TextView = viewItem.findViewById(R.id.JobTitleInvitations)
            val dateTitle: TextView = viewItem.findViewById(R.id.DateTextInvitations)



            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)

            usernameView.setText("Prijatelj" + i.toString())
            jobTitle.setText("Posao"+i.toString())
            dateTitle.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            viewItem.setOnClickListener{
                //ako sam ja organizator
                val action: NavDirections
                if (this.flag){
                    action = MyJobsFragmentDirections.actionMyJobsToJobReview()
                    this.flag = false
                }
                else{
                    action = MyJobsFragmentDirections.actionMyJobsToJobView()
                    this.flag = true
                }

                NavHostFragment.findNavController(this).navigate(action)

            }
            jobsLayout.addView(viewItem)
        }
    }

}