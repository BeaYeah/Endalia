package com.beayeah.endalia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beayeah.endalia.apis.ApiClient
import com.beayeah.endalia.entities.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm

    fun loadEmployees() {
        viewModelScope.launch {
            _employees.value = ApiClient.getUsersFromApi()
        }
    }

    val filteredEmployees: StateFlow<List<Employee>> =
        _searchTerm.combine(_employees) { searchTerm, employees ->
            if (searchTerm.isBlank()) {
                employees
            } else {
                employees.filter { employee ->
                    employee.name.contains(searchTerm, ignoreCase = true) ||
                            employee.lastName.contains(searchTerm, ignoreCase = true) ||
                            employee.jobTitle.contains(searchTerm, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchTerm(term: String) {
        _searchTerm.value = term
    }
}
