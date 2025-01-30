package dev.hyunec.townhall

import kotlin.random.Random

object TestFixture {
    fun email() = "test_${Random.nextInt(1000)}@test.com"
    fun password() = "password_${Random.nextInt(1000)}"
}
