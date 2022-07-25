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
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentInvitationsBinding
import elfak.mosis.freelencelive.databinding.FragmentNotificationsBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationsFragment : Fragment() {

    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var imageBitmap: Bitmap
    private var formCheck: BooleanArray = BooleanArray(7)
    private var imageUri: Uri? = null

    var brojInvitationsa = 3
    lateinit var notificationsLayout: LinearLayout // requireActivity().findViewById(R.id.gallery) //binding.gallery
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsLayout =  requireActivity().findViewById(R.id.NotificationsLayout) //binding.gallery
        inflater = LayoutInflater.from(requireContext())

        addNotificationsToLinearLayout()
    }

    private fun addNotificationsToLinearLayout() {
        for(i in 0..brojInvitationsa - 1){
            val viewItem: View = inflater.inflate(R.layout.fragment_notification_item, notificationsLayout, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageCameraBackground) as ImageView
            val usernameView: TextView = viewItem.findViewById(R.id.username)
            val jobTitle: TextView = viewItem.findViewById(R.id.JobTitleInvitations)
            val dateTitle: TextView = viewItem.findViewById(R.id.DateTextInvitations)
            val itemMap: ImageView = viewItem.findViewById(R.id.IconMapInvitations) as ImageView
            val acceptButton: LinearLayout = viewItem.findViewById(R.id.linearLayoutAccept) as LinearLayout
            val declineButton: LinearLayout = viewItem.findViewById(R.id.linearLayoutDecline) as LinearLayout

            itemMap.setOnClickListener{
                Toast.makeText(requireContext(), "KLIK NA VIEW LOCATION!", Toast.LENGTH_LONG).show()
                val action = NotificationsFragmentDirections.actionNotificationsToViewLocationOnMap()
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

            notificationsLayout.addView(viewItem)
        }
    }

}