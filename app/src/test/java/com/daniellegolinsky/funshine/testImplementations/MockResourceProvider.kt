package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.utilities.ResourceProvider

class MockResourceProvider: ResourceProvider {
    override fun getString(id: Int): String {
        return "You requested ID: $id"
    }

    override fun getString(id: Int, formatString: String): String {
        return "You requested ID: $id with format: $formatString"
    }
}