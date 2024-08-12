package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.project.navigation.BottomNavigationBarCustom
import org.example.project.navigation.TopBarCustom
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun TruckOptimizationScreen(navigationViewModel : NavigationViewModel){
    Scaffold(
        topBar = { TopBarCustom("Truck Optimization", {}) },
        bottomBar = { BottomNavigationBarCustom(navigationViewModel) }
    ){



    }
}