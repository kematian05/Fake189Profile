package com.kematian.profile189.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.rememberAsyncImagePainter
import com.kematian.profile189.customViews.NavigationItem
import com.kematian.profile189.R
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    onProfileClicked: () -> Unit,
    onBackPress: () -> Unit,
    imageUri: String? = null,
    username: String = "Vusat Orujov"
) {

    val navItems = listOf(
        NavItem("Ödəniş növləri", R.drawable.payment),
        NavItem("Korporativ", R.drawable.koporativ),
        NavItem("Gediş tarixçəsi", R.drawable.ride_history),
        NavItem("Promokod", R.drawable.promo_code),
        NavItem("Dəstək", R.drawable.questions),
    )

    val offerItems = listOf(
        OfferItem("Apar Balans ilə\nScooter ödənişləri\n20% daha ucuz!", R.drawable.img, true),
        OfferItem("Üstünlüklərimiz", R.drawable.img, true),
        OfferItem("Yeniliklər", R.drawable.img, false),
    )

    val coroutineScope = rememberCoroutineScope()

    var test by remember { mutableStateOf(true) }
    BackHandler(enabled = test) {
        coroutineScope.launch {
            onBackPress()
            test = false
        }
    }

    Column(
        modifier = Modifier
            .systemBarsPadding()
//            .fillMaxWidth()
            .fillMaxWidth(0.84f)
            .fillMaxHeight()
            .background(Color(0xFFF8F8F8))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(color = Color(0xFFFEFEFE))
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onProfileClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = if (imageUri != null) {
                                rememberAsyncImagePainter(imageUri.toUri())
                            } else {
                                painterResource(id = R.drawable.profile_picture)
                            },
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    Text(
                        text = username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))
//                HorizontalDivider(color = Color(0xFFBCBCBC))
//                Spacer(modifier = Modifier.height(16.dp))
//
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(offerItems.size) { index ->
//                        OfferItemCard(item = offerItems[index])
//                    }
//                }
//                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                        .shadow(5.dp, ambientColor = Color(0x0000000D))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color = Color(0xFFFEFEFE))
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                navItems.forEach { item ->
                    NavigationItem(item = item)
                    if (item != navItems.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 62.dp),
                            color = Color(0xFFBCBCBC)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomEnd = 8.dp, topEnd = 8.dp))
                        .background(color = Color(0xFF7876E4))
                        .clickable { /* TODO */ }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            )
                    ) {
                        Text(
                            text = "Sürücü ol",
                            color = Color(0xFFFEFEFE),
                            fontSize = 24.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.W700,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Uyğun qrafiklə gəlir qazan",
                            color = Color(0xFFFEFEFE),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.W300
                        )
                    }
                }
            }
        }
    }
}

data class NavItem(
    val title: String,
    @DrawableRes val icon: Int
)

data class OfferItem(
    val title: String,
    @DrawableRes val icon: Int,
    val seen: Boolean
)

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
    NavigationDrawer(
        onProfileClicked = { },
        onBackPress = { },
    )
}