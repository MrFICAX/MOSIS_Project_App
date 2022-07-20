package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentMyJobsBinding
import elfak.mosis.freelencelive.databinding.FragmentSetLocationBinding
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import elfak.mosis.freelencelive.model.LocationViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class SetLocationFragment : Fragment() {
//
    private lateinit var binding: FragmentSetLocationBinding
    private lateinit var map: MapView
//    private val locationViewModel: LocationViewModel by activityViewModels()
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) {
//                isGranted: Boolean ->
//            if(isGranted) {
//                setMyLocationOverlay()
//                setOnMapClickOverlay()
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetLocationBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_set_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map = binding.map
        setupMap()
        setMyLocationOverlay()
        setOnMapClickOverlay()


        binding.buttonSetLocation.setOnClickListener{
            findNavController().popBackStack()
            val fragmentNovi = addEventFragmentDialog()
            fragmentNovi.show(parentFragmentManager, "customString")
        }
    }
    private fun setupMap() {
        var startPoint: GeoPoint = GeoPoint(43.3289, 21.8958)
        map.controller.setZoom(15.0)
//        if(locationViewModel.setLocation){
//            setOnMapClickOverlay()
//        }
//        else{
////            if(myPlacesViewModel.selected != null){
////                startPoint = GeoPoint(myPlacesViewModel.selected!!.latitude.toDouble(), myPlacesViewModel.selected!!.longitude.toDouble())
////            } else{
//                setMyLocationOverlay()
//            //}
//        }
        map.controller.animateTo(startPoint)
    }
//
    private fun setOnMapClickOverlay() {
        var receive = object: MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint):Boolean {
                var lon = p.longitude
                var lat = p.latitude

                val startMarker = Marker(map)
                startMarker.position = GeoPoint(lon, lat)
                startMarker.icon = getResources().getDrawable(R.drawable.ic_launcher_foreground)
                startMarker.setTitle("Clicked");
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                map.overlays.add(startMarker)

                map.invalidate();

                //locationViewModel.setLocation(lon.toString(), lat.toString())
                //findNavController().popBackStack()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        var overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }
//
    private fun setMyLocationOverlay(){
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }
//
//
//
//    override fun onResume() {
//        super.onResume()
//        map.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        map.onPause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroyView()
//    }

}