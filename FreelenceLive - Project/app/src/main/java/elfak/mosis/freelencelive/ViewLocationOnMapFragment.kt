package elfak.mosis.freelencelive

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.databinding.FragmentSetLocationBinding
import elfak.mosis.freelencelive.databinding.FragmentViewLocationOnMapBinding
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import elfak.mosis.freelencelive.model.userViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class ViewLocationOnMapFragment : Fragment() {

    private lateinit var binding: FragmentViewLocationOnMapBinding
    private lateinit var map: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    lateinit var inflater: LayoutInflater // LayoutInflater.from(requireContext())

    private var tmpGeoPoint: GeoPoint = GeoPoint(43.3209, 21.8958)
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
        binding = FragmentViewLocationOnMapBinding.inflate(inflater)

        val NewEventObserver = Observer<Event> { newValue ->
//            writeOverlayAfterClick(GeoPoint(newValue.latitude, newValue.longitude))

            setSelectedEventOverlay(GeoPoint(newValue.latitude, newValue.longitude))
        }
        userViewModel.selectedEvent.observe(viewLifecycleOwner, NewEventObserver)


        map = binding.map
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)

        prepareMapAndSetAtMyLocation()

        return binding.root
        //return inflater.inflate(R.layout.fragment_view_location_on_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        map = binding.map
//        setupMap()
//        setMyLocationOverlay()
        //setOnMapClickOverlay()


        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
//            val fragmentNovi = addEventFragmentDialog()
//            fragmentNovi.show(parentFragmentManager, "customString")
        }
    }

    private fun setBasicMapOverlays() {
        setMyLocationOverlay()
        setRotationGesturesOverlay()
        setScaleBar()
        userViewModel.selectedEvent.value?.latitude?.let {
            GeoPoint(
                userViewModel.selectedEvent.value?.latitude!!,
                it
            )
        }?.let { setSelectedEventOverlay(it) }
        //setOnMapClickListener()
    }

    private fun setSelectedEventOverlay(location: GeoPoint) {

        tmpGeoPoint = location
        val startMarker = Marker(map)
        startMarker.position = location
        startMarker.icon = getResources().getDrawable(R.drawable.point_on_map)
        startMarker.setTitle("Start point");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

        map.controller.setCenter(location)
        map.invalidate()    }

    private fun prepareMapAndSetAtMyLocation() {

        map.controller.setZoom(13.0)
        val startPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setCenter(startPoint)
        //map.controller.animateTo(startPoint)

        setBasicMapOverlays()
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

    private fun setMyLocationOverlay() {
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.setDirectionArrow( BitmapFactory.decodeResource(resources, R.drawable.my_location), BitmapFactory.decodeResource(resources, R.drawable.my_location) );
        myLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(resources, R.drawable.my_location))

        //map.controller.setCenter(myLocationOverlay.myLocation)
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

}