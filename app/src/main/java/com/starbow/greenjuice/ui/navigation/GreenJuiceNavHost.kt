package com.starbow.greenjuice.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.GreenJuiceScreen
import com.starbow.greenjuice.enum.GreenJuiceTheme
import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.ui.AppViewModelProvider
import com.starbow.greenjuice.ui.screen.FilterDialog
import com.starbow.greenjuice.ui.screen.MainScreen
import com.starbow.greenjuice.ui.screen.SearchResultScreen
import com.starbow.greenjuice.ui.screen.SettingScreen
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceNavHostViewModel

@Composable
fun GreenJuiceNavHost(
    navController: NavHostController,
    theme: GreenJuiceTheme,
    changeThemeOption: (GreenJuiceTheme) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GreenJuiceNavHostViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val appUiState = viewModel.uiState.collectAsState()
    val netUiState = viewModel.netUiState

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var selectedJuiceIndex by remember { mutableStateOf(-1) }
    var selectedSentimentIndex by remember { mutableStateOf(-1) }
    var selectedThemeIndex by remember { mutableStateOf(-1) }

    Log.d("FilterState", "AdState = ${appUiState.value.juiceFilterOption} Sentiment = ${appUiState.value.sentimentFilterOption}")

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

    val settingOptions = listOf(
        stringResource(id = GreenJuiceTheme.LIGHT.stringRes),
        stringResource(id = GreenJuiceTheme.DARK.stringRes),
        stringResource(id = GreenJuiceTheme.SYSTEM.stringRes)
    )

    val juiceIndex: Int = when(appUiState.value.juiceFilterOption) {
        null -> 0
        JuiceColor.GREEN -> 1
        JuiceColor.ORANGE -> 2
        JuiceColor.RED -> 3
    }

    val sentimentIndex: Int = when(appUiState.value.sentimentFilterOption) {
        null -> 0
        Sentiment.POSITIVE -> 1
        Sentiment.NEUTRAL -> 2
        Sentiment.NEGATIVE -> 3
    }

    val themeIndex: Int = when(theme) {
        GreenJuiceTheme.LIGHT -> 0
        GreenJuiceTheme.DARK -> 1
        GreenJuiceTheme.SYSTEM -> 2
    }

    if(selectedJuiceIndex == -1) selectedJuiceIndex = juiceIndex
    if(selectedSentimentIndex == -1) selectedSentimentIndex = sentimentIndex
    Log.d("SettingScreen", "** $theme ** $selectedThemeIndex, $themeIndex")

    viewModel.showErrorToast.observe(lifecycleOwner, Observer {
        it.getContentIfNotHandled()?.let {
            Toast.makeText(context, R.string.network_sub_error, Toast.LENGTH_SHORT).show()
        }
    })

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
                onChangeQuery = { viewModel.changeQuery(it) },
                onSearch = {
                    if(viewModel.inputQuery.isBlank())
                        Toast.makeText(context, R.string.empty_query_message, Toast.LENGTH_LONG).show()
                    else {
                        navController.navigate(GreenJuiceScreen.RESULT.name)
                        viewModel.search()
                    }
                },
                onClearQuery = { viewModel.changeQuery("") }
            )
        }

        composable(
            route = GreenJuiceScreen.RESULT.name
        ) {
            if (appUiState.value.onFilter) {
                //필터 버튼을 클릭한 상태라면 필터 화면을 활성화한다.
                FilterDialog(
                    adStateOptions = juiceOptions,
                    selectedJuiceOptionIndex = selectedJuiceIndex,
                    onJuiceChanged = { selectedJuiceIndex = it },
                    sentimentOptions = sentimentOptions,
                    selectedSentimentOptionIndex = selectedSentimentIndex,
                    onSentimentChanged = { selectedSentimentIndex = it },
                    onDismissRequest = {
                        selectedJuiceIndex = juiceIndex
                        selectedSentimentIndex = sentimentIndex

                        viewModel.filterDialogDisabled()
                    },
                    onConfirmButtonClick = {
                        val nextFilterJuice = when (selectedJuiceIndex) {
                            0 -> null
                            1 -> JuiceColor.GREEN
                            2 -> JuiceColor.ORANGE
                            3 -> JuiceColor.RED
                            else -> {
                                //예외 처리
                                null
                            }
                        }

                        val nextFilterSentiment = when (selectedSentimentIndex) {
                            0 -> null
                            1 -> Sentiment.POSITIVE
                            2 -> Sentiment.NEUTRAL
                            3 -> Sentiment.NEGATIVE
                            else -> {
                                //예외 처리
                                null
                            }
                        }

                        viewModel.changeFilterState(
                            adState = nextFilterJuice, sentiment = nextFilterSentiment
                        )
                    },
                    onDismissButtonClick = {
                        selectedJuiceIndex = juiceIndex
                        selectedSentimentIndex = sentimentIndex

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
                onAddDataClick = { viewModel.getAdditionalData() }
            )
        }

        composable(
            route = GreenJuiceScreen.SETTING.name
        ) {
            if(selectedThemeIndex == -1) selectedThemeIndex = themeIndex

            SettingScreen(
                settingOptions = settingOptions,
                selectedSettingOptionIndex = selectedThemeIndex,
                onSelectedChanged = {
                    selectedThemeIndex = it

                    val themeOption: GreenJuiceTheme = when(selectedThemeIndex) {
                        0 -> GreenJuiceTheme.LIGHT
                        1 -> GreenJuiceTheme.DARK
                        2 -> GreenJuiceTheme.SYSTEM
                        else -> {
                            //예외 처리
                            GreenJuiceTheme.SYSTEM
                        }
                    }
                    changeThemeOption(themeOption)
                }
            )
        }
    }
}