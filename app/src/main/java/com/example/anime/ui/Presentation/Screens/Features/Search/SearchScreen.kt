package com.example.anime.ui.Presentation.Screens.Features.Search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.ui.Presentation.Screens.Common.AnimeGrid
import com.example.anime.ui.Presentation.Screens.Common.Loading
import com.example.anime.ui.Presentation.Screens.Common.Nothing
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    onAnimeClick: (String, Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: SearchViewModel = hiltViewModel()
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
        val search by viewModel.search.collectAsStateWithLifecycle()
        val animes by viewModel.animes.collectAsStateWithLifecycle()
        val coroutineScope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(focusRequester) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(search) {
            if (search.isNotBlank()) {
                kotlinx.coroutines.delay(600)
                viewModel.searchAnime()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { viewModel.onSearch(it) },
                label = {
                    Text(
                        text = "Anime name:",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 5.dp)
                    .height(65.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.onSearch("")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.graphicsLayer(scaleX = 1.5f, scaleY = 1.5f)
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = "Type anime name...",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = Color.Gray,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        coroutineScope.launch {
                            if (search.isNotBlank()) {
                                viewModel.searchAnime()
                            }
                        }
                    }
                )
            )

            val next by viewModel.next.collectAsStateWithLifecycle()
            AnimeGrid(
                onAnimeClick = onAnimeClick,
                animatedVisibilityScope = animatedVisibilityScope,
                next = next,
                animes = animes,
                onLoadMore = {
                    viewModel.searchAnime()
                },
                content = { Nothing() }
            )
        }
    }
}