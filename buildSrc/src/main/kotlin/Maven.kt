import org.gradle.api.artifacts.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

fun Element.applyConfigurationDependenciesToMavenPom(
        configuration: Configuration,
        scope: String = "compile"
) {
    val document = ownerDocument

    val dependenciesElement = getElementsByTagName("dependencies")
            .asList()
            .filterIsInstance<Element>()
            .first()

    val dependenciesNodes = document.createElementsForConfigurationDependencies(
            configuration, scope
    )

    for(dependency in dependenciesNodes)
        dependenciesElement.appendChild(dependency)
}

fun Document.createElementsForConfigurationDependencies(
        configuration: Configuration,
        scope: String
): List<Element> = configuration.allDependencies
        .filter { (it is ProjectDependency) || (it !is SelfResolvingDependency) }
        .map {
            createElement("dependency").apply {
                appendChild(createNode("groupId", it.group))
                appendChild(createNode("artifactId", it.name))
                appendChild(createNode("version", it.version))
                appendChild(createNode("scope", scope))

                if(it is ExternalDependency) {
                    if(it.excludeRules.isNotEmpty()) {
                        appendChild(createElementsForExcludeRules(
                                it.excludeRules
                        ))
                    }
                }
            }
        }

fun Document.createElementsForExcludeRules(
        excludeRules: MutableSet<ExcludeRule>
): Node {
    return createElement("exclusions").apply {
        for (excludeRule in excludeRules) {
            appendChild(createElement("exclusion").apply {
                appendChild(createNode("artifactId", excludeRule.module?.takeIf { it.isNotBlank() } ?: "*"))
                appendChild(createNode("groupId", excludeRule.group?.takeIf { it.isNotBlank() } ?: "*"))
            })
        }
    }
}


fun NodeList.asList() = (0..length).map { item(it) }

inline fun Document.createNode(name: String, value: Any?): Element {
    return createElement(name).apply {
        setTextContent(value.toString())
    }
}