package com.example.utilityapp.ui.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utilityapp.R

@Composable
fun CustomBottomNavBar(
    selectedTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(BottomNavItem.Shake, BottomNavItem.Compass, BottomNavItem.Steps)

    Row(
        modifier = Modifier
            .fillMaxWidth()
           .background(Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->

            val isSelected = item == selectedTab
            val color by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF6C63FF) else Color.White
            )
            val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected(item) }
                    .padding(8.dp)
            ) {
                IconWithAnimation(
                    iconRes = item.icon,
                    isSelected = isSelected
                )
                Text(
                    text = item.title,
                    color = color,
                    fontSize = 14.sp,
                    fontWeight = fontWeight
                )
            }
        }
    }
}

@Composable
fun IconWithAnimation(iconRes: Int, isSelected: Boolean) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.3f else 1f)

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = if (isSelected) Color(0xFF6C63FF) else Color.Transparent,
                shape = CircleShape
            )
            .padding(10.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Gray
        )
    }
}


enum class BottomNavItem(val title: String, val icon: Int , val route: String) {
    Shake("Shake", R.drawable.mobile , "shake"),
    Compass("Compass", R.drawable.compass , "compass"),
    Steps("Steps", R.drawable.step , "steps")
}

