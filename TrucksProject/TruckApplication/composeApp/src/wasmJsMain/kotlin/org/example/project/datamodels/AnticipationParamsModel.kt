package org.example.project.datamodels

data class AnticipationParamsModel(
    val sheetName : String,
    val NLimit : Float,
    val PLimit : Float,
    val FixedN : Float,
    val N : Float,
    val totalTrucks : Int
)