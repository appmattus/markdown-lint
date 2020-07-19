package com.appmattus.gherkin.jupiter

import com.appmattus.gherkin.dsl.Gherkin
import com.appmattus.gherkin.dsl.GherkinClass
import com.appmattus.gherkin.dsl.GherkinDsl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@GherkinDsl
@Suppress("DEPRECATION")
fun gherkin(
    @OptIn(ExperimentalCoroutinesApi::class)
    coroutineDispatcher: CoroutineDispatcher = TestCoroutineDispatcher(),
    test: suspend Gherkin.() -> Unit
): Iterable<DynamicNode> {
    contract {
        callsInPlace(test, InvocationKind.EXACTLY_ONCE)
    }

    val executables = mutableListOf<DynamicNode>()

    Gherkin().apply {
        @OptIn(ExperimentalCoroutinesApi::class)
        runBlockingTest(coroutineDispatcher) {
            test()
        }

        executables.addAll(tasks.mapIndexed { index, (name, task) ->
            DynamicTest.dynamicTest(name) {
                @OptIn(ExperimentalCoroutinesApi::class)
                runBlockingTest(coroutineDispatcher) {

                    try {
                        if (index == 0) {
                            before.forEach {
                                GherkinClass.it()
                            }
                        }

                        GherkinClass.task()
                    } finally {
                        if (index == tasks.size - 1) {
                            after.forEach {
                                GherkinClass.it()
                            }
                        }
                    }
                }
            }
        })
    }

    return executables
}

@GherkinDsl
fun <T> Iterable<T>.gherkin(name: (T) -> String, test: Gherkin.(T) -> Unit): List<DynamicNode> {
    return this.map { value ->
        DynamicContainer.dynamicContainer(name(value), gherkin {
            test(this, value)
        })
    }
}

@GherkinDsl
fun <T> Array<T>.gherkin(name: (T) -> String, test: Gherkin.(T) -> Unit): List<DynamicNode> {
    return this.map { value ->
        DynamicContainer.dynamicContainer(name(value), gherkin {
            test(this, value)
        })
    }
}

@GherkinDsl
fun <K, V> Map<K, V>.gherkin(name: (Pair<K, V>) -> String, test: Gherkin.(Pair<K, V>) -> Unit): List<DynamicNode> {
    return this.map { value ->
        DynamicContainer.dynamicContainer(name(value.toPair()), gherkin {
            test(this, value.toPair())
        })
    }
}
