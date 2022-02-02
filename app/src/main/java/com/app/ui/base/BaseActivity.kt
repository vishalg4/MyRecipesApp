package com.app.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * Inherits [androidx.appcompat.app.AppCompatActivity], Base Activity of all activities created
 * in project to define common functionality.
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Sub classes implement this method to define ViewModels observed by them.
     */
    abstract fun observeViewModel()

    /**
     * Sub classes implements this method to bind View with Activity.
     */
    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
