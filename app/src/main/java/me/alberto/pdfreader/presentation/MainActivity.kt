package me.alberto.pdfreader.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import me.alberto.pdfreader.Document
import me.alberto.pdfreader.R
import me.alberto.pdfreader.presentation.library.LibraryFragment
import me.alberto.pdfreader.presentation.reader.ReaderFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainActivityDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigationView(savedInstanceState)

    }

    private fun setupNavigationView(savedInstanceState: Bundle?) {
        nav_view.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            nav_view.menu.findItem(R.id.nav_library).isChecked = true
            nav_view.menu.performIdentifierAction(R.id.nav_library, 0)
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_library -> supportFragmentManager.beginTransaction()
                .replace(R.id.content, LibraryFragment.newInstance()).commit()
            R.id.nav_reader -> openDocument(Document.EMPTY)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun openDocument(document: Document) {
        nav_view.menu.findItem(R.id.nav_reader).isChecked = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, ReaderFragment.newInstance(document))
            .commit()
    }
}