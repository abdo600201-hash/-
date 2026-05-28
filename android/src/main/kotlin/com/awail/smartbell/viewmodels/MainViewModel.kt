package com.awail.smartbell.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awail.smartbell.data.database.SmartBellDatabase
import com.awail.smartbell.data.models.*
import com.awail.smartbell.services.AnnouncementService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _scheduleState = androidx.compose.runtime.mutableStateOf<ScheduleUIState>(ScheduleUIState())
    val scheduleState: StateFlow<ScheduleUIState> = _scheduleState.asStateFlow()

    fun loadDefaultSchedule(context: Context) {
        viewModelScope.launch {
            val defaultSchedule = getDefaultSchedule()
            _scheduleState.value = ScheduleUIState(items = defaultSchedule)
        }
    }

    fun announce(text: String, context: Context) {
        val intent = Intent(context, AnnouncementService::class.java).apply {
            action = "ANNOUNCE"
            putExtra("announcement_text", text)
            putExtra("use_ai", false)
        }
        context.startService(intent)
    }

    fun startAutoMode(context: Context) {
        val intent = Intent(context, AnnouncementService::class.java).apply {
            action = "START_AUTO_MODE"
        }
        context.startService(intent)
    }

    fun stopAutoMode(context: Context) {
        val intent = Intent(context, AnnouncementService::class.java).apply {
            action = "STOP_AUTO_MODE"
        }
        context.startService(intent)
    }

    private fun getDefaultSchedule(): List<ScheduleItem> {
        return listOf(
            ScheduleItem(
                id = "l1",
                type = ScheduleType.LESSON,
                icon = "📖",
                name = "الحصة الأولى",
                startTime = "07:30",
                endTime = "08:15",
                announcementText = "تنبيه: بدء الحصة الأولى. يرجى من جميع الطلاب التوجه إلى فصولهم والاستعداد."
            ),
            ScheduleItem(
                id = "l2",
                type = ScheduleType.LESSON,
                icon = "✏️",
                name = "الحصة الثانية",
                startTime = "08:15",
                endTime = "09:00",
                announcementText = "تنبيه: بدء الحصة الثانية. نتمنى للجميع تركيزاً وتوفيقاً."
            ),
            ScheduleItem(
                id = "b1",
                type = ScheduleType.BREAK,
                icon = "☕",
                name = "فسحة البرعم",
                startTime = "09:00",
                endTime = "09:20",
                announcementText = "حان الآن موعد الفسحة الخاصة بالأطفال والبراعم. نتمنى لكم وقتاً ممتعاً."
            ),
            ScheduleItem(
                id = "e1",
                type = ScheduleType.END_DAY,
                icon = "🏁",
                name = "نهاية الدوام",
                startTime = "12:15",
                endTime = "12:15",
                announcementText = "انتهى الدوام المدرسي لهذا اليوم. رافقتكم السلامة وعودة آمنة لمنازلكم."
            )
        )
    }
}
