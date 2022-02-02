package com.app.ui.component.splash

import android.os.Bundle
import android.os.Handler
import com.app.databinding.SplashLayoutBinding
import com.app.ui.base.BaseActivity
import com.app.SPLASH_DELAY
import com.app.ui.component.recipes.RecipesListActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * This activity inherits [BaseActivity], used to splash screen when launching the app.
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: SplashLayoutBinding

    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    override fun observeViewModel() {
    }

    private fun navigateToMainScreen() {
        Handler().postDelayed({
            val nextScreenIntent = RecipesListActivity.getInstance(this)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_DELAY.toLong())
    }
}
