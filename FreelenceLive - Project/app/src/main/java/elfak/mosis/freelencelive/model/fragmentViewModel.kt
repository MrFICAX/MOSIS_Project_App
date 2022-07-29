package elfak.mosis.freelencelive.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.User

class fragmentViewModel: ViewModel() {

    private val _fragment = MutableLiveData<Fragment>()
    val fragment : LiveData<Fragment>
        get() = _fragment

    fun setFragment(fragment: Fragment){
        _fragment.value = fragment
    }
}