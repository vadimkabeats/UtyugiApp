package com.example.utyugiapp

data class IronModel(
    val number: Int,
    val model: String,
    val color: String,
    val power: Int,
    val soleType: String,
    val steamRate: Int,
    val steamBoost: Boolean,
    val steamControl: Boolean,
    val tankVolume: Int,
    val leakProtect: Boolean,
    val waterLevel: Boolean,
    val vertical: Boolean,
    val maxPressure: Double,
    val antiScale: Boolean,
    val safety: String,
    val weight: Double,
    val quantity: Int,
    val price: Double
)