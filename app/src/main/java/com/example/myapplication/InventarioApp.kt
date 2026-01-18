package com.example.myapplication

import android.app.Application

class InventarioApp : Application() {
    val database by lazy { InventarioDatabase.getDatabase(this) }
}