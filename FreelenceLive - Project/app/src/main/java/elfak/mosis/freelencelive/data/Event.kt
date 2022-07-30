package elfak.mosis.freelencelive.data

import java.io.Serializable
import java.sql.Time
import java.util.*

data class Event(
    public var id: String,
    public var name: String,
    public var organiser: String,
    public var longitude: Double,
    public var latitude: Double,
    public var date: Date?,
    public var time: Time?,
    var listOfUsers: HashMap<String, Boolean>
) : Serializable
