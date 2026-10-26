package com.briefly.core.viewmodels

import androidx.lifecycle.ViewModel
import com.briefly.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    private val _allArticles = MutableStateFlow<List<Article>>(emptyList())
    val allArticles: StateFlow<List<Article>> = _allArticles.asStateFlow()

    private val _bookmarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    val bookmarkedArticles: StateFlow<List<Article>> = _bookmarkedArticles.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun setAllArticles(articles: List<Article>) {
        _allArticles.value = articles
    }

    fun toggleBookmark(article: Article) {
        val current = _bookmarkedArticles.value
        _bookmarkedArticles.value = if (current.any { it.id == article.id }) {
            current.filter { it.id != article.id }  // remove
        } else {
            current + article                        // add
        }
    }

    fun isBookmarked(articleId: String): Boolean {
        return _bookmarkedArticles.value.any { it.id == articleId }
    }
}