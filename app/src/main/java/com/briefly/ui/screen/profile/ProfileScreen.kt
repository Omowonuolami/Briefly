package com.briefly.ui.screen.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.briefly.ui.screen.home.BottomNavBar
import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightBodyText
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.LightMuted
import com.briefly.ui.theme.newsReader

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedNav by remember { mutableStateOf("profile") }

    // Image picker launcher
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onAvatarSelected(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {

        // ── TOP BAR ──────────────────────────────────────────────────
        ProfileTopBar(profileViewModel = viewModel)

        HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

        // ── SCROLLABLE CONTENT ────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Cream),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── AVATAR + NAME + BIO ───────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp)
                ) {
                    // Avatar with edit badge
                    Box(
                        modifier = Modifier.size(86.dp)
                    ) {
                        // Avatar circle
                        if (uiState.avatarUrl != null) {
                            AsyncImage(
                                model = uiState.avatarUrl,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, LightDivider, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Default avatar — dark circle with person icon
                            Box(
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2A2724)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFF0EBE3),
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        // Edit badge — bottom right
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(BrieflyRed)
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit photo",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // "MEMBER SINCE 2023"
                    Text(
                        text = "MEMBER SINCE ${uiState.memberSince}",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 9.sp,
                            letterSpacing = 0.18.em,
                            color = LightMuted
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    // Name
                    Text(
                        text = uiState.name,
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = LightInk,
                            lineHeight = 34.sp
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    // Bio
                    Text(
                        text = uiState.bio,
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 13.sp,
                            color = LightBodyText,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }

            // ── STATS GRID ────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = "ARTICLES",
                            value = uiState.articlesRead,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "TIME SPENT",
                            value = uiState.timeSpent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = "STREAK",
                            value = uiState.streak,
                            modifier = Modifier.weight(1f)
                        )
                        // Briefly Pro card
                        ProCard(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            // ── SELECTED INTERESTS ────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Selected Interests",
                            style = TextStyle(
                                fontFamily = newsReader,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = LightInk
                            )
                        )
                        Text(
                            text = "Edit All",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 12.sp,
                                color = LightMuted
                            ),
                            modifier = Modifier.clickable { }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Interest chips — wrap layout
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.interests.forEach { interest ->
                            InterestChip(label = interest)
                        }
                        // "+ Add Topic" chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Transparent)
                                .border(
                                    0.5.dp,
                                    BrieflyRed,
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "+ Add Topic",
                                style = TextStyle(
                                    fontFamily = DmSans,
                                    fontSize = 12.sp,
                                    color = BrieflyRed
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                }
            }

            // ── READING HABITS ────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(
                            Color(0xFFF0EBE0),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Reading Habits",
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = LightInk
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    uiState.readingHabits.forEach { habit ->
                        ReadingHabitRow(habit = habit)
                        Spacer(Modifier.height(14.dp))
                    }
                }

                Spacer(Modifier.height(28.dp))
            }

            // ── SETTINGS ─────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp,
                            color = LightInk
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    // Dark Mode toggle row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DarkMode,
                                contentDescription = null,
                                tint = LightBodyText,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Dark Mode",
                                style = TextStyle(
                                    fontFamily = DmSans,
                                    fontSize = 15.sp,
                                    color = LightInk
                                )
                            )
                        }
                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = viewModel::onToggleDarkMode,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BrieflyRed,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = LightDivider
                            )
                        )
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

                    // Notifications row
                    SettingsRow(
                        icon = Icons.Outlined.NotificationsNone,
                        label = "Notifications",
                        onClick = {}
                    )

                    HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

                    // Account row
                    SettingsRow(
                        icon = Icons.Outlined.ManageAccounts,
                        label = "Account",
                        onClick = {}
                    )

                    HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

                    // Privacy row
                    SettingsRow(
                        icon = Icons.Outlined.Shield,
                        label = "Privacy",
                        onClick = {}
                    )

                    HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

                    Spacer(Modifier.height(20.dp))

                    // Help Center
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = null,
                            tint = LightBodyText,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Help Center",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 15.sp,
                                color = LightInk
                            )
                        )
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

                    Spacer(Modifier.height(8.dp))

                    // Sign Out — red
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onSignOut { onSignOut() }
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = BrieflyRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Sign Out",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 15.sp,
                                color = BrieflyRed
                            )
                        )
                    }
                }
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

        // ── BOTTOM NAV ───────────────────────────────────────────────
        BottomNavBar(
            selectedRoute = "profile",
            onItemSelected = { route -> onNavigate(route) }
        )
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────
@Composable
fun ProfileTopBar(
    profileViewModel: ProfileViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { }
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(Modifier.width(22.dp).height(1.5.dp).background(LightInk))
            Box(Modifier.width(15.dp).height(1.5.dp).background(LightInk))
            Box(Modifier.width(22.dp).height(1.5.dp).background(LightInk))
        }

        Text(
            text = "Briefly",
            style = TextStyle(
                fontFamily = newsReader,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 26.sp,
                color = LightInk
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
        val avatarUrl = uiState.avatarUrl

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A2724))
                .border(1.5.dp, LightInk, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "E",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 13.sp,
                        color = Color(0xFFF0EBE3),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

// ── Stat Card ─────────────────────────────────────────────────────────
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFF0EBE0), RoundedCornerShape(6.dp))
            .border(0.5.dp, LightDivider, RoundedCornerShape(6.dp))
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 9.sp,
                letterSpacing = 0.16.em,
                color = LightMuted
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = value,
            style = TextStyle(
                fontFamily = newsReader,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = LightInk
            )
        )
    }
}

// ── Briefly Pro Card ──────────────────────────────────────────────────
@Composable
fun ProCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFFCECEC), RoundedCornerShape(6.dp))
            .border(0.5.dp, Color(0xFFEDC4C4), RoundedCornerShape(6.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "BRIEFLY PRO",
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 9.sp,
                letterSpacing = 0.16.em,
                color = BrieflyRed,
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(Modifier.height(8.dp))
        Icon(
            imageVector = Icons.Outlined.Verified,
            contentDescription = "Pro",
            tint = BrieflyRed,
            modifier = Modifier.size(28.dp)
        )
    }
}

// ── Interest Chip ─────────────────────────────────────────────────────
@Composable
fun InterestChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF0EBE0))
            .border(0.5.dp, LightDivider, RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 12.sp,
                color = LightBodyText
            )
        )
    }
}

// ── Reading Habit Row ─────────────────────────────────────────────────
@Composable
fun ReadingHabitRow(habit: ReadingHabit) {
    // Animate the progress bar on first composition
    var started by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (started) habit.progress else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "progress_${habit.category}"
    )
    LaunchedEffect(Unit) { started = true }

    val barColor = when (habit.color) {
        HabitColor.RED  -> BrieflyRed
        HabitColor.GOLD -> Color(0xFFC9A84C)
        HabitColor.BLUE -> Color(0xFF4A7FA5)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habit.category,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 13.sp,
                    color = LightInk
                )
            )
            Text(
                text = "${(habit.progress * 100).toInt()}%",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 13.sp,
                    color = LightMuted
                )
            )
        }
        Spacer(Modifier.height(6.dp))
        // Progress track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(LightDivider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}

// ── Settings Row ──────────────────────────────────────────────────────
@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LightBodyText,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 15.sp,
                    color = LightInk
                )
            )
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = LightMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}