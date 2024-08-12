package org.example.project.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AnticipationViewModel {

    val sheetName : MutableState<String> = mutableStateOf("")
    val NLimit : MutableState<Float> = mutableStateOf(0.0f)
    val PLimit : MutableState<Float> = mutableStateOf(0.0f)

    val fixedN : MutableState<Float> = mutableStateOf(0.0f)
    val fixedP : MutableState<Float> = mutableStateOf(0.0f)

    val totalTrucks : MutableState<Float> = mutableStateOf(0.0f)


    fun setSheetName(text : String){
        sheetName.value = text
    }

    fun getSheetName() : String{
        return sheetName.value
    }

    fun getNLimit() : Float{
        return NLimit.value
    }

    fun setNLimit(nLimit : Float){
        NLimit.value = nLimit
    }

    fun getPLimit() : Float{
        return PLimit.value
    }

    fun setPLimit(pLimit : Float){
        PLimit.value = pLimit
    }

    fun getFixedN() : Float{
        return fixedN.value
    }

    fun setFixedN(fixedN : Float){
        this.fixedN.value = fixedN
    }


    fun getFixedP() : Float{
        return fixedP.value
    }

    fun setFixedP(fixedP : Float){
        this.fixedP.value = fixedP
    }

    fun getTotalTrucks() : Float{
        return totalTrucks.value
    }

    fun setTotalTrucks(trucks : Float){
        totalTrucks.value = trucks
    }

}