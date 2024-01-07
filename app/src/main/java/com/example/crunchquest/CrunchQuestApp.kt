package com.example.crunchquest

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crunchquest.data.components.BottomNavType
import com.example.crunchquest.theme.AppThemeState
import com.example.crunchquest.theme.ColorPallet
import com.example.crunchquest.ui.DiscoverScreen
import com.example.crunchquest.ui.HomeScreen
import com.example.crunchquest.ui.RankScreen
import com.example.crunchquest.ui.ShopScreen
import com.example.crunchquest.ui.SocialScreen
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun CrunchQuestApp(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //Default home screen state is always HOME
    val homeScreenState = rememberSaveable { mutableStateOf(BottomNavType.HOME) }

    val coroutineScope = rememberCoroutineScope()

    Column {
        HomeScreenContent(
            homeScreen = homeScreenState.value,
            modifier = Modifier.weight(1f)
        )
        BottomNavigationContent(
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

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun Preview() {
    val appThemeState = remember { mutableStateOf(AppThemeState(false, ColorPallet.PURPLE)) }
    BaseView(appThemeState.value, null) {
        CrunchQuestApp()
    }
}