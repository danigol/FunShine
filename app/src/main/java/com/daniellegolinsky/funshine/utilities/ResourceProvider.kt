package com.daniellegolinsky.funshine.utilities

interface ResourceProvider {
    fun getString(id: Int): String
    fun getString(id: Int, formatString: String): String
}
