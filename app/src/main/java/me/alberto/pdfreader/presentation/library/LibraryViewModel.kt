package me.alberto.pdfreader.presentation.library

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import me.alberto.pdfreader.Document
import me.alberto.pdfreader.framework.Interactors
import me.alberto.pdfreader.framework.MajesticViewModel

class LibraryViewModel(application: Application, interactors: Interactors) :
    MajesticViewModel(application, interactors) {

    val documents: MutableLiveData<List<Document>> = MutableLiveData()

    fun loadDocuments() {
        // TODO start loading documents
    }

    fun addDocument(uri: Uri) {
        // TODO add a new document
        loadDocuments()
    }

    fun setOpenDocument(document: Document) {
        // TODO set currently open document
    }
}