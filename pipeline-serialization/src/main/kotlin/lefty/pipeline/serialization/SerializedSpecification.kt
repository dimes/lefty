package lefty.pipeline.serialization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import lefty.pipeline.Specification

class SerializedSpecification @JsonCreator constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("steps") val steps: List<SerializedStep>
) {
    fun toSpecification(): Specification {
        return Specification(name, steps.map(SerializedStep::toPipelineStep))
    }
}