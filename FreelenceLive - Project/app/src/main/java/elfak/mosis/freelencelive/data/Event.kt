package elfak.mosis.freelencelive.data

import java.sql.Time
import java.util.*

data class Event(var name:String, var longitude:String, var latitude:String, val date: Date, val time: Time)
