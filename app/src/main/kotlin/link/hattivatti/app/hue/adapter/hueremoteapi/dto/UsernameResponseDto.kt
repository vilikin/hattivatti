package link.hattivatti.app.hue.adapter.hueremoteapi.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UsernameResponseDto(
    @JsonProperty("username")
    val username: String
)
