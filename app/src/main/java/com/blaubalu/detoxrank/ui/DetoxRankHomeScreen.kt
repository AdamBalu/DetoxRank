package com.blaubalu.detoxrank.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.rank.RankHomeScreen
import com.blaubalu.detoxrank.ui.tasks.home.TasksHomeScreen
import com.blaubalu.detoxrank.ui.tasks.task.TaskViewModel
import com.blaubalu.detoxrank.ui.theme.*
import com.blaubalu.detoxrank.ui.theory.TheoryHomeScreen
import com.blaubalu.detoxrank.ui.timer.TimerHomeScreen
import com.blaubalu.detoxrank.ui.utils.Constants.HIGH_LEVEL_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.HIGH_LEVEL_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.LOW_LEVEL_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.LOW_LEVEL_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.MIN_LEVEL_TO_UNLOCK_SPECIAL_TASKS
import com.blaubalu.detoxrank.ui.utils.AppGuideOverlay
import com.blaubalu.detoxrank.ui.utils.AppGuideState
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType
import com.blaubalu.detoxrank.ui.utils.PopupQueueDisplay
import com.blaubalu.detoxrank.ui.utils.guideSteps
import com.blaubalu.detoxrank.ui.utils.getCurrentLevelFromXP
import com.blaubalu.detoxrank.ui.utils.getCurrentProgressBarProgression
import com.blaubalu.detoxrank.ui.utils.getLevelDrawableId
import kotlinx.coroutines.flow.first
import java.util.Calendar


/**
 * Main content of the app. Handles the first run setup together
 * with calendar initializations and tasks refreshes. Sets up the navigation bars
 * and handles the navigation between individual sections.
 */
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun DetoxRankAppContent(
    windowSize: WindowWidthSizeClass,
    timerService: TimerService,
    modifier: Modifier = Modifier,
    detoxRankViewModel: DetoxRankViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory),
    taskViewModel: TaskViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory),
    achievementViewModel: AchievementViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        detoxRankViewModel.firstRunGetTasks()
        val userXp = detoxRankViewModel.getUserXpPoints()
        val level = getCurrentLevelFromXP(userXp)

        val specialTaskList =
            taskViewModel.getCompletedTasksByDuration(TaskDurationCategory.Special).first()
        val noSpecialTasksCompleted = specialTaskList.none { it.completed }

        // load special tasks if user just reached the level
        if (level >= MIN_LEVEL_TO_UNLOCK_SPECIAL_TASKS && noSpecialTasksCompleted && !taskViewModel.wereTasksOpened.value) {
            taskViewModel.selectSpecialTasks()
            taskViewModel.wereTasksOpened.value = true
        }

        val calendarDaily = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Daily)
        }
        val calendarWeekly = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Weekly)
            firstDayOfWeek = Calendar.MONDAY
        }
        val calendarMonthly = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Monthly)
        }
        detoxRankViewModel.refreshTasks(calendarDaily, calendarWeekly, calendarMonthly)
    }

    val detoxRankUiState = detoxRankViewModel.uiState.collectAsState().value
    val userDataUiState = detoxRankViewModel.userDataUiState.collectAsState().value
    val onTabPressed =
        { section: Section -> detoxRankViewModel.updateCurrentSection(section = section) } // TODO reset home screen states if needed

    val navigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            DetoxRankNavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            DetoxRankNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            DetoxRankNavigationType.PERMANENT_NAVIGATION_DRAWER
        }

        else -> {
            DetoxRankNavigationType.BOTTOM_NAVIGATION
        }
    }

    val navigationItemContentList = listOf(
        NavigationItemContent(
            section = Section.Rank,
            image = ImageVector.vectorResource(id = R.drawable.rank_points_icon),
            text = stringResource(id = R.string.tab_rank)
        ),
        NavigationItemContent(
            section = Section.Tasks,
            image = ImageVector.vectorResource(id = R.drawable.tasksnavicon),
            text = stringResource(id = R.string.tab_tasks)
        ),
        NavigationItemContent(
            section = Section.Timer,
            image = ImageVector.vectorResource(id = R.drawable.timernavicon),
            text = stringResource(id = R.string.tab_timer)
        ),
        NavigationItemContent(
            section = Section.Theory,
            image = ImageVector.vectorResource(id = R.drawable.theorynavicon),
            text = stringResource(id = R.string.tab_theory),
        )
    )

    // which side each freshly opened section slides in from: navigating to a
    // tab further right slides in from the right, and vice versa
    val currentSection = detoxRankUiState.currentSection
    val previousSection = remember { mutableStateOf(currentSection) }
    val slideDirection = if (currentSection.ordinal >= previousSection.value.ordinal) 1 else -1
    LaunchedEffect(currentSection) { previousSection.value = currentSection }

    DetoxRankTheme(
        theme = userDataUiState.selectedTheme,
        section = detoxRankUiState.currentSection
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .themeTexture(
                        effectiveUiTheme(
                            userDataUiState.selectedTheme,
                            detoxRankUiState.currentSection
                        )
                    )
            ) {
            CompositionLocalProvider(LocalSectionSlideDirection provides slideDirection) {
            when (detoxRankUiState.currentSection) {
                Section.Rank -> {
                    RankHomeScreen(
                        navigationItemContentList = navigationItemContentList,
                        onTabPressed = onTabPressed,
                        navigationType = navigationType,
                        detoxRankUiState = detoxRankUiState,
                        achievementViewModel = achievementViewModel,
                        detoxRankViewModel = detoxRankViewModel
                    )
                }

                Section.Tasks -> {
                    TasksHomeScreen(
                        modifier = modifier,
                        timerService = timerService,
                        detoxRankUiState = detoxRankUiState,
                        detoxRankViewModel = detoxRankViewModel,
                        achievementViewModel = achievementViewModel,
                        taskViewModel = taskViewModel,
                        navigationType = navigationType,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList
                    )
                }

                Section.Timer -> {
                    TimerHomeScreen(
                        timerService = timerService,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList,
                        navigationType = navigationType,
                        detoxRankUiState = detoxRankUiState,
                        detoxRankViewModel = detoxRankViewModel,
                        achievementViewModel = achievementViewModel
                    )
                }

                Section.Theory -> {
                    TheoryHomeScreen(
                        modifier = modifier,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList,
                        navigationType = navigationType,
                        detoxRankUiState = detoxRankUiState,
                        detoxRankViewModel = detoxRankViewModel
                    )
                }
            }
            }
            }
        }
    }

    // Popup overlay for rank-ups and achievements
    PopupQueueDisplay(theme = userDataUiState.selectedTheme)

    // celebratory offer of freshly added catalog tasks after an app update
    val newTasksCount = detoxRankViewModel.newCatalogTasksCount.value
    if (newTasksCount > 0) {
        DetoxRankTheme(
            theme = userDataUiState.selectedTheme,
            section = detoxRankUiState.currentSection
        ) {
            NewTasksDialog(
                count = newTasksCount,
                onAccept = { detoxRankViewModel.addNewCatalogTasks() },
                onDismiss = { detoxRankViewModel.dismissNewCatalogTasks() }
            )
        }
    }

    // interactive tour: auto-starts for new users, replayable via the help icon
    val guideContext = LocalContext.current
    LaunchedEffect(Unit) {
        if (!AppGuideState.wasShown(guideContext)) AppGuideState.start()
    }
    val guideStep = AppGuideState.step.value
    if (guideStep >= 0) {
        LaunchedEffect(guideStep) {
            guideSteps.getOrNull(guideStep)?.section?.let { onTabPressed(it) }
        }
        DetoxRankTheme(
            theme = userDataUiState.selectedTheme,
            section = detoxRankUiState.currentSection
        ) {
            AppGuideOverlay(
                step = guideStep,
                onAdvance = {
                    if (guideStep >= guideSteps.lastIndex) {
                        AppGuideState.markShown(guideContext)
                        AppGuideState.step.value = -1
                    } else {
                        AppGuideState.step.value = guideStep + 1
                    }
                },
                onSkip = {
                    AppGuideState.markShown(guideContext)
                    AppGuideState.step.value = -1
                }
            )
        }
    }
}

/** "Woohoo, new tasks!" offer shown to existing users after an update */
@Composable
fun NewTasksDialog(
    count: Int,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    val themeStyle = LocalThemeStyle.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = themeStyle.cardShape ?: MaterialTheme.shapes.large,
            border = themeStyle.cardBorder,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(text = "🎉", fontSize = 42.sp)
                Text(
                    text = "Woohoo — new tasks!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = "$count fresh tasks just arrived to spice up your rotation. " +
                            "Add them to the mix?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Later")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = onAccept) {
                        Text("Add them!", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/** -1 or +1: which side the freshly opened section's content slides in from */
val LocalSectionSlideDirection = staticCompositionLocalOf { 1 }

/**
 * Unified screen entrance: every section's content slides in from the side
 * the user navigated from, giving switches a consistent sense of direction.
 * Plays exactly once per section change (the screen composable is fresh),
 * so it cannot misfire the way per-item scroll animations did.
 */
@Composable
fun SectionContentEntrance(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val direction = LocalSectionSlideDirection.current
    val appear = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = appear,
        enter = slideInHorizontally(
            animationSpec = tween(320, easing = FastOutSlowInEasing)
        ) { fullWidth -> direction * fullWidth / 3 } + fadeIn(animationSpec = tween(260)),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Floating themed dock: the theme's card shape, border and tonal surface with
 * the section texture peeking around it. The selected section expands into a
 * labelled pill with a bouncy icon; the rest sit quietly dimmed.
 */
@Composable
fun DetoxRankBottomNavigationBar(
    currentTab: Section,
    onTabPressed: ((Section) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    val themeStyle = LocalThemeStyle.current
    Surface(
        shape = themeStyle.cardShape ?: RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 10.dp)
            .height(64.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp)
        ) {
            navigationItemContentList.forEach { navItem ->
                val selected = currentTab == navItem.section
                val pillColor by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    } else {
                        Color.Transparent
                    },
                    animationSpec = tween(250),
                    label = ""
                )
                val iconScale by animateFloatAsState(
                    targetValue = if (selected) 1.15f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = ""
                )
                val iconAlpha by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.55f,
                    animationSpec = tween(250),
                    label = ""
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(pillColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabPressed(navItem.section) }
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .size(25.dp)
                            .scale(iconScale)
                            .alpha(iconAlpha)
                    )
                }
            }
        }
    }
}

/**
 * Component that displays Navigation Rail
 */
@Composable
fun DetoxRankNavigationRail(
    currentTab: Section,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
    onTabPressed: ((Section) -> Unit) = {}
) {
    NavigationRail(modifier = modifier.fillMaxHeight()) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.section,
                onClick = { onTabPressed(navItem.section) },
                icon = {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 2.dp)
                            .size(25.dp)
                    )
                },
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
        }
    }
}

/**
 * Component that displays Navigation Drawer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    selectedDestination: Section,
    onTabPressed: ((Section) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(12.dp)
    ) {
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = selectedDestination == navItem.section,
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .size(30.dp)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.section) },
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
        }
    }
}

@Composable
fun DetoxRankTopAppBar(
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val uiState by detoxRankViewModel.uiState.collectAsState()
    val currentLevel = uiState.currentLevel
    var showThemeSelector by remember { mutableStateOf(false) }
    var showThemeShop by remember { mutableStateOf(false) }

    // Get theme state
    val userState by detoxRankViewModel.userDataUiState.collectAsState()
    val currentTheme = userState.selectedTheme
    val purchasedThemesString = userState.purchasedThemes
    val purchasedThemes = remember(purchasedThemesString) {
        val names = purchasedThemesString.split(",").map { it.trim() }
        if ("ALL" in names) {
            // Awesome Supporter flag: every theme, including future additions
            UiTheme.values().toSet()
        } else {
            names.mapNotNull {
                try { UiTheme.valueOf(it) } catch (e: IllegalArgumentException) { null }
            }.toSet()
        }
    }


    LaunchedEffect(Unit) {
        val xpPoints = detoxRankViewModel.getUserXpPoints()
        val currentLevelToUpdate = getCurrentLevelFromXP(xpPoints = xpPoints)
        detoxRankViewModel.setCurrentLevel(currentLevelToUpdate)

        val progress = getCurrentProgressBarProgression(xpPoints)
        detoxRankViewModel.setLevelProgressBar(progress)
    }

    // restart the fill animation on level-up so the fresh bar fills upward from
    // zero instead of sliding backwards from the previous level's progress
    val progressAnim = remember(currentLevel) { Animatable(0f) }
    LaunchedEffect(currentLevel, uiState.levelProgressBarProgression) {
        progressAnim.animateTo(
            uiState.levelProgressBarProgression,
            ProgressIndicatorDefaults.ProgressAnimationSpec
        )
    }
    val animatedProgress = progressAnim.value

    val levelBadgeSize: Dp
    val xpBarPaddingStart: Dp
    val xpBarPaddingTop: Dp
    val xpBarHeight: Dp
    when (currentLevel) {
        in LOW_LEVEL_LOWER_CAP..LOW_LEVEL_UPPER_CAP -> {
            levelBadgeSize = 42.dp
            xpBarPaddingStart = 32.dp
            xpBarPaddingTop = 12.dp
            xpBarHeight = 25.dp
        }

        in HIGH_LEVEL_LOWER_CAP..HIGH_LEVEL_UPPER_CAP -> {
            levelBadgeSize = 65.dp
            xpBarPaddingStart = 45.dp
            xpBarPaddingTop = 25.dp
            xpBarHeight = 40.dp
        }

        else -> {
            levelBadgeSize = 40.dp
            xpBarPaddingStart = 30.dp
            xpBarPaddingTop = 25.dp
            xpBarHeight = 45.dp
        }
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Box {
                Image(
                    painterResource(getLevelDrawableId(currentLevel)),
                    null,
                    modifier = Modifier
                        .size(levelBadgeSize)
                        .zIndex(1f)
                )

                if (currentLevel != 25) {
                    val angledBars = LocalThemeStyle.current.angledBars
                    ThemedProgressBar(
                        progress = animatedProgress,
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        angled = angledBars,
                        straightShape = RoundedCornerShape(2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .height(xpBarHeight)
                            .padding(
                                // the slanted start corner needs a nudge left to stay
                                // tucked under the level badge
                                start = xpBarPaddingStart - if (angledBars) 8.dp else 0.dp,
                                end = 16.dp,
                                top = xpBarPaddingTop
                            )
                            .fillMaxWidth(0.35f)
                    )
                }
            }
        }
        
        // Actions and Theme selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            actions()
            ThemeSelectorButton(
                onClick = { showThemeSelector = true }
            )
        }
    }
    
    // Theme selector bottom sheet
    ThemeSelectorSheet(
        isVisible = showThemeSelector,
        currentTheme = currentTheme,
        currentLevel = currentLevel,
        currentRank = detoxRankViewModel.getCurrentRank(userState.rankPoints).first,
        purchasedThemes = purchasedThemes,
        coins = userState.coins,
        onCoinsEarned = { amount -> detoxRankViewModel.addCoins(amount) },
        onCoinUnlock = { theme -> detoxRankViewModel.buyThemeWithCoins(theme) },
        onThemeSelected = { theme ->
            detoxRankViewModel.selectTheme(theme)
            showThemeSelector = false
        },
        onOpenShop = {
            showThemeSelector = false
            showThemeShop = true
        },
        onDismiss = { showThemeSelector = false }
    )

    if (showThemeShop) {
        ThemeShopDialog(
            currentTheme = currentTheme,
            purchasedThemes = purchasedThemes,
            coins = userState.coins,
            onCoinsEarned = { amount -> detoxRankViewModel.addCoins(amount) },
            onCoinUnlock = { theme -> detoxRankViewModel.buyThemeWithCoins(theme) },
            onRedeemCode = { code, onResult ->
                detoxRankViewModel.redeemPromoCode(code, onResult)
            },
            onThemeSelected = { theme ->
                detoxRankViewModel.selectTheme(theme)
                showThemeShop = false
            },
            onDismiss = { showThemeShop = false }
        )
    }
}


data class NavigationItemContent(
    val section: Section,
    val image: ImageVector,
    val text: String
)