package com.starbow.greenjuice.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.GreenJuiceScreen
import com.starbow.greenjuice.enum.GreenJuiceTheme
import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.ui.AppViewModelProvider
import com.starbow.greenjuice.ui.GreenJuiceNetworkUiState
import com.starbow.greenjuice.ui.screen.*
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceNavHostViewModel

const val TAG = "GreenJuiceNavHost"

@Composable
fun GreenJuiceNavHost(
    navController: NavHostController,
    theme: GreenJuiceTheme,
    changeThemeOption: (GreenJuiceTheme) -> Unit,
    isSignIn: Boolean,
    changeSignInState: (Boolean) -> Unit,
    navBackBlocked: (Int) -> Unit,
    navBackWakeup: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GreenJuiceNavHostViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val appUiState = viewModel.uiState.collectAsState()
    val netUiState = viewModel.netUiState

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val juiceFilterOption = appUiState.value.juiceFilterOption
    val sentimentFilterOption = appUiState.value.sentimentFilterOption

    val juiceFilterOptionIndex: Int = if(juiceFilterOption != null) juiceFilterOption.ordinal+1 else 0
    val sentimentFilterOptionIndex: Int = if(sentimentFilterOption != null) sentimentFilterOption.ordinal+1 else 0

    var selectedJuiceFilterOptionIndex by rememberSaveable { mutableStateOf(juiceFilterOptionIndex) }
    var selectedSentimentFilterOptionIndex by rememberSaveable { mutableStateOf(sentimentFilterOptionIndex) }

    val accessTokenState = viewModel.accessTokenStateFlow.collectAsState()
    val refreshTokenState = viewModel.refreshTokenStateFlow.collectAsState()

    Log.d(TAG, "AdState = ${appUiState.value.juiceFilterOption} Sentiment = ${appUiState.value.sentimentFilterOption}")

    val juiceOptions = listOf(
        stringResource(id = R.string.filter_null_text),
        stringResource(id = R.string.green),
        stringResource(id = R.string.orange),
        stringResource(id = R.string.red)
    )

    val sentimentOptions = listOf(
        stringResource(id = R.string.filter_null_text),
        stringResource(id = Sentiment.POSITIVE.stringRes),
        stringResource(id = Sentiment.NEUTRAL.stringRes),
        stringResource(id = Sentiment.NEGATIVE.stringRes)
    )

    val themeOptions = listOf(
        stringResource(id = GreenJuiceTheme.LIGHT.stringRes),
        stringResource(id = GreenJuiceTheme.DARK.stringRes),
        stringResource(id = GreenJuiceTheme.SYSTEM.stringRes)
    )

    viewModel.showToast.observe(lifecycleOwner, Observer {
        it.getContentIfNotHandled()?.let { toastMessage ->
            Toast.makeText(context, toastMessage.messageRes, Toast.LENGTH_SHORT).show()
        }
    })

    val loading = (viewModel.netUiState == GreenJuiceNetworkUiState.Loading) or
            (viewModel.netUiState == GreenJuiceNetworkUiState.LoadingAdditional)

    BackHandler(enabled = loading) {
        viewModel.requestRefuse()
    }

    if(loading) navBackBlocked(1) else navBackWakeup(1)

    NavHost(
        navController = navController,
        startDestination = GreenJuiceScreen.MAIN.name,
        modifier = modifier
    ) {
        composable(
            route = GreenJuiceScreen.MAIN.name
        ) {
            MainScreen(
                query = viewModel.inputQuery,
                isSignIn = isSignIn,
                onChangeQuery = { viewModel.changeQuery(it) },
                onSearch = {
                    if(viewModel.inputQuery.isBlank())
                        Toast.makeText(context, R.string.empty_query_message, Toast.LENGTH_LONG).show()
                    else {
                        navController.navigate(GreenJuiceScreen.RESULT.name)
                        viewModel.search()
                    }
                },
                onClearQuery = { viewModel.changeQuery("") },
                onClickSignIn = { navController.navigate(GreenJuiceScreen.SIGN_IN.name) },
                onClickSignUp = { navController.navigate(GreenJuiceScreen.SIGN_UP.name) }
            )
        }

        composable(
            route = GreenJuiceScreen.SIGN_IN.name
        ) {
            SignInScreen(
                onClickSignUp = {
                    if(loading) viewModel.requestRefuse()
                    else navController.navigate(GreenJuiceScreen.SIGN_UP.name)
                },
                doSuccessSignIn = {
                    changeSignInState(true)
                    viewModel.loadFavorites(accessTokenState.value, refreshTokenState.value)
                    navController.navigateUp()
                },
                navBackBlocked = navBackBlocked,
                navBackWakeup = navBackWakeup
            )
        }

        composable(
            route = GreenJuiceScreen.SIGN_UP.name
        ) {
            SignUpScreen(
                doSuccessSignUp = { navController.navigateUp() },
                navBackBlocked = navBackBlocked,
                navBackWakeup = navBackWakeup
            )
        }

        composable(
            route = GreenJuiceScreen.RESULT.name
        ) {
            if (appUiState.value.onFilter) {
                //필터 버튼을 클릭한 상태라면 필터 화면을 활성화한다.
                FilterDialog(
                    adStateOptions = juiceOptions,
                    selectedJuiceOptionIndex = selectedJuiceFilterOptionIndex,
                    onJuiceChanged = { selectedJuiceFilterOptionIndex = it },
                    sentimentOptions = sentimentOptions,
                    selectedSentimentOptionIndex = selectedSentimentFilterOptionIndex,
                    onSentimentChanged = { selectedSentimentFilterOptionIndex = it },
                    onDismissRequest = {
                        selectedJuiceFilterOptionIndex = juiceFilterOptionIndex
                        selectedSentimentFilterOptionIndex = sentimentFilterOptionIndex

                        viewModel.filterDialogDisabled()
                    },
                    onConfirmButtonClick = {
                        val nextFilterJuice = if (selectedJuiceFilterOptionIndex == 0) null
                        else JuiceColor.values()[selectedJuiceFilterOptionIndex-1]

                        val nextFilterSentiment = if(selectedSentimentFilterOptionIndex == 0) null
                        else Sentiment.values()[selectedSentimentFilterOptionIndex-1]

                        viewModel.changeFilterState(
                            adState = nextFilterJuice, sentiment = nextFilterSentiment
                        )
                    },
                    onDismissButtonClick = {
                        selectedJuiceFilterOptionIndex = juiceFilterOptionIndex
                        selectedSentimentFilterOptionIndex = sentimentFilterOptionIndex

                        viewModel.filterDialogDisabled()
                    }
                )
            }

            SearchResultScreen(
                netUiState = netUiState,
                query = viewModel.inputQuery,
                amountOfItem = appUiState.value.numberOfItems,
                juiceStatistics = appUiState.value.juiceStatistics,
                sentimentStatistics = appUiState.value.sentimentStatistics,
                searchItems = viewModel.resultList,
                favoritesItems = viewModel.favoritesList,
                showAddButton = appUiState.value.showAddDataButton,
                onFilterClicked = { viewModel.filterDialogActivation() },
                onChangeQuery = { viewModel.changeQuery(it) },
                onSearch = {
                    if (viewModel.inputQuery.isBlank())
                        Toast.makeText(context, R.string.empty_query_message, Toast.LENGTH_LONG)
                            .show()
                    else viewModel.search()
                },
                onClearQuery = { viewModel.changeQuery("") },
                onAddDataClick = { viewModel.getAdditionalData() },
                onItemClick = { url ->
                    try {
                        Log.d(TAG, "url : $url")

                        val args = url
                            .replace("http://", "").replace("https://", "")
                            .split('/')
                        val (base_url, blogId, postId) = arrayOf(args[0], args[1], args[2])

                        Log.d(TAG, "url : $url, base_url : $base_url, blogId: $blogId, postId : $postId")

                        navController.navigate(GreenJuiceScreen.WEB_VIEW.name + "/$base_url/$blogId/$postId")
                    } catch(e: Exception) {
                        Toast.makeText(context, "해당 블로그 포스트에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                isSignIn = isSignIn,
                addFavorites = { postId ->
                    viewModel.addFavorites(accessTokenState.value, refreshTokenState.value, postId)
                },
                deleteFavorites = { postId ->
                    viewModel.deleteFavorites(accessTokenState.value, refreshTokenState.value, postId)
                }
            )
        }

        composable(
            route = GreenJuiceScreen.THEME.name
        ) {
            ThemeSettingScreen(
                themeOptions = themeOptions,
                selectedThemeOptionIndex = theme.ordinal,
                onSelectedChanged = { selectedIndex ->
                    val themeOption: GreenJuiceTheme = GreenJuiceTheme.values()[selectedIndex]
                    changeThemeOption(themeOption)
                }
            )
        }

        composable(
            route = GreenJuiceScreen.WEB_VIEW.name + "/{base_url}/{blogId}/{postId}",
            arguments = listOf(
                navArgument("base_url") { type = NavType.StringType },
                navArgument("blogId") { type = NavType.StringType },
                navArgument("postId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val argList = listOf(
                backStackEntry.arguments?.getString("base_url") ?: "",
                backStackEntry.arguments?.getString("blogId") ?: "",
                backStackEntry.arguments?.getString("postId") ?: "",
            )

            val postUrl = "http://" + argList.joinToString("/")

            WebViewScreen(url = postUrl)
        }

        composable(
            route = GreenJuiceScreen.FAVORITES.name
        ) { 
            FavoritesScreen(
                //accountId = accountId,
                favoritesList = viewModel.favoritesList,
                onItemClick = { url ->
                    try {
                        Log.d(TAG, "url : $url")
                        val args = url
                            .replace("http://", "").replace("https://", "")
                            .split('/')
                        val (base_url, blogId, postId) = arrayOf(args[0], args[1], args[2])

                        Log.d(TAG, "url : $url, base_url : $base_url, blogId: $blogId, postId : $postId")

                        navController.navigate(GreenJuiceScreen.WEB_VIEW.name + "/$base_url/$blogId/$postId")
                    } catch(e: Exception) {
                        Toast.makeText(context, "해당 블로그 포스트에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                addFavorites = { postId -> viewModel.addFavorites(accessTokenState.value, refreshTokenState.value, postId) },
                deleteFavorites = { postId -> viewModel.deleteFavorites(accessTokenState.value, refreshTokenState.value, postId) }
            )
        }
    }
}