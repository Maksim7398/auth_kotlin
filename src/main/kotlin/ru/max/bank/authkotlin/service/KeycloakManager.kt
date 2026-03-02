package ru.max.bank.authkotlin.service

import org.keycloak.admin.client.resource.UsersResource


interface KeycloakManager {

    fun getUserResource() : UsersResource
}