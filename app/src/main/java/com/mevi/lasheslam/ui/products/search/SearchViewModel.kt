package com.mevi.lasheslam.ui.products.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val firestore = Firebase.firestore

    var selectedSection by mutableStateOf(Section.CURSOS)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var rawItems by mutableStateOf(listOf<ServiceItem>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var listenerRegistration: ListenerRegistration? = null

    init {
        loadSection(Section.CURSOS)
    }

    fun onSectionChanged(section: Section) {
        selectedSection = section
        loadSection(section)
    }

    fun onSearchChanged(query: String) {
        searchQuery = query
    }

    val filteredItems: List<ServiceItem>
        get() = rawItems.filter {
            it.titulo.contains(searchQuery, ignoreCase = true)
        }

    private fun loadSection(section: Section) {
        isLoading = true
        listenerRegistration?.remove()

        val documentName = when (section) {
            Section.CURSOS -> "curse"
            Section.PRODUCTOS -> "products"
            Section.SERVICIOS -> "services"
        }

        listenerRegistration = firestore
            .collection("data")
            .document(documentName)
            .collection("items")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    rawItems = snapshot.documents.mapNotNull { it.toObject(ServiceItem::class.java) }
                }
                isLoading = false
            }
    }
}