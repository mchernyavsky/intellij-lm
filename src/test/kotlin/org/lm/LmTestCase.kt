package org.lm

interface LmTestCase {

    fun getTestDataPath(): String

    companion object {
        val testResourcesPath = "src/test/resources"
    }
}
