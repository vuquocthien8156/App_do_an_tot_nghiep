package com.example.qthien.t__t.presenter.pre_login

import com.example.qthien.t__t.interactor.InCheckExist
import com.example.qthien.t__t.view.view_login.ICheckExistAccount

class PreCheckExist(var iCheckExistAccount: ICheckExistAccount) : IPreCheckExist {
    override fun failure(message: String) {
        iCheckExistAccount.failure(message)
    }

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {
        iCheckExistAccount.resultExistAccount(email , id_fb , phone)
    }


    fun checkExistAccount(word : String){
        InCheckExist(this).checkExistAccount(word)
    }
}