package com.example.anime.ui.Presentation.Screens.Common.CharacterScreen


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
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
import com.example.anime.R
import com.example.anime.ui.Presentation.Screens.Common.Loading

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CharacterScreen(
    viewModel: CharacterViewModel = hiltViewModel(),
    id: Int,
    role: String? = "???",
    animatedVisibilityScope: AnimatedVisibilityScope
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
            viewModel.getCharacter(id)
        }

        val character by viewModel.character.collectAsStateWithLifecycle()

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
                        model = character?.images?.jpg?.large_image_url
                            ?: character?.images?.jpg?.image_url,
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
                    if (character != null) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = character!!.name,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.background(
                                    Color(0xFF8AA0D4).copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(20.dp)
                                ).padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Favorite,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp),
                                        tint = Color.Red
                                    )

                                    Text(
                                        text = if (character!!.favorites.toString() == "null") {
                                            "0"
                                        } else {
                                            character!!.favorites.toString()
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = " - ",
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (role == "Main") {
                                            Icons.Rounded.Person
                                        } else {
                                            Icons.Rounded.Groups
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                            .size(30.dp),
                                        tint = if (role == "Main") {
                                            Color(0xFFFF9800)
                                        } else {
                                            Color(0xff2196F3)
                                        }
                                    )

                                    Text(
                                        text = role ?: "???",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                }
                            }


                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Kanji name",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = character!!.name_kanji,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Normal),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Nicknames",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = character!!.nicknames.joinToString(",\n"),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Normal),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "About",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = character!!.about,
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

            }

            if (character == null) {
                Loading()
            }
        }
    }
}