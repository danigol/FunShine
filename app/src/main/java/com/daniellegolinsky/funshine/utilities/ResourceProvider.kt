package com.daniellegolinsky.funshine.utilities

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : IResourceProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getString(id: Int, formatString: String) : String {
        return context.getString(id, formatString)
    }

    override fun getString(id: Int, formatInt: Int) : String {
        return context.getString(id, formatInt)
    }
}

