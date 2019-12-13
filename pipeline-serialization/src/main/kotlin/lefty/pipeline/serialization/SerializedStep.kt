package lefty.pipeline.serialization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import lefty.builtin.git.gitPlugin
import lefty.pipeline.Step
import java.lang.IllegalArgumentException

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = SerializedStep.BuiltinSerializedStep::class, name = "builtin"),
        JsonSubTypes.Type(value = SerializedStep.CustomSerializedStep::class, name = "custom")
)
interface SerializedStep {
    fun toPipelineStep(): Step

    class BuiltinSerializedStep @JsonCreator constructor(
            @JsonProperty("name") val name: String,
            @JsonProperty("environment") val environment: Map<String, String>
    ) : SerializedStep {
        override fun toPipelineStep(): Step {
            return when (name) {
                "git" -> gitPlugin(environment)
                else -> throw IllegalArgumentException("Unknown built-in $name")
            }
        }
    }

    class CustomSerializedStep @JsonCreator constructor(
            @JsonProperty("image") val image: String,
            @JsonProperty("environment") val environment: Map<String, String>?,
            @JsonProperty("commands") val commands: List<String>
    ) : SerializedStep {
        override fun toPipelineStep(): Step {
            return Step(image, environment ?: emptyMap(), commands)
        }
    }
}