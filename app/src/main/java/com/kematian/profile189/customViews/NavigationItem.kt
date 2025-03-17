package com.kematian.profile189.customViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kematian.profile189.R
import com.kematian.profile189.ui.NavItem

@Composable
fun NavigationItem(item: NavItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(48.dp)
                .background(Color(0xFFF1F1F1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
            )
        }
        Text(
            text = item.title,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .padding(start = 14.dp)
                .weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.arrow_forward),
            contentDescription = "Navigate",
        )
    }
}