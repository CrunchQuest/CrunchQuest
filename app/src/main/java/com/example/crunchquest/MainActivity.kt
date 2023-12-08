package com.example.crunchquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.crunchquest.data.components.Tabs
import com.example.crunchquest.theme.AppThemeState
import com.example.crunchquest.theme.ColorPallet
import com.example.crunchquest.theme.Material3Theme
import com.example.crunchquest.theme.SystemUiController
import com.example.crunchquest.theme.purple700
import com.example.crunchquest.ui.AllianceScreen
import com.example.crunchquest.ui.DevelopmentScreen
import com.example.crunchquest.ui.DiscoverScreen
import com.example.crunchquest.ui.HomeScreen
import com.example.crunchquest.ui.NutritionScreen
import com.example.crunchquest.ui.RankScreen
import com.example.crunchquest.ui.ShopScreen
import com.example.crunchquest.ui.SocialScreen
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for adView demo
        //MobileAds.initialize(this)
        setContent {
            val systemUiController = remember { SystemUiController(window) }
            val appTheme = remember { mutableStateOf(AppThemeState()) }
            BaseView(appTheme.value, systemUiController) {
                MainAppContent()
            }
        }
    }
}

@Composable
fun BaseView(
    appThemeState: AppThemeState,
    systemUiController: SystemUiController?,
    content: @Composable () -> Unit
) {
    val color = when (appThemeState.pallet) {
        ColorPallet.PURPLE -> purple700
        else -> purple700
    }
    Material3Theme(
        darkTheme = appThemeState.darkTheme,
        colorPallet = appThemeState.pallet
    ) {
        systemUiController?.setStatusBarColor(color = MaterialTheme.colorScheme.onPrimaryContainer, darkIcons = appThemeState.darkTheme)
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    homeScreen: BottomNavType,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Crossfade(homeScreen) { screen ->
            Surface(color = MaterialTheme.colorScheme.background) {
                when (screen) {
                    BottomNavType.HOME -> HomeScreen()
                    BottomNavType.DISCOVER -> DiscoverScreen()
                    BottomNavType.SOCIAL -> SocialScreen()
                    BottomNavType.SHOP -> ShopScreen()
                    BottomNavType.RANK -> RankScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainAppContent() {
    //Default home screen state is always HOME
    val homeScreenState = rememberSaveable { mutableStateOf(BottomNavType.HOME) }
    val bottomNavBarContentDescription = stringResource(id = R.string.a11y_bottom_navigation_bar)

    val coroutineScope = rememberCoroutineScope()

    Column {
        HomeScreenContent(
            homeScreen = homeScreenState.value,
            modifier = Modifier.weight(1f)
        )
        BottomNavigationContent(
            modifier = Modifier
                .semantics { contentDescription = bottomNavBarContentDescription },
            homeScreenState = homeScreenState
        )
    }
}

@Composable
fun BottomNavigationContent(
    modifier: Modifier = Modifier,
    homeScreenState: MutableState<BottomNavType>
) {
    var animate by remember { mutableStateOf(false) }
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem(
            icon = {
                FaIcon(
                    faIcon = FaIcons.Home,
                    tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )
            },
            selected = homeScreenState.value == BottomNavType.HOME,
            onClick = {
                homeScreenState.value = BottomNavType.HOME
                animate = false
            },
            label = {
                Text(
                    text = stringResource(id = R.string.navigation_item_home),
                    style = TextStyle(fontSize = 12.sp)
                )
            },
        )
        NavigationBarItem(
            icon = {
                FaIcon(
                    faIcon = FaIcons.Globe, tint = LocalContentColor
                        .current.copy(
                            alpha =
                            LocalContentAlpha.current
                        )
                )
            },
            selected = homeScreenState.value == BottomNavType.DISCOVER,
            onClick = {
                homeScreenState.value = BottomNavType.DISCOVER
                animate = false
            },
            label = {
                Text(
                    text = stringResource(id = R.string.navigation_item_discover),
                    style = TextStyle(fontSize = 12.sp)
                )
            },
        )
        NavigationBarItem(
            icon = {
                FaIcon(
                    faIcon = FaIcons.Users , tint = LocalContentColor
                        .current.copy(
                            alpha =
                            LocalContentAlpha.current
                        )
                )
            },
            selected = homeScreenState.value == BottomNavType.SOCIAL,
            onClick = {
                homeScreenState.value = BottomNavType.SOCIAL
                animate = true
            },
            label = {
                Text(
                    text = stringResource(id = R.string.navigation_item_social),
                    style = TextStyle(fontSize = 12.sp)
                )
            },

        )
        NavigationBarItem(
            icon = {
                FaIcon(
                    faIcon = FaIcons.ShoppingBag, tint = LocalContentColor.current.copy(
                        alpha =
                        LocalContentAlpha.current
                    )
                )
            },
            selected = homeScreenState.value == BottomNavType.SHOP,
            onClick = {
                homeScreenState.value = BottomNavType.SHOP
                animate = false
            },
            label = {
                Text(
                    text = stringResource(id = R.string.navigation_item_shop),
                    style = TextStyle(fontSize = 12.sp)
                )
            },
        )
        NavigationBarItem(
            icon = {
                FaIcon(
                    faIcon = FaIcons.Star, tint = LocalContentColor
                        .current.copy(
                            alpha =
                            LocalContentAlpha.current
                        )
                )
            },
            selected = homeScreenState.value == BottomNavType.RANK,
            onClick = {
                homeScreenState.value = BottomNavType.RANK
                animate = false
            },
            label = {
                Text(
                    text = stringResource(id = R.string.navigation_item_rank),
                    style = TextStyle(fontSize = 12.sp)
                )
            },

        )
    }
}

@Composable
fun TabLayout() {
    val tabsName = remember { Tabs.values().map { it.value } }
    val selectedIndex = remember { mutableStateOf(Tabs.NUTRITION.ordinal) }

    Column {
        TabRow(
            selectedTabIndex = selectedIndex.value,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabsName.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedIndex.value,
                    onClick = {
                        when (title) {
                            Tabs.NUTRITION.value -> {
                                selectedIndex.value = Tabs.NUTRITION.ordinal
                            }
                            Tabs.DEVELOPMENT.value -> {
                                selectedIndex.value = Tabs.DEVELOPMENT.ordinal
                            }
                            Tabs.ALLIANCE.value -> {
                                selectedIndex.value = Tabs.ALLIANCE.ordinal
                            }
                        }
                    },
                    text = { Text(title, color = MaterialTheme.colorScheme.onPrimaryContainer ) }
                )
            }
        }
        Surface(modifier = Modifier.weight(0.5f)) {
            when (selectedIndex.value) {
                Tabs.NUTRITION.ordinal -> {
                    NutritionScreen()
                }
                Tabs.DEVELOPMENT.ordinal -> {
                    DevelopmentScreen()
                }
                Tabs.ALLIANCE.ordinal -> {
                    AllianceScreen()
                }
            }
        }

    }
    
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun DefaultPreview() {
    val appThemeState = remember { mutableStateOf(AppThemeState(false, ColorPallet.PURPLE)) }
    BaseView(appThemeState.value, null) {
        MainAppContent()
    }
}


