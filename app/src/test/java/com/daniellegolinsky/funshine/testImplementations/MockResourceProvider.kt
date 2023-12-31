package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.utilities.IResourceProvider

class MockResourceProvider: IResourceProvider {
    override fun getString(id: Int): String {
        return "You requested ID: $id"
    }

    override fun getString(id: Int, formatString: String): String {
        return "You requested ID: $id with format: $formatString"
    }

    override fun getString(id: Int, formatInt: Int): String {
        return "You requested ID: $id with format: $formatInt"
    }
}