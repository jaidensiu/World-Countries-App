package com.jaidensiu.worldcountriesapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    geoPoint: GeoPoint
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            Configuration.getInstance().userAgentValue = "com.jaidensiu.worldcountriesapp"
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
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
                tileProvider.clearTileCache()
                this.maxZoomLevel = 20.0
                this.minZoomLevel = 4.0
                this.isHorizontalMapRepetitionEnabled = false
                this.isVerticalMapRepetitionEnabled = false
                this.controller.setCenter(geoPoint)
                this.controller.setZoom(7.0)
            }
        }
    )
}