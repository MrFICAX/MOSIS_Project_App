package elfak.mosis.freelencelive.singleItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentInvitationItemBinding

class InvitationItemFragment : Fragment() {

    private lateinit var binding: FragmentInvitationItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_invitation_item, container, false)
        binding = FragmentInvitationItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.IconMapInvitations.setOnClickListener {
            Toast.makeText(context, "POZDRAV", Toast.LENGTH_LONG).show()
            //findNavController().navigate(R.id.action_invitations_to_viewLocationOnMap)

        }
    }
}