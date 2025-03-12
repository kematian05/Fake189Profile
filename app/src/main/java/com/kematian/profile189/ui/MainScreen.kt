package com.kematian.profile189.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kematian.profile189.ProfileRepository
import com.kematian.profile189.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val profileViewModel = ProfileViewModel(
        repository = ProfileRepository.getInstance(LocalContext.current)
    )
    NavHost(navController = navController, startDestination = "map") {
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }
        composable("map") {
            MapScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }
        composable("search") {
            SearchScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}