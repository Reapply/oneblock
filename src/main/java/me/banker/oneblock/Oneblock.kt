package me.banker.oneblock

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Oneblock : JavaPlugin() {
    private lateinit var joinListener: JoinListener
    private var taskRunning = false

    override fun onEnable() {
        saveDefaultConfig()
        joinListener = JoinListener(this)
        server.pluginManager.registerEvents(joinListener, this)
        joinListener.startColorChangingTask()
    }

    override fun onDisable() {
        stopBlockGivingTask()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name.equals("startblocks", true)) {
            if (!taskRunning) {
                startBlockGivingTask()
                sender.sendMessage("Random block distribution started.")
            } else {
                sender.sendMessage("Random block distribution is already running.")
            }
            return true
        } else if (command.name.equals("stopblocks", true)) {
            stopBlockGivingTask()
            sender.sendMessage("Random block distribution stopped.")
            return true
        }
        return false
    }

    private fun startBlockGivingTask() {
        val interval = config.getInt("settings.block_interval", 60) * 20L // Convert seconds to ticks
        taskRunning = true

        // Schedule a repeating task for block distribution
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            for (player in server.onlinePlayers) {
                if (player.hasPermission("oneblock.receive")) {
                    giveRandomBlock(player)
                }
            }
        }, 0L, interval)

        // Schedule a repeating task for updating BossBar countdown
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            for (player in server.onlinePlayers) {
                updatePlayerBar(player)
            }
        }, 0L, 20L) // Update every second
    }

    private fun stopBlockGivingTask() {
        Bukkit.getScheduler().cancelTasks(this)
        taskRunning = false
    }

    private fun giveRandomBlock(player: org.bukkit.entity.Player) {
        val blocks = Material.entries.filter { it.isBlock }
        val randomBlock = blocks[Random().nextInt(blocks.size)]
        player.inventory.addItem(org.bukkit.inventory.ItemStack(randomBlock, 1))
        val randomBlockMessage = (config.getString("settings.random_block_message", "{player} received a {block}!")
            ?: "{player} received a {block}!")
            .replace("{player}", player.name)
            .replace("{block}", randomBlock.name.toLowerCase().replace('_', ' '))
        Bukkit.getServer().onlinePlayers.forEach {
            it.sendMessage(randomBlockMessage)
        }
    }

    private fun updatePlayerBar(player: org.bukkit.entity.Player) {
        val bar = joinListener.getPlayerBar(player)
        if (bar != null) {
            val interval = config.getInt("settings.block_interval", 60)
            val currentProgress = bar.progress * interval
            val newProgress = currentProgress - 1

            if (newProgress <= 0) {
                bar.progress = 1.0
                bar.setTitle("Next block in $interval seconds")
            } else {
                bar.progress = newProgress / interval
                bar.setTitle("Next block in ${newProgress.toInt()} seconds")
            }
        }
    }
}
