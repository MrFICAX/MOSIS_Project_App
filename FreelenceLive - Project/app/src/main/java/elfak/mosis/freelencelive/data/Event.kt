package elfak.mosis.freelencelive.data

import com.google.type.DateTime
import java.io.Serializable
import java.sql.Time
import java.util.*

data class Event(
    public var id: String,
    public var name: String,
    public var finished: Boolean,
    public var organiser: String,
    public var longitude: Double,
    public var latitude: Double,
    public var date: Date,
//    public var time: Time?,
    var listOfUsers: HashMap<String, Boolean>,
    var dateHashMap: HashMap<String, Int>

) : Serializable
