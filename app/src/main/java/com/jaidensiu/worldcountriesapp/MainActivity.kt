package com.jaidensiu.worldcountriesapp

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jaidensiu.worldcountriesapp.presentation.CountriesScreen
import com.jaidensiu.worldcountriesapp.presentation.CountriesViewModel
import com.jaidensiu.worldcountriesapp.presentation.MapScreen
import com.jaidensiu.worldcountriesapp.ui.theme.WorldCountriesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorldCountriesAppTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                val color = MaterialTheme.colors.primary
                val navController = rememberNavController()
                val viewModel = hiltViewModel<CountriesViewModel>()
                val countriesState by viewModel.countriesState.collectAsState()
                val searchBarState by viewModel.countrySearchBarState.collectAsState()
                var latitude = 0.0
                var longitude = 0.0

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = color,
                        darkIcons = useDarkIcons
                    )
                }

                if (countriesState.selectedCountry != null) {
                    val location = countriesState.selectedCountry?.name ?: ""
                    val geocoder = Geocoder(this)
                    val addresses = geocoder.getFromLocationName(location, 1)
                    val address = addresses?.firstOrNull()
                    if (address != null) {
                        latitude = address.latitude
                        longitude = address.longitude
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "countriesScreen"
                ) {
                    composable(route = "countriesScreen") {
                        CountriesScreen(
                            countriesState = countriesState,
                            countrySearchBarState = searchBarState,
                            onSelectCountry = viewModel::selectCountry,
                            onDismissCountryDialog = viewModel::dismissCountryDialog,
                            onFilterCountries = viewModel::filterCountries,
                            resetCountries = viewModel::resetCountries,
                            onToggleSearchBar = viewModel::onToggleSearchBar,
                            onUpdateSearchQuery = viewModel::onUpdateSearchQuery,
                            navController = navController
                        )
                    }
                    composable(route = "mapScreen") {
                        MapScreen(
                            country = countriesState.selectedCountry?.name ?: "",
                            latitude = latitude,
                            longitude = longitude,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}