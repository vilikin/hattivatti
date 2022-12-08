package link.hattivatti.app.common.color

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RgbColorTest {
    @Test
    fun `should convert to XyColor`() {
        assertThat(RgbColor.GREEN.toXyColor()).isEqualTo(
            XyColor(0.30034356486185654, 0.5926780238884538)
        )
    }
}
