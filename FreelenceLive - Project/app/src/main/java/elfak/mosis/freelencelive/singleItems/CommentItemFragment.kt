package elfak.mosis.freelencelive.singleItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.databinding.FragmentCommentItemBinding


class CommentItemFragment : Fragment() {

    private lateinit var binding: FragmentCommentItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_comment_item, container, false)
        binding = FragmentCommentItemBinding.inflate(inflater)
        return binding.root
    }

}