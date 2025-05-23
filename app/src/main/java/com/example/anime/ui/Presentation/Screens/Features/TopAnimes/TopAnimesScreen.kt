package com.example.anime.ui.Presentation.Screens.Features.TopAnimes

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopAnimesScreen(
	onAnimeClick: (String, Int) -> Unit,
	animatedVisibilityScope: AnimatedVisibilityScope,
	viewModel: TopViewModel = hiltViewModel()
){
	val connectionState by rememberConnectivityState()
	val isConnected by remember(connectionState) {
		derivedStateOf {
			connectionState === NetworkConnectionState.Available
		}
	}
	if(!isConnected){
		Loading(true)
	}else {
		val topAnimes by viewModel.topAnimes.collectAsStateWithLifecycle()
		LaunchedEffect(Unit) {
			viewModel.getTopAnimes()
		}

		val next by viewModel.next.collectAsStateWithLifecycle()
		AnimeGrid(
			onAnimeClick = onAnimeClick,
			animatedVisibilityScope = animatedVisibilityScope,
			next = next,
			animes = topAnimes,
			onLoadMore = {
				viewModel.getTopAnimes()
			},
			content = { Loading() }
		)
	}
}