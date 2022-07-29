package elfak.mosis.freelencelive.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.*

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

    private val _friendReqeusts: MutableLiveData<List<friendRequest>> = MutableLiveData(listOf())
    val friendReqeusts: LiveData<List<friendRequest>>
        get() = _friendReqeusts


    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData(listOf())
    val comments: LiveData<List<Comment>>
        get() = _comments

    private val _invitations: MutableLiveData<List<Invitation>> = MutableLiveData(listOf())
    val invitations: LiveData<List<Invitation>>
        get() = _invitations

    fun addNewInvitation(newInvitation: Invitation){
        _invitations.value = _invitations.value?.plus(newInvitation)
    }

    fun addNewComment(newComment: Comment){
        _comments.value = _comments.value?.plus(newComment)
    }

    fun addCommentList(lista: List<Comment>){
        _comments.value = lista
    }

    fun addNewFriendRequest(newfriendRequest: friendRequest){
        _friendReqeusts.value = _friendReqeusts.value?.plus(newfriendRequest)
    }

    fun addFriendsRequestList(lista: List<friendRequest>){
        _friendReqeusts.value = lista
    }
    fun setSelectedUser(selectedUser: User){
        _selectedUser.value = selectedUser
    }

//POTREBNO TESTIRATI
    fun addNewEvent(newEvent: Event){
        _events.value = _events.value?.plus(newEvent)
    }

    fun addInvitationList(lista: List<Invitation>){
        _invitations.value = lista
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