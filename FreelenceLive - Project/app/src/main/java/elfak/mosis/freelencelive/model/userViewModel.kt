package elfak.mosis.freelencelive.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.*
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class userViewModel : ViewModel() {

    private val _newEvent = MutableLiveData<Event>()
    val newEvent: LiveData<Event>
        get() = _newEvent

    private var _dateChanged: Boolean = false
    val dateChanged: Boolean
        get() = _dateChanged

    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    private val _events: MutableLiveData<List<Event>> = MutableLiveData(listOf())
    val events: LiveData<List<Event>>
        get() = _events

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _users: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: LiveData<List<User>>
        get() = _users

    private val _selectedUser = MutableLiveData<User>()
    val selectedUser: LiveData<User>
        get() = _selectedUser

    private val _friendReqeusts: MutableLiveData<List<friendRequest>> = MutableLiveData(listOf())
    val friendReqeusts: LiveData<List<friendRequest>>
        get() = _friendReqeusts

    private val _askToJoin: MutableLiveData<List<askToJoin>> = MutableLiveData(listOf())
    val askToJoin: LiveData<List<askToJoin>>
        get() = _askToJoin

    private val _ratings: MutableLiveData<List<Rating>> = MutableLiveData(listOf())
    val ratings: LiveData<List<Rating>>
        get() = _ratings

    private val _comments: MutableLiveData<List<Comment>> = MutableLiveData(listOf())
    val comments: LiveData<List<Comment>>
        get() = _comments

    private val _invitations: MutableLiveData<List<Invitation>> = MutableLiveData(listOf())
    val invitations: LiveData<List<Invitation>>
        get() = _invitations

    private var _gsPhotosList: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val gsPhotosList: LiveData<List<String>>
        get() = _gsPhotosList

    fun addNewInvitation(newInvitation: Invitation) {
        _invitations.value = _invitations.value?.plus(newInvitation)
    }

//    fun addNewComment(newComment: Comment){
//        _comments.value = _comments.value?.plus(newComment)
//    }

    fun addEventList(lista: List<Event>) {
        _events.value = lista
    }

    fun addCommentList(lista: List<Comment>) {
        _comments.value = lista
    }

    fun addRatingList(lista: List<Rating>) {
        _ratings.value = lista
    }

    fun addNewFriendRequest(newfriendRequest: friendRequest) {
        _friendReqeusts.value = _friendReqeusts.value?.plus(newfriendRequest)
    }

    fun addNewAskToJoin(newAskToJoin: askToJoin) {
        _askToJoin.value = _askToJoin.value?.plus(listOf(newAskToJoin))
    }

    fun addFriendsRequestList(lista: List<friendRequest>) {
        _friendReqeusts.value = lista
    }

    fun addAskToJoinList(lista: List<askToJoin>) {
        _askToJoin.value = lista
    }

    fun setSelectedUser(selectedUser: User) {
        _selectedUser.value = selectedUser
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun setSelectedEventDate(year: Int, month: Int, day: Int) {
//        val newEvent: Event? = _newEvent.value
//        newEvent?.date = Date(year, month, day)
        val hour = _selectedEvent.value?.date?.hours
        val minute = _selectedEvent.value?.date?.minutes
        _selectedEvent.value?.date = Date(year, month, day, hour!!, minute!!)

        _dateChanged = true
    }

    fun setSelectedEventTime(hour: Int, minute: Int) {

        _selectedEvent.value?.date?.hours = hour
        _selectedEvent.value?.date?.minutes = minute
        _dateChanged = true
    }

    fun setPhotoUrlToUser(userId: String, imgUrl: String) {

        val user: User? = _users.value?.filter { it.id.equals(userId) }?.firstOrNull()
        user?.imageUrl = imgUrl
        val newListOfUsers: MutableList<User>? =
            _users.value?.filter { !it.id.equals(userId) } as MutableList<User>?
        newListOfUsers?.add(user!!)
        _users.value = newListOfUsers

    }

    fun addInvitationList(lista: List<Invitation>) {
        _invitations.value = lista
    }

    fun addUserList(lista: List<User>) {
        _users.value = lista
    }

    fun setUserName(name: String) {

        val newUser: User? = user.value
        newUser?.userName = name
        _user.value = newUser
    }

    fun setNewUser(user: User) {
        _user.value = user
    }

    fun setNewEventDate(year: Int, month: Int, day: Int) {
//        val newEvent: Event? = _newEvent.value
//        newEvent?.date = Date(year, month, day)
        _newEvent.value?.date = Date(year, month, day)
    }

    fun setNewEventTime(hour: Int, minute: Int) {
//        val newEvent: Event? = _newEvent.value
//        newEvent?.time = Time(hour, minute, 0)
//        _newEvent.value = newEvent
//
        _newEvent.value?.date?.hours = hour
        _newEvent.value?.date?.minutes = minute

        //_newEvent.value?.time = Time(hour, minute, 0)
    }

    fun setNewEventName(name: String) {

        val newEvent: Event? = _newEvent.value
        newEvent?.name = name
        _newEvent.value = newEvent
    }

    fun setNewEventLocation(latitude: Double, longitude: Double) {
        val newEvent: Event? = _newEvent.value
        newEvent?.latitude = latitude
        newEvent?.longitude = longitude
        _newEvent.value = newEvent
    }

    fun setNewEventUserAdd(userId: String) {
        _newEvent.value?.listOfUsers?.put(userId, false)

    }

    fun setNewEventUserRemove(userId: String) {
        _newEvent.value?.listOfUsers?.remove(userId)

    }

    fun InitialSetSelectedEvent(){
        _dateChanged = false
    }

    fun setNewEvent() {


        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterday = cal

        val time1 = LocalDateTime.now()
        val time = Time(time1.hour, time1.minute, time1.second)
        var date = Date(
            yesterday.get(Calendar.YEAR),
            yesterday.get(Calendar.MONTH),
            yesterday.get(Calendar.DAY_OF_MONTH),
            time1.hour,
            time1.minute
        )

        _newEvent.value = Event(
            "", "", false, "", 0.0, 0.0, date, hashMapOf<String, Boolean>(),
            hashMapOf<String, Int>(), mutableListOf()
        )
    }

    fun setSelectedEventName(name: String) {
        val selectedEvent: Event? = _selectedEvent.value
        selectedEvent?.name = name
        _selectedEvent.value = selectedEvent
    }

    fun setSelectedEventGSPhotos(photosListGSTmp: MutableList<String>) {
       _gsPhotosList.value = photosListGSTmp
    }

    fun deletePhotoFromSelectedEvent(photoString: String){
        val photoList = _selectedEvent.value?.photosList?.filter { !it.equals(photoString) }
        _selectedEvent.value?.photosList = photoList as MutableList<String>

    }

    fun setPhotoUrlToSelectedEvent( imgUrl: String) {

//        val user: User? = _users.value?.filter { it.id.equals(userId) }?.firstOrNull()
//        user?.imageUrl = imgUrl
//
        val selectedEvent: Event? = _selectedEvent.value
        selectedEvent?.photosList?.add(imgUrl)
        _selectedEvent.value = selectedEvent

//        val newListOfUsers: MutableList<User>? =
//            _users.value?.filter { !it.id.equals(userId) } as MutableList<User>?
//        newListOfUsers?.add(user!!)
//        _users.value = newListOfUsers

    }

    fun deletePhotosOfSelectedEvent(){

        _selectedEvent.value?.photosList = mutableListOf()
    }

    fun updateJob(event: Event, meId: String?) {
        val clickedEvent: Event? = _events.value?.filter { it.id.equals(event.id) }?.firstOrNull()
        //clickedEvent?.listOfUsers?.set(meId.toString(), true)

        val newListOfEvents: MutableList<Event>? =
            _events.value?.filter { !it.id.equals(event.id) } as MutableList<Event>?
        newListOfEvents?.add(clickedEvent!!)
        _events.value = newListOfEvents
    }


    init {
        setNewEvent()
    }

}