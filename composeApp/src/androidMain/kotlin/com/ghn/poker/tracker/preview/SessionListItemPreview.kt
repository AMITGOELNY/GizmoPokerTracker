package com.ghn.poker.tracker.preview;

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionListItem
import kotlin.time.Clock

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SessionListItem_Preview() {
    GizmoTheme {
        Surface(Modifier.padding(vertical = Dimens.grid_2)) {
            SessionListItem(
                SessionData(Clock.System.now(), "2000", "4000", Venue.HARD_ROCK_FL)
            )
        }
    }
}
