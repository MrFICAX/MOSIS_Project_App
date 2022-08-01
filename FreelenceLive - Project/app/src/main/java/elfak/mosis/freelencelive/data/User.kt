package elfak.mosis.freelencelive.data

import java.io.Serializable
import java.math.RoundingMode

data class User(
    var id: String,
    var email: String,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var totalScore: Float,
    var numOfRatings: Int,
    var imageUrl: String,
    var friendsList: HashMap<String,Boolean>
//    var friendsList: List<User>?,
//    var mozdafriendsId: List<String>?
                ) : Serializable
