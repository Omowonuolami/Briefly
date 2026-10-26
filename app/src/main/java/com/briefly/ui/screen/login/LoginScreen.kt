package com.briefly.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.briefly.R
import com.briefly.ui.theme.BodyDark
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.FieldBg
import com.briefly.ui.theme.FieldBorder
import com.briefly.ui.theme.FieldBorderErr
import com.briefly.ui.theme.PlaceholderC
import com.briefly.ui.theme.TaglineGrey
import com.briefly.ui.theme.newsReader

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onClose: () -> Unit,
    onSignUpClick : () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .systemBarsPadding()
    ) {

        // ── SCROLLABLE BODY ──────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(20.dp))

//            // ── TOP ROW: × left, Briefly center ─────────────────────
//            Box(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // × close button — top left
//                Text(
//                    text = "×",
//                    style = TextStyle(
//                        fontSize = 24.sp,
//                        color = BodyDark,
//                        fontWeight = FontWeight.Light
//                    ),
//                    modifier = Modifier
//                        .align(Alignment.CenterStart)
//                        .clickable { onClose() }
//                        .padding(4.dp)
//                )
//
//                // "Briefly" wordmark — centered
//
//            }

            Spacer(Modifier.height(36.dp))

            // ── "EDITORIAL ACCESS" ───────────────────────────────────

            Text(
                text = "Briefly",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 28.sp,
                    color = BrieflyRed,
                    textAlign = TextAlign.Center,
                    ),
                modifier = Modifier.fillMaxWidth()
            )

//            Text(
//                text = "EDITORIAL ACCESS",
//                style = TextStyle(
//                    fontFamily = DmSans,
//                    fontSize = 10.sp,
//                    letterSpacing = 0.22.em,
//                    color = BrieflyRed,
//                    textAlign = TextAlign.Center
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )

            Spacer(Modifier.height(10.dp))

            // ── "Welcome Back." ──────────────────────────────────────
            Text(
                text = "Welcome Back.",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    color = BodyDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 48.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // ── Subtitle ─────────────────────────────────────────────
            Text(
                text = "Rejoin the conversation with curated insights from the world's most trusted sources.",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 15.sp,
                    color = TaglineGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(36.dp))

            // ── EMAIL FIELD ──────────────────────────────────────────
            SignInTextField(
                label = "EMAIL ADDRESS",
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "name@example.com",
                keyboardType = KeyboardType.Email,
                errorMessage = uiState.emailError
            )

            Spacer(Modifier.height(16.dp))

            // ── PASSWORD LABEL ROW (label left, FORGOT? right) ───────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PASSWORD",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 10.sp,
                        letterSpacing = 0.16.em,
                        color = TaglineGrey
                    )
                )
                Text(
                    text = "FORGOT?",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 10.sp,
                        letterSpacing = 0.12.em,
                        color = BrieflyRed,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.clickable { viewModel.onForgotPassword() }
                )
            }

            Spacer(Modifier.height(6.dp))

            // ── PASSWORD FIELD (no label — handled above) ────────────
            SignInTextField(
                label = null,              // label already drawn above
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                errorMessage = uiState.passwordError
            )

            Spacer(Modifier.height(32.dp))

            // Global error
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 12.sp,
                        color = BrieflyRed,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            // ── SIGN IN BUTTON ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (uiState.isLoading) BrieflyRed.copy(alpha = 0.75f)
                        else BrieflyRed
                    )
                    .clickable(enabled = !uiState.isLoading) {
                        viewModel.onSignIn()
                        onNavigateToHome()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SIGN IN",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.22.em,
                            color = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── OR CONTINUE WITH ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                    color = FieldBorder
                )
                Text(
                    text = "OR CONTINUE WITH",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.18.em,
                        color = TaglineGrey
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                    color = FieldBorder
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── SOCIAL BUTTONS ───────────────────────────────────────
            // Google button — dark background, Apple button — light outlined
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Google — dark filled
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Cream)
                        .border(0.8.dp, FieldBorder, RoundedCornerShape(6.dp))
                        .clickable { }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    // Google G in a small box
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Cream, RoundedCornerShape(3.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(R.drawable.google_logo),
                            contentDescription = "google",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "GOOGLE",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 11.sp,
                            letterSpacing = 0.14.em,
                            color = BodyDark,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Apple — light outlined
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .border(0.8.dp, FieldBorder, RoundedCornerShape(6.dp))
                        .background(Cream)
                        .clickable { }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    // iOS badge
                    Icon(painter = painterResource(R.drawable.fb_logo),
                        contentDescription = "facebook",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "FACEBOOK",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 11.sp,
                            letterSpacing = 0.14.em,
                            color = BodyDark,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(Modifier.height(36.dp))

            // ── CREATE ACCOUNT LINK ──────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New to Briefly? ",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 14.sp,
                        color = TaglineGrey
                    )
                )
                Text(
                    text = "Create an account",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 14.sp,
                        color = BrieflyRed,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ── Outlined text field matching this design exactly ──────────────────
// Different from BrieflyTextField (underline) used in SignUp —
// this one has a full rectangular outline border
@Composable
fun SignInTextField(
    label: String?,           // null = label already drawn outside
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Label — only draw if provided
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 10.sp,
                    letterSpacing = 0.16.em,
                    color = if (errorMessage != null) BrieflyRed else TaglineGrey
                )
            )
            Spacer(Modifier.height(6.dp))
        }

        // Outlined field box
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            cursorBrush = SolidColor(BrieflyRed),
            textStyle = TextStyle(
                fontFamily = DmSans,
                fontSize = 15.sp,
                color = BodyDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    width = if (errorMessage != null) 1.2.dp else 0.8.dp,
                    color = if (errorMessage != null) FieldBorderErr else FieldBorder,
                    shape = RoundedCornerShape(6.dp)
                )
                .background(FieldBg, RoundedCornerShape(6.dp))
                .padding(horizontal = 14.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 15.sp,
                                color = PlaceholderC,
                                fontStyle = if (!isPassword) FontStyle.Italic
                                else FontStyle.Normal
                            )
                        )
                    }
                    innerTextField()
                }
                if (isPassword) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.CenterEnd)  // ← positions content to the end
                    ) {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Outlined.Visibility
                                else
                                    Icons.Outlined.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Hide password"
                                else
                                    "Show password",
                                tint = TaglineGrey,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        )

        // Error message below field
        if (errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 11.sp,
                    color = BrieflyRed
                )
            )
        }
    }

}

@Preview
@Composable
fun LoginPreview(){
    LoginScreen(
        onClose = {},
        onSignUpClick = {},
        onNavigateToHome = {},
        onNavigateToForgotPassword = {}
    )
}