package tfg.sal.tripl.appcontent.home.itinerary.data

data class CheckBoxInfo(
    val type: String,
    var selected: Boolean = false,
    var onCheckedChange: (Boolean) -> Unit
)
