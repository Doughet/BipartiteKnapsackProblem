package org.example.project.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.enums.FunctionEnum

class NavigationViewModel {
    val currentFunction : MutableStateFlow<FunctionEnum> = MutableStateFlow<FunctionEnum>(FunctionEnum.ANTICIPATION)

    fun navigateToOptimization(){
        currentFunction.value = FunctionEnum.OPTIMIZATION
    }

    fun navigateToAnticipation(){
        currentFunction.value = FunctionEnum.ANTICIPATION
    }

    fun getFunctionObservable() : MutableStateFlow<FunctionEnum>{
        return currentFunction
    }
}