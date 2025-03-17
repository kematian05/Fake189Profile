package com.kematian.profile189.customViews

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kematian.profile189.R

@Composable
fun CustomTextField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isDeleteVisible: Boolean = false,
    deleteButtonClicked: () -> Unit = {}
) {
    Row {
        Box(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = if (isDeleteVisible) 0.dp else 20.dp
                )
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .border(width = 2.dp, color = Color(0xFFBCBCBC), shape = RoundedCornerShape(12.dp))
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    onSearchQueryChange(it)
                },
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "Axtar",
                        style = TextStyle(
                            color = Color(0xFF3C3938),
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            lineHeight = 28.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF2A2A2A),
                        modifier = Modifier
                            .size(24.dp)
                    )
                },
                trailingIcon = {
                    Row(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                painter = painterResource(R.drawable.clear),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        onSearchQueryChange("")
                                    }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = "Location",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    // Show notifications
                                }
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        if (isDeleteVisible) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = Color(0xFFFF0000),
                modifier = Modifier
                    .padding(start = 8.dp, end = 20.dp)
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        deleteButtonClicked()
                    }
            )
        }
    }
}