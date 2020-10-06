package com.raywenderlich.android.creatures.model

class CompositeItem {

    lateinit var creature: Creature
        private set

    lateinit var header: Header
        private set

    var isHeader = false
        private set

    companion object {
        fun withCreature(creature: Creature): CompositeItem {
            val composite = CompositeItem()
            composite.creature = creature
            return composite
        }
        fun withHeader(header: Header): CompositeItem {
            val composite = CompositeItem()
            composite.header = header
            composite.isHeader = true
            return composite
        }
    }

    override fun toString(): String {
        return if (isHeader) header.name else creature.nickname
    }

}