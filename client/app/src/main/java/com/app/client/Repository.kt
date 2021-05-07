package com.app.client

import java.util.*

class Repository {

    companion object {
        private var userId : UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public fun initialize(id : UUID) {
        userId = id
    }

    public fun initializeWithUserRegistration() : UUID {
        userId = UUID.randomUUID()//API get new user id

        return userId;
    }
}