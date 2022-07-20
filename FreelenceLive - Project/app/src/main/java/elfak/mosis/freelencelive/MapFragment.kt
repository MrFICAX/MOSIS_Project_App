package elfak.mosis.freelencelive

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import elfak.mosis.freelencelive.databinding.FragmentMapBinding
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if(isGranted) {
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
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var ctx: Context? = activity?.applicationContext
        Configuration.getInstance().load(ctx,
            ctx?.let { PreferenceManager.getDefaultSharedPreferences(it) })
        map = requireView().findViewById<MapView>(R.id.map)

        map.setMultiTouchControls(true)

        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else{
            setMyLocationOverlay()
        }



        map.controller.setZoom(15.0)
        val startPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setCenter(startPoint)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun setOnMapClickOverlay() {
        var receive = object: MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint):Boolean {
//                var lon = p.longitude.toString()
//                var lat = p.latitude.toString()
//                locationViewModel.setLocation(lon, lat)
//                findNavController().popBackStack()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        var overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }

    private fun setMyLocationOverlay(){
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)

        //KOMPAS
//        var mCompassOverlay =
//            CompassOverlay(context, InternalCompassOrientationProvider(context), map)
//        mCompassOverlay.enableCompass()
//        map.getOverlays().add(mCompassOverlay)

        //LATITUDE I LONGITUDE LINIJE NA MAPI SA VREDNOSTIMA
//        val overlay = LatLonGridlineOverlay2()
//        map.getOverlays().add(overlay)

        //POKRETI ZA ROTIRANJE
        var mRotationGestureOverlay = RotationGestureOverlay(context, map)
        mRotationGestureOverlay.setEnabled(true)
        map.setMultiTouchControls(true)
        map.getOverlays().add(mRotationGestureOverlay)

        //SCALE BAR NA VRHU ZA VELICINU NA MAPI
        val context = getActivity();
        val dm = context?.getResources()?.getDisplayMetrics();
        var mScaleBarOverlay = ScaleBarOverlay(map)
        mScaleBarOverlay.setCentred(true);
//play around with these values to get the location on screen in the right place for your application
        if (dm != null) {
            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        };
        map.getOverlays().add(mScaleBarOverlay);

        //MiniMAPA u donjem desnom uglu
        var mMinimapOverlay = MinimapOverlay(context, map.getTileRequestCompleteHandler())
        mMinimapOverlay.setWidth(dm!!.widthPixels / 5)
        mMinimapOverlay.setHeight(dm!!.heightPixels / 5)
//optionally, you can set the minimap to a different tile source
//mMinimapOverlay.setTileSource(....);
    //optionally, you can set the minimap to a different tile source
        map.getOverlays().add(mMinimapOverlay)


        //DODAVANJE IKONICE NA MAPU SA KLIK LISTENER - OM
        val items = ArrayList<OverlayItem>()
        items.add(
            OverlayItem(
                "Title",
                "Description",
                GeoPoint(43.3209, 21.8958)
            )
        ) // Lat/Lon decimal degrees
        items.add(
            OverlayItem(
                "Filip",
                "Trajkovic",
                GeoPoint(43.0209, 21.8958)
            )
        ) // Lat/Lon decimal degrees


//the overlay

//the overlay
        val mOverlay = ItemizedOverlayWithFocus(items,
            object : OnItemGestureListener<OverlayItem?> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                    //do something
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                    return false
                }
            }, context
        )
        mOverlay.setFocusItemsOnTap(true)
        map.getOverlays().add(mOverlay)


        //CUSTOM MARKERI NA MAPI
        val startMarker = Marker(map)
        startMarker.position = startPoint
        startMarker.icon = getResources().getDrawable(R.drawable.ic_launcher_foreground)
        startMarker.setTitle("Start point");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

        map.invalidate();

        
        //RUTE NA MAPI
        val roadManager: roadManager = OSRMRoadManager(this, MY_USER_AGENT)

    }
}