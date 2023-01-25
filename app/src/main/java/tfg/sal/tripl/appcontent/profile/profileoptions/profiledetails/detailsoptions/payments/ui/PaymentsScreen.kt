package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.payments.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.appcontent.login.data.network.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.BackHeader

@Composable
fun PaymentsScreen(
    viewModel: PaymentsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackHeader {
            viewModel.onBackPressed(
                navigationController
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        PaymentsBody(viewModel, fireBaseViewModel, navigationController)
    }
}

@Composable
fun PaymentsBody(
    viewModel: PaymentsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {

}