package org.example.project.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.project.enums.FunctionEnum
import org.example.project.viewmodels.AnticipationViewModel
import org.example.project.viewmodels.OptimisationViewModel
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun ScreenMain(){

    //VIEWMODELS
    val navigationViewModel = remember { NavigationViewModel() }
    val optimisationViewModel = remember { OptimisationViewModel() }
    val anticipationViewModel = remember { AnticipationViewModel() }

    //OBSERVABLES
    var currentFunction = navigationViewModel.getFunctionObservable().collectAsState().value


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {

        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            ServicesElements(navigationViewModel)
        }

        Box(
            modifier = Modifier.weight(5.0f),
            contentAlignment = Alignment.Center
        )
        {

            if (currentFunction == FunctionEnum.OPTIMIZATION) {
                TruckOptimisationScreen(navigationViewModel, optimisationViewModel)
            } else if (currentFunction == FunctionEnum.ANTICIPATION) {
                TruckAnticipationScreen(navigationViewModel, anticipationViewModel)
            }

        }
    }



}