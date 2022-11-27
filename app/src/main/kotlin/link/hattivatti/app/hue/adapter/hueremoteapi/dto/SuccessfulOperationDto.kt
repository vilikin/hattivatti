package link.hattivatti.app.hue.adapter.hueremoteapi.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SuccessfulOperationDto<T>(
    @JsonProperty("success")
    val success: T
)
