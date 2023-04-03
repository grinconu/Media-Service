package com.ziecorp.mediaservicetest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlayerScreen(viewModel: AudioPlayerViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "ExoPlayer con Jetpack Compose")

        val progress = viewModel.progress

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
        )

        Button(
            onClick = {
                if (viewModel.isPlaying) {
                    viewModel.pause()
                } else {
                    viewModel.play()
                }
            },
        ) {
            Text(text = if (viewModel.isPlaying) "Pausar" else "Reproducir")
        }

        Button(
            onClick = { viewModel.fastForward(10000) },
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(text = "Adelantar 10s")
        }

        Button(
            onClick = { viewModel.rewind(10000) },
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(text = "Retroceder 10s")
        }

        var volume by remember { mutableStateOf(1f) }

        Slider(
            value = volume,
            onValueChange = { newVolume ->
                volume = newVolume
                viewModel.setVolume(newVolume)
            },
            modifier = Modifier.padding(top = 16.dp),
            valueRange = 0f..1f,
        )
    }
}
