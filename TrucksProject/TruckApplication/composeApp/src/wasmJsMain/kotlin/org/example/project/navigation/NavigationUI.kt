package org.example.project.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.DarkGrayCustom
import org.example.project.LightBlue
import org.example.project.enums.FunctionEnum
import org.example.project.viewmodels.NavigationViewModel

@Composable
fun TopBarCustom(
    title: String,
    onMoreOptionsClick: () -> Unit
) {
    Surface(
        elevation = 5.dp, // Add elevation here
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colors.primary
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = DarkGrayCustom,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            },
            backgroundColor = MaterialTheme.colors.background
        )
    }
}



@Composable
fun BottomNavigationBarCustom(navigationViewModel: NavigationViewModel) {

    val currentRoute by navigationViewModel.getFunctionObservable().collectAsState()

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background, RoundedCornerShape(12.dp))
            .shadow(5.dp, shape = RoundedCornerShape(12.dp))
    ) {

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Anticipation"
                )
            },
            label = { Text(text = "Truck Anticipation") },
            selected = navigationViewModel.getFunctionObservable().value == FunctionEnum.ANTICIPATION,
            selectedContentColor = LightBlue,
            unselectedContentColor = DarkGrayCustom,
            onClick = {
                navigationViewModel.navigateToAnticipation()
            }
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Optimization"
                )
            },
            label = { Text(text = "Truck Optimization") },
            selected = navigationViewModel.getFunctionObservable().value == FunctionEnum.OPTIMIZATION,
            selectedContentColor = LightBlue,
            unselectedContentColor = DarkGrayCustom,
            onClick = {
                navigationViewModel.navigateToOptimization()
            }
        )

    }
}
