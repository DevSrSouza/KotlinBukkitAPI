package br.com.devsrsouza.kotlinbukkitapi.dsl

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.runTaskTimer
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.scheduler
import br.com.devsrsouza.kotlinbukkitapi.utils.onlinePlayerMapOf
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

@DslMarker
@Retention(AnnotationRetention.BINARY)
annotation class ScoreboardDSLMarker

@ScoreboardDSLMarker
inline fun scoreboard(
        title: String,
        plugin: Plugin = KotlinBukkitAPI.INSTANCE,
        block: ScoreboardDSL.() -> Unit
) = ScoreboardDSL(plugin, title).apply(block)

val linesBounds = 1..16

class ScoreboardDSL(internal val plugin: Plugin, var title: String) {

    private val lines = mutableMapOf<Int, ScoreboardLine>()
    private val players = onlinePlayerMapOf<Objective>(plugin = plugin)

    private var titleController: ScoreboardTitle? = null

    private var taskTitle: BukkitTask? = null
    private var taskLine: BukkitTask? = null

    var updateTitleDelay: Long = 0
        set(value) {
            field = value;
            taskTitle?.cancel(); taskTitle = null
            if (value > 0 && players.isNotEmpty())
                taskTitle = scheduler { updateTitle() }.runTaskTimer(repeatDelay = value)
        }

    var updateLinesDelay: Long = 0
        set(value) {
            field = value;
            taskLine?.cancel(); taskLine = null
            if (value > 0 && players.isNotEmpty())
                taskLine = scheduler { updateLines() }.runTaskTimer(repeatDelay = value)
        }

    fun line(line: Int, scoreboardLine: ScoreboardLine) {
        if (line in linesBounds)
            lines.put(line, scoreboardLine)
    }

    fun line(line: Int, text: String) = line(line, ScoreboardLine(this, text))

    @ScoreboardDSLMarker
    inline fun line(
            line: Int,
            text: String,
            block: ScoreboardLine.() -> Unit
    ) = line(line, ScoreboardLine(this, text).apply(block))

    fun lines(vararg lines: String, startInLine: Int = 1) {
        lines(*lines, startInLine = startInLine, block = {})
    }

    @ScoreboardDSLMarker
    inline fun lines(vararg lines: String, startInLine: Int = 1, block: ScoreboardLine.() -> Unit) {
        for ((index, line) in lines.withIndex()) {
            line(index + startInLine, line, block)
        }
    }

    fun remove(line: Int): Boolean {
        return lines.remove(line) != null
    }

    fun titleController(title: ScoreboardTitle) = title.also {
        titleController = it
    }

    @ScoreboardDSLMarker
    inline fun title(block: ScoreboardTitle.() -> Unit) = titleController(ScoreboardTitle(this).apply(block))

    fun show(player: Player) {
        val max = lines.keys.max()
        if (max != null) {
            if (players.get(player)?.scoreboard != null) return
            val sb = Bukkit.getScoreboardManager().newScoreboard

            val objective = sb.getObjective(DisplaySlot.SIDEBAR)
                    ?: sb.registerNewObjective("sidebar", "dummy").apply {
                        displaySlot = DisplaySlot.SIDEBAR
                        displayName = TitleRender(player, title).also { titleController?.renderEvent?.invoke(it) }.newTitle
                    }

            for (i in 1..max) {
                lineBuild(objective, i) { sbLine ->
                    if (sbLine.renderEvent != null) {
                        LineRender(player, sbLine.text).also { sbLine.renderEvent?.invoke(it) }.newText
                    } else sbLine.text
                }
            }

            player.scoreboard = sb
            players.put(player, objective) {
                if (players.isEmpty()) {
                    taskTitle?.cancel(); taskTitle = null
                    taskLine?.cancel(); taskLine = null
                }
            }

            if (taskTitle == null && updateTitleDelay > 0)
                updateTitleDelay = updateTitleDelay
            if (taskLine == null && updateLinesDelay > 0)
                updateLinesDelay = updateLinesDelay
        }
    }

    private fun entryByLine(line: Int) = ChatColor.values()[line].toString()

    private inline fun lineBuild(objective: Objective, line: Int, lineTextTranformer: (ScoreboardLine) -> String) {
        val sb = objective.scoreboard
        val sbLine = lines[line] ?: ScoreboardLine(this, "")

        val lineEntry = entryByLine(line)
        val realScoreLine = +(line - 17)

        val text = lineTextTranformer(sbLine)

        val team = sb.getTeam("line_$line") ?: sb.registerNewTeam("line_$line")

        if (text.isEmpty()) {
            if (!team.prefix.isNullOrEmpty()) team.prefix = ""
            if (!team.suffix.isNullOrEmpty()) team.suffix = ""
            return
        }

        if (text.length > 16) {
            val fixedText = if (text.length > 32) text.substring(0, 31) else text
            val prefix = fixedText.substring(0, 15)
            val suffix = fixedText.substring(16, 31)
            if (team.prefix != prefix || team.suffix != suffix) {
                team.prefix = prefix
                team.suffix = suffix
            }
        } else {
            if (team.prefix != text) {
                team.prefix = text
                team.suffix = ""
            }
        }

        if (!team.hasEntry(lineEntry)) team.addEntry(lineEntry)

        val score = objective.getScore(lineEntry)

        if (!(score.isScoreSet && score.score == realScoreLine)) {
            score.score = realScoreLine
        }
    }

    fun updateTitle() {
        for ((player, objective) in players) {
            val titleUpdate = TitleUpdate(player, title).also { titleController?.updateEvent?.invoke(it) }
            objective.displayName = titleUpdate.newTitle
        }
    }

    fun updateLine(line: Int): Boolean {
        if (lines[line] == null) return false
        for ((player, objective) in players) {
            lineBuild(objective, line) { sbLine ->
                if (sbLine.updateEvent != null) {
                    LineUpdate(player, sbLine.text).also { sbLine.updateEvent?.invoke(it) }.newText
                } else sbLine.text
            }
        }
        return true
    }

    fun updateLines() {
        val max = lines.keys.max()
        if (max != null) {
            for (i in 1..max) {
                updateLine(i)
            }
        }
    }
}

interface PlayerScoreboard { val player: Player }

interface ChangeableTitle { var newTitle: String }

class TitleRender(override val player: Player, override var newTitle: String) : PlayerScoreboard, ChangeableTitle
class TitleUpdate(override val player: Player, override var newTitle: String) : PlayerScoreboard, ChangeableTitle

typealias TitleRenderEvent = TitleRender.() -> Unit
typealias TitleUpdateEvent = TitleUpdate.() -> Unit

class ScoreboardTitle(private val scoreboard: ScoreboardDSL) {
    internal var renderEvent: TitleRenderEvent? = null
    internal var updateEvent: TitleUpdateEvent? = null

    @ScoreboardDSLMarker
    fun onRender(block: TitleRenderEvent) {
        renderEvent = block
    }

    @ScoreboardDSLMarker
    fun onUpdate(block: TitleUpdateEvent) {
        updateEvent = block
    }
}

interface LineChangeableText { var newText: String }

class LineRender(override val player: Player, override var newText: String) : PlayerScoreboard, LineChangeableText
class LineUpdate(override val player: Player, override var newText: String) : PlayerScoreboard, LineChangeableText

typealias LineRenderEvent = LineRender.() -> Unit
typealias LineUpdateEvent = LineUpdate.() -> Unit

class ScoreboardLine(private val scoreboard: ScoreboardDSL, text: String) {
    var text: String = text
        internal set

    internal var renderEvent: LineRenderEvent? = null
    internal var updateEvent: LineUpdateEvent? = null

    @ScoreboardDSLMarker
    fun onRender(block: LineRenderEvent) {
        renderEvent = block
    }

    @ScoreboardDSLMarker
    fun onUpdate(block: LineUpdateEvent) {
        updateEvent = block
    }
}
