package ru.max.bank.authkotlin

import org.keycloak.representations.idm.UserRepresentation
import ru.max.bank.authkotlin.model.request.RegistrationRequest


fun RegistrationRequest.toUserRepresentation(): UserRepresentation =
    this.let {
        val userRepresentation = UserRepresentation()
        userRepresentation.email = this.email
        userRepresentation.username = this.phoneNumber
        userRepresentation.firstName = this.fullName.split(" ")[1]
        userRepresentation.lastName = this.fullName.split(" ")[0]
        return userRepresentation
    }