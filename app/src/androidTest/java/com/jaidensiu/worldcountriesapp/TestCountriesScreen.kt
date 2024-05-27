package com.jaidensiu.worldcountriesapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry
import com.jaidensiu.worldcountriesapp.presentation.CountriesScreen
import com.jaidensiu.worldcountriesapp.presentation.CountriesViewModel
import com.jaidensiu.worldcountriesapp.ui.theme.WorldCountriesAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountriesScreenTest {
    private val progressIndicatorId = "progressIndicator"

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingAndTopAppBar() {
        val state = CountriesViewModel.CountriesState(isLoading = true)

        composeTestRule.setContent {
            WorldCountriesAppTheme {
                CountriesScreen(
                    state = state,
                    onSelectCountry = {},
                    onDismissCountryDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText(text = "World Countries")
            .assertExists()
        composeTestRule.onNode(matcher = hasTestTag(testTag = progressIndicatorId))
            .assertIsDisplayed()
    }

    @Test
    fun testCountriesList() {
        val countries = listOf(
            SimpleCountry(
                code = "CA",
                name = "Canada",
                emoji = "🇨🇦",
                capital = "Ottawa"
            ),
            SimpleCountry(
                code = "US",
                name = "United States",
                emoji = "🇺🇸",
                capital = "Washington D.C."
            )
        )
        val state = CountriesViewModel.CountriesState(countries = countries)

        composeTestRule.setContent {
            WorldCountriesAppTheme {
                CountriesScreen(
                    state = state,
                    onSelectCountry = {},
                    onDismissCountryDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText(text = "United States")
            .assertExists()
        composeTestRule.onNodeWithText(text = "Canada")
            .assertExists()
    }

    @Test
    fun testCountryDialogIsDisplayed() {
        val selectedCountry = DetailedCountry(
            code = "US",
            name = "United States",
            emoji = "🇺🇸",
            capital = "Washington D.C.",
            currency = "USD",
            languages = listOf("English"),
            continent = "North America"
        )
        val state = CountriesViewModel.CountriesState(selectedCountry = selectedCountry)

        composeTestRule.setContent {
            WorldCountriesAppTheme {
                CountriesScreen(
                    state = state,
                    onSelectCountry = {},
                    onDismissCountryDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithText("United States").assertExists()
        composeTestRule.onNodeWithText("Continent: North America").assertExists()
        composeTestRule.onNodeWithText("Currency: USD").assertExists()
        composeTestRule.onNodeWithText("Capital: Washington D.C.").assertExists()
        composeTestRule.onNodeWithText("Language(s): English").assertExists()
    }

    @Test
    fun testCountrySelection() {
        val countries = listOf(
            SimpleCountry(
                code = "CA",
                name = "Canada",
                emoji = "🇨🇦",
                capital = "Ottawa"
            )
        )
        val state = CountriesViewModel.CountriesState(countries = countries)
        var selectedCountryCode: String? = null

        composeTestRule.setContent {
            WorldCountriesAppTheme {
                CountriesScreen(
                    state = state,
                    onSelectCountry = { code -> selectedCountryCode = code },
                    onDismissCountryDialog = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("countryItem")
            .assertIsDisplayed()
            .assert(hasText("Canada"))
        composeTestRule.onNodeWithText("Canada")
            .performClick()
        assert(selectedCountryCode == "CA")
    }
}
