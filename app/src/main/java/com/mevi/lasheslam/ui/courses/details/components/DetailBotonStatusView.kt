package com.mevi.lasheslam.ui.courses.details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.utils.Constants

@Composable
fun DetailBotonStatusView(status: String, onAddToCalendar: () -> Unit, onCreateCourseRequest: () -> Unit) {
    val buttonText = when (status) {
        Constants.Course.STATUS_REQUESTED -> stringResource(R.string.register_course)
        Constants.Course.STATUS_PANDING -> stringResource(R.string.request_pending)
        Constants.Course.STATUS_ACCEPTED -> stringResource(R.string.add_to_calendar)
        else -> stringResource(R.string.register_course)
    }

    val calendarIcon = when (status) {
        Constants.Course.STATUS_ACCEPTED -> Icons.Outlined.EventAvailable
        else -> null
    }

    val isEnabled = status != Constants.Course.STATUS_PANDING

    ElevatedButton(
        onClick = {
            when (status) {
                Constants.Course.STATUS_REQUESTED -> {
                    onCreateCourseRequest()
                }

                Constants.Course.STATUS_ACCEPTED -> {
                    onAddToCalendar()
                }
            }
        },
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(55.dp)
    ) {
        if (calendarIcon != null) {
            Icon(
                imageVector = calendarIcon,
                contentDescription = stringResource(R.string.add_to_calendar),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(buttonText)
    }
}