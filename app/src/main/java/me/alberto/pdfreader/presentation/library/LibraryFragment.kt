package me.alberto.pdfreader.presentation.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_library.*
import me.alberto.pdfreader.R
import me.alberto.pdfreader.framework.MajesticViewModelFactory
import me.alberto.pdfreader.presentation.IntentUtil.createOpenIntent
import me.alberto.pdfreader.presentation.MainActivityDelegate


class LibraryFragment : Fragment() {


    companion object {
        const val READ_REQUEST_CODE = 100
        fun newInstance() = LibraryFragment()
    }


    private lateinit var viewModel: LibraryViewModel
    private lateinit var mainActivityDelegate: MainActivityDelegate

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw e
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = DocumentsAdapter(glide = Glide.with(this)) {
            mainActivityDelegate.openDocument(it)
        }

        documentsRecyclerView.adapter = adapter

        viewModel =
            ViewModelProvider(this, MajesticViewModelFactory).get(LibraryViewModel::class.java)
        viewModel.documents.observe(viewLifecycleOwner, Observer { adapter.update(it) })
        viewModel.loadDocuments()

        fab.setOnClickListener { startActivityForResult(createOpenIntent(), READ_REQUEST_CODE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri: Uri -> viewModel.addDocument(uri) }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}