import org.gradle.api.artifacts.ProjectDependency
import java.io.File
import java.util.Locale

tasks.register("projectDependencyGraph") {
    group = "documentation"
    description = "Generates a layered dependency graph: app(top), feature–domain–data(center L→C→R), core(bottom)."

    doLast {
        val outDir = file("${rootProject.rootDir}/build/dependency-graph").apply { mkdirs() }
        val dotFile = File(outDir, "project.dot").apply { delete() }

        // ---- 분류 -----------------------------------------------------------
        fun Project.layer(): String = when {
            path == ":app"                 -> "app"
            path.startsWith(":feature:")   -> "feature"
            path.startsWith(":domain:")    -> "domain"
            path.startsWith(":data:")      -> "data"
            path.startsWith(":core:")      -> "core"
            else                           -> "ignore"
        }

        val nodes = linkedSetOf<Project>()
        val edges = linkedMapOf<Pair<Project, Project>, MutableList<String>>()

        // 프로젝트/의존성 수집 (test/kapt 제외)
        val q = ArrayDeque<Project>().apply { add(rootProject) }
        while (q.isNotEmpty()) {
            val p = q.removeFirst()
            q.addAll(p.childProjects.values)

            p.configurations
                .filterNot { it.name.contains("test", true) || it.name.startsWith("kapt") }
                .forEach { cfg ->
                    cfg.dependencies.withType(ProjectDependency::class.java)
                        .map { it.dependencyProject }
                        .forEach { dep ->
                            if (p.layer() == "ignore" || dep.layer() == "ignore") return@forEach
                            nodes += p; nodes += dep
                            val key = p to dep
                            val traits = edges.getOrPut(key) { mutableListOf() }
                            val name = cfg.name.lowercase(Locale.ROOT)
                            if (name.endsWith("implementation")) traits += "style=dotted"
                            if (name.endsWith("api"))            traits += "penwidth=2"
                        }
                }
        }

        // ---- 색상 -----------------------------------------------------------
        val fillColor = mapOf(
            "app"     to "#CE93D8", // 보라
            "feature" to "#64B5F6", // 파랑
            "domain"  to "#E57373", // 빨강
            "data"    to "#81C784", // 초록
            "core"    to "#FFD54F"  // 노랑
        )
        fun edgeColor(from: Project): String = (fillColor[from.layer()] ?: "#9E9E9E") + "AA"

        // ---- DOT 생성 -------------------------------------------------------
        dotFile.bufferedWriter().use { w ->
            w.appendLine("digraph G {")
            w.appendLine("""graph [compound=true, dpi=180, bgcolor="#FFFFFF", newrank=true, pack=true];""")
            w.appendLine("""label="${rootProject.name}"; labelloc="t"; fontsize=28; fontname="Helvetica";""")
            w.appendLine("""rankdir=TB; nodesep=0.35; ranksep=1.0; splines=ortho; overlap=prism; concentrate=false;""")
            w.appendLine("""node [shape=box, style=filled, fontname="Helvetica", fontsize=11, color="#424242"];""")
            w.appendLine("""edge [arrowsize=0.6, penwidth=1.1, weight=0, minlen=2];""")

            // 클러스터 유틸 (라벨이 가려지지 않도록 label pad를 둔다)
            fun writeCluster(name: String, label: String, bg: String, fill: String, rank: String) {
                w.appendLine(
                    """subgraph cluster_$name {
                        label="$label";
                        labelloc="t"; labeljust="c";
                        fontsize=20; fontcolor="#263238";
                        rank=$rank; color="$bg";
                        style="rounded,filled"; fillcolor="$fill";
                        margin=10; pad=0.12; // 클러스터 여백 최소화
                        rankdir=TB;
                    """.trimIndent()
                )
                // ordering/aligning anchor
                w.appendLine("""  ${name}_anchor [shape=point, width=0, height=0, label="", style=invis];""")
                // label pad (라벨과 노드 사이 공간 확보용)
                w.appendLine("""  ${name}_labelpad [shape=point, width=0, height=0, label="", style=invis];""")
                w.appendLine("""  ${name}_anchor -> ${name}_labelpad [style=invis, weight=9999, constraint=true, minlen=1];""")

                val layerNodes = nodes.filter { it.layer() == name }.sortedBy { it.path }
                // labelpad가 항상 위에 오도록
                if (layerNodes.isNotEmpty()) {
                    w.appendLine("""  { rank=min; ${name}_labelpad }""")
                }
                layerNodes.forEach { p ->
                    w.appendLine("""  "${p.path}" [fillcolor="${fillColor[name]}"];""")
                    // labelpad 아래로 노드를 밀어내고, anchor로 레이아웃 결속
                    w.appendLine("""  ${name}_labelpad -> "${p.path}" [style=invis, weight=80, constraint=true, minlen=1];""")
                    w.appendLine("""  ${name}_anchor   -> "${p.path}" [style=invis, weight=40, constraint=true];""")
                }
                w.appendLine("}")
            }

            // 상/중/하 클러스터 출력
            writeCluster("app",     "app",     "#E1BEE7", "#F3E5F5", "min")
            writeCluster("feature", "feature", "#BBDEFB", "#E3F2FD", "same")
            writeCluster("domain",  "domain",  "#FFCDD2", "#FFEBEE", "same")
            writeCluster("data",    "data",    "#C8E6C9", "#E8F5E9", "same")
            writeCluster("core",    "core",    "#FFF9C4", "#FFFDE7", "max")

            // 중앙 레이어의 좌→중앙→우 **고정 순서** (강한 보정치)
            w.appendLine("""{ rank=same; feature_anchor; domain_anchor; data_anchor; }""")
            w.appendLine("""feature_anchor -> domain_anchor [style=invis, weight=9999, minlen=6, constraint=true];""")
            w.appendLine("""domain_anchor  -> data_anchor   [style=invis, weight=9999, minlen=6, constraint=true];""")

            w.appendLine("""app_anchor    -> domain_anchor [style=invis, weight=9999, minlen=3, constraint=true];""")
            w.appendLine("""domain_anchor -> core_anchor   [style=invis, weight=9999, minlen=3, constraint=true];""")

            // 실제 의존성 엣지 (출발 모듈 색상으로 엣지 색상)
            edges.forEach { (k, traits) ->
                val color = edgeColor(k.first)
                val attrs = (traits + listOf("""color="$color"""", """weight=0""")).joinToString(",")
                w.appendLine("""  "${k.first.path}" -> "${k.second.path}" [$attrs];""")
            }

            w.appendLine("}")
        }

        // 내보내기 (SVG/PNG)
        fun runDot(fmt: String) {
            val p = ProcessBuilder("dot", "-T$fmt", "-o", "project.$fmt", dotFile.name)
                .directory(outDir)
                .redirectErrorStream(true)
                .start()
            if (p.waitFor() != 0) throw RuntimeException(p.inputStream.bufferedReader().readText())
        }
        runDot("svg")
        runDot("png")

        println("✅ Dependency graph generated:")
        println("   - ${outDir.resolve("dependency.svg")}")
        println("   - ${outDir.resolve("dependency.png")}")
    }
}
