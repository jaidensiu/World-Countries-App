package com.jaidensiu.worldcountriesapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jaidensiu.graphqlprep.R

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    country: String,
    navController: NavController
) {
    Scaffold(
       topBar = {
           TopAppBar(
               title = {
                   Text(
                       text = country,
                       fontSize = 32.sp
                   )
               },
               modifier = Modifier.height(96.dp),
               navigationIcon = {
                   IconButton(
                       onClick = {
                           navController.navigateUp()

                       }
                   ) {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_back_arrow),
                           contentDescription = "Back",
                           modifier = Modifier.size(32.dp)
                       )
                   }
               }
           )
       },
        content = {
            Box(
                modifier = modifier.padding(it)
            ) {
                Text(
                    text = "Map Screen goes here"
                )   
            }
        }
    )
}