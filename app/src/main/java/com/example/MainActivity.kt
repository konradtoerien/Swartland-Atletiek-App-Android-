package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwartlandTheme {
                MainAppScaffold()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold() {
    var currentDestination by remember { mutableStateOf(AppDestination.SETUP) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle back button when drawer is open or returning to SETUP
    BackHandler(enabled = drawerState.isOpen || currentDestination != AppDestination.SETUP) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            currentDestination = AppDestination.SETUP
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = NavySurface,
                modifier = Modifier.width(310.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyHeader)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lss_logo),
                        contentDescription = "Laerskool Swartland Logo",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Laerskool Swartland",
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Atletiek Byeenkoms Portaal",
                        color = TextLight,
                        fontSize = 13.sp
                    )
                }

                HorizontalDivider(color = NavyBorder)

                Spacer(modifier = Modifier.height(8.dp))

                AppDestination.entries.forEach { destination ->
                    NavigationDrawerItem(
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.title, fontWeight = FontWeight.SemiBold) },
                        selected = currentDestination == destination,
                        onClick = {
                            currentDestination = destination
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = GoldAccent,
                            selectedIconColor = NavyHeader,
                            selectedTextColor = NavyHeader,
                            unselectedContainerColor = NavySurface,
                            unselectedIconColor = GoldAccent,
                            unselectedTextColor = TextWhite
                        ),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .testTag("drawer_item_${destination.route}")
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.lss_logo),
                                contentDescription = "LSS Logo",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Laerskool Swartland",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = currentDestination.title,
                                    color = TextWhite,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("open_drawer_btn")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Kieslys", tint = GoldAccent)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NavyHeader,
                        titleContentColor = GoldAccent
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = NavyHeader,
                    contentColor = GoldAccent,
                    tonalElevation = 8.dp
                ) {
                    val bottomItems = listOf(
                        AppDestination.SETUP,
                        AppDestination.DRAWS,
                        AppDestination.OFFICIATING,
                        AppDestination.LIVE_FEED,
                        AppDestination.PROGRAM
                    )

                    bottomItems.forEach { destination ->
                        val isSelected = currentDestination == destination
                        NavigationBarItem(
                            icon = { Icon(destination.icon, contentDescription = destination.title) },
                            label = { Text(destination.title, fontSize = 10.sp) },
                            selected = isSelected,
                            onClick = { currentDestination = destination },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyHeader,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("bottom_nav_${destination.route}")
                        )
                    }

                    // More button to open drawer for Records and Reports
                    val isMoreSelected = currentDestination == AppDestination.RECORDS || currentDestination == AppDestination.REPORTS
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "Meer") },
                        label = { Text("Meer", fontSize = 10.sp) },
                        selected = isMoreSelected,
                        onClick = { scope.launch { drawerState.open() } },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NavyHeader,
                            selectedTextColor = GoldAccent,
                            indicatorColor = GoldAccent,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("bottom_nav_more")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    AppDestination.SETUP -> SetupScreen()
                    AppDestination.DRAWS -> DrawSheetsScreen()
                    AppDestination.OFFICIATING -> OfficiatingScreen()
                    AppDestination.RECORDS -> RecordsScreen()
                    AppDestination.LIVE_FEED -> LiveFeedScreen()
                    AppDestination.REPORTS -> ReportsScreen()
                    AppDestination.PROGRAM -> ProgramScreen()
                }
            }
        }
    }
}
