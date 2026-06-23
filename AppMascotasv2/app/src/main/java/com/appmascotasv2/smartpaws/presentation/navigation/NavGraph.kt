package com.appmascotasv2.smartpaws.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appmascotasv2.smartpaws.di.AppContainer
import com.appmascotasv2.smartpaws.presentation.feature.auth.LoginScreen
import com.appmascotasv2.smartpaws.presentation.feature.auth.LoginViewModel
import com.appmascotasv2.smartpaws.presentation.feature.auth.LoginViewModelFactory
import com.appmascotasv2.smartpaws.presentation.feature.main.MainScreen

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import com.appmascotasv2.smartpaws.presentation.feature.mascota.RegistrarMascotaScreen
import com.appmascotasv2.smartpaws.presentation.feature.mascota.RegistrarMascotaViewModel
import com.appmascotasv2.smartpaws.presentation.feature.mascota.RegistrarMascotaViewModelFactory
import com.appmascotasv2.smartpaws.presentation.feature.mascota.MascotaScreen
import com.appmascotasv2.smartpaws.presentation.feature.mascota.MascotaViewModel
import com.appmascotasv2.smartpaws.presentation.feature.mascota.MascotaViewModelFactory
import com.appmascotasv2.smartpaws.presentation.feature.about.AboutScreen

sealed class Screen(val route: String) {
    object Login       : Screen("login")

    object Main        : Screen("main/{userId}") {
        fun createRoute(userId: Int) = "main/$userId"
    }
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
                factory = LoginViewModelFactory(
                    loginUseCase    = container.loginUseCase,
                    registerUseCase = container.registerUseCase
                )
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
        // ── Main (bottom bar) ───────────────────────────────────────────────
        composable(
            route     = Screen.Main.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { back ->
            val userId = back.arguments!!.getInt("userId")
            MainScreen(
                userId               = userId,
                container            = container,
                onNavigateToMascotas = {navController.navigate(Screen.Mascotas.createRoute(userId))},
                onNavigateToAbout    = {navController.navigate(Screen.About.route)},
                onLogout = {
                    scope.launch {
                        container.logoutUseCase()
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
                factory = MascotaViewModelFactory(
                    getMascotasUseCase   = container.getMascotasUseCase,
                    deleteMascotaUseCase = container.deleteMascotaUseCase,
                    userId               = userId
                )
            )
            MascotaScreen(
                viewModel               = vm,
                onNavigateToRegisterPet = { navController.navigate(Screen.RegisterPet.createRoute(userId)) },
                onNavigateBack          = { navController.popBackStack() }
            )
        }


        // ── Registrar Mascota ──────────────────────────────────────────────
        composable(
            route     = Screen.RegisterPet.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { back ->
            val userId = back.arguments!!.getInt("userId")
            val vm: RegistrarMascotaViewModel = viewModel(
                factory = RegistrarMascotaViewModelFactory(
                    addMascotaUseCase = container.addMascotaUseCase,
                    userId            = userId
                )
            )
            RegistrarMascotaScreen(
                viewModel      = vm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Acerca De ──────────────────────────────────────────────────────
        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() })
        }
    }
}