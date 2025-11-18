package com.shau.spellingdictator

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        setContent {
            MaterialTheme {
                AppUI(
                    onSpeak = { text -> speak(text) }
                )
            }
        }
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun AppUI(onSpeak: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val words = remember { mutableStateListOf("apple", "banana", "orange") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("English Spelling Dictator") })
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter new word") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        words.add(text.trim())
                        text = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Word")
            }

            Spacer(Modifier.height(20.dp))

            LazyColumn {
                items(words) { word ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(word)
                            Button(onClick = { onSpeak(word) }) {
                                Text("Speak")
                            }
                        }
                    }
                }
            }
        }
    }
}
