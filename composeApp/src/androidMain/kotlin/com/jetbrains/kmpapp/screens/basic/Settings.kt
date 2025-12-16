package com.jetbrains.kmpapp.screens.basic
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapnook.api.user.UserViewModel

@Composable
fun Settings(onClose: () -> Unit) { // Accept the ViewModel

    var selectedTab by remember { mutableStateOf("Profile") }
    val userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val user = userViewModel.user

    if (user == null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)) { // Use a Box to allow overlaying buttons
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 26.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }

            Column {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Sign in to view settings",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) { // Use a Box to allow overlaying buttons
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 26.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Settings",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                val tabs = listOf("Profile", "Appearance", "Notifications")
                tabs.forEach { tabName ->
                    Button(
                        onClick = { selectedTab = tabName },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = if (selectedTab == tabName) BorderStroke(1.dp, Color.White) else null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == tabName) Color.Gray.copy(alpha = 0.5f) else Color.Transparent
                        )
                    ) {
                        Text(
                            text = tabName,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Content for each tab
            when (selectedTab) {
                "Profile" -> {
                    Text(
                        text = "Profile Settings",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(user?.displayName?: "", color = Color.White, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(user?.email?: "", color = Color.White, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp))
                }
                "Appearance" -> {
                    Text(
                        text = "Appearance Settings",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                "Notifications" -> {
                    Text(
                        text = "Notification Settings",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}