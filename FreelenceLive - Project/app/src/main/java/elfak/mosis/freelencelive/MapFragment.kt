package elfak.mosis.freelencelive

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.data.UserLocation
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentMapBinding
import elfak.mosis.freelencelive.dialogs.AskToJoinFragmentDialog
import elfak.mosis.freelencelive.dialogs.InviteFriendFragmentDialog
import elfak.mosis.freelencelive.model.userViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment(), LocationListener {

    lateinit var map: MapView
    private val startPoint = GeoPoint(43.3209, 21.8958)
    private lateinit var binding: FragmentMapBinding
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private val userViewModel: userViewModel by activityViewModels()


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setMyLocationOverlay()
                //setOnMapClickOverlay()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUserLocationValueEventListener()
        //setEventValueListener()


        if (userViewModel.userLocations.value?.isEmpty() == true)
            FirebaseHelper.getUserLocationsData(userViewModel)

        userViewModel.restartSearch()


        if (userViewModel.users.value?.isEmpty() == true) {
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)
        }


        if (userViewModel.askToJoin.value?.isEmpty() == true)
            FirebaseHelper.getAllAskToJoins(requireContext(), userViewModel)

        val UserLocationsObserver = Observer<List<UserLocation>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<UserLocation> = newValue
            userViewModel.events.value?.let {
                writeUsersAndEventsOverlays(
                    it,
                    newValue,
                    null,
                    false
                )
            } // needs to be implemented

        }
        userViewModel.userLocations.observe(viewLifecycleOwner, UserLocationsObserver)

        val OnlineUsersObserver = Observer<HashMap<String, Boolean>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            //val lista: List<UserLocation> = newValue
            userViewModel.events.value?.let { events ->
                userViewModel.userLocations.value?.let { userLocations ->
                    writeUsersAndEventsOverlays(
                        events,
                        userLocations,
                        null,
                        false
                    )
                }
            } // needs to be implemented

        }
        userViewModel.onlineUsers.observe(viewLifecycleOwner, OnlineUsersObserver)


        if (userViewModel.events.value?.isEmpty() == true)
            FirebaseHelper.getAllEvents(requireContext(), userViewModel)

        val EventsObserver = Observer<List<Event>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<Event> = newValue
            //addFriendsToLinearLayout(lista, false, "")
            //writeAllEventsOverlays(lista)
            userViewModel.userLocations.value?.let {
                writeUsersAndEventsOverlays(
                    newValue,
                    it, null, false
                )
            }
        }
        userViewModel.events.observe(viewLifecycleOwner, EventsObserver)

        val searchFieldsSetObserver = Observer<List<Boolean>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            if (userViewModel.events.value?.isNotEmpty() == true) {
                //writeFilteredEventsOverlays(userViewModel.events.value!!, newValue)

                userViewModel.userLocations.value?.let {
                    writeUsersAndEventsOverlays(
                        userViewModel.events.value!!,
                        it, newValue, true
                    )
                }

            }
        }
        userViewModel.searchFieldsSet.observe(viewLifecycleOwner, searchFieldsSetObserver)


        binding = FragmentMapBinding.inflate(layoutInflater)
        map = binding.map
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)

        return binding.root
    }

    private fun setUserValueListener() {
        FirebaseHelper.setUserValueChangedListener(userViewModel)
    }

    private fun setEventValueListener() {
        FirebaseHelper.setEventValueChangedListener(userViewModel)

    }

    private fun setUserLocationValueEventListener() {
        FirebaseHelper.setUserLocationEventListener(userViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locManager: LocationManager =
            requireActivity().getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        locManager.requestLocationUpdates(
            LocationManager.FUSED_PROVIDER,
            5555,
            100f,
            this
        )

        inflater = LayoutInflater.from(requireContext())
        var ctx: Context? = activity?.applicationContext
        Configuration.getInstance().load(ctx,
            ctx?.let { PreferenceManager.getDefaultSharedPreferences(it) })
        map.setMultiTouchControls(true)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }


        prepareMapAndSetAtMyLocation()
    }

    private fun prepareMapAndSetAtMyLocation() {

        map.controller.setZoom(13.0)
        val startPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setCenter(startPoint)

        //myLocationOverlay.enableFollowLocation()
        myLocationOverlay.enableFollowLocation()
        map.controller.setCenter(myLocationOverlay.myLocation)

        setBasicMapOverlays()

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun setBasicMapOverlays() {
        setMyLocationOverlay()
        setRotationGesturesOverlay()
        setScaleBar()
    }

    private fun setMyLocationOverlay() {
//        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
//        myLocationOverlay.enableMyLocation()
//        map.overlays.add(myLocationOverlay)

        myLocationOverlay.enableMyLocation()
        myLocationOverlay.setDirectionArrow(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.my_location
            ), BitmapFactory.decodeResource(resources, R.drawable.my_location)
        );
        myLocationOverlay.setPersonIcon(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.my_location
            )
        )
        map.overlays.add(myLocationOverlay)
    }

    private fun setRotationGesturesOverlay() {
        var mRotationGestureOverlay = RotationGestureOverlay(context, map)
        mRotationGestureOverlay.setEnabled(true)
        map.setMultiTouchControls(true)
        map.getOverlays().add(mRotationGestureOverlay)
    }

    private fun setScaleBar() {
        val context = getActivity();
        val dm = context?.getResources()?.getDisplayMetrics();
        var mScaleBarOverlay = ScaleBarOverlay(map)
        mScaleBarOverlay.setCentred(true);
//play around with these values to get the location on screen in the right place for your application
        if (dm != null) {
            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        };
        map.getOverlays().add(mScaleBarOverlay);
    }

    private fun writeUsersAndEventsOverlays(
        listOfEvents: List<Event>,
        listOfUserLocations: List<UserLocation>,
        listaFiltera: List<Boolean>?,
        filtersFlag: Boolean
    ) {

        map.overlays.removeAll(map.overlays)
        setBasicMapOverlays()

        if (listOfEvents.isNotEmpty()) {
            if (filtersFlag) {
                if (listaFiltera != null) {
                    writeFilteredEventsOverlays(listOfEvents, listaFiltera)
                }
            } else
                writeAllEventsOverlays(listOfEvents)
        }
        if (listOfUserLocations.isNotEmpty())
            writeAllUserLocationsOverlays(listOfUserLocations)
    }

    private fun writeAllUserLocationsOverlays(listOfUserLocations: List<UserLocation>) {
        listOfUserLocations.forEach { element ->
            writeUserOverlay(element)
        }
    }

    private fun writeAllEventsOverlays(lista: List<Event>) {

        lista.forEach { element ->
            writeEventOverlay(element)
        }
    }

    private fun writeFilteredEventsOverlays(lista: List<Event>, listOfBooleans: List<Boolean>) {


        var listOfEvents: List<Event> = lista
        if (listOfBooleans.get(0))
            listOfEvents = filterByString(
                listOfEvents,
                userViewModel.searchBarEventName.value!!
            )
        if (listOfBooleans.get(1)) {
            listOfEvents = filterByRadius(listOfEvents, userViewModel.searchByRadius.value!!)
            //writeCircleAroudMarker()
        }

        listOfEvents.forEach { element ->
            writeEventOverlay(element)
        }
    }

    private fun filterByRadius(listaEventa: List<Event>, inputRadius: Double): List<Event> {
        val myLocation: GeoPoint = myLocationOverlay.myLocation

        var tmpLista: MutableList<Event> = mutableListOf()
        var distance: Double
        listaEventa.forEach {
            distance = myLocation.distanceToAsDouble(GeoPoint(it.latitude, it.longitude));
            if (distance < inputRadius)
                tmpLista.add(it)
        }
        return tmpLista
    }

    private fun filterByString(listaEventa: List<Event>, filterString: String): List<Event> {

        return listaEventa.filter { it.name.contains(filterString) }
    }


    private fun writeCircleAroudMarker() {
        val oPolygon = Polygon(map);
        val radius: Double = 161.0;
        var circlePoints: MutableList<GeoPoint> = mutableListOf<GeoPoint>();

        val myLocation: GeoPoint = myLocationOverlay.myLocation
        for (i in 0..360) {
            circlePoints.add(
                GeoPoint(myLocation.latitude, myLocation.longitude).destinationPoint(
                    radius,
                    i.toDouble()
                )
            );

        }

        oPolygon.setPoints(circlePoints);
        map.overlays.add(oPolygon)
        map.invalidate()

    }

    private fun writeEventOverlay(event: Event) {

        val startMarker = Marker(map)
        startMarker.position = GeoPoint(event.latitude, event.longitude)
        startMarker.icon = getResources().getDrawable(R.drawable.job_icon)
        //startMarker.setTitle("Start point");
        startMarker.setOnMarkerClickListener { _, _ ->

            userViewModel.setSelectedEvent(event)
            val fragmentNovi = AskToJoinFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
            true
        }
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(startMarker)

        map.invalidate()

    }

    private fun writeUserOverlay(element: UserLocation) {
        val startMarker = Marker(map)
        startMarker.position = GeoPoint(element.latitude, element.longitude)

        if (userViewModel.onlineUsers.value?.containsKey(element.userId) == true) {
            if (userViewModel.onlineUsers.value?.get(element.userId )!!){
                startMarker.icon = getResources().getDrawable(R.drawable.online_online)

            } else{
                startMarker.icon = getResources().getDrawable(R.drawable.online_offline)

            }
        } else {
            startMarker.icon = getResources().getDrawable(R.drawable.online_unknown)

        }


        //startMarker.setTitle("Start point");
        startMarker.setOnMarkerClickListener { _, _ ->

            var selectedUser: User? =
                userViewModel.users.value?.filter { it.id.equals(element.userId) }?.firstOrNull()

            if (selectedUser != null) {
                userViewModel.setSelectedUser(selectedUser)
            }

            val fragmentNovi = InviteFriendFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
            true
        }
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(startMarker)

        map.invalidate()
    }

    override fun onLocationChanged(location: Location) {
        val userMap = mapOf(
            "lat" to location.latitude,
            "lon" to location.longitude
        )

        FirebaseHelper.postMyLocation(
            GeoPoint(location.latitude, location.longitude),
            userViewModel
        )

//        //ogranicavanje skrolovanja mape
//        val constraintOffset = 0.1
//        val scrollConstraints = BoundingBox(
//            location.latitude+constraintOffset,
//            location.longitude+constraintOffset,
//            location.latitude-constraintOffset,
//            location.longitude-constraintOffset
//        )
    }

//    fun setMarkerIconAsPhoto(marker: Marker, thumbnail: Bitmap) {
//        var thumbnail = thumbnail
//        val borderSize = 2
//        thumbnail = Bitmap.createScaledBitmap(thumbnail, 48, 48, true)
//        val withBorder: ImageBitmap = Bitmap.createBitmap(
//            thumbnail.width + borderSize * 2,
//            thumbnail.height + borderSize * 2,
//            thumbnail.config
//        ) as ImageBitmap
//        val canvas = Canvas(withBorder as ImageBitmap)
//
//        canvas.drawColor(Color.WHITE)
//        canvas.drawBitmap(thumbnail, borderSize, borderSize, null)
//        val icon = BitmapDrawable(resources, withBorder as Bitmap)
//        marker.icon = icon
//    }


//    private fun viewToBitmap(cardView: CardView): Bitmap? {
//        cardView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
//        val bitmap = Bitmap.createBitmap(cardView.measuredWidth, cardView.measuredHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        cardView.layout(0, 0, cardView.measuredWidth, cardView.measuredHeight)
//        cardView.draw(canvas)
//        return bitmap
//    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}