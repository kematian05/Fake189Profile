package com.kematian.profile189.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import com.kematian.profile189.customViews.CustomTextField
import com.kematian.profile189.customViews.LocationItem
import com.kematian.profile189.R
import kotlinx.coroutines.delay

data class LocationItemData(
    val name: String,
    val location: String,
    @DrawableRes val icon: Int
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMotionApi::class)
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

    BackHandler(enabled = true) {
        onClose()
    }

    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(progress) {
        while (progress < 1f) {
            progress += 0.2f
            if (progress >= 1f) {
                progress = 1f
            }
            delay(200)
        }
    }

    Scaffold {
        MotionLayout(
            start = ConstraintSet {
                val topBox = createRefFor("topBox")
                val bottomBox = createRefFor("bottomBox")

                constrain(topBox) {
                    bottom.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }

                constrain(bottomBox) {
                    top.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                }
            },
            end = ConstraintSet {
                val topBox = createRefFor("topBox")
                val bottomBox = createRefFor("bottomBox")

                constrain(topBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }

                constrain(bottomBox) {
                    top.linkTo(topBox.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                }
            },
            progress = progress,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {
            Box(
                modifier = Modifier
                    .layoutId("topBox")
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(color = Color(0xFFFEFEFE))
                    .zIndex(1f)
            ) {
                Column {
                    Box(
                        modifier = Modifier
//                        .padding(top = 20.dp)
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
                            },
                            isDeleteVisible = searchList.size > 2,
                            deleteButtonClicked = {
                                if (searchList.size > 2) {
                                    searchList.removeAt(index)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Box(
                modifier = Modifier
                    .layoutId("bottomBox")
                    .padding(top = 8.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(color = Color(0xFFFEFEFE))
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
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(onClose = {})
}