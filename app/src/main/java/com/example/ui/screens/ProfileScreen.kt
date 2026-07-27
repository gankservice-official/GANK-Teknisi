package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeoBrutalistBadge
import com.example.ui.components.NeoBrutalistCard
import com.example.ui.theme.GankColors

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NeoBrutalistCard(
                backgroundColor = GankColors.Ink,
                shadowColor = GankColors.GankYellow
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(GankColors.GankYellow)
                            .border(3.dp, GankColors.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = GankColors.Ink,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "GANK SERVICE STORE",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = GankColors.White,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Pusat Reparasi Smartphone & Hardware",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = GankColors.Silver
                        )
                    }
                }
            }
        }

        // Workshop Details
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "PROFIL BENGKEL SERVIS",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "📍 Alamat: Jl. GANK Service No. 1, Jakarta", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(text = "📞 Hotline / WA: 0812-3456-7890", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(text = "👨‍🔧 Teknisi Penanggung Jawab: Lead Tech GANK", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ Jam Operasional: Senin - Sabtu (09.00 - 21.00 WIB)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // CI/CD Status
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GITHUB ACTIONS CI/CD",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Auto Build APK on Push/Pull Request",
                            fontSize = 11.sp,
                            color = GankColors.Steel
                        )
                    }
                    NeoBrutalistBadge(
                        text = "READY",
                        containerColor = GankColors.GreenOK,
                        textColor = GankColors.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GankColors.GreenOK,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ".github/workflows/android-build.yml aktif & teruji.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
