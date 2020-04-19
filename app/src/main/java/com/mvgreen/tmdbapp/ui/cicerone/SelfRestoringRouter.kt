package com.mvgreen.tmdbapp.ui.cicerone

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import java.util.*

class SelfRestoringRouter : Router() {

    private var stack = LinkedList<Screen>()

    @Suppress("UNCHECKED_CAST")
    fun restore(startScreen: Screen, stateSaved: Boolean) {
        if (stack.isEmpty()) {
            stack.push(startScreen)
            super.newRootChain(*stack.toTypedArray())
        }
        // Пересоздание цепочки требуется только при создании фрагмента с нуля
        if (!stateSaved) {
            super.newRootChain(*stack.reversed().toTypedArray())
        }
    }

    fun reset() {
        stack.clear()
    }

    override fun newChain(vararg screens: Screen?) {
        for (screen in screens) {
            stack.push(screen)
        }
        super.newChain(*screens)
    }

    override fun exit() {
        stack.pop()
        super.exit()
    }

    override fun navigateTo(screen: Screen) {
        stack.push(screen)
        super.navigateTo(screen)
    }

    override fun finishChain() {
        stack.clear()
        super.finishChain()
    }

    override fun backTo(screen: Screen?) {
        do {
            val s = stack.pop()
        } while (s != screen)
        // Возвращаем удаленный на место
        screen?.let { stack.push(it) }

        super.backTo(screen)
    }

    override fun newRootChain(vararg screens: Screen?) {
        stack.clear()
        for (screen in screens) {
            stack.push(screen)
        }
        super.newRootChain(*screens)
    }

    override fun replaceScreen(screen: Screen) {
        stack.pop()
        stack.push(screen)
        super.replaceScreen(screen)
    }

    override fun newRootScreen(screen: Screen) {
        stack.clear()
        stack.push(screen)
        super.newRootScreen(screen)
    }
}