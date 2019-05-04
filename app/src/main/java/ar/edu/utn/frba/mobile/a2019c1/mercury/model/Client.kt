package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import java.time.LocalTime

data class Client(val name: String, val phoneNumber: String, val location: String, val timeToVisit: LocalTime)