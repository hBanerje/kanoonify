package com.multiplatform.kanoonify.data.mapper

import com.multiplatform.kanoonify.db.Law as DbLaw
import com.multiplatform.kanoonify.domain.model.Law

fun DbLaw.toDomain(): Law {
    return Law(
        id = id,
        title = title,
        category = category,
        description = description,
        punishment = punishment
    )
}

fun Law.toDb(): DbLaw {
    return DbLaw(
        id = id,
        title = title,
        category = category,
        description = description,
        punishment = punishment
    )
}
