package com.mindmemo.data.di

import com.mindmemo.domain.repository.NoteRepository
import com.mindmemo.domain.repository.SettingRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MemoModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var noteRepository: NoteRepository

    @Inject
    lateinit var settingRepository: SettingRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testNoteRepositoryInjection() {
        Assert.assertNotNull(noteRepository)
    }

    @Test
    fun testSettingRepositoryInjection() {
        Assert.assertNotNull(settingRepository)
    }
}
