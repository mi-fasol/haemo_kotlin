package com.example.haemo_kotlin.util

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.viewModel.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel

@Composable
fun TextEnterField(type: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            type,
            fontSize = 17.5.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.mainColor),
            modifier = Modifier.weight(1f)
        )
        Column(
            Modifier.weight(6f)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = colorResource(id = R.color.postRegisterTextColor),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Divider()
        }
    }
}

@Composable
fun PostSelectDropDownMenu(
    text: String,
    list: List<String>,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(text) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val borderColor =
        if (expanded) colorResource(id = R.color.mainColor) else colorResource(id = R.color.postRegisterTextColor)

    Box(
        modifier = Modifier
            .border(width = 1.5.dp, borderColor, shape = RoundedCornerShape(25.dp))
            .clickable {
                expanded = true
            }
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedText, color = borderColor)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(icon, contentDescription = null, tint = borderColor)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .then(
                    if (list.size > 5) {
                        Modifier.height(200.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            list.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = label
                        onValueChange(label)
                        expanded = false
                    },
                ) {
                    Text(text = label, fontSize = 11.sp)
                }
            }
        }

    }
}

@Composable
fun ContentEnterField(value: String, onValueChange: (String) -> Unit) {
    val conf = LocalConfiguration.current
    val screenHeight = conf.screenHeightDp
    Box(
        Modifier
            .background(Color(0xfff5f5f5), RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height((screenHeight / 2.2).dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(15.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color(0xffb6b6b6),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PostRegisterButton(
    viewModel: PostViewModel?,
    clubViewModel: ClubPostViewModel?,
    hotPlacePostViewModel: HotPlacePostViewModel?,
    type: Int,
    navController: NavController,
    onClicked: () -> Unit
) {
    val isValid = when (type) {
        1 -> viewModel!!.isValid.collectAsState().value
        2 -> clubViewModel!!.isValid.collectAsState().value
        3 -> viewModel!!.isValid.collectAsState().value
        else -> false
    }

    val context = LocalContext.current

    Button(
        onClick = {
            onClicked()
        },
        enabled = isValid,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.mainColor),
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.White,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        androidx.compose.material3.Text(
            "등록하기",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
    }
}