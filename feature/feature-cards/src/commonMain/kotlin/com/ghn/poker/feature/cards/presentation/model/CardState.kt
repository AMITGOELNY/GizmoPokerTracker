package com.ghn.poker.feature.cards.presentation.model

import com.ghn.gizmodb.common.models.Card

data class CardState(val card: Card, val disabled: Boolean)
