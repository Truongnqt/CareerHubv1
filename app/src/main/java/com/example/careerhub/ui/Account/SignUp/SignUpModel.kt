package com.example.careerhub.ui.Account.SignUp

class SignUpModel {

    data class SignupModel(
        /**
         * TODO Replace with dynamic value
         */
        var txtSigninto: String? =
            MyApp.getInstance().resources.getString(R.string.msg_sign_in_to_jobiago)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtPosition: String? = MyApp.getInstance().resources.getString(R.string.lbl_username)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtPositionOne: String? = MyApp.getInstance().resources.getString(R.string.lbl_mobile_number)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtPositionTwo: String? =
            MyApp.getInstance().resources.getString(R.string.msg_enter_email_address)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtPositionThree: String? = MyApp.getInstance().resources.getString(R.string.lbl_password)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtUiuxdesigner: String? = MyApp.getInstance().resources.getString(R.string.lbl_or)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtContinuewith: String? =
            MyApp.getInstance().resources.getString(R.string.msg_continue_with_google)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtContinuewith1: String? =
            MyApp.getInstance().resources.getString(R.string.msg_continue_with_facebook)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var txtConfirmation: String? =
            MyApp.getInstance().resources.getString(R.string.msg_already_have_an)
        ,
        /**
         * TODO Replace with dynamic value
         */
        var etUserNameValue: String? = null,
        /**
         * TODO Replace with dynamic value
         */
        var etMobileNumberValue: String? = null,
        /**
         * TODO Replace with dynamic value
         */
        var etEmailValue: String? = null,
        /**
         * TODO Replace with dynamic value
         */
        var etPasswordValue: String? = null
    )

}