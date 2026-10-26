package com.briefly.ui.screen.signup

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.briefly.R
import com.briefly.ui.theme.BodyDark
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.FieldBorder
import com.briefly.ui.theme.TaglineGrey
import com.briefly.ui.theme.newsReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onNavigateToSignIn: () -> Unit,
                 navController: NavController,
                 onCreateAccount: () -> Unit = {},
                 onGoogleSignIn: () -> Unit = {},
                 onFacebookSignIn: () -> Unit = {},
                 viewModel: SignUpViewModel = hiltViewModel()
 ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "By joining, you agree to our Terms of Service and\n" +
                            "Privacy Policy.",
                    color = Color(0xFF8F6F6C),
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontFamily = DmSans
                )
            }

        }, containerColor = Cream
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Cream)
                .systemBarsPadding()
        ) {
//            // ── TOP BAR ──────────────────────────────────────────────────
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Cream)
//                    .padding(vertical = 14.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                // "Briefly" wordmark — left
//                Text(
//                    text = "Briefly",
//                    style = TextStyle(
//                        fontFamily = newsReader,
//                        fontWeight = FontWeight.Bold,
//                        fontStyle = FontStyle.Italic,
//                        fontSize = 43.sp,
//                        color = BrieflyRed
//                    )
//                )
//            }

            // ── SCROLLABLE BODY ──────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {

                // ── DASHED BORDER CONTAINER ──────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .border(
//                            width = 1.dp,
//                            color = DashedBorder,
//                            shape = RoundedCornerShape(4.dp)
//                            // Note: Compose doesn't support native dashed borders
//                            // Use the Canvas workaround below for true dashed lines
//                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Briefly",
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 43.sp,
                            color = BrieflyRed
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // "Join Briefly."
                    Text(
                        text = "Join Briefly.",
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            color = BodyDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 44.sp
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Subtitle
                    Text(
                        text = "The news, curated with intent.\nExperience journalism as it was meant to be read.",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 14.sp,
                            color = TaglineGrey,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(Modifier.height(28.dp))

                    // ── FULL NAME FIELD ──────────────────────────────────
                    BrieflyTextField(
                        label = "FULL NAME",
                        value = uiState.name,
                        onValueChange = { viewModel.onNameChange(it) },
                        placeholder = "Evelyn Thorne",
                        keyboardType = KeyboardType.Text,
                        errorMessage = uiState.nameError
                    )

                    Spacer(Modifier.height(16.dp))

                    // ── EMAIL FIELD ──────────────────────────────────────
                    BrieflyTextField(
                        label = "EMAIL ADDRESS",
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        placeholder = "evelyn@domain.com",
                        keyboardType = KeyboardType.Email,
                        errorMessage = uiState.emailError
                    )

                    Spacer(Modifier.height(16.dp))

                    // ── PASSWORD FIELD ───────────────────────────────────
                    BrieflyTextField(
                        label = "PASSWORD",
                        value = uiState.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        placeholder = "••••••••",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        errorMessage = uiState.passwordError
                    )

                    Spacer(Modifier.height(28.dp))

                    // ── CREATE ACCOUNT BUTTON ────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BrieflyRed)
                            .clickable {
                                if (viewModel.onCreateAccount()) {
                                    onCreateAccount()
                                }
//                                onCreateAccount()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                letterSpacing = 0.18.em,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── OR CONTINUE WITH ─────────────────────────────────
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

                    // ── SOCIAL BUTTONS ───────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Google
                        SocialButton(
                            modifier = Modifier.weight(1f),
                            label = "GOOGLE",
                            icon = {
                                Icon(painter = painterResource(R.drawable.google_logo),
                                    contentDescription = "google",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            onClick = onGoogleSignIn
                        )

                        // Facebook
                        SocialButton(
                            modifier = Modifier.weight(1f),
                            label = "FACEBOOK",
                            icon = {
                                // Facebook "f" icon
                                Icon(painter = painterResource(R.drawable.fb_logo),
                                    contentDescription = "facebook",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onClick = onFacebookSignIn
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                // ── SIGN IN LINK ─────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 13.sp,
                            color = TaglineGrey
                        )
                    )
                    Text(
                        text = "Sign in here",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 13.sp,
                            color = BrieflyRed,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.None
                        ),
                        modifier = Modifier.clickable { onNavigateToSignIn() }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ── Reusable text field matching the design ───────────────────────────
@Composable
fun BrieflyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Label — small spaced caps above field
        Text(
            text = label,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 9.sp,
                letterSpacing = 0.18.em,
                color = if (errorMessage != null) BrieflyRed else TaglineGrey  // ← red label on error
            )
        )

        Spacer(Modifier.height(6.dp))

        // Field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            keyboardActions = KeyboardActions.Default,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = DmSans,
                fontSize = 15.sp,
                color = BodyDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Cream)
                .padding(vertical = 10.dp),
            decorationBox = { innerTextField ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Text input takes all remaining space
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontFamily = DmSans,
                                        fontSize = 15.sp,
                                        color = TaglineGrey.copy(alpha = 0.5f),
                                        fontStyle = if (!isPassword) FontStyle.Italic
                                        else FontStyle.Normal
                                    )
                                )
                            }
                            innerTextField()
                        }

                        // Eye icon sits beside the text, not over it
                        if (isPassword) {
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

                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(
                        thickness = if (errorMessage != null) 1.5.dp else 0.8.dp,
                        color = if (errorMessage != null) BrieflyRed else FieldBorder
                    )
                }
            }
        )
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

// ── Social login button ───────────────────────────────────────────────
@Composable
fun SocialButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .border(0.8.dp, FieldBorder, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        icon()
        Text(
            text = label,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 10.sp,
                letterSpacing = 0.15.em,
                color = BodyDark,
                fontWeight = FontWeight.Medium
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpPreview(){
    SignUpScreen(
        onGoogleSignIn = {},
        onNavigateToSignIn = {},
        onCreateAccount = {},
        onFacebookSignIn = {},
        navController = rememberNavController(),
    )
}