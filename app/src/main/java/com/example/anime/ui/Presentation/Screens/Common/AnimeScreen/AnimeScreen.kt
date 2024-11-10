package com.example.anime.ui.Presentation.Screens.Common.AnimeScreen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.ui.Presentation.Composable.CharacterCard
import com.example.anime.ui.Presentation.Screens.Common.Loading
import com.example.anime.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AnimeScreen(
    viewModel: AnimeViewModel = hiltViewModel(),
    coverImage: String?,
    id: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCharacterClick: (Int, String) -> Unit
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
        LaunchedEffect(key1 = true) {
            viewModel.getAnimeFull(id)
        }

        val anime by viewModel.anime.collectAsStateWithLifecycle()
        val characters by viewModel.characters.collectAsStateWithLifecycle()

        Scaffold { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    AsyncImage(
                        model = coverImage ?: R.drawable.logo,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomEnd = 20.dp,
                                    bottomStart = 20.dp
                                )
                            )
                            .sharedElement(
                                rememberSharedContentState(key = id.toString()),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.logo)
                    )
                }

                item {
                    if (anime != null) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = anime!!.title ?: "",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.background(
                                    Color(0xFF8AA0D4).copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                ).padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp),
                                        tint = Color.Yellow
                                    )
                                    var score = anime!!.score.toString()
                                    Text(
                                        text = if (score.equals("null")) {
                                            "0"
                                        } else {
                                            score
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Text(
                                    text = " - ",
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Tag,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                            .size(25.dp),
                                        tint = Color(0xFF1565C0)
                                    )
                                    var rank = anime!!.rank.toString()
                                    Text(
                                        text = if (rank.equals("null")) {
                                            "0"
                                        } else {
                                            rank
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Text(
                                    text = " - ",
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Favorite,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp),
                                        tint = Color.Red
                                    )
                                    var favorite = anime!!.favorites.toString()
                                    Text(
                                        text = if (favorite.equals("null")) {
                                            "0"
                                        } else {
                                            favorite
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.background(
                                    Color(0xFF8AA0D4).copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(10.dp)
                                ).padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = anime!!.type.toString() + ", " + (anime!!.aired!!.from?.split(
                                        "-"
                                    )?.first() ?: "YYYY"),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = anime!!.status.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when (anime!!.status) {
                                        "Finished Airing" -> Color(0xFFC81D25)
                                        "Currently Airing" -> MaterialTheme.colorScheme.onBackground
                                        else -> Color.Yellow
                                    }
                                )
                                var ep = if (anime!!.episodes != null) {
                                    anime!!.episodes.toString()
                                } else {
                                    "??"
                                }
                                Text(

                                    text = ep + " ep, " + anime!!.duration?.let {
                                        val regex = """(\d+\s*min)""".toRegex()
                                        val matchResult = regex.find(it)
                                        matchResult?.value ?: "?? min"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }


                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = ("Studio"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = anime!!.studios?.joinToString(", ") { it.name.toString() }
                                        ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Genres",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = anime!!.genres?.joinToString(", ") { it.name.toString() }
                                        ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Demographic",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = anime!!.demographics?.joinToString(", ") { it.name.toString() }
                                        ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Synopsis",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = anime!!.synopsis ?: "",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Normal),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }

                    }
                }
                item {
                    if (characters != null) {
                        Column(
                            Modifier.fillMaxSize()
                                .padding(top = 20.dp)
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "Characters",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(100.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(16.dp)
                                    .height(700.dp)
                            ) {
                                items(characters) { character ->
                                    CharacterCard(
                                        Character = character,
                                        onClick = {
                                            onCharacterClick(
                                                character.character.mal_id,
                                                character.role
                                            )
                                        },
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                }
                            }
                        }
                    } else {
                        Loading()
                    }
                }
            }

            if (anime == null) {
                Loading()
            }
        }
    }
}