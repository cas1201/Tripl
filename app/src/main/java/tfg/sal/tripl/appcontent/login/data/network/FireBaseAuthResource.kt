package tfg.sal.tripl.appcontent.login.data.network

sealed class FireBaseAuthResource<out R> {
    data class Success<out R>(val result: R) : FireBaseAuthResource<R>()
    data class Error(val exception: Exception) : FireBaseAuthResource<Nothing>()
    object Loading : FireBaseAuthResource<Nothing>()
    object Reset : FireBaseAuthResource<Nothing>()
}