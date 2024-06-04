package com.jaidensiu.worldcountriesapp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.jaidensiu.worldcountriesapp.BuildConfig
import com.jaidensiu.worldcountriesapp.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OsmMapView(modifier: Modifier = Modifier, geoPoint: GeoPoint) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
            MapView(context).apply {
                setTileSource(TileSourceFactory.USGS_TOPO)
                setScrollableAreaLimitLatitude(
                    MapView.getTileSystem().maxLatitude,
                    MapView.getTileSystem().minLatitude,
                    0
                )
                setScrollableAreaLimitLongitude(
                    MapView.getTileSystem().minLongitude,
                    MapView.getTileSystem().maxLongitude,
                    0
                )
                setMultiTouchControls(true)
                setUseDataConnection(true)
                this.maxZoomLevel = 20.0
                this.minZoomLevel = 4.0
                this.isHorizontalMapRepetitionEnabled = false
                this.isVerticalMapRepetitionEnabled = false
                this.controller.setCenter(geoPoint)
                this.controller.setZoom(7.0)
                val marker = Marker(this)
                marker.setOnMarkerClickListener { _, _ -> true }
                marker.setPosition(geoPoint)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_marker)
                val resizedDrawable = resizeDrawable(context, drawable!!, 100, 100)
                marker.icon = resizedDrawable
                this.overlays.add(marker)
                this.invalidate()
            }
        }
    )
}

fun resizeDrawable(context: Context, drawable: Drawable, width: Int, height: Int): Drawable {
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    return BitmapDrawable(context.resources, resizedBitmap)
}