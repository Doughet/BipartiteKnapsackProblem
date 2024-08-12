package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.unit.dp
import org.example.project.DarkGrayCustom
import org.example.project.LightBlue
import org.example.project.LightGrayCustom
import org.example.project.LightRed
import org.example.project.enums.FunctionEnum
import org.example.project.viewmodels.NavigationViewModel


@Composable
fun ElementCard(
    text : String,
    isActive : Boolean,
    onClick : () -> Unit
){
    val mainColor = if(isActive) LightBlue else DarkGrayCustom

    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(mainColor, RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                    .height(30.dp)
            )

            Box(
                modifier = Modifier.padding(20.dp),
                contentAlignment = Alignment.Center
            ){
                SubTitleTextBlock(text, color = mainColor)
            }
        }
    }

}

@Composable
fun ServicesElements(navigationViewModel: NavigationViewModel){

    val functionType = navigationViewModel.getFunctionObservable().collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().background(LightRed, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ElementCard(
            "Truck Anticipation",
            functionType == FunctionEnum.ANTICIPATION,
            {
                navigationViewModel.navigateToAnticipation()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElementCard(
            "Truck Optimization",
            functionType == FunctionEnum.OPTIMIZATION,
            {
                navigationViewModel.navigateToOptimization()
            }
        )
    }
}