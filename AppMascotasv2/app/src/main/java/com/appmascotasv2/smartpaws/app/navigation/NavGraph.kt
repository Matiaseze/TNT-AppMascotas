package com.appmascotasv2.smartpaws.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appmascotasv2.smartpaws.di.AppContainer
import com.appmascotasv2.smartpaws.feature.auth.LoginScreen
import com.appmascotasv2.smartpaws.feature.auth.LoginViewModel
import com.appmascotasv2.smartpaws.feature.auth.LoginViewModelFactory
import com.appmascotasv2.smartpaws.feature.main.MainScreen

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/*
import com.appmascotasv2.smartpaws.feature.home.HomeScreen
import com.appmascotasv2.smartpaws.feature.home.HomeViewModel
*/
import com.appmascotasv2.smartpaws.feature.mascota.RegistrarMascotaScreen
import com.appmascotasv2.smartpaws.feature.mascota.RegistrarMascotaViewModel
import com.appmascotasv2.smartpaws.feature.mascota.RegistrarMascotaViewModelFactory
import com.appmascotasv2.smartpaws.feature.mascota.MascotaScreen
import com.appmascotasv2.smartpaws.feature.mascota.MascotaViewModel
import com.appmascotasv2.smartpaws.feature.mascota.MascotaViewModelFactory
import com.appmascotasv2.smartpaws.feature.perfil.PerfilScreen
import com.appmascotasv2.smartpaws.feature.about.AboutScreen

sealed class Screen(val route: String) {
    object Login       : Screen("login")

    object Main        : Screen("main/{userId}") {
        fun createRoute(userId: Int) = "main/$userId"
    }
    /*object Home        : Screen("home/{userId}") {
        fun createRoute(userId: Int) = "home/$userId"
    }*/
    object Mascotas        : Screen("pets/{userId}") {
        fun createRoute(userId: Int) = "pets/$userId"
    }
    object RegisterPet : Screen("register_pet/{userId}") {
        fun createRoute(userId: Int) = "register_pet/$userId"
    }
    object Profile     : Screen("profile")
    object About       : Screen("about")
}

@Composable
fun NavGraph(container: AppContainer, startUserId: Int) {
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    val start = if (startUserId > 0)
        Screen.Main.createRoute(startUserId)
    else
        Screen.Login.route

    NavHost(navController = navController, startDestination = start) {

        // ── Login ──────────────────────────────────────────────────────────
        composable(Screen.Login.route) {
            val vm: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(container.authRepository)
            )
            LoginScreen(
                viewModel      = vm,
                onLoginSuccess = { userId ->
                    navController.navigate(Screen.Main.createRoute(userId)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        // ── Main (con bottom bar) ──────────────────────────────────────────
        composable(
                route     = Screen.Main.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { back ->
                val userId = back.arguments!!.getInt("userId")
                MainScreen(
                    userId                  = userId,
                    onNavigateToMascotas    = { navController.navigate(Screen.Mascotas.createRoute(userId)) },
                    onLogout = {
                        scope.launch {
                            container.authRepository.logout()

                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

        // ── Mis Mascotas ───────────────────────────────────────────────────
        composable(
            route     = Screen.Mascotas.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { back ->
            val userId = back.arguments!!.getInt("userId")
            val vm: MascotaViewModel = viewModel(
                factory = MascotaViewModelFactory(container.mascotaRepository, userId)
            )
            MascotaScreen(
                viewModel             = vm,
                onNavigateToRegisterPet = {
                    navController.navigate(Screen.RegisterPet.createRoute(userId))
                },
                onNavigateBack        = { navController.popBackStack() }
            )
        }

        // ── Registrar Mascota ──────────────────────────────────────────────
        composable(
            route     = Screen.RegisterPet.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { back ->
            val userId = back.arguments!!.getInt("userId")
            val vm: RegistrarMascotaViewModel = viewModel(
                factory = RegistrarMascotaViewModelFactory(container.mascotaRepository, userId)
            )
            RegistrarMascotaScreen(
                viewModel      = vm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Mi Perfil ──────────────────────────────────────────────────────
        composable(Screen.Profile.route) {
            PerfilScreen(onNavigateBack = { navController.popBackStack() })
        }

        // ── Acerca De ──────────────────────────────────────────────────────
        composable(Screen.About.route) {
            AboutScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}