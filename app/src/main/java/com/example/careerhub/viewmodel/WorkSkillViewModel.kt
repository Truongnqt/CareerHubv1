package com.example.careerhub.viewmodel
import androidx.lifecycle.ViewModel
import com.example.careerhub.data.repository.WorkSkillRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.example.careerhub.model.WorkSkillRespone

class WorkSkillViewModel : ViewModel(){
   val workSkill : WorkSkillRepository = WorkSkillRepository()
    private val _workSkillList = MutableLiveData<WorkSkillRespone>()
    fun getSkill(email: String){
        viewModelScope.launch {
            workSkill.getSkill(email){
                _workSkillList.postValue(it)
            }
        }
    }
}