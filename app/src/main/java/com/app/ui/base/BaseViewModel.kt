package com.app.ui.base

import androidx.lifecycle.ViewModel
import com.app.usecase.errors.ErrorManager
import javax.inject.Inject


abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}
