package com.example.anime.ui.Presentation.Screens.Features.Random

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.R
import com.example.anime.ui.Presentation.Screens.Common.Loading

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RandomAnimeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: RandomViewModel = hiltViewModel(),
    onAnimeGet: ((String, Int)->Unit)
) {
    val connectionState by rememberConnectivityState()
    val isConnected by remember(connectionState) {
        derivedStateOf {
            connectionState === NetworkConnectionState.Available
        }
    }
    if(!isConnected){
        Loading(true)
    }else {
        val anime by viewModel.anime.collectAsStateWithLifecycle()
        val isAnimeLoaded by viewModel.isAnimeLoaded.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = true) {
            viewModel.getRandomAnime()
        }
        if (anime != null && isAnimeLoaded) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    onClick = {
                        onAnimeGet(
                            anime!!.images?.jpg?.large_image_url ?: anime!!.images?.jpg?.image_url
                            ?: "",
                            anime!!.mal_id ?: 0
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.6f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        Modifier.align(Alignment.CenterHorizontally)
                            .padding(top = 15.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = anime!!.images?.jpg?.image_url ?: R.drawable.logo,
                            contentDescription = "anime image",
                            modifier = Modifier
                                .size(400.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .sharedElement(
                                    rememberSharedContentState(key = anime!!.mal_id ?: ""),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.logo)
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    Color(0xFF8AA0D4).copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "rating",
                                tint = Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                            var rating = anime!!.score.toString()
                            Text(
                                text =
                                if (rating.equals("null")) {
                                    "0"
                                } else {
                                    rating
                                },
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        anime!!.title?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        } else {
            Loading()
        }
    }
}
