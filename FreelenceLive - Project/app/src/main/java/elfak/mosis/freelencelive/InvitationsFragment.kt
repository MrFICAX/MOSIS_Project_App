package elfak.mosis.freelencelive

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import elfak.mosis.freelencelive.adapters.InvitationsRecyclerAdapter
import elfak.mosis.freelencelive.databinding.FragmentInvitationsBinding
import elfak.mosis.freelencelive.databinding.FragmentJobViewBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InvitationsFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null

    var brojInvitationsa = 3
    lateinit var invitationsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())


    private lateinit var binding: FragmentInvitationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvitationsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_invitations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invitationsLayout =  requireActivity().findViewById(R.id.InvitationsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        binding.shapeableImageView.setOnClickListener{
            val action = InvitationsFragmentDirections.actionInvitationsToStartpage()
            NavHostFragment.findNavController(this).navigate(action)
        }

        addInvitationsToLinearLayout()
    }

    private fun addInvitationsToLinearLayout() {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for(i in 0..brojInvitationsa - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_invitation_item, invitationsLayout, false)
            val imageView: ImageView = viewItem.findViewById(R.id.userProfilePictureInvitations) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.usernameTextInvitations)
            val jobTitle: TextView  = viewItem.findViewById(R.id.JobTitleInvitations)
            val dateTitle: TextView  = viewItem.findViewById(R.id.DateTextInvitations)
            val itemMap: ImageView = viewItem.findViewById(R.id.IconMapInvitations) as ImageView
            val acceptButton: LinearLayout = viewItem.findViewById(R.id.linearLayoutAccept) as LinearLayout
            val declineButton: LinearLayout = viewItem.findViewById(R.id.linearLayoutDecline) as LinearLayout

            itemMap.setOnClickListener{
                Toast.makeText(requireContext(), "KLIK NA VIEW LOCATION!", Toast.LENGTH_LONG).show()
                val action = InvitationsFragmentDirections.actionInvitationsToViewLocationOnMap()
                findNavController().navigate(action)
            }
            acceptButton.setOnClickListener {
                Toast.makeText(requireContext(), "ACCEPT BUTTON!", Toast.LENGTH_LONG).show()
            }
            declineButton.setOnClickListener {
                Toast.makeText(requireContext(), "DECLINE BUTTON!", Toast.LENGTH_LONG).show()
            }


            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)

            usernameView.setText("Prijatelj" + i.toString())
            jobTitle.setText("Posao"+i.toString())
            dateTitle.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

            invitationsLayout.addView(viewItem)
        }
    }
}