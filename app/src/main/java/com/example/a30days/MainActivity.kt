package com.example.a30days

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a30days.data.Data
import com.example.a30days.data.days
import com.example.a30days.ui.theme.DaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaysTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App(this)  // Pass the context here
                }
            }
        }
    }
}

@Composable
fun App(context: Context) {
    var selectedQuote by remember { mutableStateOf(randomQuote()) }
    var favoriteQuotes by remember { mutableStateOf(listOf<Data>()) }

    Scaffold(
        topBar = { AppTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedQuote = randomQuote() // Refresh quote on button press
                },
                containerColor = MaterialTheme.colorScheme.primary  // Set the color same as buttons
            ) {
                Image(
                    painter = painterResource(id = R.drawable.refresh),
                    contentDescription = "refresh"
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            DaysCard(
                data = selectedQuote,
                onFavoriteClick = {
                    favoriteQuotes = favoriteQuotes + selectedQuote
                },
                onShareClick = {
                    shareQuote(context, selectedQuote)
                }
            )
        }
    }
}

@Composable
fun DaysCard(
    modifier: Modifier = Modifier,
    data: Data,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }  // Track favorite state

    Card(
        onClick = { expanded = !expanded },
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Text(text = stringResource(id = data.dayTitle), style = MaterialTheme.typography.bodyLarge)
            Image(
                painter = painterResource(id = data.imageRes),
                contentDescription = stringResource(id = data.dayDescription),
                Modifier.fillMaxWidth()
            )
            if (expanded) {
                Text(text = stringResource(id = data.dayDescription), style = MaterialTheme.typography.bodyLarge)
                Row {
                    Button(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary  // Match button color with FloatingActionButton
                        )
                    ) {
                        Image(
                            painter = painterResource(id = if (isFavorite) R.drawable.favorite_filled else R.drawable.favorite_unfilled),
                            contentDescription = if (isFavorite) "Unfavorite" else "Favorite"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onShareClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary  // Match button color
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "Share"
                        )
                    }
                }
            }
        }
    }
}



fun randomQuote(): Data {
    return days.random() // Select a random quote
}

fun shareQuote(context: Context, data: Data) {
    val shareText = "${context.getString(data.dayTitle)}: ${context.getString(data.dayDescription)}"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, null))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleLarge)
        }
    })
}
