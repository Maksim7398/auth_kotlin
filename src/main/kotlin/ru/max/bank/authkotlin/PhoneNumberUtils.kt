package ru.max.bank.authkotlin

import org.apache.commons.lang3.StringUtils

class PhoneNumberUtils {

    companion object {
        fun toMaskedPhoneNumber(phoneNumber: String?): String? {
            if (StringUtils.isEmpty(phoneNumber) || phoneNumber!!.length != 12) {
                return phoneNumber
            }
            return phoneNumber.take(3) + "*****" + phoneNumber.substring(8)
        }

        fun cleanPhoneNumber(phoneNumber: String): String {
            return phoneNumber.replace("[+\\s]".toRegex(), "")
        }
    }

}