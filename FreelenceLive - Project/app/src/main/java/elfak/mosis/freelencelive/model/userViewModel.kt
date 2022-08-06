package elfak.mosis.freelencelive.model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.freelencelive.data.*
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class userViewModel : ViewModel() {

    private val _newEvent = MutableLiveData<Event>()
    val newEvent: LiveData<Event>
        get() = _newEvent

    private val _backGroundServiceActivated = MutableLiveData<Boolean>( false )
    val backGroundServiceActivated: LiveData<Boolean>
        get() = _backGroundServiceActivated

    private val _onlineActivated = MutableLiveData<Boolean>( false )
    val onlineActivated: LiveData<Boolean>
        get() = _onlineActivated


    private var _dateChanged: Boolean = false
    val dateChanged: Boolean
        get() = _dateChanged

    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    private val _events: MutableLiveData<List<Event>> = MutableLiveData(listOf())
    val events: LiveData<List<Event>>
        get() = _events

    private val _onlineUsers: MutableLiveData<HashMap<String, Boolean>> = MutableLiveData(hashMapOf())
    val onlineUsers: LiveData<HashMap<String, Boolean>>
        get() = _onlineUsers

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _users: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: LiveData<List<User>>
        get() = _users

    private val _userLocations: MutableLiveData<List<UserLocation>> = MutableLiveData(listOf())
    val userLocations: LiveData<List<UserLocation>>
        get() = _userLocations


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

    private val _searchBarEventName = MutableLiveData<String>("")
    val searchBarEventName: LiveData<String>
        get() = _searchBarEventName

    private val _searchByRadius = MutableLiveData<Double>(0.0)
    val searchByRadius: LiveData<Double>
        get() = _searchByRadius

    private val _searchFieldsSet: MutableLiveData<MutableList<Boolean>> =
        MutableLiveData(mutableListOf(false, false))
    val searchFieldsSet: LiveData<MutableList<Boolean>>
        get() = _searchFieldsSet


    fun setBackGroundService(flag: Boolean){
        _backGroundServiceActivated.value = flag
    }

    fun setOnlineValueForUser(flag: Boolean){
        _onlineActivated.value = flag
        _events.value = _events.value
    }

    fun setSearchBarEventName(input: String) {
        _searchBarEventName.value = input

        if (input.trim().isNotEmpty()) {

            var booleanList: MutableList<Boolean>? = searchFieldsSet.value
            booleanList?.set(0, true)
            _searchFieldsSet.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = searchFieldsSet.value
            booleanList?.set(0, false)
            _searchFieldsSet.value = booleanList
        }
    }

    fun setRadiusValue(value: Double) {
        _searchByRadius.value = value
        if (value.equals(0.0)) {
            var booleanList: MutableList<Boolean>? = searchFieldsSet.value
            booleanList?.set(1, false)
            _searchFieldsSet.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = searchFieldsSet.value
            booleanList?.set(1, true)
            _searchFieldsSet.value = booleanList
        }
    }

    fun restartSearch() {
        var booleanList: MutableList<Boolean>? = searchFieldsSet.value
        booleanList?.set(0, false)
        booleanList?.set(1, false)
        _searchFieldsSet.value = booleanList
    }

    private val _searchBarEventNameAdvancedSearch = MutableLiveData<String>("")
    val searchBarEventNameAdvancedSearch: LiveData<String>
        get() = _searchBarEventNameAdvancedSearch

    private val _searchBarEventOrganiserNameAdvancedSearch = MutableLiveData<String>("")
    val searchBarEventOrganiserNameAdvancedSearch: LiveData<String>
        get() = _searchBarEventOrganiserNameAdvancedSearch

    private val _searchBarEventFinishedAdvancedSearch = MutableLiveData<Boolean>(false)
    val searchBarEventFinishedAdvancedSearch: LiveData<Boolean>
        get() = _searchBarEventFinishedAdvancedSearch

    private val _searchBarEventMyJobAdvancedSearch = MutableLiveData<Boolean>(false)
    val searchBarEventMyJobAdvancedSearch: LiveData<Boolean>
        get() = _searchBarEventMyJobAdvancedSearch

    private val _searchFieldsSetAdvancedSearch: MutableLiveData<MutableList<Boolean>> = MutableLiveData(mutableListOf(false, false, false, false))
    val searchFieldsSetAdvancedSearch: LiveData<MutableList<Boolean>>
        get() = _searchFieldsSetAdvancedSearch

    fun setSearchBarEventNameAdvancedSearch(input: String) {
        _searchBarEventNameAdvancedSearch.value = input

        if (input.trim().isNotEmpty()) {

            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(0, true)
            _searchFieldsSetAdvancedSearch.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(0, false)
            _searchFieldsSetAdvancedSearch.value = booleanList
        }
    }

    fun setSearchBarEventOrganiserNameAdvancedSearch(input: String) {
        _searchBarEventOrganiserNameAdvancedSearch.value = input

        if (input.trim().isNotEmpty()) {

            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(1, true)
            _searchFieldsSetAdvancedSearch.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(1, false)
            _searchFieldsSetAdvancedSearch.value = booleanList
        }
    }

    fun setSearchBarEventFinishedAdvancedSearch(input: Boolean) {
        _searchBarEventFinishedAdvancedSearch.value = input

        if (input) {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(2, true)
            _searchFieldsSetAdvancedSearch.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(2, false)
            _searchFieldsSetAdvancedSearch.value = booleanList
        }
    }

    fun setSearchBarEventMyJobAdvancedSearch(input: Boolean) {
        _searchBarEventMyJobAdvancedSearch.value = input

        if (input) {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(3, true)
            _searchFieldsSetAdvancedSearch.value = booleanList
        } else {
            var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
            booleanList?.set(3, false)
            _searchFieldsSetAdvancedSearch.value = booleanList
        }
    }

    fun restartAdvancedSearch() {
        var booleanList: MutableList<Boolean>? = _searchFieldsSetAdvancedSearch.value
        booleanList?.set(0, false)
        setSearchBarEventNameAdvancedSearch("")
        booleanList?.set(1, false)
        setSearchBarEventOrganiserNameAdvancedSearch("")
        booleanList?.set(2, false)
        setSearchBarEventFinishedAdvancedSearch(false)
        booleanList?.set(3, false)
        setSearchBarEventMyJobAdvancedSearch(false)
        _searchFieldsSetAdvancedSearch.value = booleanList
    }

    fun addNewInvitation(newInvitation: Invitation) {
        _invitations.value = _invitations.value?.plus(newInvitation)
    }

//    fun addNewComment(newComment: Comment){
//        _comments.value = _comments.value?.plus(newComment)
//    }

    fun addUserLocationsList(lista: List<UserLocation>) {
        _userLocations.value = lista
    }

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

    fun setNewUser(user: User?) {
        _user.value = user
    }

    fun setOnlineUsersMap(mapa: HashMap<String, Boolean>) {
        _onlineUsers.value = mapa
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

    fun InitialSetSelectedEvent() {
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

    fun deletePhotoFromSelectedEvent(photoString: String) {
        val photoList = _selectedEvent.value?.photosList?.filter { !it.equals(photoString) }
        _selectedEvent.value?.photosList = photoList as MutableList<String>

    }

    fun setPhotoUrlToSelectedEvent(imgUrl: String) {

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

    fun deletePhotosOfSelectedEvent() {

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

    fun addNewUserToJob(event: Event, userId: String?) {
        val clickedEvent: Event? = _events.value?.filter { it.id.equals(event.id) }?.firstOrNull()
        //clickedEvent?.listOfUsers?.put(userId.toString(), true)

        val newListOfEvents: MutableList<Event>? =
            _events.value?.filter { !it.id.equals(event.id) } as MutableList<Event>?
        newListOfEvents?.add(clickedEvent!!)
        _events.value = newListOfEvents
    }

    fun addNewInvitedUserToJob(event: Event, issuedBy: String?) {
        val clickedEvent: Event? = _events.value?.filter { it.id.equals(event.id) }?.firstOrNull()

//        clickedEvent.listOfUsers.put(issuedBy!!, false)
        val newListOfEvents: MutableList<Event>? = _events.value?.filter { !it.id.equals(event.id) } as MutableList<Event>?
        newListOfEvents?.add(clickedEvent!!)
        _events.value = newListOfEvents
    }

    fun updateEvent(id: String, eventTmp: Event) {
        var clickedEvent: Event?

        clickedEvent = eventTmp
        val newListOfEvents: MutableList<Event>? =
            _events.value?.filter { !it.id.equals(id) } as MutableList<Event>?
        newListOfEvents?.add(clickedEvent!!)
        _events.value = newListOfEvents

        if (selectedEvent.value?.id.equals(id))
            _selectedEvent.value = eventTmp
    }

    fun updateUser(id: String, userTmp: User) {
        try {
            var updatedUser: User?

            updatedUser = userTmp
            val newListOfEvents: MutableList<User>? =
                _users.value?.filter { !it.id.equals(id) } as MutableList<User>?
            newListOfEvents?.add(updatedUser!!)
            _users.value = newListOfEvents
        }catch (e: Exception){
            Log.d("greska", e.toString())
        }
    }

    fun updateUserData(user: User, map: HashMap<String, String>){

        if (map.containsKey("userName"))
            user.userName = map.get("userName").toString()
        if (map.containsKey("firstName"))
            user.firstName = map.get("firstName").toString()
        if (map.containsKey("lastName"))
            user.lastName = map.get("lastName").toString()
        if (map.containsKey("phoneNumber"))
            user.phoneNumber = map.get("phoneNumber").toString()

    }


    init {
        setNewEvent()
        _ratings.value = mutableListOf()
    }

}