package com.starbow.greenjuice.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.GreenJuiceScreen
import com.starbow.greenjuice.enum.GreenJuiceTheme
import com.starbow.greenjuice.ui.navigation.GreenJuiceNavHost
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceAppViewModel

//루트 컴포저블
@Composable
fun GreenJuiceApp(
    modifier: Modifier = Modifier,
    viewModel: GreenJuiceAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreenName: String = backStackEntry?.destination?.route ?: GreenJuiceScreen.MAIN.name

    val themeState = viewModel.themeState.collectAsState()
    val currentTheme = themeState.value

    var showMenu by remember { mutableStateOf(false) }

    GreenJuiceTheme(
        darkTheme = when(currentTheme) {
            GreenJuiceTheme.LIGHT -> false
            GreenJuiceTheme.DARK -> true
            GreenJuiceTheme.SYSTEM -> isSystemInDarkTheme()
        }
    ) {
        Scaffold(
            topBar = {
                when(
                    val currentScreen = GreenJuiceScreen
                        .valueOf(currentScreenName.split('/')[0])
                ) {
                    GreenJuiceScreen.THEME,
                    GreenJuiceScreen.SIGN_IN,
                    GreenJuiceScreen.SIGN_UP,
                    GreenJuiceScreen.FAVORITES ->
                        GreenJuiceTitleTopBar(
                            title = currentScreen.title,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                        )
                    else -> {
                        GreenJuiceIconTopBar(
                            showIcon = currentScreen != GreenJuiceScreen.WEB_VIEW,
                            showTheme = currentScreen != GreenJuiceScreen.WEB_VIEW,
                            showSignIn = !(((currentScreen == GreenJuiceScreen.MAIN) and !viewModel.isSignIn())
                                    or (currentScreen == GreenJuiceScreen.WEB_VIEW)),
                            showMenu = showMenu,
                            isSignIn = viewModel.isSignIn(),
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            navigateTheme = { navController.navigate(GreenJuiceScreen.THEME.name) },
                            onClickAccount = { showMenu = !showMenu },
                            onMenuDismissRequest = { showMenu = false },
                            onClickFavorites = {
                                navController.navigate(GreenJuiceScreen.FAVORITES.name)
                                showMenu = false
                            },
                            onClickSignOut = {
                                viewModel.signOut()
                                Toast.makeText(
                                    context, R.string.sign_out_success, Toast.LENGTH_LONG
                                ).show()
                                showMenu = false
                            },
                            navigateSignIn = { navController.navigate(GreenJuiceScreen.SIGN_IN.name) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colors.background,
            ) {
                GreenJuiceNavHost(
                    navController = navController,
                    accountId = viewModel.curAccount,
                    theme = currentTheme,
                    changeThemeOption = { theme -> viewModel.updateThemeOption(theme) },
                    isSignIn = viewModel.isSignIn(),
                    signIn = { id, pw -> viewModel.signIn(id, pw) },
                    signUp = { id, pw -> viewModel.signUp(id, pw) },
                    isDuplicatedId = { id -> viewModel.isDuplicatedId(id) }
                )
            }
        }
    }
}

@Composable
fun GreenJuiceIconTopBar(
    showIcon: Boolean,
    showTheme: Boolean,
    showSignIn: Boolean,
    showMenu: Boolean,
    isSignIn: Boolean,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateTheme: () -> Unit,
    onClickAccount: () -> Unit,
    onMenuDismissRequest: () -> Unit,
    onClickFavorites: () -> Unit,
    onClickSignOut: () -> Unit,
    navigateSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppBarHeight)
            .padding(horizontal = AppBarHorizontalPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(AppBarButtonWidth)
                .align(Alignment.CenterStart)
        ) {
            if (canNavigateBack) {
                AppBarIconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(AppBarHeight)
                .align(Alignment.Center)
        ) {
            if (showIcon) {
                Image(
                    painter = painterResource(id = R.drawable.green_juice),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (showTheme) {
                    AppBarIconButton(onClick = navigateTheme) {
                        Icon(
                            painter = painterResource(id = R.drawable.lightbulb),
                            contentDescription = null,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (showSignIn) {
                    if (isSignIn) {
                        AppBarIconButton(onClick = onClickAccount) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                            )
                        }
                    } else {
                        TextButton(
                            onClick = navigateSignIn,
                        ) {
                            Text(text = stringResource(id = R.string.sign_in),)
                        }
                    }
                }
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onMenuDismissRequest,
            ) {
                DropdownMenuItem(
                    onClick = onClickFavorites
                ) {
                    Text(
                        text = stringResource(id = R.string.favorites),
                        style = MaterialTheme.typography.body1
                    )
                }
                DropdownMenuItem(
                    onClick = onClickSignOut
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_out),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
fun GreenJuiceTitleTopBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        elevation = 0.dp,
        modifier = modifier
    )
}

@Composable
fun AppBarIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(AppBarButtonWidth)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.Center)
                .size(AppBarButtonWidth),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconTopBarHasSignInAccountPreview() {
    GreenJuiceTheme {
        GreenJuiceIconTopBar(
            showIcon = true,
            showTheme = true,
            showSignIn = true,
            showMenu = true,
            isSignIn = true,
            canNavigateBack = true,
            navigateUp = {},
            onClickAccount = {},
            onMenuDismissRequest = {},
            onClickFavorites = {},
            onClickSignOut = {},
            navigateTheme = {},
            navigateSignIn = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconTopBarNoHasSignInAccountPreview() {
    GreenJuiceTheme {
        GreenJuiceIconTopBar(
            showIcon = true,
            showTheme = true,
            showSignIn = false,
            showMenu = false,
            isSignIn = false,
            canNavigateBack = true,
            navigateUp = {},
            onClickAccount = {},
            onMenuDismissRequest = {},
            onClickFavorites = {},
            onClickSignOut = {},
            navigateTheme = {},
            navigateSignIn = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TitleTopBarNoHasSignInAccountPreview() {
    GreenJuiceTheme {
        GreenJuiceTitleTopBar(
            title = GreenJuiceScreen.FAVORITES.title,
            canNavigateBack = true,
            navigateUp = {},
        )
    }
}

private val AppBarHeight = 56.dp
private val AppBarButtonWidth = 48.dp
private val AppBarHorizontalPadding = 4.dp