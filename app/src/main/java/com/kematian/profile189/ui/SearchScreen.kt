package com.kematian.profile189.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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

data class LocationItemData(
    val name: String,
    val location: String,
    @DrawableRes val icon: Int
)

@SuppressLint("RememberReturnType", "UseOfNonLambdaOffsetOverload")
@Composable
fun SearchScreen(
    onClose: () -> Unit,
) {
    val searchList = remember { mutableStateListOf("", "") }
    val locationList = remember {
        mutableStateListOf(
            LocationItemData(
                name = "Park Bulvar Ticarət Mərkəzi",
                location = "Bakı, Azərbaycan",
                icon = R.drawable.shopping
            ),
            LocationItemData(
                name = "Bakı Dövlət Universiteti",
                location = "Bakı, Azərbaycan",
                icon = R.drawable.school
            ),
            LocationItemData(
                name = "Ağ Şəhər Bulvarı",
                location = "Bakı, Azərbaycan",
                icon = R.drawable.non_binary
            )
        )
    }

    val topBoxOffset = remember { Animatable(-500f) }
    val bottomBoxOffset = remember { Animatable(500f) }

    LaunchedEffect(Unit) {
        topBoxOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 50, easing = FastOutSlowInEasing)
        )
        bottomBoxOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 50, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFF8F8F8))
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
//                .systemBarsPadding()
                .fillMaxWidth()
//                .shadow(5.dp, ambientColor = Color(0xFFFEFEFE))
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(color = Color(0xFFFEFEFE))
                .offset(y = topBoxOffset.value.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Ünvanlar",
                        style = TextStyle(
                            fontSize = 32.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3C3938)
                        ),
                        modifier = Modifier
                            .padding(top = 29.dp)
                            .align(Alignment.Center)
                    )

                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = "Close",
                        tint = Color(0xFFBCBCBC),
                        modifier = Modifier
                            .padding(start = 20.dp, top = 30.dp)
                            .align(Alignment.CenterStart)
                            .size(24.dp)
                            .clickable {
                                onClose()
                            }
                    )

                    Icon(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = "Add",
                        tint = Color(0xFF2A2A2A),
                        modifier = Modifier
                            .padding(end = 20.dp, top = 24.dp)
                            .align(Alignment.CenterEnd)
                            .size(36.dp)
                            .clickable {
                                if (searchList.size < 5) {
                                    searchList.add("")
                                }
                            }
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
                repeat(searchList.size) { index ->
                    CustomTextField(
                        searchQuery = searchList[index],
                        onSearchQueryChange = {
                            searchList[index] = it
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
//                .shadow(5.dp, ambientColor = Color(0xFFFEFEFE))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color = Color(0xFFFEFEFE))
                .offset(y = bottomBoxOffset.value.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                repeat(locationList.size) { index ->
                    LocationItem(
                        name = locationList[index].name,
                        location = locationList[index].location,
                        icon = locationList[index].icon
                    )
                    if (index != locationList.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFBCBCBC)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                start = 20.dp,
                end = 20.dp,
            )
            .fillMaxWidth()
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
}

@Composable
fun LocationItem(name: String, location: String, @DrawableRes icon: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(top = 20.dp, bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 44.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFFCFCFC)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 24.dp, height = 24.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF14110F)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = location,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF9B9B9B)
                )
            )
        }
        Icon(
            painter = painterResource(R.drawable.favourite),
            contentDescription = "Favorite",
            tint = Color(0xFF2A2A2A),
            modifier = Modifier
                .padding(end = 8.dp)
                .size(width = 24.dp, height = 24.dp)
                .clickable { /* TODO */ }
        )
    }
}