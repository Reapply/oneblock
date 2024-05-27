package me.banker.oneblock

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*
import org.bukkit.scheduler.BukkitRunnable

class JoinListener(private val plugin: Oneblock) : Listener {
    private val playerBars: MutableMap<UUID, BossBar> = mutableMapOf()
    private val colors = BarColor.values()
    private var colorIndex = 0

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val welcomeMessage = plugin.config.getString("settings.welcome_message", "Welcome to the server!")
        if (welcomeMessage != null) {
            player.sendMessage(welcomeMessage)
        }

        // Create and show a boss bar to the player
        val bar = Bukkit.createBossBar("Next block in 60 seconds", BarColor.GREEN, BarStyle.SOLID)
        bar.progress = 1.0
        bar.addPlayer(player)
        playerBars[player.uniqueId] = bar
    }

    fun getPlayerBar(player: Player): BossBar? {
        return playerBars[player.uniqueId]
    }

    fun removePlayerBar(player: Player) {
        playerBars.remove(player.uniqueId)?.removeAll()
    }

    fun startColorChangingTask() {
        object : BukkitRunnable() {
            override fun run() {
                for (bar in playerBars.values) {
                    bar.color = colors[colorIndex]
                }
                colorIndex = (colorIndex + 1) % colors.size
            }
        }.runTaskTimer(plugin, 0L, 10L) // Change color every 10 ticks (0.5 seconds)
    }
}
