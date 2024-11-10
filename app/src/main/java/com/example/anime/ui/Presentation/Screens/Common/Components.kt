package com.example.anime.ui.Presentation.Screens.Common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anime.domain.model.Data
import com.example.anime.ui.Presentation.Composable.AnimeCard
import com.example.anime.R
import kotlinx.coroutines.delay

@Composable
fun CardLoad(
	onClick: () -> Unit
){
	Box(
		modifier = Modifier.size(250.dp)
			.clip(shape = RoundedCornerShape(100))
			.background(Color.Transparent),
		contentAlignment = Alignment.Center,

		){
		Image(
			painter = painterResource(id = R.drawable.more),
			contentDescription = "Loading",
			contentScale = ContentScale.Crop,
			modifier = Modifier.size(160.dp)
				.clickable {
					onClick()
				}
		)
	}
}


@Composable
fun Loading(
	noInternet: Boolean = false
){
	var dotCount by remember { mutableIntStateOf(1) }

	LaunchedEffect(Unit) {
		while (true) {
			dotCount = (dotCount % 3) + 1
			delay(500)
		}
	}

	val loadingText = ". ".repeat(dotCount)

	Box(
		modifier = Modifier.fillMaxSize()
			.background(MaterialTheme.colorScheme.secondary),
		contentAlignment = Alignment.Center
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = loadingText,
				style = MaterialTheme.typography.bodyLarge,
				color = Color.White,
				fontWeight = FontWeight.Bold,
				fontSize = 80.sp
			)
			if(noInternet){
				Icon(
					imageVector = Icons.Default.WifiOff,
					contentDescription = "No internet connection",
					tint = Color.White,
					modifier = Modifier.size(150.dp)
				)
			}
		}
	}

	// Otra manera de mostrar el estado de carga
//	Box(
//		modifier = Modifier.fillMaxSize()
//			.background(MaterialTheme.colorScheme.secondary),
//		contentAlignment = Alignment.Center
//	){
//		CircularProgressIndicator(
//			modifier = Modifier.width(75.dp),
//			color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
//			trackColor = MaterialTheme.colorScheme.surface,
//			strokeWidth = 6.dp
//		)
//	}

}

@Composable
fun Nothing(){
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "Nothing to show",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)
	}
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AnimeGrid(
	onAnimeClick: (String, Int) -> Unit,
	animatedVisibilityScope: AnimatedVisibilityScope,
	next: Boolean,
	animes: List<Data>,
	onLoadMore: () -> Unit,
	content: @Composable () -> Unit
){
	val state = rememberLazyGridState()
	val isEnd by remember {
		derivedStateOf {
			val lastVisibleItemIndex = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index
			val totalItemsCount = state.layoutInfo.totalItemsCount
			lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - 7
		}
	}

	LaunchedEffect(state, next) {
		snapshotFlow { isEnd }
			.collect { reachedEnd ->
				if (reachedEnd && next) {
					onLoadMore()
				}
			}
	}
	AnimatedContent(
		targetState = animes.isEmpty(),
		label = "",
		modifier = Modifier.fillMaxSize()
			.background(MaterialTheme.colorScheme.secondary)
	) { isEmpty ->
		if (isEmpty) {
			content()
		} else {
			LazyVerticalGrid(
				state = state,
				columns = GridCells.Adaptive(minSize = 100.dp),
				contentPadding = PaddingValues(10.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				items(animes) { anime ->
					AnimeCard(
						anime = anime,
						onClick = {
							anime.mal_id?.let {
								anime.images?.jpg?.let { it1 ->
									onAnimeClick(
										it1.large_image_url ?: it1.image_url,
										it
									)
								}
							}
						},
						animatedVisibilityScope = animatedVisibilityScope
					)
				}
			}
		}
	}
}