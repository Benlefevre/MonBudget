import androidx.test.ext.junit.runners.AndroidJUnit4
import com.benoitlefevre.monbudget.injection.appModule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class ModuleTest : KoinTest {

    @get: Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Test
    fun checkAllModule() = checkModules {
        modules(appModule)
    }
}