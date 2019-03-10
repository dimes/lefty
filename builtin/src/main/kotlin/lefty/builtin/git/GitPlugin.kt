package lefty.builtin.git

import lefty.pipeline.Step

fun gitPlugin(environment: Map<String, String>): Step {
    return Step(
            "cf31599d7188",
            environment,
            listOf(
                    "[ \"$(ls -A .)\" ] || git clone \"\$CUSTOM_GIT_REPO\" .",
                    "git clean -ffdx",
                    "git reset --hard",
                    "git checkout \"\$CUSTOM_GIT_BRANCH\"",
                    "git pull"
            )
    )
}
