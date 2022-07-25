package elfak.mosis.freelencelive.data

import java.sql.Time
import java.util.*

data class Event(public var name:String,public var organiser: String, var longitude:String, var latitude:String,public val date: String, val time: String)
