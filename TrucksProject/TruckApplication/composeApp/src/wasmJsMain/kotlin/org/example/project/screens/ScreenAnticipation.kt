package org.example.project.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import org.example.project.navigation.BottomNavigationBarCustom
import org.example.project.navigation.TopBarCustom
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun TruckOptimizationScreen(navigationViewModel : NavigationViewModel){
    Scaffold(
        topBar = { TopBarCustom("Truck Anticipation", {}) },
        bottomBar = { BottomNavigationBarCustom(navigationViewModel) }
    ){



    }
}