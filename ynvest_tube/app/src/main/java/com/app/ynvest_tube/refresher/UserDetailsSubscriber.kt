package com.app.ynvest_tube.refresher

import com.app.ynvest_tube.model.UserDetailsResponse

data class UserDetailsSubscriber(val successful: (UserDetailsResponse) -> Unit)