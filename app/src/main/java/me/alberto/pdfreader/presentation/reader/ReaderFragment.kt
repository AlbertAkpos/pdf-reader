package me.alberto.pdfreader.presentation.reader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_reader.*
import me.alberto.pdfreader.Document
import me.alberto.pdfreader.R
import me.alberto.pdfreader.framework.MajesticViewModelFactory
import me.alberto.pdfreader.presentation.IntentUtil
import me.alberto.pdfreader.presentation.library.LibraryFragment


class ReaderFragment : Fragment() {

    companion object {
        fun newInstance(document: Document): ReaderFragment = ReaderFragment().apply {
            arguments = ReaderViewModel.createArguments(document)
        }
    }


    private lateinit var viewModel: ReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reader, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = BookmarksAdapter { viewModel.openBookmark(it) }
        bookmarksRecyclerView.adapter = adapter
        viewModel =
            ViewModelProvider(this, MajesticViewModelFactory).get(ReaderViewModel::class.java)

        viewModel.document.observe(viewLifecycleOwner, Observer {
            if (it == Document.EMPTY) {
                //show picker action
                startActivityForResult(
                    IntentUtil.createOpenIntent(),
                    LibraryFragment.READ_REQUEST_CODE
                )
            }
        })

        viewModel.bookmarks.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })

        viewModel.isBookmaked.observe(viewLifecycleOwner, Observer {
            val bookmarkDrawable = if (it) R.drawable.ic_library else R.drawable.ic_library_border
            tabLibrary.setCompoundDrawablesRelativeWithIntrinsicBounds(0, bookmarkDrawable, 0, 0)
        })

        viewModel.currentPage.observe(viewLifecycleOwner, Observer { showPage(it) })
    }

    private fun showPage(page: PdfRenderer.Page) {
        iv_page.visibility = View.VISIBLE
        pagesTextView.visibility = View.VISIBLE
        tabPreviousPage.visibility = View.VISIBLE
        tabNextPage.visibility = View.VISIBLE

        if (iv_page.drawable != null) {
            (iv_page.drawable as BitmapDrawable).bitmap.recycle()
        }

        val size = Point() // Point is used to hold two integer coordinates
        activity?.windowManager?.defaultDisplay?.getSize(size)

        val pageWidth = size.x
        val pageHeight = page.height * pageWidth / page.width

        val bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888)

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        iv_page.setImageBitmap(bitmap)
        pagesTextView.text = getString(
            R.string.page_navigation_format,
            page.index + 1,
            viewModel.renderer.value?.pageCount
        )
        page.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Process open file intent.
        if (requestCode == LibraryFragment.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri -> viewModel.openDocument(uri) }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}