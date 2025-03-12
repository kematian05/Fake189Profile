package com.kematian.profile189

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CustomRadioButton(selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color = Color.Transparent)
            .clickable { onClick() }
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White)
        ) {
            drawCircle(
                color = if (selected) Color(0xFFFBE502) else Color(0xFF687281),
                radius = 28.dp.toPx(),
                style = if (selected) Stroke(44.dp.toPx()) else Stroke(31.dp.toPx())
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomRadioButtonPreview() {
    CustomRadioButton(selected = false, onClick = {})
}