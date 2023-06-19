package br.com.devsrsouza.kotlinbukkitapi.scoreboard

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.utility.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

@DslMarker
@Retention(AnnotationRetention.BINARY)
public annotation class ScoreboardDSLMarker

@ScoreboardDSLMarker
public inline fun WithPlugin<*>.scoreboard(
        title: String,
        block: ScoreboardDSLBuilder.() -> Unit
): ScoreboardDSL = plugin.scoreboard(title, block)

@ScoreboardDSLMarker
public inline fun Plugin.scoreboard(
        title: String,
        block: ScoreboardDSLBuilder.() -> Unit
): ScoreboardDSL = scoreboard(title, this, block)

/**
 *
 * onRender events: Called when show/set the scoreboard to a player
 * onUpdate events: Called when updateDelay trigger or force update by using [ScoreboardDSL.updateTitle],
 * [ScoreboardDSL.updateLine], [ScoreboardDSL.updateLines].
 */
@ScoreboardDSLMarker
public inline fun scoreboard(
        title: String,
        plugin: Plugin,
        block: ScoreboardDSLBuilder.() -> Unit
): ScoreboardDSL = ScoreboardDSLBuilder(plugin, title).apply(block)

internal val linesBounds = 1..16

public interface ScoreboardDSL {

    public val players: Map<Player, Objective>

    /**
     * Show/set the built scoreboard to a [player]
     */
    public fun show(player: Player)

    /**
     * Update the title to all players with the scoreboard set see [show])
     */
    public fun updateTitle()

    /**
     * Update a specific line to all players with the scoreboard set see [show])
     *
     * Returns false if the line doesn't exists, true if the line was founded and update.
     */
    public fun updateLine(line: Int): Boolean

    /**
     * Update all lines to all players with the scoreboard set (see [show])
     */
    public fun updateLines()

    /**
     * Remove all scoreboard of the players and cancel all internal tasks
     */
    public fun dispose()
}

public class ScoreboardDSLBuilder(internal val plugin: Plugin, public var title: String) : ScoreboardDSL {

    private val lines = mutableMapOf<Int, ScoreboardLine>()
    private val _players = plugin.onlinePlayerMapOf<Objective>()
    override val players: Map<Player, Objective> = _players

    private var titleController: ScoreboardTitle? = null

    private var taskTitle: BukkitTask? = null
    private var taskLine: BukkitTask? = null

    public var updateTitleDelay: Long = 0
        set(value) {
            field = value;
            taskTitle?.cancel(); taskTitle = null
            if (value > 0 && _players.isNotEmpty())
                taskTitle = scheduler { updateTitle() }.runTaskTimer(plugin, 0, value)
        }

    public var updateLinesDelay: Long = 0
        set(value) {
            field = value;
            taskLine?.cancel(); taskLine = null
            if (value > 0 && _players.isNotEmpty())
                taskLine = scheduler { updateLines() }.runTaskTimer(plugin, 0, value)
        }

    override fun dispose() {
        taskTitle?.cancel()
        taskLine?.cancel()

        for (objective in players.values) {
            objective.unregister()
        }
    }

    public fun line(line: Int, scoreboardLine: ScoreboardLine) {
        if (line in linesBounds)
            lines.put(line, scoreboardLine)
    }

    /**
     * set a [text] to specified [line] of the scoreboard.
     */
    public fun line(line: Int, text: String): Unit = line(line, text, block = {})

    /**
     * set a [text] to specified [line] (1 to 16) of the scoreboard with a builder.
     *
     * In the builder you can use [ScoreboardLine.onRender] to change the value
     * when the line renders to the player, or [ScoreboardLine.onUpdate] when you call [updateLine] or
     * specify a value greater then 0 to [updateTitleDelay] to update all lines periodic.
     *
     * If [line] be greater then 16 or less then 1 the line will be ignored.
     */
    @ScoreboardDSLMarker
    public inline fun line(
            line: Int,
            text: String,
            block: ScoreboardLine.() -> Unit
    ): Unit = line(line, ScoreboardLine(this, text).apply(block))

    /**
     * add a array of lines at scoreboard starting at the [startInLine] value.
     */
    public fun lines(vararg lines: String, startInLine: Int = 1) {
        lines(*lines, startInLine = startInLine, block = {})
    }

    /**
     * Add a array of lines at scoreboard starting at the [startInLine] value with a builder.
     *
     * In the builder you can use [ScoreboardLine.onRender] to change the value
     * when the line renders to the player, or [ScoreboardLine.onUpdate] when you call [updateLine] or
     * specify a value greater then 0 to [updateTitleDelay] to update all lines periodic.
     */
    @ScoreboardDSLMarker
    public inline fun lines(vararg lines: String, startInLine: Int = 1, block: ScoreboardLine.() -> Unit) {
        for ((index, line) in lines.withIndex()) {
            line(index + startInLine, line, block)
        }
    }

    public fun remove(line: Int): Boolean {
        return lines.remove(line) != null
    }

    public fun titleController(title: ScoreboardTitle): ScoreboardTitle = title.also {
        titleController = it
    }

    /**
     * The DSL block to manage how the title of the scoreboard will be displayed to a specific player.
     */
    @ScoreboardDSLMarker
    public inline fun title(block: ScoreboardTitle.() -> Unit): ScoreboardTitle = titleController(ScoreboardTitle(this).apply(block))

    override fun show(player: Player) {
        val max = lines.keys.maxOrNull()
        if (max != null) {
            if (_players.get(player)?.scoreboard != null) return
            val sb = requireNotNull(Bukkit.getScoreboardManager()).newScoreboard

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
            _players.put(player, objective) {
                if (_players.isEmpty()) {
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

    private val lineColors = (0..15).map {
        it.toByte().toString(2).take(4).map {
            if(it == '0') ChatColor.RESET.toString()
            else ChatColor.WHITE.toString()
        }.joinToString("")
    }

    private fun entryByLine(line: Int) = lineColors[line]

    private inline fun lineBuild(objective: Objective, line: Int, lineTextTransformer: (ScoreboardLine) -> String) {
        val sb = requireNotNull(objective.scoreboard)
        val sbLine = lines[line] ?: ScoreboardLine(this, "")

        val lineEntry = entryByLine(line)
        val realScoreLine = 17 - line

        val text = lineTextTransformer(sbLine)

        val team = sb.getTeam("line_$line") ?: sb.registerNewTeam("line_$line")

        if (text.isEmpty()) {
            if (!team.prefix.isNullOrEmpty()) team.prefix = ""
            if (!team.suffix.isNullOrEmpty()) team.suffix = ""
            return
        }

        if (text.length > 16) {
            val fixedText = if (text.length > 32) text.take(32) else text
            val prefix = fixedText.substring(0, 16)
            val suffix = fixedText.substring(16, fixedText.length-1)
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

    override fun updateTitle() {
        for ((player, objective) in _players) {
            val titleUpdate = TitleUpdate(player, title).also { titleController?.updateEvent?.invoke(it) }
            objective.displayName = titleUpdate.newTitle
        }
    }

    override fun updateLine(line: Int): Boolean {
        if (lines[line] == null) return false
        for ((player, objective) in _players) {
            lineBuild(objective, line) { sbLine ->
                if (sbLine.updateEvent != null) {
                    LineUpdate(player, sbLine.text).also { sbLine.updateEvent?.invoke(it) }.newText
                } else sbLine.text
            }
        }
        return true
    }

    override fun updateLines() {
        val max = lines.keys.maxOrNull()
        if (max != null) {
            for (i in 1..max) {
                updateLine(i)
            }
        }
    }
}

public interface PlayerScoreboard { public val player: Player }

public interface ChangeableTitle { public var newTitle: String }

public class TitleRender(override val player: Player, override var newTitle: String) : PlayerScoreboard, ChangeableTitle
public class TitleUpdate(override val player: Player, override var newTitle: String) : PlayerScoreboard, ChangeableTitle

public typealias TitleRenderEvent = TitleRender.() -> Unit
public typealias TitleUpdateEvent = TitleUpdate.() -> Unit

public class ScoreboardTitle(private val scoreboard: ScoreboardDSLBuilder) {
    internal var renderEvent: TitleRenderEvent? = null
    internal var updateEvent: TitleUpdateEvent? = null

    @ScoreboardDSLMarker
    public fun onRender(block: TitleRenderEvent) {
        renderEvent = block
    }

    @ScoreboardDSLMarker
    public fun onUpdate(block: TitleUpdateEvent) {
        updateEvent = block
    }
}

public interface LineChangeableText { public var newText: String }

public class LineRender(override val player: Player, override var newText: String) : PlayerScoreboard, LineChangeableText
public class LineUpdate(override val player: Player, override var newText: String) : PlayerScoreboard, LineChangeableText

public typealias LineRenderEvent = LineRender.() -> Unit
public typealias LineUpdateEvent = LineUpdate.() -> Unit

public class ScoreboardLine(private val scoreboard: ScoreboardDSLBuilder, text: String) {
    public var text: String = text
        internal set

    internal var renderEvent: LineRenderEvent? = null
    internal var updateEvent: LineUpdateEvent? = null

    @ScoreboardDSLMarker
    public fun onRender(block: LineRenderEvent) {
        renderEvent = block
    }

    @ScoreboardDSLMarker
    public fun onUpdate(block: LineUpdateEvent) {
        updateEvent = block
    }
}
