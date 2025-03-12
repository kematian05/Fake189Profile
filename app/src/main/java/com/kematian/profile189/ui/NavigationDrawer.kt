package com.kematian.profile189.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kematian.profile189.R

@Composable
fun NavigationDrawer(
    navController: NavController,
    imageUri: String? = null
) {
    val navItems = listOf(
        NavItem("Ödəniş növləri", Icons.Default.Payment),
        NavItem("Korporativ", Icons.Default.Business),
        NavItem("Gediş tarixçəsi", Icons.Default.History),
        NavItem("Promokod", Icons.Default.TagFaces),
        NavItem("Dəstək", Icons.Default.ChatBubbleOutline)
    )

    Column(
        modifier = Modifier
            .width(316.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    navController.navigate("profile")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = if (imageUri != null) {
                        rememberAsyncImagePainter(Uri.parse(imageUri))
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
                text = "Vusat Orujov",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(start = 20.dp, end = 20.dp))
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .height(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF5C6BC0))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "5 gediş et\n1 gediş hədiyyə\nqazan!",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .height(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Üstünlüklərimiz",
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .height(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "    Yeniliklər      ",
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp))

        navItems.forEach { item ->
            NavigationItem(item = item)
            HorizontalDivider(modifier = Modifier.padding(start = 62.dp, end = 20.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
        ) {
            Text(
                text = "Sürücü ol\nUyğun qrafiklə gəlir qazan",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun NavigationItem(item: NavItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color.Gray
        )
        Text(
            text = item.title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp),
            fontSize = 14.sp
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}

data class NavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
    NavigationDrawer(
        navController = NavController(LocalContext.current)
    )
}