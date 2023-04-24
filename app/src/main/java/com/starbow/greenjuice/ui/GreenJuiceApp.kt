package com.starbow.greenjuice.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.starbow.greenjuice.enum.GreenJuiceScreen
import com.starbow.greenjuice.enum.GreenJuiceTheme
import com.starbow.greenjuice.ui.navigation.GreenJuiceNavHost
import com.starbow.greenjuice.ui.screen.*
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceAppViewModel

//루트 컴포저블
@Composable
fun GreenJuiceApp(
    modifier: Modifier = Modifier,
    viewModel: GreenJuiceAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreenName: String = backStackEntry?.destination?.route ?: GreenJuiceScreen.MAIN.name

    val themeState = viewModel.themeState.collectAsState()
    val currentTheme = themeState.value

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
                    GreenJuiceScreen.SETTING,
                    GreenJuiceScreen.SIGN_IN,
                    GreenJuiceScreen.SIGN_UP ->
                        GreenJuiceTopBar(
                            title = currentScreen.title,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                        )
                    else ->
                        GreenJuiceCustomTopBar(
                            showTitle = currentScreen == GreenJuiceScreen.RESULT,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            navigateSettings = { navController.navigate(GreenJuiceScreen.SETTING.name) }
                        )
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
                    theme = currentTheme,
                    changeThemeOption = { theme -> viewModel.updateThemeOption(theme) }
                )
            }
        }
    }
}

@Composable
fun GreenJuiceCustomTopBar(
    showTitle: Boolean,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.size(48.dp)) {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
        Box(
            modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(8.dp)
        ) {
            if (showTitle) {
                AppTitle(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
        Box(modifier = Modifier.size(48.dp)) {
            IconButton(
                onClick = navigateSettings,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            }
        }
    }
}

@Composable
fun GreenJuiceTopBar(
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

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    GreenJuiceTheme {
        GreenJuiceCustomTopBar(
            showTitle = true,
            canNavigateBack = true,
            navigateUp = {},
            navigateSettings = {}
        )
    }
}