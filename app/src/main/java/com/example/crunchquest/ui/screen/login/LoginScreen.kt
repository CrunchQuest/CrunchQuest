package com.example.crunchquest.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crunchquest.R

@Preview
@Composable
fun LoginScreen() {
    Column(
        Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .background(color = Color(0xFFE5E5E5)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id = R.drawable.top_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.TopCenter
        )
        Text(
            text = "CrunchQuest",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.height(250.dp),
            color = MaterialTheme.colorScheme.primary
        )
        var user by remember { mutableStateOf("Username") }
        var password by remember { mutableStateOf("Password") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        TextField(value = user, {text -> user = text},
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp)
                .border(
                    1.dp, MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            ),


            )


    }
    
}