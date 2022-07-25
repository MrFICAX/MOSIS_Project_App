package elfak.mosis.freelencelive.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.freelencelive.InvitationsFragmentDirections
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.StartPageFragmentDirections
import elfak.mosis.freelencelive.data.Event
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InvitationsRecyclerAdapter : RecyclerView.Adapter<InvitationsRecyclerAdapter.ViewHolder>() {

    val current = LocalDateTime.now()
    val Dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")// HH:mm:ss.SSS")
    val Timeformatter = DateTimeFormatter.ofPattern("HH:mm")// HH:mm:ss.SSS")
    val formattedDate = current.format(Dateformatter)
    val formattedTime = current.format(Timeformatter)

    private var events = arrayOf(
        Event("Cepanje drva", "Jovan", "3.2","5.2", formattedDate, formattedTime.toString()),
        Event("Kosenje trave", "Jovan", "3.2","5.2", formattedDate, formattedTime.toString()),
        Event("Selidba", "Jovan", "3.2","5.2", formattedDate, formattedTime.toString())
    )
    override fun getItemCount(): Int {
        return events.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationsRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_invitation_item, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: InvitationsRecyclerAdapter.ViewHolder, position: Int) {
        holder.imageView.setImageResource(R.drawable.img_0944)
        holder.usernameView.text = events[position].organiser
        holder.jobTitle.text = events[position].name
        holder.dateTitle.text = events[position].date
    }


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imageView: ImageView
        var usernameView: TextView
        var jobTitle: TextView
        var dateTitle: TextView
        var itemMap: ImageView
//        var acceptLayout: LinearLayout
//        var declineLayout: LinearLayout

        init {
            imageView = itemView.findViewById(R.id.userProfilePictureInvitations)
            usernameView = itemView.findViewById(R.id.usernameTextInvitations)
            jobTitle = itemView.findViewById(R.id.JobTitleInvitations)
            dateTitle = itemView.findViewById(R.id.DateTextInvitations)
            itemMap = itemView.findViewById(R.id.IconMapInvitations)
            itemMap.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                Toast.makeText(itemView.context, "You clicked on event"+ events[position].name, Toast.LENGTH_LONG).show()
                val action = InvitationsFragmentDirections.actionInvitationsToViewLocationOnMap()
                Navigation.findNavController(itemView).navigate(action)

            }
//            acceptLayout = itemView.findViewById(R.id.linearLayoutAccept)
//            declineLayout = itemView.findViewById(R.id.linearLayoutDecline)

        }
    }



}