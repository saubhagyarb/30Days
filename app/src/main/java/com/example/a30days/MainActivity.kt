package com.example.a30days

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a30days.data.Data
import com.example.a30days.data.days
import com.example.a30days.ui.theme.DaysTheme
import com.example.a30days.ui.theme.backgroundLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaysTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }
}

@Composable
fun DaysCard(modifier: Modifier = Modifier, data: Data){
    var state by remember {
        mutableStateOf(false)
    }
    Card(onClick = { state = !state },
        modifier = modifier
            .padding(8.dp)) {
        Column(modifier = modifier
            .padding(10.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )) {
            Text(text = stringResource(id = data.day),
                style = MaterialTheme.typography.labelMedium)
            Text(text = stringResource(id = data.dayTitle),
                style = MaterialTheme.typography.bodyLarge)
            Image(painter = painterResource(id = data.imageRes),
                contentDescription = stringResource(id = data.dayDescription),
                Modifier.fillMaxWidth())
            if (state){
                Text(text = stringResource(id = data.dayDescription),
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ListDays(days : List<Data>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier) {
        items(days){ data ->
            DaysCard(data = data)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(title = {
        Row(modifier = modifier
            .fillMaxWidth(),
            Arrangement.Start,
            ) {
            Text(text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge)
        }
    })
}

@Composable
fun App(){
    Scaffold(topBar = { AppTopBar() }) { paddingValues ->
        ListDays(days = days,
            modifier = Modifier.padding(paddingValues))
    }
}
