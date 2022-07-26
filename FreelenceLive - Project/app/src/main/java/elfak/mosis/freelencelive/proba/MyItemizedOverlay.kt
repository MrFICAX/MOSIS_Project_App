package elfak.mosis.freelencelive.proba

import android.graphics.Point
import android.graphics.drawable.Drawable
import org.osmdroid.api.IMapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.OverlayItem

class MyItemizedOverlay(var defaultMarker:Drawable) : ItemizedOverlay<OverlayItem>(defaultMarker) {
    override fun onSnapToItem(x: Int, y: Int, snapPoint: Point?, mapView: IMapView?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createItem(i: Int): OverlayItem? {
        when (i) {

            1 -> {

                val lat: Double = 37.422006 * 1E6;
                val lng: Double = -122.084095 * 1E6;
                val point: GeoPoint = GeoPoint(lat.toInt(), lng.toInt());
                val oi: OverlayItem;
                oi = OverlayItem("Marker", "Marker Text", point);
                return oi;
            }
            else -> return null
        }
    }

    override fun size(): Int {
        return 1
    }

}