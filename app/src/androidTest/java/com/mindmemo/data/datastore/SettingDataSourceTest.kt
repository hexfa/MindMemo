package com.mindmemo.data.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SettingDataSourceTest {

    private lateinit var context: Context
    private lateinit var settingDataSource: SettingDataSource

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        settingDataSource = SettingDataSource(context)
    }

    @Test
    fun saveTheme_shouldPersistValue() = runTest {
        // Save value
        settingDataSource.saveTheme(true)

        // Assert value
        val result = settingDataSource.isDarkTheme.first()
        assertTrue(result)
    }

    @Test
    fun saveGridView_shouldPersistValue() = runTest {
        // Save value
        settingDataSource.saveGridView(true)

        // Assert value
        val result = settingDataSource.isGridView.first()
        assertTrue(result)
    }

    @Test
    fun defaultTheme_shouldBeFalse() = runTest {
        val result = settingDataSource.isDarkTheme.first()
        assertFalse(result)
    }

    @Test
    fun defaultGridView_shouldBeFalse() = runTest {
        val result = settingDataSource.isGridView.first()
        assertFalse(result)
    }
}