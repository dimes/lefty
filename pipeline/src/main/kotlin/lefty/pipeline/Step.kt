package lefty.pipeline

/**
 * Represents a single step in a pipeline
 */
class Step(
        val image: String,
        val commands: List<String>
)