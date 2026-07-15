import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

// R1: expose Compose testTags as Android resource-ids so Maestro can select them
// via `id:`. Set once at the root here (Android-only); the whole tree inherits it.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainView() = Box(Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }) {
    ChatAppWithScaffold()
}
