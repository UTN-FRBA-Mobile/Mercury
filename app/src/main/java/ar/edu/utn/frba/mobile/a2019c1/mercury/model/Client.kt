package ar.edu.utn.frba.mobile.a2019c1.mercury.model

data class Client(val name: String, val phoneNumber: String, val location: String) {
    companion object {
        fun buildFromDatabase(map: HashMap<String, Any>): Client {
            val name = map.get("name") as String
            val phoneNumber = map.get("phoneNumber") as String
            val location = map.get("location") as String
            return Client(name, phoneNumber, location)
        }
    }
}