package com.sarftec.lifelessons.application.listener

import com.sarftec.lifelessons.application.enums.Destination

interface NavigationListener {
    fun navigate(destination: Destination)
}