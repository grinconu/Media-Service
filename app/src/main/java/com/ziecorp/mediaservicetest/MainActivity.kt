package com.ziecorp.mediaservicetest

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Audio
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ziecorp.mediaservicetest.ui.theme.MediaServiceTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaServiceTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val viewModel: AudioPlayerViewModel = AudioPlayerViewModel()
                    viewModel.initializePlayer(this, Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"))

                    AudioPlayerScreen(viewModel)
                }
            }
        }
    }
}
