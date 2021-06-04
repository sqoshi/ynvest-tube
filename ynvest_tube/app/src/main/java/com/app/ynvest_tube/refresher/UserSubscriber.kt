package com.app.ynvest_tube.refresher

import com.app.ynvest_tube.model.User

data class UserSubscriber(val successful: (User) -> Unit)