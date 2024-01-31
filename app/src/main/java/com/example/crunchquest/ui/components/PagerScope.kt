package com.example.crunchquest.data.components

/**
 * Scope for [Pager] content.
 */
class PagerScope(
    private val state: PagerState,
    val commingPage: Int
) {
    /**
     * Returns the current selected page
     */
    val currentPage: Int
        get() = state.currentPage

    /**
     * Returns the current selected page offset
     */
    val currentPageOffset: Float
        get() = state.currentPageOffset

    /**
     * Returns the current selection state
     */
    val selectionState: SelectionState
        get() = state.selectionState
}

    /**
     * Modifier which scales pager items according to their offset position. Similar in effect
     * to a carousel.
     */