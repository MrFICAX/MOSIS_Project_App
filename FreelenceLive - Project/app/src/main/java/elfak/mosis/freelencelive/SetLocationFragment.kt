package elfak.mosis.freelencelive

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databinding.FragmentMyJobsBinding
import elfak.mosis.freelencelive.databinding.FragmentSetLocationBinding
import elfak.mosis.freelencelive.dialogs.AskToJoinFragmentDialog
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import elfak.mosis.freelencelive.model.LocationViewModel
import elfak.mosis.freelencelive.model.userViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class SetLocationFragment : Fragment() {
    //
    private lateinit var binding: FragmentSetLocationBinding
    private lateinit var map: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    private var tmpGeoPoint: GeoPoint = GeoPoint(0, 0)
    private val userViewModel: userViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setBasicMapOverlays()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetLocationBinding.inflate(inflater)
        map = binding.map
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)

        prepareMapAndSetAtMyLocation()
        handleButtonSetLocationClick()

        val NewEventObserver = Observer<Event> { newValue ->
            writeOverlayAfterClick(GeoPoint(newValue.latitude, newValue.longitude))
        }
        userViewModel.newEvent.observe(viewLifecycleOwner, NewEventObserver)


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



        //ProbaDodavanjaNovihMarkera()
    }


    private fun handleButtonSetLocationClick() {
        binding.buttonSetLocation.setOnClickListener {

            if (tmpGeoPoint.latitude.equals(0) || tmpGeoPoint.latitude.equals(0)) {
                Toast.makeText(requireContext(), "Location not set", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.setNewEventLocation(tmpGeoPoint.latitude, tmpGeoPoint.longitude)
                findNavController().popBackStack()
                val fragmentNovi = addEventFragmentDialog()
                fragmentNovi.show(parentFragmentManager, "customString")
            }
        }
    }

    private fun prepareMapAndSetAtMyLocation() {

        map.controller.setZoom(13.0)
        val startPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setCenter(startPoint)
        //map.controller.animateTo(startPoint)

        setBasicMapOverlays()
    }

    private fun setBasicMapOverlays() {
        setMyLocationOverlay()
        setRotationGesturesOverlay()
        setScaleBar()
        setOnMapClickListener()
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

    //
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

    private fun setOnMapClickListener() {
        var receive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                var lon = p.longitude
                var lat = p.latitude

                userViewModel.setNewEventLocation(lat, lon)
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        var overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }

    private fun ProbaDodavanjaNovihMarkera() {

        val startPoint = GeoPoint(43.3209, 21.8958)

//        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
//        myLocationOverlay.enableMyLocation()
//        map.overlays.add(myLocationOverlay)

        //KOMPAS
//        var mCompassOverlay =
//            CompassOverlay(context, InternalCompassOrientationProvider(context), map)
//        mCompassOverlay.enableCompass()
//        map.getOverlays().add(mCompassOverlay)

        //LATITUDE I LONGITUDE LINIJE NA MAPI SA VREDNOSTIMA
//        val overlay = LatLonGridlineOverlay2()
//        map.getOverlays().add(overlay)

        //POKRETI ZA ROTIRANJE
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

        //MiniMAPA u donjem desnom uglu
//        var mMinimapOverlay = MinimapOverlay(context, map.getTileRequestCompleteHandler())
//        mMinimapOverlay.setWidth(dm!!.widthPixels / 5)
//        mMinimapOverlay.setHeight(dm!!.heightPixels / 5)
//        map.getOverlays().add(mMinimapOverlay)


        //DODAVANJE IKONICE NA MAPU SA KLIK LISTENER - OM
//        val items = ArrayList<OverlayItem>()
//
//        val item = OverlayItem(
//            "Title",
//            "Description",
//            "PVOO JE RSFD ASPROIV",
//
//            GeoPoint(43.3209, 21.8958)
//        )
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
//        val mOverlay = ItemizedOverlayWithFocus(
//            items,
//            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem?> {
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


        //CUSTOM MARKERI NA MAPI
//        val startMarker = Marker(map)
//        startMarker.position = startPoint
//        startMarker.icon = getResources().getDrawable(R.drawable.ic_launcher_foreground)
//        startMarker.setTitle("Start point");
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)


//        var receive = object : MapEventsReceiver {
//            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
//                var lon = p.longitude
//                var lat = p.latitude
//
//                Toast.makeText(
//                    requireContext(),
//                    "click na: " + lon.toString() + lat.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                userViewModel.setNewEventLocation(lat, lon)
////                val startMarker = Marker(map)
////
////                startMarker.position = GeoPoint(lon, lat)
////                startMarker.icon = getResources().getDrawable(R.drawable.point_on_map)
////                startMarker.setTitle("Clicked");
////                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
////                map.overlays.add(startMarker)
////
////                //map.invalidate();
////                startMarker.position = GeoPoint(lon, lat)
////                //startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
////                startMarker.icon = getResources().getDrawable(R.drawable.job_icon)
////                startMarker.setTitle("Start point");
////
////                startMarker.setOnMarkerClickListener { _, _ ->
////
////                    val fragmentNovi = AskToJoinFragmentDialog()
////                    fragmentNovi.show(parentFragmentManager, "customString")
////                    true
////                }
////                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
////                map.overlays.add(startMarker)
//
//                //locationViewModel.setLocation(lon.toString(), lat.toString())
//                //findNavController().popBackStack()
//                return true
//            }
//
//            override fun longPressHelper(p: GeoPoint?): Boolean {
//                return false
//            }
//        }
//        var overlayEvents = MapEventsOverlay(receive)
//        map.overlays.add(overlayEvents)


        val startMarker1 = Marker(map)
        val startMarker = Marker(map)
        startMarker.position = startPoint
        //startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        startMarker.icon = getResources().getDrawable(R.drawable.job_icon)
        startMarker.setTitle("Start point");

        startMarker.setOnMarkerClickListener { _, _ ->

            val fragmentNovi = AskToJoinFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
            true
        }
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

        map.invalidate();
//        map.tileProvider.getTileRequestCompleteHandlers().add(map.getTileRequestCompleteHandler());


    }

    private fun writeOverlayAfterClick(location: GeoPoint) {
        map.overlays.removeAll(map.overlays)
        setBasicMapOverlays()

        tmpGeoPoint = location
        val startMarker = Marker(map)
        startMarker.position = location
        startMarker.icon = getResources().getDrawable(R.drawable.point_on_map)
        startMarker.setTitle("Start point");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

        map.invalidate()

        //map.tileProvider.getTileRequestCompleteHandlers().add(map.getTileRequestCompleteHandler());
    }


    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroyView()
    }

}