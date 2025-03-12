package com.kematian.profile189

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePickerDialog(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var showYearSelection by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                            .lowercase(),
                        color = Color(0xFFFFEB3B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable { showYearSelection = true }
                            .weight(1f)
                    )

                    Row {
                        IconButton(
                            onClick = {
                                currentYearMonth = currentYearMonth.minusMonths(1)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month",
                                tint = Color(0xFFFFEB3B)
                            )
                        }

                        IconButton(
                            onClick = {
                                currentYearMonth = currentYearMonth.plusMonths(1)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = Color(0xFFFFEB3B)
                            )
                        }
                    }
                }

                if (showYearSelection) {
                    YearSelectionDialog(
                        selectedYear = currentYearMonth.year,
                        onYearSelected = { year ->
                            currentYearMonth = YearMonth.of(year, currentYearMonth.monthValue)
                            showYearSelection = false
                        },
                        onDismiss = { showYearSelection = false }
                    )
                } else {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
                        daysOfWeek.forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val firstDayOfMonth = currentYearMonth.atDay(1)
                    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                    val daysInMonth = currentYearMonth.lengthOfMonth()

                    val prevMonth = currentYearMonth.minusMonths(1)
                    val daysInPrevMonth = prevMonth.lengthOfMonth()

                    Column {
                        var dayCount = 1 - firstDayOfWeek

                        repeat(6) {
                            if (dayCount <= daysInMonth) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    repeat(7) {
                                        val day = dayCount++

                                        if (day <= 0) {
                                            val prevMonthDay = daysInPrevMonth + day
                                            DayCell(
                                                day = prevMonthDay.toString(),
                                                isSelected = false,
                                                isCurrentMonth = false,
                                                onClick = { }
                                            )
                                        } else if (day <= daysInMonth) {
                                            val currentDate = currentYearMonth.atDay(day)
                                            val isSelected = currentDate == selectedDate

                                            DayCell(
                                                day = day.toString(),
                                                isSelected = isSelected,
                                                isCurrentMonth = true,
                                                onClick = {
                                                    selectedDate = currentDate
                                                    onDateSelected(currentDate)
                                                }
                                            )
                                        } else {
                                            val nextMonthDay = day - daysInMonth
                                            DayCell(
                                                day = nextMonthDay.toString(),
                                                isSelected = false,
                                                isCurrentMonth = false,
                                                onClick = { }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearSelectionDialog(
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentYear = LocalDate.now().year
    val years = (1900..currentYear).reversed().toList()
    val columns = 3

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Select Year",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.height(300.dp)) {
                    LazyColumn {
                        items(years.chunked(columns)) { rowYears ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowYears.forEach { year ->
                                    YearItem(
                                        year = year,
                                        isSelected = year == selectedYear,
                                        onClick = { onYearSelected(year) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearItem(year: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFFFEB3B) else Color.Transparent)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = year.toString(),
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun DayCell(
    day: String,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Color(0xFFFFEB3B)
                    else -> Color.Transparent
                }
            )
            .then(
                if (isSelected) {
                    Modifier
                } else {
                    Modifier.border(
                        width = if (isCurrentMonth) 0.dp else 0.dp,
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                }
            )
            .clickable(enabled = isCurrentMonth) { onClick() }
    ) {
        Text(
            text = day,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isSelected -> Color.White
                isCurrentMonth -> Color.Black
                else -> Color.LightGray
            },
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onOpenDialog: () -> Unit
) {
    val displayDate = selectedDate?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: ""

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFFBCBCBC),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onOpenDialog)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayDate.ifEmpty { "MM/DD/YY" },
                color = if (displayDate.isEmpty()) Color(0xFFBCBCBC) else Color.Black
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Open Date Picker",
                tint = Color(0xFFBCBCBC)
            )
        }
    }
}