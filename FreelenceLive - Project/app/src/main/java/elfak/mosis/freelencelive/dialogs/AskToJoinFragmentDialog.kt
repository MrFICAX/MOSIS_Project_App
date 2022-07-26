package elfak.mosis.freelencelive.dialogs

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import elfak.mosis.freelencelive.HelpFragmentDirections
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.StartPageFragmentDirections
import elfak.mosis.freelencelive.databinding.FragmentDialogAskToJoinBinding
import elfak.mosis.freelencelive.databinding.FragmentDialogInviteFriendBinding


class AskToJoinFragmentDialog : DialogFragment() {

    var brojPrijatelja = 5

    lateinit var friendsView: LinearLayout
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    lateinit var binding: FragmentDialogAskToJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogAskToJoinBinding.inflate(inflater)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsView = binding.friendsView
        inflater = LayoutInflater.from(requireContext())

        addFriendsToFriendsView()

    }

    private fun addFriendsToFriendsView() {
        //DODAVANJE FRIEND ITEM-A IZ LISTE PRIJAVLJENIH KORISNIKA
        //DODAVANJE SLIKA IZ LISTE SLIKA ZA OVAJ JOB
        for(i in 0..brojPrijatelja - 1){
            val viewItem: View = inflater.inflate(R.layout.user_item, friendsView, false)
            val imageView: ImageView = viewItem.findViewById(R.id.imageView) as ImageView

            imageView.setOnClickListener {
                //appViewModel.selectedUser = nizKorisnika[i]
                val action = StartPageFragmentDirections.actionStartpageToFriendsProfile()
                NavHostFragment.findNavController(this).navigate(action)
                dialog?.dismiss()
            }

            if (i < 2)
                imageView.setImageResource(R.drawable.img_0944)
            else
                imageView.setImageResource(R.drawable.img_0950)

            val textView: TextView = viewItem.findViewById(R.id.userNameText) as TextView
            textView.setText("Prijatelj" + i.toString())

            friendsView.addView(viewItem)
        }
    }

}