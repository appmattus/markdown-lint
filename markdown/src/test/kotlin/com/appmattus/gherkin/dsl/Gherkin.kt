package com.appmattus.gherkin.dsl

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@GherkinDsl
class Gherkin internal constructor() {
    internal val before = mutableListOf<suspend GherkinClass.() -> Unit>()
    internal val tasks = mutableListOf<Pair<String, suspend GherkinClass.() -> Unit>>()
    internal val after = mutableListOf<suspend GherkinClass.() -> Unit>()
}

@GherkinDsl
@UseExperimental(ExperimentalContracts::class)
@Suppress("DEPRECATION", "TestFunctionName")
fun Gherkin.Before(block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    before.add(block)
}

@GherkinDsl
@UseExperimental(ExperimentalContracts::class)
@Suppress("DEPRECATION", "TestFunctionName")
fun Gherkin.After(block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    after.add(block)
}

@GherkinDsl
@UseExperimental(ExperimentalContracts::class)
@Suppress("DEPRECATION", "TestFunctionName")
fun Gherkin.Given(description: String, block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    require(description.isNotBlank()) { "'Given' description should not be blank - a meaningful description should be provided." }

    tasks.add("Given $description" to block)
}

@GherkinDsl
@UseExperimental(ExperimentalContracts::class)
@Suppress("DEPRECATION", "TestFunctionName")
fun Gherkin.When(description: String, block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    require(description.isNotBlank()) { "'When' description should not be blank - a meaningful description should be provided." }

    tasks.add("When $description" to block)
}

@GherkinDsl
@UseExperimental(ExperimentalContracts::class)
@Suppress("DEPRECATION", "TestFunctionName")
fun Gherkin.Then(description: String, block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    require(description.isNotBlank()) { "'Then' description should not be blank - a meaningful description should be provided." }

    tasks.add("Then $description" to block)
}

@GherkinDsl
@Suppress("DEPRECATION", "TestFunctionName")
@UseExperimental(ExperimentalContracts::class)
fun Gherkin.And(description: String, block: suspend GherkinClass.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    require(description.isNotBlank()) { "'And' description should not be blank - a meaningful description should be provided." }

    tasks.add("And $description" to block)
}
