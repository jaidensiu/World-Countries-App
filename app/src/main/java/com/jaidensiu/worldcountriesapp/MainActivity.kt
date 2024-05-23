package com.jaidensiu.worldcountriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jaidensiu.worldcountriesapp.presentation.CountriesScreen
import com.jaidensiu.worldcountriesapp.presentation.CountriesViewModel
import com.jaidensiu.worldcountriesapp.ui.theme.WorldCountriesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorldCountriesAppTheme {
                val viewModel = hiltViewModel<CountriesViewModel>()
                val state by viewModel.state.collectAsState()

                CountriesScreen(
                    state = state,
                    onSelectCountry = viewModel::selectCountry,
                    onDismissCountryDialog = viewModel::dismissCountryDialog
                )

                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                val color = MaterialTheme.colors.primary

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = color,
                        darkIcons = useDarkIcons
                    )
                }
            }
        }
    }
}