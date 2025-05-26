package com.amaurysdm.codequest.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amaurysdm.codequest.R

@Composable
fun UpDownKeys(
    modifier: Modifier = Modifier,
    initialNumber: MutableIntState = mutableIntStateOf(1)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { initialNumber.intValue++ },
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_upward_24),
                contentDescription = "Increase",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "${initialNumber.intValue}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        IconButton(
            onClick = {
                if (initialNumber.intValue > 1) initialNumber.intValue--
            },
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_downward_24),
                contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpDownKeysPreview() {
    UpDownKeys()
}