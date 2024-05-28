package com.jaidensiu.worldcountriesapp

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
                val state by viewModel.state.collectAsState()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = color,
                        darkIcons = useDarkIcons
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = "countriesScreen"
                ) {
                    composable(route = "countriesScreen") {
                        CountriesScreen(
                            state = state,
                            onSelectCountry = viewModel::selectCountry,
                            onDismissCountryDialog = viewModel::dismissCountryDialog,
                            navController = navController
                        )
                    }
                    composable(route = "mapScreen") {
                        MapScreen(
                            country = state.selectedCountry!!.name,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}