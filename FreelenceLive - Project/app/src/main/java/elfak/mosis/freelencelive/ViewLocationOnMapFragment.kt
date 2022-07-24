package elfak.mosis.freelencelive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import elfak.mosis.freelencelive.databinding.FragmentSetLocationBinding
import elfak.mosis.freelencelive.databinding.FragmentViewLocationOnMapBinding
import elfak.mosis.freelencelive.dialogs.addEventFragmentDialog
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class ViewLocationOnMapFragment : Fragment() {

    private lateinit var binding: FragmentViewLocationOnMapBinding
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
        binding = FragmentViewLocationOnMapBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_view_location_on_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map = binding.map
        setupMap()
        setMyLocationOverlay()
        //setOnMapClickOverlay()


        binding.buttonBack.setOnClickListener{
            findNavController().popBackStack()
//            val fragmentNovi = addEventFragmentDialog()
//            fragmentNovi.show(parentFragmentManager, "customString")
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

    private fun setMyLocationOverlay(){
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }

}