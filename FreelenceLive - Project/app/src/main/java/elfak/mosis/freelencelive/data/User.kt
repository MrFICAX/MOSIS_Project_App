package elfak.mosis.freelencelive.data

import java.io.Serializable
import java.math.RoundingMode

data class User(
    var email: String,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var totalScore: Int,
    var numOfRatings: Int,
    var imageUrl: String
//    var friendsList: List<User>?,
//    var mozdafriendsId: List<String>?
                ) : Serializable
