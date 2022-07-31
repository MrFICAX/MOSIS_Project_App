package elfak.mosis.freelencelive

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import elfak.mosis.freelencelive.databinding.FragmentMapBinding
import elfak.mosis.freelencelive.dialogs.AskToJoinFragmentDialog
import elfak.mosis.freelencelive.dialogs.InviteFriendFragmentDialog
import elfak.mosis.freelencelive.model.userViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment() {

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

        if (userViewModel.users.value?.isEmpty() == true)
            FirebaseHelper.getOtherUsers(requireContext(), userViewModel)

        val FriendsObserver = Observer<List<User>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<User> = newValue
            //drawAllUsersMarkers(lista) // needs to be implemented

        }
        userViewModel.users.observe(viewLifecycleOwner, FriendsObserver)

        if (userViewModel.events.value?.isEmpty() == true)
            FirebaseHelper.getAllEvents(requireContext(), userViewModel)

        val EventsObserver = Observer<List<Event>> { newValue ->
            //binding.buttonCreateJob.setText(newValue)
            val lista: List<Event> = newValue
            //addFriendsToLinearLayout(lista, false, "")
            writeAllEventsOverlays(lista)
        }
        userViewModel.events.observe(viewLifecycleOwner, EventsObserver)


        binding = FragmentMapBinding.inflate(layoutInflater)
        map = binding.map
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

//        myLocationOverlay.enableMyLocation()
//        myLocationOverlay.enableFollowLocation()
//        map.controller.setCenter(myLocationOverlay.myLocation)
//        map.overlays.add(myLocationOverlay)

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

//    private fun setOnMapClickOverlay() {
//        var receive = object: MapEventsReceiver {
//            override fun singleTapConfirmedHelper(p: GeoPoint):Boolean {
////                var lon = p.longitude.toString()
////                var lat = p.latitude.toString()
////                locationViewModel.setLocation(lon, lat)
////                findNavController().popBackStack()
//                return true
//            }
//
//            override fun longPressHelper(p: GeoPoint?): Boolean {
//                return false
//            }
//        }
//        var overlayEvents = MapEventsOverlay(receive)
//        map.overlays.add(overlayEvents)
//    }

//    private fun setMyLocationOverlay(){
////        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
////        myLocationOverlay.enableMyLocation()
////        map.overlays.add(myLocationOverlay)
//
//        //KOMPAS
////        var mCompassOverlay =
////            CompassOverlay(context, InternalCompassOrientationProvider(context), map)
////        mCompassOverlay.enableCompass()
////        map.getOverlays().add(mCompassOverlay)
//
//        //LATITUDE I LONGITUDE LINIJE NA MAPI SA VREDNOSTIMA
////        val overlay = LatLonGridlineOverlay2()
////        map.getOverlays().add(overlay)
//
//        //POKRETI ZA ROTIRANJE
//        var mRotationGestureOverlay = RotationGestureOverlay(context, map)
//        mRotationGestureOverlay.setEnabled(true)
//        map.setMultiTouchControls(true)
//        map.getOverlays().add(mRotationGestureOverlay)
//
//        //SCALE BAR NA VRHU ZA VELICINU NA MAPI
//        val context = getActivity();
//        val dm = context?.getResources()?.getDisplayMetrics();
//        var mScaleBarOverlay = ScaleBarOverlay(map)
//        mScaleBarOverlay.setCentred(true);
////play around with these values to get the location on screen in the right place for your application
//        if (dm != null) {
//            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
//        };
//        map.getOverlays().add(mScaleBarOverlay);
//
//        //MiniMAPA u donjem desnom uglu
////        var mMinimapOverlay = MinimapOverlay(context, map.getTileRequestCompleteHandler())
////        mMinimapOverlay.setWidth(dm!!.widthPixels / 5)
////        mMinimapOverlay.setHeight(dm!!.heightPixels / 5)
////        map.getOverlays().add(mMinimapOverlay)
//
//
//        //DODAVANJE IKONICE NA MAPU SA KLIK LISTENER - OM
//        val items = ArrayList<OverlayItem>()
//
//           val item = OverlayItem(
//                "Title",
//                "Description",
//                "PVOO JE RSFD ASPROIV",
//
//                GeoPoint(43.3209, 21.8958)
//            )
//        item.setMarker(this.getResources().getDrawable(R.drawable.world_map))
//        items.add(item) // Lat/Lon decimal degrees
//        items.add(
//            OverlayItem(
//                "Filip",
//                "Trajkovic",
//                "PVOO JE RSFD ASPROIV",
//                GeoPoint(43.2209, 21.8958)
//            )
//        ) // Lat/Lon decimal degrees
//
//        //the overlay
//        val mOverlay = ItemizedOverlayWithFocus(items,
//            object : OnItemGestureListener<OverlayItem?> {
//                override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
//                    //do something
//                    return true
//                }
//
//                override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
//                    return false
//                }
//            }, context
//        )
//        mOverlay.setFocusItemsOnTap(true)
//        map.getOverlays().add(mOverlay)
//
//
//        //CUSTOM MARKERI NA MAPI
//        val startMarker = Marker(map)
//        startMarker.position = startPoint
//        startMarker.icon = getResources().getDrawable(R.drawable.ic_launcher_foreground)
//        startMarker.setTitle("Start point");
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        //map.overlays.add(startMarker)
//
//
////        val markerView = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.marker_user_on_map, null)
////        val imageView = markerView.findViewById<ImageView>(R.id.imageView)
////        imageView.setImageResource(R.drawable.img_0944)
////
////        val cardView = markerView.findViewById<CardView>(R.id.cardView)
////
////        val bitmap = Bitmap.createScaledBitmap(viewToBitmap(cardView)!!, cardView.width, cardView.height, false)
////        val smallMarker = BitmapDescriptorFactory.fromBitmap(bitmap)
//
//
////        val probniMarker = Marker(map)
////        startMarker.position = startPoint
////        startMarker.setTitle("Start point");
////        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
////        map.overlays.add(startMarker)
//
//        val markerView = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.marker_user_on_map, null)
//        val imageView = markerView.findViewById<ImageView>(R.id.imageView)
//        imageView.setImageResource(R.drawable.img_0944)
//        val cardView = markerView.findViewById<CardView>(R.id.cardView)
//
//        val startMarker1 = Marker(map)
//        startMarker.position = startPoint
//        //startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
//        startMarker.icon = getResources().getDrawable(R.drawable.job_icon)
//        startMarker.setTitle("Start point");
//
//        startMarker.setOnMarkerClickListener { _, _ ->
//
//            val fragmentNovi = AskToJoinFragmentDialog()
//            fragmentNovi.show(parentFragmentManager, "customString")
//            true
//        }
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        map.overlays.add(startMarker)
//
//        map.invalidate();
//
//
//    }

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
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.setDirectionArrow( BitmapFactory.decodeResource(resources, R.drawable.my_location), BitmapFactory.decodeResource(resources, R.drawable.my_location) );
        myLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(resources, R.drawable.my_location))
        map.controller.setCenter(myLocationOverlay.myLocation)
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


    private fun writeAllEventsOverlays(lista: List<Event>) {

        map.overlays.removeAll(map.overlays)
        setBasicMapOverlays()

        lista.forEach { element ->
            writeEventOverlay(element)
        }
    }

    private fun writeEventOverlay(event: Event) {

        val startMarker = Marker(map)
        startMarker.position = GeoPoint(event.latitude, event.longitude)
        startMarker.icon = getResources().getDrawable(R.drawable.job_icon)
        startMarker.setTitle("Start point");
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
}