package com.sarftec.lifelessons.application.tools

class CustomListIterator<T>(private val list: List<T>) {
    private var iterator = -1

    fun hasNext(): Boolean {
        return iterator + 1 >= 0 && iterator + 1 < list.size
    }

    fun hasPrevious() : Boolean {
        return iterator - 1 >= 0 && iterator - 1 < list.size
    }

    fun next(): T {
        iterator += 1
        return list[iterator]
    }

    fun previous() : T {
        iterator -= 1
        return list[iterator]
    }
}