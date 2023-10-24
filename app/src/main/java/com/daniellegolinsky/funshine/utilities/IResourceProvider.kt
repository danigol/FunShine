package com.daniellegolinsky.funshine.utilities

interface IResourceProvider {
    fun getString(id: Int): String
    fun getString(id: Int, formatString: String): String
    fun getString(id: Int, formatInt: Int): String
}
