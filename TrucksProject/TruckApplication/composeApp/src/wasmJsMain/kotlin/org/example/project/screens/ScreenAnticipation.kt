package org.example.project.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.navigation.BottomNavigationBarCustom
import org.example.project.navigation.TopBarCustom
import org.example.project.viewmodels.AnticipationViewModel
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun TrucksNumbersBlock(
    C1 : String,
    onC1Change1: (String) -> Unit,
    C2 : String,
    onC2Change: (String) -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(Modifier.weight(1.0f)){
            ColumnInputCustom("Number of Trucks in Group 1:", C1, onC1Change1)
        }

        Box(Modifier.weight(1.0f)){
            ColumnInputCustom("Number of Trucks in Group 2:", C2, onC2Change)
        }
    }

}

@Composable
fun AnticipationBlock(
    sheetName : String,
    onSheetNameChange: (String) -> Unit,
    onButtonClick : () -> Unit,
    nLimit : String,
    onNLimitChange: (String) -> Unit,
    pLimit : String,
    onPLimitChange : (String) -> Unit,
    C1: String,
    onC1Change : (String) -> Unit,
    C2 : String,
    onC2Change : (String) -> Unit,
    onDowload : () -> Unit
){

    val sheetName = if(sheetName == "0.0") "" else sheetName
    val nLimit = if(nLimit == "0.0") "" else nLimit
    val pLimit = if(pLimit == "0.0") "" else pLimit
    val C1 = if(C1 == "0") "" else C1
    val C2 = if(C2 == "0") "" else C2

    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 30.dp, horizontal = 30.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        TitleTextBlock("Excel Command File")

        Spacer(Modifier.height(10.dp))

        AddingFileButton(onButtonClick)

        Spacer(Modifier.height(40.dp))

        GeneralInformationBlock(
            sheetName,
            onSheetNameChange,
            nLimit,
            onNLimitChange,
            pLimit,
            onPLimitChange
        )

        Spacer(Modifier.height(40.dp))

        TrucksNumbersBlock(
            C1,
            onC1Change,
            C2,
            onC2Change
        )

        Spacer(Modifier.height(40.dp))

        MainButton(onDowload)
    }
}


@Composable
fun TruckAnticipationScreen(navigationViewModel : NavigationViewModel, anticipationViewModel: AnticipationViewModel){
    Scaffold(
        topBar = { TopBarCustom("Truck Anticipation", {}) },
        //bottomBar = { BottomNavigationBarCustom(navigationViewModel) }
    ){

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ){
            AnticipationBlock(
                anticipationViewModel.getSheetName(),
                {sheetName ->
                    anticipationViewModel.setSheetName(sheetName)
                },
                {
                    anticipationViewModel.pickFile { bytes, s ->  }
                },
                anticipationViewModel.getNLimit().toString(),
                {nLimit ->
                    try {
                        anticipationViewModel.setNLimit(nLimit.toFloat())
                    }catch (e : Exception){
                        anticipationViewModel.setNLimit(0f)
                    }
                },
                anticipationViewModel.getPLimit().toString(),
                {pLimit ->
                    try {
                        anticipationViewModel.setPLimit(pLimit.toFloat())
                    }catch (e : Exception){
                        anticipationViewModel.setPLimit(0f)
                    }
                },
                anticipationViewModel.getC1().toString(),
                {c1 ->
                    try {
                        anticipationViewModel.setC1(c1.toInt())
                    }catch (e : Exception){
                        anticipationViewModel.setC1(0)
                    }
                },
                anticipationViewModel.getC2().toString(),
                {c2 ->
                    try {
                        anticipationViewModel.setC2(c2.toInt())
                    }catch (e : Exception){
                        anticipationViewModel.setC2(0)
                    }
                },
                {
                    anticipationViewModel.postAnticipation()
                }
            )
        }

    }
}