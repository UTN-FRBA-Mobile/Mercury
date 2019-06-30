package ar.edu.utn.frba.mobile.a2019c1.mercury.db

import com.google.firebase.database.DataSnapshot

class DataSnapshotAdapter {
    private var items = mutableListOf<HashMap<String, Any>>()

    fun toHashMapList(dataSnapshot: DataSnapshot) : MutableList<HashMap<String, Any>> {
        val itemsIterator = itemsIterator(dataSnapshot)
        itemsIterator?.forEachRemaining {
            val map = it.getValue() as HashMap<String, Any>
            items.add(map)
        }
        return items
    }

    private fun itemsIterator(dataSnapshot: DataSnapshot): MutableIterator<DataSnapshot>? {
        val iterator = dataSnapshot.children.iterator()
        if (iterator.hasNext()) {
            val listIndex = iterator.next()
            return listIndex.children.iterator()
        }
        return null
    }
}