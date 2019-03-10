package lefty.builtin.git

import lefty.pipeline.Step

val GIT_PLUGIN = Step(
        "cf31599d7188",
        listOf(
                "[ \"$(ls -A .)\" ] || git clone https://github.com/dimes/lefty.git",
                "git clean -ffdx",
                "git reset --hard",
                "git checkout master",
                "git pull"
        )
)