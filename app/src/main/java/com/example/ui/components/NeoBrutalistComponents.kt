package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GankColors

@Composable
fun NeoBrutalistCard(
    modifier: Modifier = Modifier,
    shadowOffset: Dp = 5.dp,
    borderWidth: Dp = 3.dp,
    cornerRadius: Dp = 8.dp,
    backgroundColor: Color = GankColors.White,
    shadowColor: Color = GankColors.Ink,
    borderColor: Color = GankColors.Ink,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Hard Pitch-Black Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor)
        )
        // Main Foreground Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun NeoBrutalistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = GankColors.GankYellow,
    contentColor: Color = GankColors.Ink,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    val shadowOffset by animateDpAsState(if (isPressed || !enabled) 0.dp else 5.dp, label = "btnShadow")
    val contentOffset by animateDpAsState(if (isPressed) 5.dp else 0.dp, label = "btnContent")

    Box(modifier = modifier) {
        if (enabled && shadowOffset > 0.dp) {
            Box(
                Modifier
                    .matchParentSize()
                    .offset(x = 5.dp, y = 5.dp)
                    .background(GankColors.Ink, RoundedCornerShape(8.dp))
            )
        }
        Box(
            Modifier
                .offset(x = contentOffset, y = contentOffset)
                .background(
                    if (enabled) containerColor else GankColors.Silver,
                    RoundedCornerShape(8.dp)
                )
                .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp).padding(end = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = text,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = contentColor,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun NeoBrutalistBadge(
    text: String,
    containerColor: Color = GankColors.GankYellow,
    textColor: Color = GankColors.Ink,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(4.dp))
            .border(2.dp, GankColors.Ink, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = textColor,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun NeoBrutalistStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    badgeText: String? = null,
    badgeColor: Color = GankColors.GankYellow,
    cardColor: Color = GankColors.White,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val valueFontSize = when {
        value.length > 12 -> 13.sp
        value.length > 9 -> 15.sp
        value.length > 5 -> 18.sp
        else -> 22.sp
    }

    NeoBrutalistCard(
        modifier = modifier,
        backgroundColor = cardColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(GankColors.Ink, RoundedCornerShape(6.dp))
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GankColors.GankYellow,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (badgeText != null) {
                    NeoBrutalistBadge(text = badgeText, containerColor = badgeColor)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = title.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = GankColors.Steel,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    fontWeight = FontWeight.Black,
                    fontSize = valueFontSize,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun NeoBrutalistFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                if (isSelected) GankColors.GankYellow else GankColors.White,
                RoundedCornerShape(6.dp)
            )
            .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            color = GankColors.Ink,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun NeoBrutalistTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = GankColors.Ink,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GankColors.Paper, RoundedCornerShape(8.dp))
                .border(2.5.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    if (placeholder.isNotEmpty()) {
                        Text(placeholder, fontSize = 13.sp, color = GankColors.Steel)
                    }
                },
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = GankColors.Ink,
                    focusedTextColor = GankColors.Ink,
                    unfocusedTextColor = GankColors.Ink
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

