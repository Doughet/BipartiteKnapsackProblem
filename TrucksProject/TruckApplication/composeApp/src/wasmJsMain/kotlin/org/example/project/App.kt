package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.project.screens.ScreenMain
import org.jetbrains.compose.resources.painterResource

import truckapplication.composeapp.generated.resources.Res
import truckapplication.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {

        ScreenMain()

    }
}