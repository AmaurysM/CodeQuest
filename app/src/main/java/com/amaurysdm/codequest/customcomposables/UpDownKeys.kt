package com.amaurysdm.codequest.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amaurysdm.codequest.R

// Composable for the up and down keys
@Preview(showBackground = true)
@Composable
fun UpDownKeys(
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background),
    initialNumber: MutableIntState = mutableIntStateOf(1)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier
            .size(80.dp, 30.dp)
            .clickable { initialNumber.intValue++ }
            .padding(bottom = 5.dp)
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_upward_24),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        CreateText(
            "${initialNumber.intValue}", modifier = Modifier.size(30.dp)
        )
        Box(
            modifier = Modifier
                .size(80.dp, 30.dp)
                .clickable { if (initialNumber.intValue > 1) initialNumber.intValue-- }
                .padding(top = 5.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_downward_24),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}