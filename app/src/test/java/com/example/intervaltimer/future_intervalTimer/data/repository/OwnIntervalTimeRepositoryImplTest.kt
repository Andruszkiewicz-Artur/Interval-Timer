package com.example.intervaltimer.future_intervalTimer.data.repository

import com.example.intervaltimer.future_intervalTimer.data.data_source.OwnIntervalTimeDao
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OwnIntervalTimeRepositoryImplTest {

    private lateinit var repository: OwnIntervalTimeRepositoryImpl
    private lateinit var mockDao: OwnIntervalTimeDao

    private val timer1 = OwnIntervalTime(
        1,
        "timer1",
        10,
        10,
        10,
        10
    )

    private val timer2 = OwnIntervalTime(
        2,
        "timer2",
        10,
        10,
        10,
        10
    )

    @Before
    fun setUp() {
        mockDao = mockk()
        repository = OwnIntervalTimeRepositoryImpl(mockDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllOwnIntervalTimes returns Flow of OwnIntervalTimes`() = runBlocking {
        val ownIntervalTimes = listOf(timer1, timer2)
        coEvery { mockDao.getAllOwnIntervalTimes() } returns flowOf(ownIntervalTimes)

        val result = repository.getAllOwnIntervalTimes().first()

        assertEquals(ownIntervalTimes, result)
    }

    @Test
    fun `insertOwnIntervalTime calls Dao method`() = runBlocking {
        coEvery { mockDao.insertOwnIntervalTime(any()) } just runs

        repository.insertOwnIntervalTime(timer1)

        coVerify { mockDao.insertOwnIntervalTime(timer1) }
    }

    @Test
    fun `deleteOwnIntervalTime calls Dao method`() = runBlocking {
        coEvery { mockDao.deleteOwnIntervalTIme(any()) } just runs

        repository.deleteOwnIntervalTime(timer1)

        coVerify { mockDao.deleteOwnIntervalTIme(timer1) }
    }
}