package elfak.mosis.freelencelive.singleItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentNotificationItemBinding


class NotificationItemFragment : Fragment() {

    private lateinit var binding: FragmentNotificationItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationItemBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_notification_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconMap.setOnClickListener {
            Toast.makeText(context, "POZDRAV", Toast.LENGTH_LONG).show()
            //findNavController().navigate(R.id.action_notifications_to_viewLocationOnMap)

        }
    }

}