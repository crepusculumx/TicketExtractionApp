package edu.bupt.ticketextraction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.bupt.ticketextraction.ui.theme.TicketExtractionTheme


class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketExtractionTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navControllers = rememberNavController()
                    Scaffold(
                        topBar = {
                            TopBarTest()
                        },
                        bottomBar = {
                            BottomNavigationTest(navControllers)
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { /*TODO*/ },
                                Modifier.size(80.dp),
                                backgroundColor = Color.Black
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_outline_photo_camera_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = Color.White
                                )
                            }
                        },
                        isFloatingActionButtonDocked = true,
                        floatingActionButtonPosition = FabPosition.Center,
                        content = {
                            NavHost(navControllers, startDestination = Screen.Receipt.route) {
                                composable(Screen.Receipt.route) { ReceiptFragment() }
                                composable(Screen.Email.route) { EmailFragment() }
                                composable(Screen.Setting.route) { SettingsUI() }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReceiptFragment() {
    Greeting("1")
}

@Composable
fun EmailFragment() {
    Greeting("2")
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun TopBarTest() {
    TopAppBar(
        title = { Text("发票识别") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Share, contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }
        }
    )
}

sealed class Screen(var route: String, var resId: Int) {
    object Receipt : Screen("receipt", R.drawable.ic_baseline_receipt_24)
    object Email : Screen("email", R.drawable.ic_baseline_email_24)
    object Setting : Screen("setting", R.drawable.ic_baseline_settings_24)
}

@Composable
fun BottomNavigationTest(navControllers: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Screen.Receipt, Screen.Setting)
    BottomAppBar(cutoutShape = RoundedCornerShape(80.dp)) {
        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(screen.resId), contentDescription = null) },
                label = { Text(screen.route) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navControllers.navigate(screen.route) {
                        popUpTo(navControllers.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TicketExtractionTheme {
        Greeting("Android")
    }
}