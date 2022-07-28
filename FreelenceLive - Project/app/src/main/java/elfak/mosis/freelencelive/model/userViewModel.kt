package elfak.mosis.freelencelive.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User

class userViewModel: ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val _events: MutableLiveData<List<Event>> = MutableLiveData(listOf())
    val events: LiveData<List<Event>>
        get() = _events

    private val _users: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: LiveData<List<User>>
        get() = _users

    private val _selectedUser = MutableLiveData<User>()
    val selectedUser: LiveData<User>
        get() = _selectedUser

    fun setSelectedUser(selectedUser: User){
        _selectedUser.value = selectedUser
    }

//POTREBNO TESTIRATI
    fun addNewEvent(newEvent: Event){
        _events.value = _events.value?.plus(newEvent)
    }

    fun addUserList(lista: List<User>){
        _users.value = lista
    }

    fun setUserName(name: String){

        val newUser: User? = user.value
        newUser?.userName = name
        _user.value = newUser
    }

    fun setNewUser(user: User){
        _user.value = user
    }

    init{

    }

}