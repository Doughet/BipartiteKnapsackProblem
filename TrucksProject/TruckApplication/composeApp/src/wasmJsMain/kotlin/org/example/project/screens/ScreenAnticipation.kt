package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.DarkGrayCustom
import org.example.project.LightBlue
import org.example.project.LightGrayCustom
import org.example.project.LighterGrayCustom
import org.example.project.navigation.BottomNavigationBarCustom
import org.example.project.navigation.TopBarCustom
import org.example.project.viewmodels.AnticipationViewModel
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun TitleTextBlock(text : String, color: Color = DarkGrayCustom){
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SubTitleTextBlock(text : String, size: Int = 20, color: Color = DarkGrayCustom){
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = size.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CustomInputText(
    text : String,
    onChangeValue : (String) -> Unit
){

    OutlinedTextField(
        value = text,
        onValueChange = { onChangeValue(it) },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(
                color = LightGrayCustom,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = LightGrayCustom,
                shape = RoundedCornerShape(12.dp)
            ),
        textStyle = LocalTextStyle.current.copy(
            color = LighterGrayCustom,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = LighterGrayCustom,
            textColor = LighterGrayCustom,
            unfocusedLabelColor = LighterGrayCustom,
        )
    )

}

@Composable
fun CorpTextBlock(text : String, size: Int = 12){
    Text(
        text = text,
        color = DarkGrayCustom,
        fontWeight = FontWeight.Medium,
        fontSize = size.sp
    )
}

@Composable
fun AddingFileButton(

){
    Button(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LightBlue,
        ),
        modifier = Modifier.fillMaxWidth(0.5f).height(70.dp),

        ){

        SubTitleTextBlock("+ Add an Excel File", color = Color.White)

    }
}

@Composable
fun MainButton(){
    Button(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LightBlue,
        ),
        modifier = Modifier.fillMaxWidth(0.5f),
        contentPadding = PaddingValues(vertical = 12.dp)

        ){

        SubTitleTextBlock("Process", color = Color.White)

    }
}

@Composable
fun GeneralInformationBlock(
    sheetName: String,
    onSheetNameChange : (String) -> Unit,
    nLimit : String,
    onNLimitChange: (String) -> Unit,
    pLimit : String,
    onPLimitChange: (String) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(Modifier.weight(1.0f)){
            ColumnInputCustom("Sheet Name:", sheetName, onSheetNameChange)
        }

        Column (
            modifier = Modifier.weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            SubTitleTextBlock(text = "Weight and Palettes Per Truck:")

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.weight(2.0f),
                    contentAlignment = Alignment.Center
                ){
                    CustomInputText(
                        pLimit,
                        {
                            onPLimitChange(it)
                        }
                    )
                }

                Box(
                    modifier = Modifier.weight(1.0f),
                    contentAlignment = Alignment.Center
                ){
                    CustomInputText(
                        nLimit,
                        {
                            onNLimitChange(it)
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun FixedInformationBlock(
    fixedN : String,
    onFixedNChange: (String) -> Unit,
    fixedP : String,
    onFixedPChange: (String) -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(Modifier.weight(1.0f)){
            ColumnInputCustom("Fixed Palets:", fixedN, onFixedNChange)
        }

        Box(Modifier.weight(1.0f)){
            ColumnInputCustom("Fixed Weight:", fixedP, onFixedPChange)
        }

    }

}


@Composable
fun ColumnInputCustom(
    title : String,
    textVar: String,
    onTextVarChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SubTitleTextBlock(text = title)

        Spacer(Modifier.height(10.dp))

        CustomInputText(
            textVar,
            {
                onTextVarChange(it)
            }
        )
    }
}

@Composable
fun AnticipationBlock(
    sheetName : String,
    onSheetNameChange: (String) -> Unit,
    nLimit : String,
    onNLimitChange: (String) -> Unit,
    pLimit : String,
    onPLimitChange: (String) -> Unit,
    fixedN : String,
    onFixedNChange: (String) -> Unit,
    fixedP : String,
    onFixedPChange: (String) -> Unit,
    totalTrucks : String,
    onTotalTrucksChange: (String) -> Unit
){

    val sheetName = if(sheetName == "0.0") "" else sheetName
    val nLimit = if(nLimit == "0.0") "" else nLimit
    val pLimit = if(pLimit == "0.0") "" else pLimit
    val fixedN = if(fixedN == "0.0") "" else fixedN
    val fixedP = if(fixedP == "0.0") "" else fixedP
    val totalTrucks = if(totalTrucks == "0.0") "" else totalTrucks

    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 30.dp, horizontal = 30.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        TitleTextBlock("Excel Command File")

        Spacer(Modifier.height(40.dp))

        AddingFileButton()

        Spacer(Modifier.height(40.dp))

        GeneralInformationBlock(
            sheetName,
            onSheetNameChange,
            nLimit,
            onNLimitChange,
            pLimit,
            onPLimitChange
        )

        Spacer(Modifier.height(30.dp))

        FixedInformationBlock(
            fixedN,
            onFixedNChange,
            fixedP,
            onFixedPChange
        )

        Spacer(Modifier.height(30.dp))

        ColumnInputCustom(
            "Total Trucks Amout:",
            totalTrucks,
            onTotalTrucksChange
        )

        Spacer(Modifier.height(40.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Box (
                modifier = Modifier.weight(1.0f),
                contentAlignment = Alignment.Center
            ){
                MainButton()
            }

            Box (
                modifier = Modifier.weight(1.0f),
                contentAlignment = Alignment.Center
            ){
                MainButton()
            }

        }

    }
}

@Composable
fun TruckAnticipationScreen(navigationViewModel: NavigationViewModel,
                            anticipationViewModel: AnticipationViewModel){



    Scaffold(
        topBar = { TopBarCustom("Truck Anticipation", {}) },
        bottomBar = { BottomNavigationBarCustom(navigationViewModel) }
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
                anticipationViewModel.getNLimit().toString(),
                {nLimitString ->

                    try {
                        anticipationViewModel.setNLimit(nLimitString.toFloat())
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
                anticipationViewModel.getFixedN().toString(),
                {fixedN ->
                    try {
                        anticipationViewModel.setFixedN(fixedN.toFloat())
                    }catch (e : Exception){
                        anticipationViewModel.setFixedN(0f)
                    }
                },
                anticipationViewModel.getFixedP().toString(),
                {fixedP ->
                    try {
                        anticipationViewModel.setFixedP(fixedP.toFloat())
                    }catch (e : Exception){
                        anticipationViewModel.setFixedP(0f)
                    }
                },
                anticipationViewModel.getTotalTrucks().toString(),
                {totalTrukcs ->
                    try {
                        anticipationViewModel.setTotalTrucks(totalTrukcs.toFloat())
                    }catch (e : Exception){
                        anticipationViewModel.setTotalTrucks(0f)
                    }
                },
            )
        }
    }
}