package elfak.mosis.freelencelive.model

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.Event

class addEventViewModel :ViewModel() {
    private val _event =   MutableLiveData<Event>()
    val event : LiveData<Event>
        get() = _event

    fun setEventName(name: Editable?){
        _event.value?.name = name.toString()
    }
}