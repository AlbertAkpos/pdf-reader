package me.alberto.pdfreader.presentation.reader

import android.app.Application
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.alberto.pdfreader.Bookmark
import me.alberto.pdfreader.Document
import me.alberto.pdfreader.framework.Interactors
import me.alberto.pdfreader.framework.MajesticViewModel
import java.io.IOException

class ReaderViewModel(application: Application, interactors: Interactors) :
    MajesticViewModel(application, interactors) {

    companion object {
        private const val DOCUMENT_TAG = "document"
        fun createArguments(document: Document) = bundleOf(DOCUMENT_TAG to document)
    }

    val document = MutableLiveData<Document>()
    val bookmarks = MediatorLiveData<List<Bookmark>>().apply {

    }

    val currentPage = MediatorLiveData<PdfRenderer.Page>()
    val hasPreviousPage: LiveData<Boolean> = Transformations.map(currentPage) {
        it.index > 0
    }

    val hasNextPage: LiveData<Boolean> = Transformations.map(currentPage) {
        renderer.value?.let { renderer -> it.index < renderer.pageCount - 1 }
    }

    val isBookmaked = MediatorLiveData<Boolean>().apply {
        addSource(document) { value = isCurrentPageBookmarked() }
        addSource(currentPage) { value = isCurrentPageBookmarked() }
        addSource(bookmarks) { value = isCurrentPageBookmarked() }
    }

    val isInLibrary: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(document) { value = isInLibrary(it) }
    }

    val renderer = MediatorLiveData<PdfRenderer>().apply {
        addSource(document) {
            try {
                val pdfReader =
                    getFileDescriptor(Uri.parse(it.url))?.let { it1 -> PdfRenderer(it1) }
                value = pdfReader
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileDescriptor(uri: Uri) =
        application.contentResolver.openFileDescriptor(uri, "r")

    private fun isCurrentPageBookmarked() =
        bookmarks.value?.any { it.page == currentPage.value?.index } == true

    private fun isInLibrary(document: Document) = false

    fun loadArguments(arguments: Bundle?) {
        if (arguments == null) {
            return
        }
    }

    fun openDocument(uri: Uri) {
        // TODO open document
    }


    fun openBookmark(bookmark: Bookmark) {
        openPage(bookmark.page)
    }

    private fun openPage(page: Int) {
        renderer.value?.let {
            currentPage.value = it.openPage(page)
        }
    }

    fun nextPage() = currentPage.value?.let { openPage(it.index.plus(1)) }
    fun previousPage() = currentPage.value?.let { openPage(it.index.minus(1)) }
    fun reopenPage() = openPage(currentPage.value?.index ?: 0)

    fun toggleBookmark() {
        // TODO toggle bookmark on the current page
    }

    fun toggleInLibrary() {
        // TODO toggle if open document is in library
    }

}