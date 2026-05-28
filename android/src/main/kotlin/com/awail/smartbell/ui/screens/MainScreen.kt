package com.awail.smartbell.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awail.smartbell.viewmodels.MainViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val currentTime by remember { mutableStateOf(LocalTime.now()) }
    val context = LocalContext.current
    var audioEnabled by remember { mutableStateOf(false) }
    var autoModeActive by remember { mutableStateOf(false) }
    var customText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadDefaultSchedule(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0b3b4a))
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // Header
        SchoolHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // Clock Display
        ClockDisplay()

        Spacer(modifier = Modifier.height(16.dp))

        // Control Card
        ControlCard(
            audioEnabled = audioEnabled,
            autoModeActive = autoModeActive,
            onAudioToggle = { audioEnabled = it },
            onAutoModeToggle = { autoModeActive = it },
            onAnnounce = { viewModel.announce(it, context) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Settings Panel
        SettingsPanel()

        Spacer(modifier = Modifier.height(16.dp))

        // Schedule Grid
        ScheduleSection(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Messages
        QuickMessagesSection(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Announcement
        CustomAnnouncementCard(
            text = customText,
            onTextChange = { customText = it },
            onAnnounce = {
                viewModel.announce(customText, context)
                customText = ""
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SchoolHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFf9b81b), shape = RoundedCornerShape(30.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏫 مدارس الأوائل الأهلية النموذجية",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1e3a2f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "الجرس الناطق الذكي v1.0",
            fontSize = 14.sp,
            color = Color(0xFF2c2b26),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ClockDisplay() {
    var currentTime by remember { mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) }
    var currentDate by remember { mutableStateOf("جاري التحميل...") }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            kotlinx.coroutines.delay(1000)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0b3b4a)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1a4a5a))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentTime,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFf9b81b)
            )
            Text(
                text = "السبت، 28 مايو 2026",
                fontSize = 14.sp,
                color = Color(0xFFcbd5e1),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "⏳ القادم: الحصة الأولى بعد 15 دقيقة",
                fontSize = 12.sp,
                color = Color(0xFFe2e8f0),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ControlCard(
    audioEnabled: Boolean,
    autoModeActive: Boolean,
    onAudioToggle: (Boolean) -> Unit,
    onAutoModeToggle: (Boolean) -> Unit,
    onAnnounce: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0b3b4a)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1a4a5a))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    onClick = { },
                    label = {
                        Text(
                            if (audioEnabled) "🔊 الصوت مفعل" else "🔇 الصوت غير مفعل",
                            color = Color.White
                        )
                    },
                    colors = ChipDefaults.chipColors(
                        containerColor = if (audioEnabled) Color(0xFF22c55e) else Color(0xFFef4444)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAudioToggle(!audioEnabled) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e5f7a))
                ) {
                    Text("🎤 تفعيل الصوت")
                }

                Button(
                    onClick = { onAutoModeToggle(!autoModeActive) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (autoModeActive) Color(0xFFb91c1c) else Color(0xFFb86f2c)
                    )
                ) {
                    Text("⏰ التلقائي")
                }
            }
        }
    }
}

@Composable
fun SettingsPanel() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0b3b4a)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1a4a5a))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⚙️ إعدادات النظام",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFf9b81b)
            )
            // Settings options can be added here
        }
    }
}

@Composable
fun ScheduleSection(viewModel: MainViewModel) {
    Text(
        "📚 الحصص الدراسية",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFf9b81b),
        modifier = Modifier.padding(vertical = 8.dp)
    )
    // Grid of schedule cards can be added here
}

@Composable
fun QuickMessagesSection(viewModel: MainViewModel) {
    Text(
        "🎙️ الرسائل السريعة",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFf9b81b),
        modifier = Modifier.padding(vertical = 8.dp)
    )
    // Quick message buttons can be added here
}

@Composable
fun CustomAnnouncementCard(
    text: String,
    onTextChange: (String) -> Unit,
    onAnnounce: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0b3b4a)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1a4a5a))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "✍️ إعلان مخصص",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFf9b81b)
            )
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("اكتب الإعلان...") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            Button(
                onClick = onAnnounce,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e5f7a))
            ) {
                Text("📢 إذاعة الإعلان")
            }
        }
    }
}
