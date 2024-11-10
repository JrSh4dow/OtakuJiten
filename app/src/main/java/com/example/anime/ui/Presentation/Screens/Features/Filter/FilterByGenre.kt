package com.example.anime.ui.Presentation.Screens.Features.Filter

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.anime.Connection.NetworkConnectionState
import com.example.anime.Connection.rememberConnectivityState
import com.example.anime.ui.Presentation.Screens.Common.AnimeGrid
import com.example.anime.ui.Presentation.Screens.Common.Loading
import com.example.anime.ui.Presentation.Screens.Common.Nothing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.FilterScreen(
    onAnimeClick: (String, Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: FilterViewModel = hiltViewModel()
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
        val genres = arrayOf(
            "Action",
            "Adventure",
            "Avant Garde",
            "Award Winning",
            "Comedy",
            "Drama",
            "Fantasy",
            "Gourmet",
            "Horror",
            "Mystery",
            "Romance",
            "Sci-Fi",
            "Slice of Life",
            "Sports",
            "Suspense",
            "Supernatural",
            "Adult Cast",
            "Anthropomorphic",
            "CGDCT",
            "Childcare",
            "Combat Sports",
            "Crossdressing",
            "Delinquents",
            "Detective",
            "Educational",
            "Gag Humor",
            "Gore",
            "Harem",
            "High Stakes Game",
            "Historical",
            "Idols (Female)",
            "Idols (Male)",
            "Isekai",
            "Iyashikei",
            "Love Polygon",
            "Love Status Quo",
            "Magical Sex Shift",
            "Mahou Shoujo",
            "Martial Arts",
            "Mecha",
            "Medical",
            "Military",
            "Music",
            "Mythology",
            "Organized Crime",
            "Otaku Culture",
            "Parody",
            "Performing Arts",
            "Pets",
            "Psychological",
            "Racing",
            "Reincarnation",
            "Samurai",
            "School",
            "Showbiz",
            "Space",
            "Strategy Game",
            "Super Power",
            "Survival",
            "Team Sports",
            "Time Travel",
            "Urban Fantasy",
            "Vampire",
            "Video Game",
            "Villainess",
            "Visual Arts",
            "Workplace",
            "Josei",
            "Kids",
            "Seinen",
            "Shoujo",
            "Shounen"
        )
        val animes by viewModel.filteredAnimes.collectAsStateWithLifecycle()
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf("") }
        var text by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        var filteredOptions by remember { mutableStateOf(emptyList<String>()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(focusRequester) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            scope.launch {
                                delay(400)
                                filteredOptions =
                                    genres.filter { it.contains(text, ignoreCase = true) }
                                expanded = filteredOptions.isNotEmpty()
                            }
                        },
                        readOnly = false,
                        label = {
                            Text(
                                text = "Start typing the genre",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .menuAnchor(
                                type = MenuAnchorType.PrimaryEditable,
                                enabled = true
                            )
                            .focusRequester(focusRequester),
                        maxLines = 1,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = Color.DarkGray,
                            cursorColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    if (filteredOptions.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredOptions.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    onClick = {
                                        selectedText = item
                                        text = item
                                        viewModel.setFilter(item)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                IconButton(
                    onClick = {
                        viewModel.filterAnime()
                    },
                    modifier = Modifier.size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.error,
                        disabledContainerColor = Color.Gray
                    ),
                    enabled = selectedText.isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            }
            val next by viewModel.next.collectAsStateWithLifecycle()
            AnimeGrid(
                onAnimeClick = onAnimeClick,
                animatedVisibilityScope = animatedVisibilityScope,
                next = next,
                animes = animes,
                onLoadMore = {
                    viewModel.filterAnime()
                },
                content = { Nothing() }
            )
        }
    }
}