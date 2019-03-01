package com.appmattus

import javax.script.ScriptEngineManager

object Kts {
    private val engine by lazy {
        ScriptEngineManager().getEngineByName("kotlin")
    }

    fun eval(script: String) = engine.eval(script)
}
