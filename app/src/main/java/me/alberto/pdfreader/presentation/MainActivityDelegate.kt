package me.alberto.pdfreader.presentation

import me.alberto.pdfreader.Document

interface MainActivityDelegate {
   fun openDocument(document: Document)
}
