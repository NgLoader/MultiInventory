package de.ngloader.multiinventory.autosave;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.MultiInventoryUtil;
import de.ngloader.multiinventory.event.MultiInventorySavePlayerEvent;
import de.ngloader.multiinventory.listener.PlayerListener;
import de.ngloader.multiinventory.storage.ExtensionInventory;
import net.imprex.storage.api.StorageApi;

public class MultiInventoryAutoSaveHandler {

	private static final Queue<Player> SAVE_QUEUE = new LinkedList<Player>();

	private static final Thread saveQueueThread;

	private static Integer taskId;

	static {
		saveQueueThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bukkit.getConsoleSender().sendMessage("§8[§4M§culti§4I§cnv§8] Saving all inventorys from online players§8.");

				Integer queueDelay = MultiInventory.getConfiguration().autoSaveQueueDelay;

				while(!MultiInventoryAutoSaveHandler.SAVE_QUEUE.isEmpty()) {
					Player player = MultiInventoryAutoSaveHandler.SAVE_QUEUE.poll();

					if(player == null || !player.isOnline())
						continue;

					try {
						if(PlayerListener.NO_SAVE_WHILE_ERROR.contains(player.getUniqueId())) {
							PlayerListener.NO_SAVE_WHILE_ERROR.remove(player.getUniqueId());
							continue;
						}

						MultiInventorySavePlayerEvent inventorySavePlayerEvent = new MultiInventorySavePlayerEvent(player, player.getWorld(), MultiInventoryUtil.getPlayerConfig(player));
						Bukkit.getPluginManager().callEvent(inventorySavePlayerEvent);

						StorageApi.getStorageService().getExtension(ExtensionInventory.class).setInventory(player, player.getWorld(), inventorySavePlayerEvent.getPlayerConfig());
					} catch(Exception e) {
						e.printStackTrace();

						PlayerListener.NO_SAVE_WHILE_ERROR.add(player.getUniqueId());

						try {
							player.kickPlayer(MultiInventory.getConfiguration().kickReason);
						} catch(Exception e2) {
							player.kickPlayer("MultiInventory: FATAL ERROR");
						}
					}

					try {
						Thread.sleep(queueDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Bukkit.getConsoleSender().sendMessage("§8[§4M§culti§4I§cnv§8] Saved all inventorys from online players§8.");
			}
		}, "MultiInventory - AutoSave Queue");
		saveQueueThread.setDaemon(true);
	}

	public static void check() {
		if(MultiInventory.getConfiguration().autoSave)
			MultiInventoryAutoSaveHandler.start();
		else
			MultiInventoryAutoSaveHandler.stop();
	}

	public static void stop() {
		if(MultiInventoryAutoSaveHandler.taskId == null)
			return;

		Bukkit.getScheduler().cancelTask(taskId);
	}

	public static void start() {
		if(MultiInventoryAutoSaveHandler.taskId != null)
			return;

		//(20 * ?) is the convertion from ticks to seconds
		Integer delay = 20 * MultiInventory.getConfiguration().autoSaveDelay;

		MultiInventoryAutoSaveHandler.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MultiInventory.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().isEmpty() || saveQueueThread.isAlive())
					return;

				MultiInventoryAutoSaveHandler.SAVE_QUEUE.addAll(Bukkit.getOnlinePlayers());
				MultiInventoryAutoSaveHandler.saveQueueThread.run();
			}
		}, delay, delay);
	}
}