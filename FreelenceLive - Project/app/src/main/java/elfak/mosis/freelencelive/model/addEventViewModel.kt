package elfak.mosis.freelencelive.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.Event
import java.sql.Date
import java.sql.Time

class addEventViewModel :ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event : LiveData<Event>
        get() = _event

    private val _longitude =   MutableLiveData<String>("")
    val longitude : LiveData<String>
        get() = _longitude

    fun setEventName(name: String){

        val newEvent: Event? = event.value
        newEvent?.name = name
        _event.value = newEvent
        //_event.value?.name = name


//        if (_event.value != null)
//            _event.value = _event.value!!.let {
//                it.name = name
//                it
//            }
    }

    fun setLocation(lon: String, lat:String) {
        _longitude.value = lat
    }

    init{
        _event.value = Event("","",false, "",0.0,0.0, Date(0, 0, 0), hashMapOf(), hashMapOf(), mutableListOf())
        _event.value?.name = "posao"
        _longitude.value = "CLICK ME!"
    }
}