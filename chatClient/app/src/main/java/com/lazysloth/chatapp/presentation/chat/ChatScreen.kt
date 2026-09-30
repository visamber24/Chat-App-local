package com.lazysloth.chatapp.presentation.chat

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.lazysloth.chatapp.domain.model.MessageType
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    username: String?, viewModel: ChatViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (username != null) {
                        Text(
                            text = "Welcome $username",

                            )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    titleContentColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current
        LaunchedEffect(key1 = true) {
            viewModel.toastEvent.collectLatest { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

            }
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    viewModel.connectToChat()
                } else if (event == Lifecycle.Event.ON_STOP) {
                    viewModel.disconnect()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        val state = viewModel.state.collectAsState().value
//        LaunchedEffect(Unit) {
//            Log.d("UI", "ChatScreen entered composition")
//        }
        Log.d("UI", "ChatScreen recomposed: ${state.messageUi}")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .imePadding(),


            ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                items(state.messageUi) { message ->

//                    when(state.messageType)
//                    {
//                        MessageType.Gif -> {
//                            AsyncImage(
//                                model = message.url,
//                                contentDescription = null
//                            )
//                        }
//                        MessageType.Text -> {
//
//                        }
//                        MessageType.Image -> {}
//                    }
                    val isOwnMessage = message.username == username
                    Box(
                        contentAlignment = if (isOwnMessage) Alignment.CenterEnd
                        else Alignment.CenterStart, modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .drawBehind {
                                    val cornerRadius = 10.dp.toPx()
                                    val triangleHeight = 20.dp.toPx()
                                    val triangleWidth = 25.dp.toPx()
                                    val trianglePath = androidx.compose.ui.graphics.Path().apply {
                                        if (isOwnMessage) {
                                            moveTo(size.width, size.height - cornerRadius)
                                            lineTo(size.width, size.height + triangleHeight)
                                            lineTo(
                                                size.width - triangleWidth,
                                                size.height - cornerRadius
                                            )
                                            close()
                                        } else {
                                            moveTo(0f, size.height - cornerRadius)
                                            lineTo(0f, size.height + triangleHeight)
                                            lineTo(
                                                triangleWidth - triangleWidth,
                                                size.height - cornerRadius
                                            )
                                            close()
                                        }
                                    }
                                    drawPath(
                                        path = trianglePath,
                                        color = if (isOwnMessage) Color.Green else message.username.toColor()
                                    )
                                }
                                .background(
                                    color = if (isOwnMessage) Color.Green else message.username.toColor(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)) {
                            Text(
                                text = message.username,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = message.text, color = Color.White
                            )
                            Text(
                                text = message.formattedTime,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End)
                            )

                        }

                    }
                    Spacer(Modifier.height(16.dp))
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = viewModel.messageText.value,
                    onValueChange = viewModel::onMessageChange,
                    placeholder = {
                        Text(
                            text = "Enter a message"
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedContainerColor = Color.Green
                    ),
//                keyboardActions = KeyboardActions(onDone = { viewModel.sendMessage() }),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            enabled = true,
                            onClick = {})

                )
                IconButton(onClick = viewModel::sendMessage, colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send message "
                    )
                }
            }
        }
    }

}

fun String.toColor(): Color {
    val random = kotlin.random.Random(hashCode())
    return Color(
        red = random.nextInt(50, 256),
        green = random.nextInt(50, 256),
        blue = random.nextInt(50, 256)
    )
}