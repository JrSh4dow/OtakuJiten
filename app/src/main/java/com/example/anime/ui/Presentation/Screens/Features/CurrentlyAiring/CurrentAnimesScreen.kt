package com.example.anime.ui.Presentation.Screens.Features.CurrentlyAiring

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.ui.Presentation.Screens.Common.AnimeGrid
import com.example.anime.ui.Presentation.Screens.Common.Loading

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CurrentAnimesScreen(
    onAnimeClick: (String, Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CurrentAnimesViewModel = hiltViewModel()
){
    val connectionState by rememberConnectivityState()
    val isConnected by remember(connectionState) {
        derivedStateOf {
            connectionState === NetworkConnectionState.Available
        }
    }
    if(!isConnected){
        Loading(true)
    }else{
        val currentAnimes by viewModel.currentAnimes.collectAsStateWithLifecycle()

        LaunchedEffect(Unit){
            viewModel.getCurrentAnimes()
        }

        val next by viewModel.next.collectAsStateWithLifecycle()
        AnimeGrid(
            onAnimeClick = onAnimeClick,
            animatedVisibilityScope = animatedVisibilityScope,
            next = next,
            animes = currentAnimes,
            onLoadMore = {
                viewModel.getCurrentAnimes()
            },
            content = { Loading() }
        )
    }
}