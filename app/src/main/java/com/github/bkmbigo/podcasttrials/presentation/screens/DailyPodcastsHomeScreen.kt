package com.github.bkmbigo.podcasttrials.presentation.screens

import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bkmbigo.podcasttrials.R
import com.github.bkmbigo.podcasttrials.ui.theme.andikaFontFamily
import com.github.bkmbigo.podcasttrials.ui.theme.lobsterFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Preview
@Composable
fun DailyPodcastHomeScreen() {

    DailyPodcastHomeScreenContent(
        modifier = Modifier
            .fillMaxSize()
    )
}

private enum class HomeCategory(
    val categoryName: String
) {
    All("All"),
    Android("Android"),
    Kotlin("Kotlin"),
    Web("Web"),
    AI_ML("AI/ML")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DailyPodcastHomeScreenContent(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current

    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

        DailyPodcastHomeGeneralLayout(
            modifier = Modifier
                .fillMaxSize(),
            toggleButton = { onCategoryChanged, _ ->
                IconButton(
                    onClick = { onCategoryChanged() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            },
            topRow = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello,\nBrian",
                        modifier = Modifier
                            .weight(1f),
                        fontSize = 22.sp
                    )

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_brian_mbigo),
                            contentDescription = null
                        )
                    }
                }
            },
            sidePanel = { homeCategory: HomeCategory, onCategoryChanged: (HomeCategory) -> Unit, _ ->
                SidePanelActionCustomLayout(
                    currentCategory = homeCategory,
                    onCategoryChanged = onCategoryChanged,
                    modifier = Modifier
                        .drawBehind {

                            val isRtl = configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

                            val startX = if (!isRtl)
                                0f
                            else
                                size.width

                            val endX = if (!isRtl)
                                size.width
                            else
                                0f

                            val point1X = if (!isRtl)
                                size.width * 5 / 6
                            else
                                size.width * 1 / 9

                            val point2X = if (!isRtl)
                                size.width * 1 / 9
                            else
                                size.width * 5 / 6



                            drawRect(
                                color = colorScheme.primaryContainer
                            )

                            drawPath(
                                path = Path().apply {
                                    moveTo(endX, size.height - 16f)
                                    lineTo(endX, size.height + 32f)

                                    cubicTo(
                                        x1 = point1X,
                                        y1 = size.height + 80f,
                                        x2 = point2X,
                                        y2 = size.height + 20f,
                                        x3 = startX,
                                        y3 = size.height + 120f
                                    )
                                    lineTo(startX, size.height - 16f)
                                    lineTo(endX, size.height - 16f)
                                },
                                color = colorScheme.primaryContainer
                            )
                        }
                )
            },
            content = { homeCategory, _ ->
                DailyPodcastsContent(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DailyPodcastsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            placeholder = {
                Text(
                    text = "Search your favorite podcast"
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {

            val pagerState = rememberPagerState(
                pageCount = { 2 }
            )

            LaunchedEffect(Unit) {
                //  TODO: Handle case where user is already scrolling
                delay(5000)
                if (isActive) {
                    if (pagerState.canScrollForward) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        pagerState.animateScrollToPage(0)
                    }
                }
            }

            Text(
                text = "Latest \uD83D\uDD25",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                key = { it },
                pageSpacing = 16.dp,
                pageSize = PageSize.Fill
            ) { pageCount ->
                when (pageCount) {
                    0 -> {
                        HottestSongItem(
                            imageResource = R.drawable.adb_backdrop,
                            title = "Android Developers Backstage",
                            modifier = Modifier
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    1 -> {
                        HottestSongItem(
                            imageResource = R.drawable.fragmented_backdrop,
                            title = "Fragmented",
                            modifier = Modifier
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            val pagerState = rememberPagerState(
                pageCount = { 2 }
            )
            Text(
                text = "Past Episodes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    HottestSongItem(
                        imageResource = R.drawable.adb,
                        title = "Android Developers Backstage",
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                item {
                    HottestSongItem(
                        imageResource = R.drawable.kotlin_talking_logo,
                        title = "Talking Kotlin",
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                item {
                    HottestSongItem(
                        imageResource = R.drawable.happy_path,
                        title = "Happy Path Programming",
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                item {
                    HottestSongItem(
                        imageResource = R.drawable.fragmented,
                        title = "Fragmented",
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                item {
                    HottestSongItem(
                        imageResource = R.drawable.kotlin_talking_logo,
                        title = "Talking Kotlin",
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@NonRestartableComposable
@Composable
private fun HottestSongItem(
    @DrawableRes imageResource: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                )
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = andikaFontFamily
        )
    }
}

@Composable
private fun SidePanelActionCustomLayout(
    currentCategory: HomeCategory,
    onCategoryChanged: (HomeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current

    val coroutineScope = rememberCoroutineScope()

    val pillTopLeft = remember { Animatable(0f) }
    var pillSize by remember { mutableStateOf(Size.Zero) }

    Layout(
        content = {
            Box(
                modifier = Modifier
                    .drawBehind {

                        val pillRoundedSize = 30f

                        val isRtl = configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

                        val rtlWidthFactor = if (isRtl)
                            (size.width - pillSize.width)
                        else
                            0f

                        val startX = if (!isRtl) 0f else pillSize.width + rtlWidthFactor
                        val endX = if (!isRtl) pillSize.width else size.width - pillSize.width

                        drawPath(
                            path = Path().apply {
                                moveTo(endX, -pillRoundedSize)

                                quadraticBezierTo(
                                    x1 = endX,
                                    y1 = 0f,
                                    x2 = if (!isRtl) {
                                        pillSize.width - pillRoundedSize
                                    } else {
                                        rtlWidthFactor + pillRoundedSize
                                    },
                                    y2 = 0f
                                )

                                lineTo(
                                    x = if (!isRtl) {
                                        pillRoundedSize
                                    } else {
                                        rtlWidthFactor + pillSize.width - pillRoundedSize
                                    },
                                    y = 0f
                                )

                                quadraticBezierTo(
                                    x1 = startX,
                                    y1 = 0f,
                                    x2 = startX,
                                    y2 = pillRoundedSize
                                )

                                lineTo(
                                    x = startX,
                                    y = pillSize.height - pillRoundedSize
                                )

                                quadraticBezierTo(
                                    x1 = startX,
                                    y1 = pillSize.height,
                                    x2 = if (!isRtl) {
                                        pillRoundedSize
                                    } else {
                                        rtlWidthFactor + pillSize.width - pillRoundedSize
                                    },
                                    y2 = pillSize.height
                                )

                                lineTo(
                                    x = if (!isRtl) {
                                        pillSize.width - pillRoundedSize
                                    } else {
                                        rtlWidthFactor + pillRoundedSize
                                    },
                                    y = pillSize.height
                                )

                                quadraticBezierTo(
                                    x1 = endX,
                                    y1 = pillSize.height,
                                    x2 = endX,
                                    y2 = pillSize.height + pillRoundedSize
                                )

                                lineTo(endX, -pillRoundedSize)
                            },
                            color = colorScheme.background
                        )
                    }
            )

            HomeCategory.entries.forEach { category ->
                Surface(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            onCategoryChanged(category)
                        }
                        .layoutId("tab"),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 8.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.categoryName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    val isRtl =
                                        configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

                                    rotationZ = if (!isRtl) {
                                        -90f
                                    } else {
                                        90f
                                    }
                                },
                            maxLines = 1,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        modifier = modifier,
        measurePolicy = object : MeasurePolicy {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {

                val tabs = measurables.drop(1)

                val tabMinWidth =
                    tabs.maxOfOrNull { it.minIntrinsicWidth(constraints.maxHeight) } ?: 45
                val tabHeights = tabs.maxOfOrNull { it.maxIntrinsicHeight(tabMinWidth) } ?: 20

                val pillPlaceable = measurables.take(1).first().measure(constraints)

                val tabPlaceables = tabs.map {
                    it.measure(
                        constraints = Constraints.fixed(
                            width = constraints.maxWidth + 2,
                            height = constraints.maxWidth
                        )
                    )
                }

                val tabMeasuredWidth = tabPlaceables.maxOf { it.width }

                coroutineScope.launch {
                    pillSize = Size(
                        width = tabMeasuredWidth.toFloat() * 3 / 4,
                        height = tabMeasuredWidth.toFloat()
                    )

                    pillTopLeft.animateTo(currentCategory.ordinal * tabMinWidth.toFloat())
                }


                return layout(constraints.maxWidth, tabMinWidth * tabs.size) {
                    pillPlaceable.placeRelative(
                        x = tabMeasuredWidth * 1 / 4,
                        y = pillTopLeft.value.toInt()
                    )

                    tabPlaceables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(
                            x = 0,
                            y = index * tabMinWidth
                        )
                    }
                }
            }

            override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                measurables: List<IntrinsicMeasurable>,
                height: Int
            ): Int {
                return measurables.drop(1).maxOfOrNull { it.minIntrinsicWidth(height) } ?: 45
            }
        }
    )
}

@Composable
private fun DailyPodcastHomeGeneralLayout(
    modifier: Modifier = Modifier,
    toggleButton: @Composable (
        onToggleSidePanel: () -> Unit,
        modifier: Modifier
    ) -> Unit,
    topRow: @Composable () -> Unit,
    sidePanel: @Composable (
        currentCategory: HomeCategory,
        onCurrentCategoryChanged: (HomeCategory) -> Unit,
        modifier: Modifier
    ) -> Unit,
    content: @Composable (
        currentCategory: HomeCategory,
        modifier: Modifier
    ) -> Unit
) {
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()

    val colorScheme = MaterialTheme.colorScheme

    var isSidePanelOpen by remember { mutableStateOf(false) }
    var currentCategory by remember { mutableStateOf(HomeCategory.All) }

    val sidePanelMaxWidth = remember { Animatable(0f) }

    // TODO: find means to query for height
    var topStatusBarHeight by remember { mutableFloatStateOf(0f) }

    Layout(
        content = {
            toggleButton(
                /* onToggleSidePanel = */ {
                    coroutineScope.launch {
                        if (!isSidePanelOpen) {
                            sidePanelMaxWidth.animateTo(0f, tween(800, easing = EaseInQuart))
                        } else {
                            sidePanelMaxWidth.animateTo(1f, tween(800, easing = EaseOutQuart))
                        }
                    }

                    isSidePanelOpen = !isSidePanelOpen
                },
                /* modifier = */ Modifier
            )

            topRow()

            // Top Content Spacer --> The Spacer is not measured nor displayed. Only used to query for height
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))


            // Top Content Curve
            Box(
                modifier = Modifier
                    .drawBehind {
                        val isRtl = configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

                        val pillRoundedSize = 30f

                        val startX = if (!isRtl)
                            0f
                        else
                            size.width

                        val contentEndX = if (!isRtl)
                            size.width
                        else
                            0f

                        val endX = if (!isRtl)
                            size.width * 1.8f
                        else
                            -(size.width * 0.8f)

                        drawPath(
                            path = Path().apply {
                                moveTo(startX, 0f)
                                lineTo(startX, size.height + topStatusBarHeight)
                                lineTo(contentEndX, size.height + topStatusBarHeight)

                                lineTo(contentEndX, topStatusBarHeight + pillRoundedSize)

                                quadraticBezierTo(
                                    x1 = contentEndX,
                                    y1 = topStatusBarHeight,
                                    x2 = if (!isRtl)
                                        contentEndX + pillRoundedSize
                                    else
                                        contentEndX - pillRoundedSize,
                                    y2 = topStatusBarHeight
                                )

                                lineTo(
                                    if (!isRtl)
                                        endX - pillRoundedSize
                                    else
                                        endX + pillRoundedSize,
                                    topStatusBarHeight
                                )

                                quadraticBezierTo(
                                    x1 = endX,
                                    y1 = topStatusBarHeight,
                                    x2 = endX,
                                    y2 = topStatusBarHeight - pillRoundedSize
                                )

                                lineTo(endX, 0f)

                                lineTo(startX, 0f)
                            },
                            color = colorScheme.primaryContainer
                        )
                    }
            )

            sidePanel(
                /* currentCategory = */ currentCategory,
                /* onCurrentCategoryChanged = */ {
                    currentCategory = it
                },
                /* modifier = */ Modifier
            )

            content(
                /*currentCategory = */ currentCategory,
                /* modifier = */ Modifier
            )
        },
        modifier = modifier
    ) { measurables, constraints ->

        val toggleButtonPlaceable = measurables[0].measure(Constraints())

        val sidePanelIntrinsicWidth = measurables[4].maxIntrinsicWidth(constraints.maxHeight)

        val measuredSidePanelWidth = (sidePanelIntrinsicWidth * sidePanelMaxWidth.value).toInt()

        val topRowPlaceable = measurables[1].measure(
            Constraints(
                maxWidth = constraints.maxWidth - sidePanelIntrinsicWidth
            )
        )

        val sidePanelTopPlaceable = measurables[3].measure(
            Constraints.fixed(
                width = measuredSidePanelWidth,
                height = topRowPlaceable.height
            )
        )

        val topStatusBarPlaceable = measurables[2].maxIntrinsicHeight(constraints.maxWidth)

        // Update topStatusBarHeight
        topStatusBarHeight = topStatusBarPlaceable.toFloat()

        val sidePanelPlaceable = measurables[4].measure(
            constraints = Constraints.fixedWidth(
                width = measuredSidePanelWidth
            )
        )

        val contentPlaceable = measurables[5].measure(
            constraints = Constraints.fixed(
                width = constraints.maxWidth - measuredSidePanelWidth,
                height = constraints.maxHeight - topRowPlaceable.height
            )
        )

        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place content behind
            contentPlaceable.placeRelative(
                x = measuredSidePanelWidth,
                y = topStatusBarPlaceable + topRowPlaceable.height
            )

            // Place side panel
            if (sidePanelMaxWidth.value > 0f) {
                sidePanelTopPlaceable.placeRelative(
                    x = 0,
                    y = 0
                )

                sidePanelPlaceable.placeRelative(
                    x = 0,
                    y = topStatusBarPlaceable + topRowPlaceable.height
                )
            }

            // Place Toggle Icon
            toggleButtonPlaceable.placeRelative(
                x = (sidePanelIntrinsicWidth / 2) - (toggleButtonPlaceable.width / 2),
                y = topStatusBarPlaceable + (topRowPlaceable.height - toggleButtonPlaceable.height) / 2
            )

            topRowPlaceable.placeRelative(
                x = sidePanelIntrinsicWidth,
                y = topStatusBarPlaceable
            )
        }
    }

}