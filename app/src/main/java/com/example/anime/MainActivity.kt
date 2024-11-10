package com.example.anime

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.ui.Presentation.Screens.Common.AnimeScreen.AnimeScreen
import com.example.anime.ui.Presentation.Screens.Common.CharacterScreen.CharacterScreen
import com.example.anime.ui.Presentation.Screens.Common.Loading
import com.example.anime.ui.Presentation.Screens.Features.CurrentlyAiring.CurrentAnimesScreen
import com.example.anime.ui.Presentation.Screens.Features.Filter.FilterScreen
import com.example.anime.ui.Presentation.Screens.Features.Random.RandomAnimeScreen
import com.example.anime.ui.Presentation.Screens.Features.Search.SearchScreen
import com.example.anime.ui.Presentation.Screens.Features.TopAnimes.TopAnimesScreen
import com.example.anime.ui.Presentation.Screens.Features.Upcoming.UpcomingAnimesScreen
import com.example.anime.ui.theme.AnimeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class NavItem(
    val title: String,
    val route: Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val items = listOf(
    NavItem(
        title = "Now Airing",
        route = CurrentAnimes,
        selectedIcon = Icons.Filled.LiveTv,
        unselectedIcon = Icons.Outlined.LiveTv
    ),
    NavItem(
        title = "Upcoming",
        route = UpcomingAnimes,
        selectedIcon = Icons.Filled.Event,
        unselectedIcon = Icons.Outlined.Event
    ),
    NavItem(
        title = "Top Animes",
        route = TopAnime,
        selectedIcon = Icons.Filled.Stars,
        unselectedIcon = Icons.Outlined.Stars
    ),
    NavItem(
        title = "Random",
        route = Random,
        selectedIcon = Icons.Filled.Shuffle,
        unselectedIcon = Icons.Outlined.Shuffle
    ),
    NavItem(
        title = "Search",
        route = Search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    NavItem(
        title = "Filter",
        route = Filter,
        selectedIcon = Icons.Filled.FilterAlt,
        unselectedIcon = Icons.Outlined.FilterAlt
    )
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeTheme {
                val navController = rememberNavController()
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        android.graphics.Color.TRANSPARENT
                    )
                )

                val connectionState by rememberConnectivityState()
                val isConnected by remember(connectionState) {
                    derivedStateOf {
                        connectionState === NetworkConnectionState.Available
                    }
                }
                if(!isConnected){
                    Loading(true)
                }else {
                SharedTransitionLayout {
                    val drawerState = rememberDrawerState(
                        initialValue = DrawerValue.Closed
                    )

                    var selectedItem by rememberSaveable{ mutableIntStateOf(0) }
                    var BarTitle by rememberSaveable{ mutableStateOf("Now Airing") }
                    val scope = rememberCoroutineScope()
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerContainerColor = MaterialTheme.colorScheme.primary
                            ){
                                Spacer(modifier = Modifier.padding(15.dp))
                                Image(painter = painterResource(
                                        id = R.drawable.logo
                                    ),
                                    contentDescription = "Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(text = "OtakuJiten",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                                Spacer(modifier = Modifier.padding(4.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = MaterialTheme.colorScheme.surface,
                                            unselectedContainerColor = Color.Transparent,
                                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        label = { Text(
                                            text = item.title,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Medium
                                        ) },
                                        selected = selectedItem == index,
                                        onClick = {
                                            BarTitle = item.title
                                            selectedItem = index
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                            }
                                            scope.launch { drawerState.close() }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItem) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState,
                        gesturesEnabled = true
                    ) {
                        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                            state = rememberTopAppBarState()
                        )
                        val navBackStackEntry = navController.currentBackStackEntryAsState().value
                        val currentRoute = navBackStackEntry?.destination?.route
                        Scaffold(
                            topBar = {
                                if(currentRoute != null && !currentRoute.contains("AnimeRoute") && !currentRoute.contains("CharacterRoute")){
                                    TopAppBar(
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.secondary.copy(0.7f),
                                            scrolledContainerColor = MaterialTheme.colorScheme.secondary.copy(0.7f)
                                        ),
                                        scrollBehavior = scrollBehavior,
                                        title = {
                                            Text(
                                                text = BarTitle,
                                                fontSize = 20.sp,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                                },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            }
                                            )  {
                                                Icon(
                                                    imageVector = Icons.Default.Menu,
                                                    contentDescription = "Menu",
                                                    modifier = Modifier.size(28.dp),
                                                    tint = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) { paddingValues ->

                            NavHost(
                                navController = navController,
                                startDestination = CurrentAnimes,
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                composable<CurrentAnimes> {
                                    CurrentAnimesScreen(
                                        animatedVisibilityScope = this,
                                        onAnimeClick = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        }
                                    )

                                }
                                composable<UpcomingAnimes> {
                                    UpcomingAnimesScreen(
                                        animatedVisibilityScope = this,
                                        onAnimeClick = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        }
                                    )
                                }
                                composable<AnimeRoute> {
                                    val args = it.toRoute<AnimeRoute>()
                                    AnimeScreen(
                                        id = args.id,
                                        coverImage = args.coverImage,
                                        animatedVisibilityScope = this,
                                        onCharacterClick = { id, role ->
                                            navController.navigate(
                                                CharacterRoute(
                                                    id = id,
                                                    role = role
                                                )
                                            )
                                        }
                                    )
                                }
                                composable<CharacterRoute> {
                                    val args = it.toRoute<CharacterRoute>()
                                    CharacterScreen(
                                        id = args.id,
                                        animatedVisibilityScope = this,
                                        role = args.role
                                    )
                                }
                                composable<Random> {
                                    RandomAnimeScreen(
                                        onAnimeGet = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        },
                                        animatedVisibilityScope = this
                                    )
                                }
                                composable<Search> {
                                    SearchScreen(
                                        onAnimeClick = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        },
                                        this
                                    )
                                }
                                composable<Filter> {
                                    FilterScreen(
                                        onAnimeClick = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        },
                                        this
                                    )
                                }
                                composable<TopAnime> {
                                    TopAnimesScreen(
                                        animatedVisibilityScope = this,
                                        onAnimeClick = { cover, id ->
                                            navController.navigate(
                                                AnimeRoute(
                                                    id = id,
                                                    coverImage = cover
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                }
            }
        }
    }
}


/*--------------------- Start Navigation Objects ---------------------*/
@Serializable
object CurrentAnimes

@Serializable
object UpcomingAnimes

@Serializable
object Search

@Serializable
object Random

@Serializable
object Filter

@Serializable
object TopAnime

@Serializable
data class AnimeRoute(
    val id: Int,
    val coverImage: String?
)

@Serializable
data class CharacterRoute(val id: Int, val role: String)
/*--------------------- End Navigation Objects ---------------------*/