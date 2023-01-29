package ml.mops.woolbattle;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import ml.mops.base.MopsPlugin;
import ml.mops.base.commands.Commands;
import ml.mops.utils.Cuboid;
import ml.mops.utils.MopsFiles;
import ml.mops.utils.Translation;
import ml.mops.utils.MopsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Plugin extends MopsPlugin implements Listener, CommandExecutor {

	final BaseItems baseItems = new BaseItems(this);
	Translation translator;

	final List<Block> ppbs = new ArrayList<>();
	World mainworld;
	boolean hardmode = false;
	boolean gameactive = false;

	int redkills, yellowkills, greenkills, bluekills, orangekills, pinkkills = 0;

	int requiredKills = 0;

	boolean gensLocked = false;

	Generator genA = new Generator("A");
	Generator genB = new Generator("B");
	Generator genC = new Generator("C");
	Generator genD = new Generator("D");

	private final HashMap<Player, Integer> combo = new HashMap<>();
	private final HashMap<Player, Integer> deathTimer = new HashMap<>();
	private final HashMap<Player, Player> lastDamager = new HashMap<>();
	private final HashMap<Player, Integer> killCount = new HashMap<>();
	private final HashMap<Player, Integer> expectedMopsCoinMin = new HashMap<>();
	private final HashMap<Player, Integer> expectedMopsCoinMax = new HashMap<>();

	private final HashMap<Player, Boolean> hasWrittenAnything = new HashMap<>();
	private final HashMap<Player, Boolean> globalChat = new HashMap<>();

	private final HashMap<Player, Integer> slimeCooldownSeconds = new HashMap<>();
	private final HashMap<Player, Integer> stickCooldownTicks = new HashMap<>();

	ScoreboardManager manager;
	Scoreboard mainboard;
	Scoreboard newboard;

	List<Player> redTeamPlayers, yellowTeamPlayers, greenTeamPlayers, blueTeamPlayers, orangeTeamPlayers, pinkTeamPlayers = new ArrayList<>();

	int generatorTask;

	BukkitTask worldBorderTask;
	BukkitTask generatorBlockTask;

	boolean testmode = false;
	boolean infinitewool = false;

	String connectToIP = "mopsnet.ml";
	int respawnTime = 4;

	final String colon = ChatColor.WHITE + ": ";

	final List<ItemStack> boomsticks = new ArrayList<>();
	final List<ItemStack> boomsticksMK2 = new ArrayList<>();
	final List<ItemStack> boomsticksMK3 = new ArrayList<>();
	List<ItemStack> platforms = new ArrayList<>();
	List<ItemStack> platforms3d = new ArrayList<>();
	List<ItemStack> slimeballs = new ArrayList<>();
	List<ItemStack> doubleJumpBoots = new ArrayList<>();
	List<ItemStack> shears = new ArrayList<>();

	boolean firstBlood = true;

	private static final double DEGREES_TO_RADIANS = 0.017453292519943295;
	final double BLOCK_ROTATION_DEGREES = 15;
	final double BLOCK_ROTATION_RADIANS = BLOCK_ROTATION_DEGREES * DEGREES_TO_RADIANS;
	final double PI_TIMES_TWO = Math.PI * 2;

	ItemGUI itemGUI = null;

	@Override
	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		this.saveDefaultConfig();
		this.config = this.getConfig();

		StringBuilder data;

		try (Scanner reader = new Scanner(Objects.requireNonNull(getResource("translations.yml")))) {
			data = new StringBuilder();
			while (reader.hasNextLine()) {
				data.append("\n").append(reader.nextLine());
			}
		}

		try {
			this.translation.loadFromString(data.toString());
		} catch (InvalidConfigurationException ignored) { }

		translator = new Translation(translation, getLogger(), "woolbattle");

		itemGUI = new ItemGUI(this);

		Items items = new Items(this);
		platforms.add(items.platform("eng"));
		platforms3d.add(items.platform3d("eng"));
		boomsticksMK2.add(items.boomstickMK2("eng"));
		boomsticksMK3.add(items.boomstickMK3("eng"));

		mainworld = Bukkit.getServer().getWorlds().get(0);
		manager = Bukkit.getScoreboardManager();
		mainboard = manager.getMainScoreboard();
		newboard = manager.getNewScoreboard();

		genA.setBlocks(getBlockCube(new Location(mainworld, 46, 254, -28).getBlock(), 2));
		genB.setBlocks(getBlockCube(new Location(mainworld, -28, 254, -28).getBlock(), 2));
		genC.setBlocks(getBlockCube(new Location(mainworld, -28, 254, 46).getBlock(), 2));
		genD.setBlocks(getBlockCube(new Location(mainworld, 46, 254, 46).getBlock(), 2));

		genA.setLongBlocks(getBlockCube(new Location(mainworld, 46, 254, -28).getBlock(), 3));
		genB.setLongBlocks(getBlockCube(new Location(mainworld, -28, 254, -28).getBlock(), 3));
		genC.setLongBlocks(getBlockCube(new Location(mainworld, -28, 254, 46).getBlock(), 3));
		genD.setLongBlocks(getBlockCube(new Location(mainworld, 46, 254, 46).getBlock(), 3));

		Bukkit.removeRecipe(NamespacedKey.minecraft("red_carpet"));
		Bukkit.removeRecipe(NamespacedKey.minecraft("yellow_carpet"));
		Bukkit.removeRecipe(NamespacedKey.minecraft("lime_carpet"));
		Bukkit.removeRecipe(NamespacedKey.minecraft("light_blue_carpet"));

		for(Entity entity : mainworld.getEntities()) {
			if(entity.getScoreboardTags().contains("generatorTitle")) {
				ArmorStand stand = (ArmorStand) entity;
				stand.setHelmet(new ItemStack(Material.AIR));
			}
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::every5Ticks, 80L, 5L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::everyTick, 80L, 1L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::everySecond, 80L, 20L);

		final boolean[] enableRegen = {false};

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			if(hardmode) {
				if(enableRegen[0]) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0, true, false));
					}
				}
				enableRegen[0] = !enableRegen[0];
			}
		}, 80L, 160L);

		wipeoutWorld(mainworld, 150, 144, 319);
		loadCuboid("https://cdn.discordapp.com/attachments/897853554340020254/1065378410572021843/cubes.txt", mainworld);

		for (Entity entity : mainworld.getEntities()) {
			if (entity.getScoreboardTags().contains("lootstands")) {
				ArmorStand lootstand = (ArmorStand) entity;

				lootstand.setHelmet(new ItemStack(Material.AIR));
			}
		}

		WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

		WebhookEmbed embed = new WebhookEmbedBuilder()
				.setColor(java.awt.Color.GREEN.getRGB())
				.setDescription("\uD83D\uDFE2 `wool-battle` is enabled")
				.build();
		client.send(embed);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}



	@Override
	public void onDisable() {
		WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

		WebhookEmbed embed = new WebhookEmbedBuilder()
				.setColor(java.awt.Color.RED.getRGB())
				.setDescription("\uD83D\uDD34 `wool-battle` is disabled.")
				.build();
		client.send(embed);

		stopGame();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.YELLOW + "Server closed.\nShortly will be back on, maybe.");
		}
	}

	final int[] minutes = {0};
	final int[] seconds = {0};

	final int[] minutes0 = {0};
	final int[] seconds0 = {-1};

	final int[] actualgametime = {0};
	final int[] actualgametime0 = {-1};

	int dominationTime = 3600;
	Teams dominationTeam = Teams.SPECTATOR;
	boolean isDominating = false;
	String dominationEvent = "(NO EVENT | 0:00)";

	String nextevent = ChatColor.RED + "(NO EVENT | 0:00)";
	String nextevent0 = ChatColor.RED + "(NO EVENT | 0:00)";

	int scoreboardTask;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (woolbattleCommands(sender, command, label, args, this)) {
			return true;
		} else {
			return new Commands().commandsExecutor(sender, command, label, args, this);
		}
	}

	public boolean woolbattleCommands(CommandSender sender, Command command, @NotNull String label, String[] args, Plugin plugin) {
		boolean perms = sender.isOp();
		Player player = (Player) sender;
		String commandName = command.getName().toLowerCase(Locale.ROOT);

		if (commandName.equals("spawn") || commandName.equals("lobby") || commandName.equals("l") || commandName.equals("hub")) {
			MopsUtils.sendToServer(this, player, "mopslobby");
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.4F, 1.5F);
			return true;
		}
		if (commandName.equals("setrespawntime")) {
			respawnTime = Integer.parseInt(args[0]);
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.4F, 1.5F);
			return true;
		}
		if(commandName.equals("playsong")) {
			try {

				URI url = URI.create(args[0]);
				try (InputStream inputStream = url.toURL().openStream()) {

					File file = new File("\\song.nbs");
					try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
						int read;
						byte[] bytes = new byte[8192];
						while ((read = inputStream.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);
						}
					}

					Song song = NBSDecoder.parse(file);
					NoteBlockSongPlayer nbsp = new NoteBlockSongPlayer(song);
					nbsp.addPlayer(player);

					nbsp.setPlaying(true);
				}
			} catch (IOException ignored) { }

			return true;
		}
		if(commandName.equals("globalchat")) {
			globalChat.putIfAbsent(player, false);

			if(globalChat.get(player)) {
				player.sendMessage(getByLang(MopsFiles.getLanguage(player), "globalChat.cancel"));
				globalChat.put(player, false);
			} else {
				player.sendMessage(getByLang(MopsFiles.getLanguage(player), "globalChat"));
				globalChat.put(player, true);
			}
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
			return true;
		}
		if (perms) {
			if (commandName.equals("startgame")) {
				startGame(args);
				return true;
			}
			if (commandName.equals("clearblocks")) {
				for (Block block : ppbs) {
					block.setType(Material.AIR);
				}
				return true;
			}
			if (commandName.equals("stopgame")) {
				stopGame();
				return true;
			}
			if (commandName.equals("hardmode")) {
				activateHardmode();
				return true;
			}
			if (commandName.equals("refill")) {
				lootGenerator();

				for (Player allPlayers : mainworld.getPlayers()) {
					allPlayers.sendTitle(getStringByLang(MopsFiles.getLanguage(allPlayers), "refill.activation.1"), getStringByLang(MopsFiles.getLanguage(allPlayers), "refill.activation.2"), 1, 20, 20);
					allPlayers.playSound(allPlayers.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				}
				return true;
			}
			if (commandName.equals("clearscoreboard")) {
				clearScoreboard(player);
				return true;
			}
			if(commandName.equals("testmode")) {
				try {
					if (args[0].toLowerCase(Locale.ROOT).equals("false")) {
						testmode = false;
					} else if (args[0].toLowerCase(Locale.ROOT).equals("true")) {
						testmode = true;
					}
				} catch (IndexOutOfBoundsException error) {
					testmode = !testmode;
				}
				player.sendMessage("testmode был изменён на " + testmode);
				return true;
			}
			if(commandName.equals("addkills")) {
				try {
					switch (args[0]) {
						case "red" -> redkills++;
						case "yellow" -> yellowkills++;
						case "green" -> greenkills++;
						case "blue" -> bluekills++;
					}
				} catch (ArrayIndexOutOfBoundsException error) {
					String teamname = mainboard.getPlayerTeam(player).getName();
					switch (teamname) {
						case "red" -> redkills++;
						case "yellow" -> yellowkills++;
						case "green" -> greenkills++;
						case "blue" -> bluekills++;
					}
				}
				return true;
			}
			if(commandName.equals("dominate")) {
				int time = Integer.parseInt(args[0]);
				Teams team = Teams.valueOf(args[1]);

				dominate(team, time);
				return true;
			}
			if(commandName.equals("infinitewool")) {
				infinitewool = !infinitewool;
				player.sendMessage("you changed infinite wool to " + infinitewool);

				return true;
			}
			if (commandName.equals("cubicstuff")) {
				int radius = Integer.parseInt(args[0]);
				Material material = Material.valueOf(args[1]);

				List<Block> blocclist = getBlockCube(player.getLocation().getBlock(), radius);

				for (Block blocc : blocclist) {
					if (blocc.getType() == Material.AIR) {
						blocc.setType(material);
						ppbs.add(blocc);
					}
				}
				return true;
			}
			if (commandName.equals("items")) {
				player.openInventory(itemGUI.getInventory());
				return true;
			}
			if(commandName.equals("wipeout")) {
				int radius = Integer.parseInt(args[0]);
				boolean confirm = Boolean.parseBoolean(args[1]);
				boolean confirm2 = args[2].equals("CONFIRM");

				if (confirm && confirm2) {
					wipeoutWorld(player.getWorld(), radius, 144, 319);
				}
			}
		} else {
			player.sendTitle(" ", getStringByLang(MopsFiles.getLanguage(player), "noPerms"), 0, 20, 15);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1, 1);
			return true;
		}

		return false;
	}

	@EventHandler
	public void onBlockHitArrow(ProjectileHitEvent event) {
		try {
			if (event.getEntity() instanceof Arrow arrow) {
				Block block = event.getHitBlock();
				if (block.getType() != Material.OAK_LEAVES) {
					if (ppbs.contains(block)) {
						block.setType(Material.AIR);
						arrow.remove();
					}
				}
			}
		} catch (Exception | Error ignored) { }
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!hardmode) {
			event.setFoodLevel(20);
		} else {
			event.setFoodLevel(14);
		}
	}

	private final HashMap<Player, BukkitTask> cancelLastAttacker = new HashMap<>();

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory() == itemGUI.getInventory()) {
			event.setCancelled(true);
			try {
				event.getWhoClicked().getInventory().addItem(itemGUI.getInventory().getItem(event.getSlot()));
			} catch (Exception ignored) { }
		}
	}



	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		try {
			Entity attacker0 = event.getDamager();
			Entity victim0 = event.getEntity();

			if (victim0 instanceof Player victim && attacker0 instanceof Player attacker) {
				event.setDamage(0);

				if(attacker.getScoreboardTags().contains("spectator") || victim.getScoreboardTags().contains("spectator")) {
					event.setCancelled(true);
				}

				if(victim.getScoreboardTags().contains("immunity")) {
					event.setCancelled(true);
					attacker.sendMessage(getByLang(MopsFiles.getLanguage(attacker), "woolbattle.immunity.guideline"));
				}

				if(!mainboard.getPlayerTeam(attacker).getName().equals(mainboard.getPlayerTeam(victim).getName())) {

					if(victim.getHealth()-1 <= 0) {
						simulateHardmodeDeath(victim);
						victim.playSound(victim.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.8F, 1);
					} else {
						if (hardmode) {
							event.setDamage(1);
						}
					}

					if(!victim.getScoreboardTags().contains("immunity")) {
						if (attacker.getScoreboardTags().contains("ingame")) {
							combo.put(attacker, (combo.get(attacker) + 1));
						} else {
							combo.put(attacker, 0);
						}
					}

					Team team = mainboard.getPlayerTeam(attacker);
					String teamname = team.getName();

					if (combo.get(attacker) >= 8) {
						victim.setVelocity(attacker.getEyeLocation().getDirection().multiply(0.75).add(victim.getVelocity()).add(new Vector(0, 0.75, 0)));
						attacker.showTitle(genTitle(MopsFiles.getLanguage(attacker), "woolbattle.combo.title", "woolbattle.combo.subtitle", 2, 20, 15));
						attacker.playSound(attacker.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 2);
						combo.put(attacker, 0);
					}

					if(gameactive) {
						if (victim.getScoreboardTags().contains("ingame")) {
							lastDamager.put(victim, attacker);
						}
					}

					if (cancelLastAttacker.get(victim) != null) {
						cancelLastAttacker.get(victim).cancel();
						cancelLastAttacker.put(victim, null);
					}

					cancelLastAttacker.put(victim, new BukkitRunnable() {
						@Override
						public void run() {
							lastDamager.put(victim, null);
							combo.put(attacker, 0);
						}
					}.runTaskLater(this, 420L));
				}

			} else if(victim0 instanceof Player victim && attacker0 instanceof Projectile projectile) {
				event.setDamage(0);

				Player attacker = (Player) projectile.getShooter();

				if(attacker.getScoreboardTags().contains("spectator") || victim.getScoreboardTags().contains("spectator")) {
					event.setCancelled(true);
				}

				if (victim.getScoreboardTags().contains("immunity")) {
					event.setCancelled(true);
					attacker.sendMessage(getByLang(MopsFiles.getLanguage(attacker), "woolbattle.immunity.guideline"));
				}


				if(!mainboard.getPlayerTeam(attacker).getName().equals(mainboard.getPlayerTeam(victim).getName())) {

					if(victim.getHealth()-1 <= 0) {
						simulateHardmodeDeath(victim);
						broadcastFinalDeath(victim);
						victim.playSound(victim.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.8F, 1);
					} else if (hardmode) {
						event.setDamage(1);
					}

					if (victim != attacker) {
						Team team = mainboard.getPlayerTeam(attacker);
						String teamname = team.getName();

						lastDamager.put(victim, attacker);

						if (cancelLastAttacker.get(victim) != null) {
							cancelLastAttacker.get(victim).cancel();
							cancelLastAttacker.put(victim, null);
						}

						cancelLastAttacker.put(victim, new BukkitRunnable() {
							@Override
							public void run() {
								lastDamager.put(victim, null);
							}
						}.runTaskLater(this, 420L));
					}
				}
			}


		} catch (Exception | Error ignored) { }
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();

		List<Material> matList = new ArrayList<>();
		matList.add(Material.RED_WOOL);
		matList.add(Material.YELLOW_WOOL);
		matList.add(Material.LIME_WOOL);
		matList.add(Material.LIGHT_BLUE_WOOL);

		event.setCancelled(!matList.contains(item.getType()));

		updateLevels(player);
	}

	@EventHandler
	public void onEntityInteract(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();

		Entity entity = event.getRightClicked();

		if(entity.getType() == EntityType.ARMOR_STAND) {
			ArmorStand stand = (ArmorStand) entity;

			if (player.getScoreboardTags().contains("spectator")) {
				event.setCancelled(true);
			} else {
				if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
					player.getInventory().setItem(player.getInventory().getHeldItemSlot(), stand.getHelmet());

				} else {
					player.getInventory().addItem(stand.getHelmet());

				}
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
				stand.setHelmet(new ItemStack(Material.AIR));
			}
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		Material type = item.getType();

		Team team = mainboard.getPlayerTeam(player);
		String teamname = team.getName();

		if((teamname.contains("red") && type == Material.RED_WOOL) || (teamname.contains("yellow") && type == Material.YELLOW_WOOL) ||
				(teamname.contains("green") && type == Material.LIME_WOOL) || (teamname.contains("blue") && type == Material.LIGHT_BLUE_WOOL)) {
			event.setCancelled(player.getInventory().contains(Material.RED_WOOL, 512) || player.getInventory().contains(Material.YELLOW_WOOL, 512) || player.getInventory().contains(Material.LIME_WOOL, 512) || player.getInventory().contains(Material.LIGHT_BLUE_WOOL, 512));
		} else {
			event.setCancelled(true);
		}
		updateLevels(player);
	}

	@EventHandler
	public void onPlayerJoining(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		slimeCooldownSeconds.putIfAbsent(player, 0);
		stickCooldownTicks.putIfAbsent(player, 0);
		killCount.putIfAbsent(player, 0);

		expectedMopsCoinMin.putIfAbsent(player, 1);
		expectedMopsCoinMax.putIfAbsent(player, 3);

		event.setJoinMessage("");
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(player.getName() + " joined the game.");
		}

		if(!gameactive) {
			mainboard.getTeam("nothing").addPlayer(player);

			player.teleport(new Location(mainworld, 9.5, 257, 9.5));
			clearScoreboard(player);

			if (player.getScoreboardTags().contains("ingame")) {
				updateLevels(player);

				player.getScoreboardTags().remove("ingame");
			}
			player.getInventory().clear();
			player.setHealth(20);
			player.setFoodLevel(20);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.setGameMode(GameMode.SURVIVAL);
			player.getScoreboardTags().add("spawn");
			clearScoreboard(player);

			player.getInventory().setHeldItemSlot(0);

			ItemStack leaveButton = new Items(this).leaveButton(MopsFiles.getLanguage(player));
			player.getInventory().setItem(8, leaveButton);

			player.setCollidable(true);

			player.setFlying(false);
			player.setAllowFlight(false);

			player.removeScoreboardTag("spectator");
			clearScoreboard(player);

			player.setPlayerListName((MopsFiles.getRank(player).getPrefix() + player.getName() + " " + MopsFiles.getBadge(player).getSymbol()).trim());

			resetEveryFuckingKillScoreboard(player);
			try {
				Bukkit.getScheduler().cancelTask(deathTimer.get(player));
			} catch (Throwable ignored) { }
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		event.setQuitMessage("");
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(player.getName() + " left the game.");
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Team team = mainboard.getPlayerTeam(player);
		String teamname = team.getName();
		String msg = event.getMessage().replaceAll(":skull:", ChatColor.GRAY + "☠" + ChatColor.RESET);
		hasWrittenAnything.putIfAbsent(player, false);
		globalChat.putIfAbsent(player, false);

		if(MopsFiles.getRank(player).getPermLevel() > 10) {
			msg = MopsUtils.convertColorCodes(msg);
		}

		for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if(msg.toLowerCase(Locale.ROOT).contains(onlinePlayer.getName().toLowerCase(Locale.ROOT))) {
				String[] texts = msg.split(" ");
				int i = 0;
				while (i < texts.length) {
					if(texts[i].toLowerCase(Locale.ROOT).equals(onlinePlayer.getName().toLowerCase(Locale.ROOT))) {
						texts[i] = MopsFiles.getRank(onlinePlayer).getPrefix() + onlinePlayer.getName() + ChatColor.RESET;
						onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
					}
					i++;
				}
				msg = MopsUtils.combineStrings(texts, " ");
			}
		}

		event.setCancelled(true);

		ChatColor color = ChatColor.WHITE;

		if(teamname.contains("red")) {
			color = ChatColor.RED;
		}
		if(teamname.contains("yellow")) {
			color = ChatColor.YELLOW;
		}
		if(teamname.contains("green")) {
			color = ChatColor.GREEN;
		}
		if(teamname.contains("blue")) {
			color = ChatColor.AQUA;
		}

		if(gameactive) {
			if (!player.getScoreboardTags().contains("spectator")) {
				if (!globalChat.get(player)) {
					if (msg.startsWith("!")) {
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.sendMessage(ChatColor.AQUA + "[!] " + color + player.getName() + ChatColor.WHITE + ": " + msg.replaceFirst("!", ""));
						}
						hasWrittenAnything.put(player, true);
					} else {
						if (!hasWrittenAnything.get(player)) {
							player.sendMessage(getByLang(MopsFiles.getLanguage(player), "chat.guideline"));
							hasWrittenAnything.put(player, true);
						}

						for (OfflinePlayer teamPlayer : team.getPlayers()) {
							if (teamPlayer.isOnline()) {
								teamPlayer.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[" + getStringByLang(MopsFiles.getLanguage(player), "woolbattle.team") + "] " + color + player.getName() + ChatColor.WHITE + ": " + color + msg);
							}
						}
					}
				} else {
					if (msg.startsWith("!")) {
						for (OfflinePlayer teamPlayer : team.getPlayers()) {
							if (teamPlayer.isOnline()) {
								teamPlayer.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[" + getStringByLang(MopsFiles.getLanguage(player), "woolbattle.team") + "] " + color + player.getName() + ChatColor.WHITE + ": " + color + msg.replaceFirst("!", ""));
							}
						}
					} else {
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.sendMessage(ChatColor.AQUA + "[!] " + color + player.getName() + ChatColor.WHITE + ": " + msg);
						}
					}
					hasWrittenAnything.put(player, true);
				}
			}

			if (player.getScoreboardTags().contains("spectator")) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (msg.startsWith("!")) {
						msg = msg.replaceFirst("!", "");
					}

					players.sendMessage(ChatColor.GRAY + "[" + getStringByLang(MopsFiles.getLanguage(player), "woolbattle.spectators") + "] " + color + player.getName() + ChatColor.WHITE + ": " + msg);
				}
			}
		} else {
			TextComponent preMessage = Component.text(MopsFiles.getRank(player).getPrefix() + player.getName());
			TextComponent messageBadge = Component.text(MopsFiles.getBadge(player).getSymbol()).hoverEvent(Component.text(MopsFiles.getBadge(player).getDescription()));
			TextComponent afterMessage = Component.text(ChatColor.RESET + ": " + msg.trim());

			TextComponent fullMessage = preMessage.append(messageBadge).append(afterMessage);

			for(Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage(fullMessage);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		if(block.getType() == Material.BRICKS) {
			event.setCancelled(true);
		}
		if(block.getType() == Material.MANGROVE_DOOR) {
			player.sendMessage(ChatColor.RED + "Leaving the server...");
			MopsUtils.sendToServer(this, player, "mopslobby");
			event.setCancelled(true);
		}

		if(player.getScoreboardTags().contains("spectator")) {
			event.setCancelled(true);
		} else {

			List<Block> genBlockList = new ArrayList<>(genA.getBlocks());
			genBlockList.addAll(genB.getBlocks());
			genBlockList.addAll(genC.getBlocks());
			genBlockList.addAll(genD.getBlocks());

			if (!genBlockList.contains(block)) {
				ppbs.add(block);
			}

			updateLevels(player);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Material type = block.getType();
		Player player = event.getPlayer();

		Team team = mainboard.getPlayerTeam(player);
		String teamname = team.getName();

		if(player.getScoreboardTags().contains("spectator") || player.getScoreboardTags().contains("spawn")) {
			event.setCancelled(true);
		} else {

			List<Block> genBlockList = new ArrayList<>(genA.getBlocks());
			genBlockList.addAll(genB.getBlocks());
			genBlockList.addAll(genC.getBlocks());
			genBlockList.addAll(genD.getBlocks());

			if (!player.getScoreboardTags().contains("canbreak") && !genBlockList.contains(block)) {
				event.setCancelled(true);
			}

			boolean materialstuff = (type == Material.WHITE_WOOL);

			if (teamname.contains("red")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.RED_WOOL);
			}
			if (teamname.contains("yellow")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.YELLOW_WOOL);
			}
			if (teamname.contains("green")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.LIME_WOOL);
			}
			if (teamname.contains("blue")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.LIGHT_BLUE_WOOL);
			}
			if (teamname.contains("orange")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.ORANGE_WOOL);
			}
			if (teamname.contains("pink")) {
				materialstuff = (type == Material.WHITE_WOOL || type == Material.MAGENTA_WOOL);
			}

			if (!hardmode) {
				if (genBlockList.contains(block)) {
					if (block.getType() != Material.WHITE_CONCRETE && block.getType() != Material.RED_CONCRETE && block.getType() != Material.YELLOW_CONCRETE && block.getType() != Material.LIME_CONCRETE && block.getType() != Material.LIGHT_BLUE_CONCRETE && block.getType() != Material.ORANGE_WOOL && block.getType() != Material.MAGENTA_WOOL) {
						if (!((teamname.contains("red") && block.getType() == Material.RED_WOOL) || (teamname.contains("yellow") && block.getType() == Material.YELLOW_WOOL) || (teamname.contains("green") && block.getType() == Material.LIME_WOOL) || (teamname.contains("blue") && block.getType() == Material.LIGHT_BLUE_WOOL)) || (teamname.contains("orange") && block.getType() == Material.ORANGE_WOOL) || (teamname.contains("pink") && block.getType() == Material.MAGENTA_WOOL)) {
							event.setCancelled(true);
							block.setType(Material.AIR);

							materialstuff = (type == Material.WHITE_WOOL || type == Material.RED_WOOL || type == Material.YELLOW_WOOL || type == Material.LIME_WOOL || type == Material.LIGHT_BLUE_WOOL || type == Material.ORANGE_WOOL || type == Material.MAGENTA_WOOL);

						} else {
							event.setCancelled(true);
						}
					} else {
						event.setCancelled(true);
					}
				}
			} else {
				event.setCancelled(true);
			}

			if (teamname.contains("red")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.RED_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.RED_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.redWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
			if (teamname.contains("yellow")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.YELLOW_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.YELLOW_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.yellowWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
			if (teamname.contains("green")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.LIME_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.LIME_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.greenWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
			if (teamname.contains("blue")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.LIGHT_BLUE_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.blueWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
			if (teamname.contains("orange")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.ORANGE_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.ORANGE_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.orangeWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
			if (teamname.contains("pink")) {
				if (materialstuff || ppbs.contains(block)) {
					if (!player.getInventory().contains(Material.MAGENTA_WOOL, 512)) {
						ItemStack woolitem = new ItemStack(Material.MAGENTA_WOOL, 1);
						ItemMeta woolmeta = woolitem.getItemMeta();
						woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.pinkWool"));
						woolitem.setItemMeta(woolmeta);
						player.getInventory().addItem(woolitem);
					} else {
						player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "woolLimit", 0, 15, 10));
					}
				} else {
					player.showTitle(genTitle(MopsFiles.getLanguage(player), null, "cantBreak", 0, 15, 10));
				}
			}
		}
		if (ppbs.contains(block)) {
			block.setType(Material.AIR);
		}

		updateLevels(player);
	}


	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		try {
			Player player = event.getPlayer();
			ItemStack item = Objects.requireNonNull(event.getItem());

			if (event.getAction().isRightClick()) {
				if(item.getType() == Material.MANGROVE_DOOR) {
					player.sendMessage(ChatColor.RED + "Leaving the server...");
					MopsUtils.sendToServer(this, player, "mopslobby");
					event.setCancelled(true);
				}
				if (slimeballs.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = hasWool(160, player, teamname);

					if (hasItems) {
						if (slimeCooldownSeconds.get(player) == 0) {
							woolRemove(160, player, teamname);
							slimeCooldownSeconds.put(player, 5);

							Location loc = player.getLocation();
							int height = (int) (loc.getY() - 25);
							height = Math.min(Math.max(height, 170), 319);
							loc.setY(height);

							List<Block> blocclist = new ArrayList<>();
							Arrays.stream(HorizontalFaces).map(loc.getBlock()::getRelative).forEach(blocclist::add);

							for (Block blocc : blocclist) {
								if (blocc.getType().isAir()) {
									blocc.setType(Material.SLIME_BLOCK);
									player.playSound(player.getLocation(), Sound.ENTITY_SLIME_ATTACK, 0.25F, 1);

									Bukkit.getScheduler().runTaskLater(this, () -> blocc.setType(Material.AIR), 120L);
								}

								player.setVelocity(new Vector(0, -1, 0));
							}
						} else {
							player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.onCooldown"));
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}
				}
				if (platforms.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = woolRemove(128, player, teamname);

					if (hasItems) {
						Location loc = player.getLocation();
						int height = (int) (loc.getY() - 5);
						height = Math.min(Math.max(height, 170), 319);
						loc.setY(height);

						Material mat = Material.WHITE_WOOL;

						if (teamname.contains("red")) {
							mat = Material.RED_WOOL;
						}
						if (teamname.contains("yellow")) {
							mat = Material.YELLOW_WOOL;
						}
						if (teamname.contains("green")) {
							mat = Material.LIME_WOOL;
						}
						if (teamname.contains("blue")) {
							mat = Material.LIGHT_BLUE_WOOL;
						}
						if (teamname.contains("orange")) {
							mat = Material.ORANGE_WOOL;
						}
						if (teamname.contains("pink")) {
							mat = Material.MAGENTA_WOOL;
						}

						List<Block> blocklist = new ArrayList<>();
						Arrays.stream(HorizontalFaces).map(loc.getBlock()::getRelative).forEach(blocklist::add);

						for (Block blocc : blocklist) {
							if (blocc.getType() == Material.AIR) {
								blocc.setType(mat);
								ppbs.add(blocc);
								player.playSound(player.getLocation(), Sound.BLOCK_WOOL_PLACE, 0.5F, 1);
							}
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}
				}
				if (platforms3d.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = woolRemove(256, player, teamname);

					if (hasItems) {
						Location loc = player.getLocation();
						int height = (int) (loc.getY() - 10);
						height = Math.min(Math.max(height, 170), 318);
						loc.setY(height);

						Material mat = Material.WHITE_WOOL;

						if (teamname.contains("red")) {
							mat = Material.RED_WOOL;
						}
						if (teamname.contains("yellow")) {
							mat = Material.YELLOW_WOOL;
						}
						if (teamname.contains("green")) {
							mat = Material.LIME_WOOL;
						}
						if (teamname.contains("blue")) {
							mat = Material.LIGHT_BLUE_WOOL;
						}
						if (teamname.contains("orange")) {
							mat = Material.ORANGE_WOOL;
						}
						if (teamname.contains("pink")) {
							mat = Material.MAGENTA_WOOL;
						}

						List<Block> blocklist = new ArrayList<>(getBlockCube(loc.getBlock(), 1));

						for (Block blocc : blocklist) {
							if (blocc.getType() == Material.AIR) {
								blocc.setType(mat);
								ppbs.add(blocc);
								player.playSound(player.getLocation(), Sound.BLOCK_WOOL_PLACE, 0.2F, 1);
							}
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}
				}
			}

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (boomsticks.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = hasWool(28, player, teamname);

					if (hasItems) {
						if (stickCooldownTicks.get(player) == 0) {
							woolRemove(28, player, teamname);
							stickCooldownTicks.put(player, 1);

							double x = player.getEyeLocation().getDirection().getX();
							double z = player.getEyeLocation().getDirection().getZ();
							double y = 0.4;

							x = x * -1.9;
							z = z * -1.9;

							player.setVelocity(player.getVelocity().add((new Vector(x, y, z))));

							Location loc = event.getClickedBlock().getLocation();
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
							for (Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1F, 2F);
							}
						} else {
							player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.onCooldown"));
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}

				} else if (boomsticksMK2.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = hasWool(40, player, teamname);

					if (hasItems) {
						if (stickCooldownTicks.get(player) == 0) {
							woolRemove(28, player, teamname);
							stickCooldownTicks.put(player, 1);

							double x = player.getEyeLocation().getDirection().getX();
							double z = player.getEyeLocation().getDirection().getZ();
							double y = 0.5;

							x = x * -2.3;
							z = z * -2.3;

							player.setVelocity(player.getVelocity().add((new Vector(x, y, z))));

							Location loc = event.getClickedBlock().getLocation();
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
							for (Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1F, 2);
							}
						} else {
							player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.onCooldown"));
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}
				} else if (boomsticksMK3.contains(item)) {
					Team team = mainboard.getPlayerTeam(player);
					String teamname = team.getName();

					boolean hasItems = hasWool(120, player, teamname);

					if (hasItems) {
						if (stickCooldownTicks.get(player) == 0) {
							woolRemove(120, player, teamname);
							stickCooldownTicks.put(player, 1);

							double x = player.getEyeLocation().getDirection().getX();
							double z = player.getEyeLocation().getDirection().getZ();
							double y = 0.8;

							x = x * -4.6;
							z = z * -4.6;

							Location loc = event.getClickedBlock().getLocation();
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);

							player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 0);
							player.playSound(player.getLocation(), Sound.WEATHER_RAIN, 0.5F, 0);
							double finalZ = z;
							double finalX = x;

							Bukkit.getScheduler().runTaskLater(this, () -> {
								loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
								player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 0);
								Bukkit.getScheduler().runTaskLater(this, () -> {
									loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
									player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 0);
									player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_AGITATED, 1, 2);
									Bukkit.getScheduler().runTaskLater(this, () -> {
										loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
										player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1, 0);
										player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1, 1);
										Bukkit.getScheduler().runTaskLater(this, () -> {
											loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
											player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1, 1);
											Bukkit.getScheduler().runTaskLater(this, () -> {
												loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
												player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1, 1);
												player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 0);
												player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);
												player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 2);
												Bukkit.getScheduler().runTaskLater(this, () -> {
													player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 2);
													for (Player players : Bukkit.getOnlinePlayers()) {
														players.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 2);
													}
													player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 2);
													player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 2);
													player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
													player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);
													player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
													player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 0);

													loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
													player.setVelocity(player.getVelocity().add((new Vector(finalX, y, finalZ))));
												}, 16L);
											}, 8L);
										}, 2L);
									}, 4L);
								}, 4L);
							}, 4L);
						} else {
							player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.onCooldown"));
						}
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}


				} else if(shears.contains(item)) {
					Block block = event.getClickedBlock();
					if(genA.getBlocks().contains(block) || genB.getBlocks().contains(block) || genC.getBlocks().contains(block) || genD.getBlocks().contains(block)) {
						Team team = mainboard.getPlayerTeam(player);
						String teamname = team.getName();

						Material mat = Material.AIR;
						if(teamname.contains("red")) {
							mat = Material.RED_WOOL;
						}
						if(teamname.contains("yellow")) {
							mat = Material.YELLOW_WOOL;
						}
						if(teamname.contains("green")) {
							mat = Material.LIME_WOOL;
						}
						if(teamname.contains("blue")) {
							mat = Material.LIGHT_BLUE_WOOL;
						}
						if (teamname.contains("orange")) {
							mat = Material.ORANGE_WOOL;
						}
						if (teamname.contains("pink")) {
							mat = Material.MAGENTA_WOOL;
						}

						if(block.getType() == mat) {
							boolean hasItems = woolRemove(6, player, teamname);
							if (hasItems) {
								block.setType(Material.AIR);
								player.playSound(block.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1, 2);
							} else {
								player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
							}
						}
					}
				}
			}
		} catch (Exception | Error ignored) { }


	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		try {
			Bukkit.getScheduler().runTaskLater(this, () -> event.getPlayer().getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 12)), 2L);
		} catch (Exception ignored) { }
	}

	public void wipeoutWorld(World world, int radius, int bottom, int up) {
		Location loc1 = new Location(world, -radius, bottom, -radius);
		Location loc2 = new Location(world, radius, up, radius);

		Cuboid cuboid = new Cuboid(loc1, loc2);

		for(Block block : cuboid) {
			if(block.getType() != Material.AIR) {
				block.setType(Material.AIR);
			}
		}
	}

	public void loadCuboid(String link, World world) {
		String[] rowArray = new String[] {""};

		try {
			InputStream stream = new URL(link).openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

			StringBuilder stringBuilder = new StringBuilder();

			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(inputLine);
				stringBuilder.append(System.lineSeparator());
			}
			bufferedReader.close();

			rowArray = stringBuilder.toString().split("\n");;
		} catch (IOException ignored) { }

		for (String row : rowArray) {
			String materialtext = row.substring(row.indexOf("[") + 1, row.indexOf("]")).trim();

			String locationString = row.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();
			Material type = Material.valueOf(materialtext);

			String[] xyz = locationString.split(" ");

			double x = Double.parseDouble(String.valueOf(xyz[0]));
			double y = Double.parseDouble(String.valueOf(xyz[1]));
			double z = Double.parseDouble(String.valueOf(xyz[2]));

			Location location = new Location(world, x, y, z);

			String veryRawBlockData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();
			String rawBlockData = veryRawBlockData.substring(veryRawBlockData.indexOf(":") + 1, veryRawBlockData.indexOf("}")).trim();
			BlockData data = Bukkit.createBlockData(rawBlockData);

			location.getBlock().setType(type);
			location.getBlock().setBlockData(data, true);
		}
	}

	public boolean woolRemove(int itemcount, Player player, String teamname) {
		boolean hasItems = infinitewool;

		if(!infinitewool) {
			if (teamname.contains("red")) {
				if (player.getInventory().contains(Material.RED_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.RED_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.redWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
			if (teamname.contains("yellow")) {
				if (player.getInventory().contains(Material.YELLOW_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.YELLOW_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.yellowWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
			if (teamname.contains("green")) {
				if (player.getInventory().contains(Material.LIME_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.LIME_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.greenWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
			if (teamname.contains("blue")) {
				if (player.getInventory().contains(Material.LIGHT_BLUE_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.LIGHT_BLUE_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.blueWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
			if (teamname.contains("orange")) {
				if (player.getInventory().contains(Material.ORANGE_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.ORANGE_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.orangeWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
			if (teamname.contains("pink")) {
				if (player.getInventory().contains(Material.MAGENTA_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.MAGENTA_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.pinkWool"));
					woolitem.setItemMeta(woolmeta);
					player.getInventory().removeItem(woolitem);
					hasItems = true;
				}
			}
		}

		updateLevels(player);

		return hasItems;
	}

	public boolean hasWool(int itemcount, Player player, String teamname) {
		boolean hasItems = infinitewool;

		if(!infinitewool) {
			if (teamname.contains("red")) {
				if (player.getInventory().contains(Material.RED_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.RED_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.redWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
			if (teamname.contains("yellow")) {
				if (player.getInventory().contains(Material.YELLOW_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.YELLOW_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.yellowWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
			if (teamname.contains("green")) {
				if (player.getInventory().contains(Material.LIME_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.LIME_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.greenWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
			if (teamname.contains("blue")) {
				if (player.getInventory().contains(Material.LIGHT_BLUE_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.LIGHT_BLUE_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.blueWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
			if (teamname.contains("orange")) {
				if (player.getInventory().contains(Material.ORANGE_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.ORANGE_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.orangeWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
			if (teamname.contains("pink")) {
				if (player.getInventory().contains(Material.MAGENTA_WOOL, itemcount)) {
					ItemStack woolitem = new ItemStack(Material.MAGENTA_WOOL, itemcount);
					ItemMeta woolmeta = woolitem.getItemMeta();
					woolmeta.displayName(getByLang(MopsFiles.getLanguage(player), "woolbattle.pinkWool"));
					woolitem.setItemMeta(woolmeta);
					hasItems = true;
				}
			}
		}

		return hasItems;
	}

	public ItemStack randomLoot(ItemStack item) {
		int max = 13;
		int min = 1;
		int loot = (int) (Math.random() * (max - min + 1)) + min;

		Items items = new Items(this);

		switch (loot) {
			case 1 -> item = items.shield("eng");
			case 2, 3 -> item = items.axe("eng");
			case 4, 5 -> item = items.potion("eng");
			case 6 -> item = MopsUtils.amount(items.leaves("eng"), 8);
			case 7 -> item = MopsUtils.amount(items.leaves("eng"), 16);
			case 8 -> item = MopsUtils.amount(items.leaves("eng"), 32);
			case 9, 10 -> item = MopsUtils.amount(items.enderpearl("eng"), 1);
			case 11 -> item = MopsUtils.amount(items.enderpearl("eng"), 2);
			case 12 -> {
				item = items.platform("eng");
				platforms.add(item);
			}
			case 13 -> {
				item = items.boomstickMK2("eng");
				boomsticksMK2.add(item);
			}
		}
		return item;
	}

	private static final BlockFace[] HorizontalFaces = new BlockFace[]{
			BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST,
			BlockFace.WEST, BlockFace.SELF, BlockFace.EAST,
			BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST
	};

	public void activateHardmode() {
		hardmode = true;
		mainworld.getWorldBorder().setSize(90, 90);

		cancelDomination();

		worldBorderTask = Bukkit.getScheduler().runTaskLater(this, () -> mainworld.getWorldBorder().setSize(17, 70), 2100L);
		for (Player player : mainworld.getPlayers()) {
			player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "hardmode.activation.1"), getStringByLang(MopsFiles.getLanguage(player), "hardmode.activation.2"), 5, 30, 15);
			player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 0);
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 0);

			player.sendMessage(getByLang(MopsFiles.getLanguage(player), "woolbattle.hardmodeMessage"));

			player.setFoodLevel(14);

			generatorBlockTask = Bukkit.getScheduler().runTaskLater(this, () -> {
				player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.blocked.1"), getStringByLang(MopsFiles.getLanguage(player), "generator.blocked.2"), 5, 30, 15);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F, 0);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F, 1);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F, 2);
			}, 50L);
		}

		gensLocked = true;
	}

	public void clearScoreboard(Player player) {
		Objective fakekills = player.getScoreboard().getObjective("fakekills");

		String you = getStringByLang(MopsFiles.getLanguage(player), "kills.you");

		String redyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("red")) { redyourteam = " " + you; }
		String yellowyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("yellow")) { yellowyourteam = " " + you; }
		String greenyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("green")) { greenyourteam = " " + you; }
		String blueyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("blue")) { blueyourteam = " " + you; }

		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.red") + colon + ChatColor.RED + (redkills) + redyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.yellow") + colon + ChatColor.YELLOW + (yellowkills) + yellowyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.green") + colon + ChatColor.GREEN + (greenkills) + greenyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.blue") + colon + ChatColor.AQUA + (bluekills) + blueyourteam);

		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.red") + colon + ChatColor.RED + (redkills-1) + redyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.yellow") + colon + ChatColor.YELLOW + (yellowkills-1) + yellowyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.green") + colon + ChatColor.GREEN + (greenkills-1) + greenyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "kills.blue") + colon + ChatColor.AQUA + (bluekills-1) + blueyourteam);


		fakekills.getScoreboard().resetScores(ChatColor.RED + " ");

		Bukkit.getScheduler().cancelTask(scoreboardTask);


		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "scoreboardTime") + colon + ChatColor.YELLOW + minutes0[0] + ":" + "0" + seconds0[0] + nextevent0);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "scoreboardTime") + colon + ChatColor.YELLOW + minutes0[0] + ":" + seconds0[0] + nextevent0);

		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + "0" + seconds[0] + nextevent);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + seconds[0] + nextevent);



		fakekills.getScoreboard().resetScores(ChatColor.GOLD + " ");

		String Acopy = getStringByLang(MopsFiles.getLanguage(player), genA.getStatus());
		String Bcopy = getStringByLang(MopsFiles.getLanguage(player), genB.getStatus());
		String Ccopy = getStringByLang(MopsFiles.getLanguage(player), genC.getStatus());
		String Dcopy = getStringByLang(MopsFiles.getLanguage(player), genD.getStatus());

		String Acopy2 = getStringByLang(MopsFiles.getLanguage(player), genA.getStatus());
		String Bcopy2 = getStringByLang(MopsFiles.getLanguage(player), genB.getStatus());
		String Ccopy2 = getStringByLang(MopsFiles.getLanguage(player), genC.getStatus());
		String Dcopy2 = getStringByLang(MopsFiles.getLanguage(player), genD.getStatus());

		if(gensLocked) {
			Acopy = Acopy + ChatColor.GRAY + " ⚠";
			Bcopy = Bcopy + ChatColor.GRAY + " ⚠";
			Ccopy = Ccopy + ChatColor.GRAY + " ⚠";
			Dcopy = Dcopy + ChatColor.GRAY + " ⚠";
		}

		Acopy2 = Acopy2 + genA.getPercent();
		Bcopy2 = Bcopy2 + genB.getPercent();
		Ccopy2 = Ccopy2 + genC.getPercent();
		Dcopy2 = Dcopy2 + genD.getPercent();

		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.a") + " - " + Acopy);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.b") + " - " + Bcopy);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.c") + " - " + Ccopy);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.d") + " - " + Dcopy);

		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.a") + " - " + Acopy2);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.b") + " - " + Bcopy2);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.c") + " - " + Ccopy2);
		fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.d") + " - " + Dcopy2);

		actualgametime[0] = 0;
		actualgametime0[0] = -1;
		seconds[0] = 0;
		seconds0[0] = -1;
		minutes[0] = 0;
		minutes0[0] = 0;

		genA.setStatus("woolbattle.generator.uncaptured");
		genB.setStatus("woolbattle.generator.uncaptured");
		genC.setStatus("woolbattle.generator.uncaptured");
		genD.setStatus("woolbattle.generator.uncaptured");

		nextevent = getStringByLang(MopsFiles.getLanguage(player), "event.refill.1");
		nextevent0 = getStringByLang(MopsFiles.getLanguage(player), "event.refill.1");

		player.setScoreboard(fakekills.getScoreboard());
	}

	public void lootGenerator() {
		for (Entity entity : mainworld.getEntities()) {
			if (entity.getScoreboardTags().contains("lootstands")) {
				ArmorStand lootstand = (ArmorStand) entity;

				Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
					if (lootstand.getHelmet().hasItemMeta()) {
						lootstand.setCustomNameVisible(true);
						ItemStack helmet = lootstand.getHelmet();
						ItemMeta helmetmeta = lootstand.getHelmet().getItemMeta();
						lootstand.setCustomName(helmetmeta.getDisplayName() + " " + ChatColor.DARK_GRAY + "(" + helmet.getAmount() + ")");
						lootstand.setMarker(false);

						List<Entity> nearbyEntities = lootstand.getNearbyEntities(5, 5, 5);
						List<EntityType> nearbyEntityTypes = new ArrayList<>();
						for(Entity entity0 : nearbyEntities) {
							nearbyEntityTypes.add(entity0.getType());
						}
						if(nearbyEntityTypes.contains(EntityType.PLAYER)) {
							lootstand.getWorld().spawnParticle(Particle.WAX_OFF, lootstand.getLocation().clone().add(0, 1.55, 0), 5, 0.04, 0.04 ,0.04, 0);
						}
					} else {
						lootstand.setMarker(true);
						lootstand.setCustomNameVisible(false);
						lootstand.setCustomName("");
					}
				}, 1L, 10L);

				ItemStack lootitem = new ItemStack(Material.WHITE_CONCRETE);
				lootitem = randomLoot(lootitem);
				lootstand.setHelmet(lootitem);
			}
		}
	}

	public void updateLevels(Player player) {
		Team team = mainboard.getPlayerTeam(player);
		String teamname = team.getName();

		int woolcount = 0;

		if (teamname.contains("red")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.RED_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		if (teamname.contains("yellow")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.YELLOW_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		if (teamname.contains("green")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.LIME_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		if (teamname.contains("blue")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.LIGHT_BLUE_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		if (teamname.contains("orange")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.ORANGE_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		if (teamname.contains("pink")) {
			for (ItemStack contents : player.getInventory().getContents()) {
				if (contents != null && contents.getType().equals(Material.PINK_WOOL)) {
					woolcount += contents.getAmount();
				}
			}
		}
		player.setExp(0);
		player.setLevel(Math.min(woolcount, 512));
	}

	public ArrayList<Block> getBlockCube(Block start, int radius) {
		ArrayList<Block> blocks = new ArrayList<>();
		for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
			for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
				for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {
					Location loc = new Location(start.getWorld(), x, y, z);
					blocks.add(loc.getBlock());
				}
			}
		}
		return blocks;
	}

	public int countGenWool(int redcount, int yellowcount, int greencount, int bluecount, int orangecount, int pinkcount, List<Block> blocklist) {
		int integer = 0;

		for (Block block : blocklist) {
			if (block.getType().equals(Material.RED_WOOL)) {
				redcount++;
			}
			if (block.getType().equals(Material.YELLOW_WOOL)) {
				yellowcount++;
			}
			if (block.getType().equals(Material.LIME_WOOL)) {
				greencount++;
			}
			if (block.getType().equals(Material.LIGHT_BLUE_WOOL)) {
				bluecount++;
			}
			if (block.getType().equals(Material.ORANGE_WOOL)) {
				orangecount++;
			}
			if (block.getType().equals(Material.MAGENTA_WOOL)) {
				pinkcount++;
			}

			if (redcount >= 79) {
				integer = 1;
			}
			if (yellowcount >= 79) {
				integer = 2;
			}
			if (greencount >= 79) {
				integer = 3;
			}
			if (bluecount >= 79) {
				integer = 4;
			}
			if (orangecount >= 79) {
				integer = 5;
			}
			if (pinkcount >= 79) {
				integer = 6;
			}
		}
		return integer;
	}

	public String countGenPercents(List<Block> blocklist) {
		int redcount = 0;
		int yellowcount = 0;
		int greencount = 0;
		int bluecount = 0;
		int orangecount = 0;
		int pinkcount = 0;


		for (Block block : blocklist) {
			if (block.getType().equals(Material.RED_WOOL)) {
				redcount++;
			}
			if (block.getType().equals(Material.YELLOW_WOOL)) {
				yellowcount++;
			}
			if (block.getType().equals(Material.LIME_WOOL)) {
				greencount++;
			}
			if (block.getType().equals(Material.LIGHT_BLUE_WOOL)) {
				bluecount++;
			}
			if (block.getType().equals(Material.ORANGE_WOOL)) {
				orangecount++;
			}
			if (block.getType().equals(Material.MAGENTA_WOOL)) {
				pinkcount++;
			}
		}

		double redpercent = redcount/98.0*100;
		double yellowpercent = yellowcount/98.0*100;
		double greenpercent = greencount/98.0*100;
		double bluepercent = bluecount/98.0*100;
		double orangepercent = orangecount/98.0*100;
		double pinkpercent = pinkcount/98.0*100;

		double biggestpercentage = Math.max(Math.max(redpercent, Math.max(yellowpercent, orangepercent)), Math.max(greenpercent, Math.max(bluepercent, pinkpercent)));

		String truepercentage = ChatColor.GRAY + " (0%)";

		if(biggestpercentage > 0) {
			if(biggestpercentage == redpercent) {
				truepercentage = ChatColor.RED + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
			if(biggestpercentage == yellowpercent) {
				truepercentage = ChatColor.YELLOW + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
			if(biggestpercentage == greenpercent) {
				truepercentage = ChatColor.GREEN + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
			if(biggestpercentage == bluepercent) {
				truepercentage = ChatColor.AQUA + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
			if(biggestpercentage == orangepercent) {
				truepercentage = ChatColor.GOLD + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
			if(biggestpercentage == pinkpercent) {
				truepercentage = ChatColor.LIGHT_PURPLE + " (" + (int) Math.round(biggestpercentage) + "%)";
			}
		}

		return truepercentage;
	}


	public void genCaptureChecks(List<Block> genBlocks, List<Block> genLongBlocks, String genLetter) {
		if(!hardmode) {
			String genStatus = "woolbattle.generator.uncaptured";

			if (genBlocks == genA.getBlocks()) {
				genStatus = genA.getStatus();
			}
			if (genBlocks == genB.getBlocks()) {
				genStatus = genB.getStatus();
			}
			if (genBlocks == genC.getBlocks()) {
				genStatus = genC.getStatus();
			}
			if (genBlocks == genD.getBlocks()) {
				genStatus = genD.getStatus();
			}

			int redcount = 0;
			int yellowcount = 0;
			int greencount = 0;
			int bluecount = 0;
			int orangecount = 0;
			int pinkcount = 0;

			int genowner = countGenWool(redcount, yellowcount, greencount, bluecount, orangecount, pinkcount, genBlocks);

			switch (genowner) {
				case 1 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.RED_CONCRETE);
						}
					}
					if (!genStatus.equals("woolbattle.generator.red")) {
						genBroadcast(genLetter, 1);
						genStatus = "woolbattle.generator.red";
					}
				}
				case 2 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.YELLOW_CONCRETE);
						}
					}
					if (!genStatus.equals("woolbattle.generator.yellow")) {
						genBroadcast(genLetter, 2);
						genStatus = "woolbattle.generator.yellow";
					}
				}
				case 3 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.LIME_CONCRETE);
						}
					}
					if (!genStatus.equals("woolbattle.generator.green")) {
						genBroadcast(genLetter, 3);
						genStatus = "woolbattle.generator.green";
					}
				}
				case 4 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.LIGHT_BLUE_CONCRETE);
						}
					}
					if (!genStatus.equals("woolbattle.generator.blue")) {
						genBroadcast(genLetter, 4);
						genStatus = "woolbattle.generator.blue";
					}
				}
				case 5 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.ORANGE_WOOL);
						}
					}
					if (!genStatus.equals("woolbattle.generator.orange")) {
						genBroadcast(genLetter, 5);
						genStatus = "woolbattle.generator.orange";
					}
				}
				case 6 -> {
					for (Block block : genLongBlocks) {
						if (String.valueOf(block.getType()).contains("CONCRETE") && block.getType() != Material.AIR) {
							block.setType(Material.MAGENTA_WOOL);
						}
					}
					if (!genStatus.equals("woolbattle.generator.pink")) {
						genBroadcast(genLetter, 6);
						genStatus = "woolbattle.generator.pink";
					}
				}
			}

			if (genBlocks == genA.getBlocks()) {
				genA.setStatus(genStatus);
			}
			if (genBlocks == genB.getBlocks()) {
				genB.setStatus(genStatus);
			}
			if (genBlocks == genC.getBlocks()) {
				genC.setStatus(genStatus);
			}
			if (genBlocks == genD.getBlocks()) {
				genD.setStatus(genStatus);
			}
		}
	}

	public void onCapture(String oldA, String oldB, String oldC, String oldD, String newA, String newB, String newC, String newD) {
		String genAcopy = newA.replace("woolbattle.generator.", "");
		String genBcopy = newB.replace("woolbattle.generator.", "");
		String genCcopy = newC.replace("woolbattle.generator.", "");
		String genDcopy = newD.replace("woolbattle.generator.", "");

		if(genAcopy.equals(genBcopy) && genBcopy.equals(genCcopy) && genCcopy.equals(genDcopy)) {
			String generatorOwner = genAcopy.toUpperCase(Locale.ROOT);
			Teams team = Teams.valueOf(generatorOwner);

			dominate(team, 120);
		}

		if(oldA.equals(oldB) && oldB.equals(oldC) && oldC.equals(oldD)) {
			if(!oldA.equals(newA) || !oldB.equals(newB) || !oldC.equals(newC) || !oldD.equals(newD)) {
				cancelDomination();
			}
		}

		if(!oldA.equals(newA)) {
			String generatorOwner = genAcopy.toUpperCase(Locale.ROOT);
			Teams team = Teams.valueOf(generatorOwner);
			ItemStack item = new ItemStack(team.getType);
			Color leatherColor = team.getLeatherColor;

			for(Entity entity : mainworld.getEntities()) {
				if(entity.getScoreboardTags().contains("genAtitle")) {
					ArmorStand stand = (ArmorStand) entity;
					stand.setHelmet(item);
					stand.setCustomNameVisible(true);
					stand.setCustomName(team.getChatColor + "Generator A");

					Particle.DustOptions dustOptions = new Particle.DustOptions(leatherColor, 1F);
					stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation().add(0, 1.5, 0), 10, 0.5, 0.5, 0.5, dustOptions);
				}
			}

			for(Block block : genA.getBlocks()) {
				if (block.getType().isAir() || block.getType().toString().contains("WOOL")) {
					block.setType(team.getType);
				}
			}
		}
		if(!oldB.equals(newB)) {
			String generatorOwner = genBcopy.toUpperCase(Locale.ROOT);
			Teams team = Teams.valueOf(generatorOwner);
			ItemStack item = new ItemStack(team.getType);
			Color leatherColor = team.getLeatherColor;

			for(Entity entity : mainworld.getEntities()) {
				if(entity.getScoreboardTags().contains("genBtitle")) {
					ArmorStand stand = (ArmorStand) entity;
					stand.setHelmet(item);
					stand.setCustomNameVisible(true);
					stand.setCustomName(team.getChatColor + "Generator B");

					Particle.DustOptions dustOptions = new Particle.DustOptions(leatherColor, 1F);
					stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation().add(0, 1.5, 0), 10, 0.5, 0.5, 0.5, dustOptions);
				}
			}

			for(Block block : genB.getBlocks()) {
				if (block.getType().isAir() || block.getType().toString().contains("WOOL")) {
					block.setType(team.getType);
				}
			}
		}
		if(!oldC.equals(newC)) {
			String generatorOwner = genCcopy.toUpperCase(Locale.ROOT);
			Teams team = Teams.valueOf(generatorOwner);
			ItemStack item = new ItemStack(team.getType);
			Color leatherColor = team.getLeatherColor;

			for(Entity entity : mainworld.getEntities()) {
				if(entity.getScoreboardTags().contains("genCtitle")) {
					ArmorStand stand = (ArmorStand) entity;
					stand.setHelmet(item);
					stand.setCustomNameVisible(true);
					stand.setCustomName(team.getChatColor + "Generator C");

					Particle.DustOptions dustOptions = new Particle.DustOptions(leatherColor, 1F);
					stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation().add(0, 1.5, 0), 10, 0.5, 0.5, 0.5, dustOptions);
				}
			}

			for(Block block : genC.getBlocks()) {
				if (block.getType().isAir() || block.getType().toString().contains("WOOL")) {
					block.setType(team.getType);
				}
			}
		}
		if(!oldD.equals(newD)) {
			String generatorOwner = genDcopy.toUpperCase(Locale.ROOT);
			Teams team = Teams.valueOf(generatorOwner);
			ItemStack item = new ItemStack(team.getType);
			Color leatherColor = team.getLeatherColor;

			for(Entity entity : mainworld.getEntities()) {
				if(entity.getScoreboardTags().contains("genDtitle")) {
					ArmorStand stand = (ArmorStand) entity;
					stand.setHelmet(item);
					stand.setCustomNameVisible(true);
					stand.setCustomName(team.getChatColor + "Generator D");

					Particle.DustOptions dustOptions = new Particle.DustOptions(leatherColor, 1F);
					stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation().add(0, 1.5, 0), 10, 0.5, 0.5, 0.5, dustOptions);
				}
			}

			for(Block block : genD.getBlocks()) {
				if (block.getType().isAir() || block.getType().toString().contains("WOOL")) {
					block.setType(team.getType);
				}
			}
		}
	}


	public void genBroadcast(String genLetter, int genowner) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			switch (genowner) {
				case 1 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.RED.2"), 0, 60, 20);
				case 2 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.YELLOW.2"), 0, 60, 20);
				case 3 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.GREEN.2"), 0, 60, 20);
				case 4 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.BLUE.2"), 0, 60, 20);
				case 5 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.ORANGE.2"), 0, 60, 20);
				case 6 -> player.sendTitle(getStringByLang(MopsFiles.getLanguage(player), "generator.capture", Map.of("letter", genLetter)), getStringByLang(MopsFiles.getLanguage(player), "team.PINK.2"), 0, 60, 20);
			}
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.7F, 2);
		}
	}

	public void cancelDomination() {
		if(isDominating) {
			dominationTime = 3600;

			Bukkit.getScheduler().runTaskLater(this, () -> {
				for (Player player : Bukkit.getOnlinePlayers()) {
					Map<String, String> map = Map.of("TEAMCOLOR", dominationTeam.getChatColor + "");
					player.sendTitle("", getStringByLang(MopsFiles.getLanguage(player), "domination.cancel", map), 5, 60, 40);
					player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1, 1);

					dominationTeam = Teams.SPECTATOR;
					isDominating = false;
				}
			}, 80L);
		}
	}

	public void stopGameDominationCancel() {
		dominationTime = 3600;
		dominationTeam = Teams.SPECTATOR;
		isDominating = false;
	}

	public void recoloringGenerators(List<Block> gen, List<Block> genLONG) {
		for(Block block : genLONG) {
			if(String.valueOf(block.getType()).contains("CONCRETE")) {
				block.setType(Material.WHITE_CONCRETE);
			}
			if(String.valueOf(block.getType()).contains("WOOL")) {
				block.setType(Material.WHITE_WOOL);
			}
		}
		for(Block block : gen) {
			if(block.getType().equals(Material.AIR)) {
				block.setType(Material.WHITE_WOOL);
			}
			if(block.getType().equals(Material.OAK_LEAVES)) {
				block.setType(Material.WHITE_WOOL);
			}
		}

		for(Entity entity : mainworld.getEntities()) {
			if(entity.getScoreboardTags().contains("generatorTitle")) {
				ArmorStand stand = (ArmorStand) entity;
				stand.setHelmet(new ItemStack(Material.IRON_BLOCK));

				stand.setCustomNameVisible(true);
				if(entity.getScoreboardTags().contains("genAtitle")) {
					stand.setCustomName(ChatColor.WHITE + "Generator A");
				}
				if(entity.getScoreboardTags().contains("genBtitle")) {
					stand.setCustomName(ChatColor.WHITE + "Generator B");
				}
				if(entity.getScoreboardTags().contains("genCtitle")) {
					stand.setCustomName(ChatColor.WHITE + "Generator C");
				}
				if(entity.getScoreboardTags().contains("genDtitle")) {
					stand.setCustomName(ChatColor.WHITE + "Generator D");
				}
			}
		}
	}

	public void resetGeneratorText(Player player) {
		List<String> genStatuses = new ArrayList<>();

		genStatuses.add("woolbattle.generator.uncaptured");
		genStatuses.add("woolbattle.generator.red");
		genStatuses.add("woolbattle.generator.yellow");
		genStatuses.add("woolbattle.generator.green");
		genStatuses.add("woolbattle.generator.blue");
//		genStatuses.add("woolbattle.generator.orange");
//		genStatuses.add("woolbattle.generator.pink");

		for(String genStatus : genStatuses) {
			String genStatus0 = getStringByLang(MopsFiles.getLanguage(player), genStatus);

			Objective fakekills = player.getScoreboard().getObjective("fakekills");

			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.a") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.b") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.c") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.d") + " - " + genStatus0);

			genStatus0 = genStatus0 + ChatColor.GRAY + " ⚠";

			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.a") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.b") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.c") + " - " + genStatus0);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.d") + " - " + genStatus0);

			String genStatusA = genStatus0 + genA.getPercent();
			String genStatusB = genStatus0 + genB.getPercent();
			String genStatusC = genStatus0 + genC.getPercent();
			String genStatusD = genStatus0 + genD.getPercent();

			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.a") + " - " + genStatusA);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.b") + " - " + genStatusB);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.c") + " - " + genStatusC);
			fakekills.getScoreboard().resetScores(getStringByLang(MopsFiles.getLanguage(player), "woolbattle.generator.d") + " - " + genStatusD);

			player.setScoreboard(fakekills.getScoreboard());
		}
	}

	public void stopGame() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				Bukkit.getScheduler().cancelTask(deathTimer.get(player));
			} catch (Throwable ignored) { }

			if(player.getGameMode() == GameMode.SPECTATOR) {
				player.setGameMode(GameMode.SURVIVAL);
			}

			clearScoreboard(player);
			if(player.getScoreboardTags().contains("ingame")) {
				player.getInventory().clear();
				player.setHealth(20);
				player.setFoodLevel(20);

				player.getScoreboardTags().add("spectator");
				player.setAllowFlight(true);
				player.setFlying(true);

				updateLevels(player);

				player.getScoreboardTags().remove("ingame");
			}

			player.getInventory().setHeldItemSlot(0);

			ItemStack leaveButton = new Items(this).leaveButton(MopsFiles.getLanguage(player));
			player.getInventory().setItem(8, leaveButton);

			player.setCollidable(true);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);

			clearScoreboard(player);
			mainboard.getTeam("nothing").addPlayer(player);

			player.setPlayerListName((MopsFiles.getRank(player).getPrefix() + player.getName() + " " + MopsFiles.getBadge(player).getSymbol()).trim());
			resetEveryFuckingKillScoreboard(player);
		}

		stopGameDominationCancel();

		recoloringGenerators(genA.getBlocks(), genA.getLongBlocks());
		recoloringGenerators(genB.getBlocks(), genB.getLongBlocks());
		recoloringGenerators(genC.getBlocks(), genC.getLongBlocks());
		recoloringGenerators(genD.getBlocks(), genD.getLongBlocks());
		gensLocked = false;

		mainworld.getWorldBorder().setSize(200, 1);
		if(worldBorderTask != null) {
			worldBorderTask.cancel();
		}

		if(generatorBlockTask != null) {
			generatorBlockTask.cancel();
		}

		hardmode = false;
		gameactive = false;

		requiredKills = 3;

		Bukkit.getScheduler().cancelTask(scoreboardTask);

		try {
			worldBorderTask.cancel();
		} catch (Throwable ignored) {}


		shears.clear();
		boomsticks.clear();
		boomsticksMK2.clear();
		boomsticksMK3.clear();
		platforms.clear();
		platforms3d.clear();
		slimeballs.clear();
		doubleJumpBoots.clear();

		try {
			Bukkit.getScheduler().runTaskLater(this, () -> {
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					MopsUtils.sendToServer(this, onlinePlayer, "mopslobby");
				}
				Bukkit.getScheduler().runTaskLater(this, () -> {
					MopsUtils.restartServer(this);
				}, 40L);
			}, 600L);
		} catch (Exception ignored) { }
	}

	public void winningBroadcast(int winner, String key) {
		Player maxKillsPlayer = null;
		String mostKills = "";

		for(Player player : killCount.keySet()) {
			if(killCount.get(player).equals(Collections.max(killCount.values()))) {
				mostKills = MopsFiles.getRank(player).getPrefix() + player.getName() + ChatColor.RESET + ChatColor.GRAY + " - " + ChatColor.YELLOW + Collections.max(killCount.values()) + " kills";
				maxKillsPlayer = player;
			}
		}

		for(Player player : Bukkit.getOnlinePlayers()) {
			String colorWon;

			switch (winner) {
				case 1 -> colorWon = "RED";
				case 2 -> colorWon = "YELLOW";
				case 3 -> colorWon = "GREEN";
				case 4 -> colorWon = "BLUE";
				case 5 -> colorWon = "ORANGE";
				case 6 -> colorWon = "PINK";
				default -> colorWon = "NO ONE";
			}
			if(player == maxKillsPlayer) {
				expectedMopsCoinMin.put(player, expectedMopsCoinMax.get(maxKillsPlayer) + 2);
				expectedMopsCoinMax.put(player, expectedMopsCoinMax.get(maxKillsPlayer) + 3);
			}
			expectedMopsCoinMin.put(player, expectedMopsCoinMax.get(maxKillsPlayer) + killCount.get(player));

			int expectedCoins = (int) (Math.random() * (expectedMopsCoinMax.get(player) - expectedMopsCoinMin.get(player) + 1)) + expectedMopsCoinMin.get(player);
			double equation = expectedCoins * (-expectedCoins)/80.0 + expectedCoins;
			int coins = (int) Math.round(equation);

			String lang = MopsFiles.getLanguage(player);

			String string = getStringByLang(lang, "coinReward.1");
			if(coins > 1) {
				string = getStringByLang(lang, "coinReward.2");
			}

			player.sendMessage(ChatColor.YELLOW + "====================================");

			switch (key) {
				case "win" -> {
					player.sendMessage("        " + getStringByLang(lang, "game.win") + " " + ChatColor.RESET + getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"));
					player.sendTitle(getStringByLang(lang, "game.win"), getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"), 5, 50, 40);
				}
				case "wipeout" -> {
					player.sendMessage("        " + getStringByLang(lang, "game.wipeout") + " " + ChatColor.RESET + getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"));
					player.sendTitle(getStringByLang(lang, "game.wipeout"), getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"), 5, 50, 40);
				}
				case "domination" -> {
					player.sendMessage("        " + getStringByLang(lang, "game.domination") + " " + ChatColor.RESET + getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"));
					player.sendTitle(getStringByLang(lang, "game.domination"), getStringByLang(lang, "team." + colorWon) + " " + getStringByLang(lang, "team.won"), 5, 50, 40);
				}
			}

			player.sendMessage(" ");
			player.sendMessage(mostKills);
			player.sendMessage(getStringByLang(lang, "youGotAwarded") + ChatColor.GOLD + coins + " " + ChatColor.GOLD + string + ChatColor.RESET + ".");
			player.sendMessage(ChatColor.YELLOW + "====================================");

			MopsFiles.setCoins(player, MopsFiles.getCoins(player) + coins);

			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.2F);
			Bukkit.getScheduler().runTaskLater(this, () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.25F);
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5F);
				Bukkit.getScheduler().runTaskLater(this, () -> {
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.6F);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.8F);
				}, 6L);
			}, 3L);
		}
	}

	public void resetEveryFuckingKillScoreboard(Player player) {
		Objective fakekills = player.getScoreboard().getObjective("fakekills");
		String lang = MopsFiles.getLanguage(player);

		String you = " " + getStringByLang(lang, "kills.you");

		String redyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("red")) { redyourteam = you; }
		String yellowyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("yellow")) { yellowyourteam = you;}
		String greenyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("green")) { greenyourteam = you; }
		String blueyourteam = ""; if(mainboard.getPlayerTeam(player).getName().contains("blue")) { blueyourteam = you; }

		String comma = ChatColor.WHITE + ": ";

		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.red") + comma + ChatColor.RED + redkills + redyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.red") + comma + ChatColor.RED + (redkills-1) + redyourteam);
		redkills = 0;
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.yellow") + comma + ChatColor.YELLOW + yellowkills + yellowyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.yellow") + comma + ChatColor.YELLOW + (yellowkills-1) + yellowyourteam);
		yellowkills = 0;
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.green") + comma + ChatColor.GREEN + greenkills + greenyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.green") + comma + ChatColor.GREEN + (greenkills-1) + greenyourteam);
		greenkills = 0;
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.blue") + comma + ChatColor.AQUA + bluekills + blueyourteam);
		fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.blue") + comma + ChatColor.AQUA + (bluekills-1) + blueyourteam);
		bluekills = 0;
		player.setScoreboard(fakekills.getScoreboard());
	}

	public void simulateHardmodeDeath(Player player) {
		broadcastFinalDeath(player);

		player.addScoreboardTag("spectator");
		player.hidePlayer(this, player);
		player.setAllowFlight(true);
		player.setFlying(true);

		player.setCollidable(false);

		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1600, 0, true, false));

		Location mid = new Location(player.getWorld(), 9, 270, 9);
		player.teleport(mid);

		player.getInventory().clear();

		ItemStack leaveButton = new Items(this).leaveButton(MopsFiles.getLanguage(player));
		player.getInventory().setItem(8, leaveButton);
	}

	public void broadcastDeath(Player dead, String deathCause, String afterDeathCause) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getScoreboardTags().contains("ingame") || player == dead) {
				Team team = mainboard.getPlayerTeam(dead);
				String teamname = team.getName();

				ChatColor color = ChatColor.WHITE;
				if(teamname.contains("red")) {
					color = ChatColor.RED;
				}
				if(teamname.contains("yellow")) {
					color = ChatColor.YELLOW;
				}
				if(teamname.contains("green")) {
					color = ChatColor.GREEN;
				}
				if(teamname.contains("blue")) {
					color = ChatColor.AQUA;
				}
				if(teamname.contains("orange")) {
					color = ChatColor.GOLD;
				}
				if(teamname.contains("pink")) {
					color = ChatColor.LIGHT_PURPLE;
				}

				String firstBloodText = "";
				if(firstBlood) {
					firstBloodText = " " + getStringByLang(MopsFiles.getLanguage(player), "firstBlood");
					firstBlood = false;
				}

				player.sendMessage(ChatColor.RED + "[☠] " + color + dead.getName() + ChatColor.GRAY + " " + getStringByLang(MopsFiles.getLanguage(player), deathCause) + afterDeathCause + firstBloodText);
			}
		}
	}

	public void broadcastFinalDeath(Player dead) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getScoreboardTags().contains("ingame") || player == dead) {
				Team team = mainboard.getPlayerTeam(dead);
				String teamname = team.getName();

				ChatColor color = ChatColor.WHITE;
				if(teamname.contains("red")) {
					color = ChatColor.RED;
				}
				if(teamname.contains("yellow")) {
					color = ChatColor.YELLOW;
				}
				if(teamname.contains("green")) {
					color = ChatColor.GREEN;
				}
				if(teamname.contains("blue")) {
					color = ChatColor.AQUA;
				}
				if(teamname.contains("orange")) {
					color = ChatColor.GOLD;
				}
				if(teamname.contains("pink")) {
					color = ChatColor.LIGHT_PURPLE;
				}

				String firstBloodText = "";
				if(firstBlood) {
					firstBloodText = " " + getStringByLang(MopsFiles.getLanguage(player), "firstBlood");
					firstBlood = false;
				}

				player.sendMessage(ChatColor.RED + "[☠] " + color + dead.getName() + getStringByLang(MopsFiles.getLanguage(player), "woolbattle.death.final") + firstBloodText);
			}
		}
	}

	public void recountTeamMembers() {
		List<Player> redTeamPlayersLeft = new ArrayList<>();
		List<Player> yellowTeamPlayersLeft = new ArrayList<>();
		List<Player> greenTeamPlayersLeft = new ArrayList<>();
		List<Player> blueTeamPlayersLeft = new ArrayList<>();
		List<Player> orangeTeamPlayersLeft = new ArrayList<>();
		List<Player> pinkTeamPlayersLeft = new ArrayList<>();

		for(Player player : Bukkit.getOnlinePlayers()) {
			Team team = mainboard.getPlayerTeam(player);
			String teamname = team.getName();

			boolean letCounting = !(player.getScoreboardTags().contains("spectator") && hardmode);

			if(letCounting) {
				if (teamname.contains("red")) {
					redTeamPlayersLeft.add(player);
				}
				if (teamname.contains("yellow")) {
					yellowTeamPlayersLeft.add(player);
				}
				if (teamname.contains("green")) {
					greenTeamPlayersLeft.add(player);
				}
				if (teamname.contains("blue")) {
					blueTeamPlayersLeft.add(player);
				}
				if (teamname.contains("orange")) {
					orangeTeamPlayersLeft.add(player);
				}
				if (teamname.contains("pink")) {
					pinkTeamPlayersLeft.add(player);
				}
			}

			redTeamPlayers = redTeamPlayersLeft;
			yellowTeamPlayers = yellowTeamPlayersLeft;
			greenTeamPlayers = greenTeamPlayersLeft;
			blueTeamPlayers = blueTeamPlayersLeft;
			orangeTeamPlayers = orangeTeamPlayersLeft;
			pinkTeamPlayers = pinkTeamPlayersLeft;
		}
	}

	public void gameStartingTitle(Player player) {
		List<String> mainTitles = new ArrayList<>();
		List<String> subTitles = new ArrayList<>();

		mainTitles.add("frog"); subTitles.add("bottom text");
		mainTitles.add("guys win pls"); subTitles.add("i want to go eat");
		mainTitles.add(ChatColor.GOLD + "" + ChatColor.BOLD + "WoolBattle"); subTitles.add("The game has started.");
		mainTitles.add("el"); subTitles.add("gato");
		mainTitles.add("there are"); subTitles.add("no pigeons");
		mainTitles.add("me when the"); subTitles.add("when the uhhh");
		mainTitles.add("Sponsored by"); subTitles.add("Astarta");
		mainTitles.add("Made in"); subTitles.add("Ukraine");
		mainTitles.add("but here's"); subTitles.add("the woolbattler");
		mainTitles.add("hi"); subTitles.add("hello");
		mainTitles.add("im so silly"); subTitles.add("blehhh");
		mainTitles.add("woolbattle"); subTitles.add("(Catalyst included)");
		mainTitles.add("robby"); subTitles.add("best");

		int maxMain = mainTitles.size();
		int maxSub = subTitles.size();
		int min = 1;
		int randomMainTitle = (int) (Math.random() * (maxMain - min + 1)) + min;
		int randomSubTitle = (int) (Math.random() * (maxSub - min + 1)) + min;

		int fadeIn = 5;
		int hold = 40;
		int fadeOut = 35;

		player.sendTitle(mainTitles.get(randomMainTitle-1), subTitles.get(randomSubTitle-1), fadeIn, hold, fadeOut);
	}

	public void gameStartSequence(Player player, String teamname) {
		Teams team = Teams.SPECTATOR;
		String lang = MopsFiles.getLanguage(player);

		if (teamname.contains("red")) {
			Location loc = new Location(player.getWorld(), 9.5, 258, -27.5);
			player.teleport(loc);
			team = Teams.RED;
		} else if (teamname.contains("yellow")) {
			Location loc = new Location(player.getWorld(), -27.5, 258, 9.5);
			loc.setYaw(-90);
			player.teleport(loc);
			team = Teams.YELLOW;
		} else if (teamname.contains("green")) {
			Location loc = new Location(player.getWorld(), 9.5, 258, 46.5);
			loc.setYaw(-180);
			player.teleport(loc);
			team = Teams.GREEN;
		} else if (teamname.contains("blue")) {
			Location loc = new Location(player.getWorld(), 46.5, 258, 9.5);
			loc.setYaw(90);
			player.teleport(loc);
			team = Teams.BLUE;
		}

		player.setPlayerListName(team.getChatColor + player.getName());

		gameStartingTitle(player);

		double s = Bukkit.getOnlinePlayers().size();
		double equation = s * 3 + (s*s)/150;
		requiredKills = (int) (Math.round(equation));

		Bukkit.getScheduler().runTaskLater(this, () -> {
			player.sendTitle(getStringByLang(lang, "killRequirement.1"), getStringByLang(lang, "killRequirement.2", Map.of("kills", String.valueOf(requiredKills))), 5, 40, 40);

			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.2F);
			Bukkit.getScheduler().runTaskLater(this, () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.25F);
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5F);
				Bukkit.getScheduler().runTaskLater(this, () -> {
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.6F);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.8F);

					player.sendMessage(getByLang(lang, "onStartMessage", Map.of("kills", String.valueOf(requiredKills))));
				}, 3L);
			}, 3L);

		}, 60L);

		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
		player.removeScoreboardTag("spawn");
		player.removeScoreboardTag("canbreak");
		player.addScoreboardTag("ingame");
		player.setHealth(player.getMaxHealth());
		player.setGameMode(GameMode.SURVIVAL);

		try {
			cancelLastAttacker.get(player).cancel();
		} catch (Throwable ignored) {}

		this.gameactive = true;

		baseItems.startGame(lang, team, player);
	}

	public void timedGameStart(Player player, String teamname) {
		String lang = MopsFiles.getLanguage(player);

		Bukkit.getScheduler().runTaskLater(this, () -> {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
			player.sendTitle(getStringByLang(lang, "startsIn"), ChatColor.RED + "3", 1, 20, 20);
			Bukkit.getScheduler().runTaskLater(this, () -> {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				player.sendTitle(getStringByLang(lang, "startsIn"), ChatColor.YELLOW + "2", 1, 20, 20);
				Bukkit.getScheduler().runTaskLater(this, () -> {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
					player.sendTitle(getStringByLang(lang, "startsIn"), ChatColor.GREEN + "1", 1, 20, 20);

					Bukkit.getScheduler().runTaskLater(this, () -> gameStartSequence(player, teamname), 40L);
				}, 40L);
			}, 40L);
		}, 40L);
	}

	public void checkForWoolCap(Player player) {
		Team team = mainboard.getPlayerTeam(player);
		String teamname = team.getName();
		String lang = MopsFiles.getLanguage(player);

		if (teamname.contains("red")) {
			if (player.getInventory().contains(Material.RED_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.RED_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "woolbattle.redWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
		if (teamname.contains("yellow")) {
			if (player.getInventory().contains(Material.YELLOW_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.YELLOW_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "woolbattle.yellowWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
		if (teamname.contains("green")) {
			if (player.getInventory().contains(Material.LIME_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.LIME_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "woolbattle.greenWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
		if (teamname.contains("blue")) {
			if (player.getInventory().contains(Material.LIGHT_BLUE_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.LIGHT_BLUE_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "blueWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
		if (teamname.contains("orange")) {
			if (player.getInventory().contains(Material.ORANGE_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.ORANGE_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "orangeWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
		if (teamname.contains("pink")) {
			if (player.getInventory().contains(Material.MAGENTA_WOOL, 512)) {
				ItemStack woolitem = new ItemStack(Material.MAGENTA_WOOL);
				ItemMeta woolmeta = woolitem.getItemMeta();
				woolmeta.displayName(getByLang(lang, "pinkWool"));
				woolitem.setItemMeta(woolmeta);
				woolitem.setAmount(getAmount(player, woolitem)-512);
				player.getInventory().removeItem(woolitem);
			}
		}
	}

	public static int getAmount(Player player, ItemStack item) {
		if (item == null)
			return 0;
		int itemCount = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack slot = player.getInventory().getItem(i);
			if (slot == null || !slot.isSimilar(item))
				continue;
			itemCount += slot.getAmount();
		}
		return itemCount;
	}

	public void dominate(Teams team, int time) {
		dominationTeam = team;
		dominationTime = actualgametime[0]+time+1;
		isDominating = true;

		int secondsCopy = seconds[0]+time;
		int minutesCopy = minutes[0];

		if(secondsCopy >= 60) {
			minutesCopy++;
			secondsCopy -= 60;

			if(secondsCopy >= 60) {
				minutesCopy++;
				secondsCopy -= 60;

				if(secondsCopy >= 60) {
					minutesCopy++;
					secondsCopy -= 60;
				}
			}
		}

		String colon = ":";

		if(secondsCopy < 10) {
			colon = ":0";
		}

		for(Player player : Bukkit.getOnlinePlayers()) {
			String lang = MopsFiles.getLanguage(player);

			Map<String, String> map = Map.of("TEAM", getStringByLang(lang, dominationTeam.getTranslationKey), "TEAMCOLOR", dominationTeam.getChatColor + "");
			player.sendTitle(getStringByLang(lang, "domination.warning.1", map), getStringByLang(lang, "domination.warning.2", map), 5, 60, 40);
			player.sendMessage(getByLang(lang, "dominationWarning", map));
			player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1, 1);
			player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1, 0);
		}

		dominationEvent = ChatColor.DARK_GRAY + " (" + team.getChatColor + ChatColor.BOLD + team.getName.substring(0, 3) + ChatColor.RESET + getStringByLang("eng", "woolbattle.event.domination") + minutesCopy + colon + secondsCopy + ")";
	}

	public void assignTeams(List<Player> playerList) {
		int size = playerList.size();
		Teams team = Teams.SPECTATOR;

		if(size == 1) {
			List<Teams> teamList = new ArrayList<>(Arrays.asList(Teams.RED, Teams.YELLOW, Teams.GREEN, Teams.BLUE));
			int random = (int) (Math.random() * (4 - 1 + 1)) + 1;
			switch (random) {
				case 1 -> team = teamList.get(0);
				case 2 -> team = teamList.get(1);
				case 3 -> team = teamList.get(2);
				case 4 -> team = teamList.get(3);
			}
			mainboard.getTeam(team.getID).addPlayer(playerList.get(0));
		}
		if(size == 2) {
			List<Teams> teamList = new ArrayList<>(Arrays.asList(Teams.RED, Teams.YELLOW, Teams.GREEN, Teams.BLUE));
			int random = (int) (Math.random() * (4 - 1 + 1)) + 1;
			switch (random) {
				case 1 -> team = teamList.get(0);
				case 2 -> team = teamList.get(1);
				case 3 -> team = teamList.get(2);
				case 4 -> team = teamList.get(3);
			}
			mainboard.getTeam(team.getID).addPlayer(playerList.get(0));
			mainboard.getTeam(getOppositeTeam(team).getID).addPlayer(playerList.get(1));
		}

		if(size == 3) {
			mainboard.getTeam(Teams.RED.getID).addPlayer(playerList.get(0));
			mainboard.getTeam(Teams.YELLOW.getID).addPlayer(playerList.get(1));
			mainboard.getTeam(Teams.GREEN.getID).addPlayer(playerList.get(2));
		}
		if(size == 4) {
			mainboard.getTeam(Teams.RED.getID).addPlayer(playerList.get(0));
			mainboard.getTeam(Teams.YELLOW.getID).addPlayer(playerList.get(1));
			mainboard.getTeam(Teams.GREEN.getID).addPlayer(playerList.get(2));
			mainboard.getTeam(Teams.GREEN.getID).addPlayer(playerList.get(3));
		}
	}

	public void respawnPlayer(Player player, String teamname, ItemStack[] inventory) {
		player.setGameMode(GameMode.SURVIVAL);
		updateLevels(player);

		if (teamname.contains("red")) {
			Location loc = new Location(player.getWorld(), 9, 257, -28);
			while (loc.getBlock().getType() != Material.AIR && !(loc.getY() >= 319)) {
				loc = loc.add(0, 1, 0);
			}
			player.teleport(loc.add(0.5, 1, 0.5));
		}
		if (teamname.contains("yellow")) {
			Location loc = new Location(player.getWorld(), -28, 257, 9);
			while (loc.getBlock().getType() != Material.AIR && !(loc.getY() >= 319)) {
				loc = loc.add(0, 1, 0);
			}
			loc.setYaw(-90);
			player.teleport(loc.add(0.5, 1, 0.5));
		}
		if (teamname.contains("green")) {
			Location loc = new Location(player.getWorld(), 9, 257, 46);
			while (loc.getBlock().getType() != Material.AIR && !(loc.getY() >= 319)) {
				loc = loc.add(0, 1, 0);
			}
			loc.setYaw(-180);
			player.teleport(loc.add(0.5, 1, 0.5));
		}
		if (teamname.contains("blue")) {
			Location loc = new Location(player.getWorld(), 46, 257, 9);
			while (loc.getBlock().getType() != Material.AIR && !(loc.getY() >= 319)) {
				loc = loc.add(0, 1, 0);
			}
			loc.setYaw(90);
			player.teleport(loc.add(0.5, 1, 0.5));
		}

		player.setAllowFlight(false);
		player.setFlying(false);
		player.removeScoreboardTag("spectator");

		player.getInventory().clear();
		player.getInventory().setContents(inventory);
		player.updateInventory();

		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

		player.addScoreboardTag("immunity");
		Bukkit.getScheduler().runTaskLater(this, () -> player.removeScoreboardTag("immunity"), 80L);
	}

	public void startGame(String[] args) {
		mainworld.getWorldBorder().setSize(200, 1);
		hardmode = false;

		assignTeams(new ArrayList<>(Bukkit.getOnlinePlayers()));

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (mainboard.getPlayerTeam(player) != null) {
				Team team = mainboard.getPlayerTeam(player);
				assert team != null;
				String teamname = team.getName();

				player.setFoodLevel(20);

				try {
					if (args[0].equals("instant")) {
						gameStartSequence(player, teamname);
					} else {
						if(!testmode) {
							timedGameStart(player, teamname);
						}
					}
				} catch (IndexOutOfBoundsException error) {
					if(!testmode) {
						timedGameStart(player, teamname);
					}
				}
			}
		}

		for (Block block : ppbs) {
			block.setType(Material.AIR);
		}

		ppbs.clear();

		recoloringGenerators(genA.getBlocks(), genA.getLongBlocks());
		recoloringGenerators(genB.getBlocks(), genB.getLongBlocks());
		recoloringGenerators(genC.getBlocks(), genC.getLongBlocks());
		recoloringGenerators(genD.getBlocks(), genD.getLongBlocks());

		Items items = new Items(this);
		platforms.add(items.platform("eng"));
		platforms3d.add(items.platform3d("eng"));
		boomsticksMK2.add(items.boomstickMK2("eng"));
		boomsticksMK3.add(items.boomstickMK3("eng"));

		lootGenerator();

		scoreboardTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			if(gameactive) {
				seconds[0] = seconds[0] + 1;

				if (seconds[0] == 60) {
					minutes[0] = minutes[0] + 1;
					seconds[0] = 0;
				}

				seconds0[0] = seconds0[0] + 1;

				if (seconds0[0] == 60) {
					minutes0[0] = minutes0[0] + 1;
					seconds0[0] = 0;
				}

				actualgametime[0] = actualgametime[0] + 1;
				actualgametime0[0] = actualgametime0[0] + 1;

				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					newboard = manager.getNewScoreboard();
					Objective fakekills = newboard.registerNewObjective("fakekills", "dummy", Component.text("WoolBattle", NamedTextColor.GOLD, TextDecoration.BOLD));
					fakekills.setDisplaySlot(DisplaySlot.SIDEBAR);

					String lang = MopsFiles.getLanguage(player);

					if (actualgametime[0] < 240) {
						nextevent = getStringByLang(lang, "event.refill.1");
					}
					if (actualgametime[0] == 240) {
						lootGenerator();
						player.sendTitle(getStringByLang(lang, "refill.activation.1"), getStringByLang(lang, "refill.activation.2"), 1, 20, 20);
						player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
					}
					if (actualgametime[0] < 480 && actualgametime[0] > 240) {
						nextevent = getStringByLang(lang, "event.refill.2");
					}
					if (actualgametime[0] == 480) {
						lootGenerator();
						player.sendTitle(getStringByLang(lang, "refill.activation.1"), getStringByLang(lang, "refill.activation.2"), 1, 20, 20);
						player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
					}
					if (actualgametime[0] < 720 && actualgametime[0] > 480) {
						nextevent = getStringByLang(lang, "event.refill.3");
					}
					if (actualgametime[0] == 720) {
						lootGenerator();
						player.sendTitle(getStringByLang(lang, "refill.activation.1"), getStringByLang(lang, "refill.activation.2"), 1, 20, 20);
						player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
					}
					if (actualgametime[0] < 1200 && actualgametime[0] > 720) {
						nextevent = getStringByLang(lang, "event.hardmode");
					}
					if (actualgametime[0] == 1200) {
						if (!hardmode) {
							activateHardmode();
						}
						nextevent = getStringByLang(lang, "event.deathmatch");
					}


					if (actualgametime0[0] < 240) {
						nextevent0 = getStringByLang(lang, "event.refill.1");
					}
					if (actualgametime0[0] < 480 && actualgametime0[0] > 240) {
						nextevent0 = getStringByLang(lang, "event.refill.2");
					}
					if (actualgametime0[0] < 720 && actualgametime0[0] > 480) {
						nextevent0 = getStringByLang(lang, "event.refill.3");
					}
					if (actualgametime0[0] < 1200 && actualgametime0[0] > 720) {
						nextevent0 = getStringByLang(lang, "event.hardmode");
					}
					if (actualgametime0[0] == 1200) {
						nextevent0 = getStringByLang(lang, "event.deathmatch");
					}

					String you = getStringByLang(lang, "kills.you");
					Team playerteam = Objects.requireNonNull(mainboard.getPlayerTeam(player));
					String teamname = playerteam.getName();

					String redyourteam = "";
					String yellowyourteam = "";
					String greenyourteam = "";
					String blueyourteam = "";

					if(teamname.contains("red")) { redyourteam = redyourteam + " " + you;}
					if(teamname.contains("yellow")) { yellowyourteam = yellowyourteam + " " + you; }
					if(teamname.contains("green")) { greenyourteam = greenyourteam + " " + you; }
					if(teamname.contains("blue")) { blueyourteam = blueyourteam + " " + you; }


					fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.red") + colon + ChatColor.RED + (redkills - 1) + redyourteam);
					fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.yellow") + colon + ChatColor.YELLOW + (yellowkills - 1) + yellowyourteam);
					fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.green") + colon + ChatColor.GREEN + (greenkills - 1) + greenyourteam);
					fakekills.getScoreboard().resetScores(getStringByLang(lang, "kills.blue") + colon + ChatColor.AQUA + (bluekills - 1) + blueyourteam);

					String event = nextevent;
					String event0 = nextevent0;

					if(isDominating) {
						event = dominationEvent;
					}

					if (seconds0[0] < 10) {
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes0[0] + ":" + "0" + seconds0[0] + event0);
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes0[0] + ":" + "0" + seconds0[0] + dominationEvent);
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes0[0] + ":" + "0" + seconds0[0] + nextevent0);
					} else {
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + seconds[0] + event);
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + seconds[0] + nextevent);
						fakekills.getScoreboard().resetScores(getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + seconds[0] + dominationEvent);
					}

					fakekills.getScore(getStringByLang(lang, "kills.red") + colon + ChatColor.RED + redkills + redyourteam).setScore(12);
					fakekills.getScore(getStringByLang(lang, "kills.yellow") + colon + ChatColor.YELLOW + yellowkills + yellowyourteam).setScore(11);
					fakekills.getScore(getStringByLang(lang, "kills.green") + colon + ChatColor.GREEN + greenkills + greenyourteam).setScore(10);
					fakekills.getScore(getStringByLang(lang, "kills.blue") + colon + ChatColor.AQUA + bluekills + blueyourteam).setScore(9);


					fakekills.getScore(ChatColor.RED + " ").setScore(8);

					if (seconds[0] < 10) {
						fakekills.getScore(ChatColor.WHITE + getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + "0" + seconds[0] + event).setScore(7);
					} else {
						fakekills.getScore(ChatColor.WHITE + getStringByLang(lang, "scoreboardTime") + colon + ChatColor.YELLOW + minutes[0] + ":" + seconds[0] + event).setScore(7);
					}

					fakekills.getScore(ChatColor.GOLD + " ").setScore(6);

					for(Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
						resetGeneratorText(allPlayers);
					}

					genA.setPercent(countGenPercents(genA.getBlocks()));
					genB.setPercent(countGenPercents(genB.getBlocks()));
					genC.setPercent(countGenPercents(genC.getBlocks()));
					genD.setPercent(countGenPercents(genD.getBlocks()));

					String Acopy = getStringByLang(lang, genA.getStatus());
					String Bcopy = getStringByLang(lang, genB.getStatus());
					String Ccopy = getStringByLang(lang, genC.getStatus());
					String Dcopy = getStringByLang(lang, genD.getStatus());

					if(gensLocked) {
						Acopy += ChatColor.GRAY + " ⚠";
						Bcopy += ChatColor.GRAY + " ⚠";
						Ccopy += ChatColor.GRAY + " ⚠";
						Dcopy += ChatColor.GRAY + " ⚠";
					} else {
						Acopy += genA.getPercent();
						Bcopy += genB.getPercent();
						Ccopy += genC.getPercent();
						Dcopy += genD.getPercent();
					}

					fakekills.getScore(getStringByLang(lang, "woolbattle.generator.a") + " - " + Acopy).setScore(5);
					fakekills.getScore(getStringByLang(lang, "woolbattle.generator.b") + " - " + Bcopy).setScore(4);
					fakekills.getScore(getStringByLang(lang, "woolbattle.generator.c") + " - " + Ccopy).setScore(3);
					fakekills.getScore(getStringByLang(lang, "woolbattle.generator.d") + " - " + Dcopy).setScore(2);

					fakekills.getScore(ChatColor.YELLOW + " ").setScore(1);
					fakekills.getScore(ChatColor.DARK_GRAY + connectToIP + ":" + Bukkit.getPort()).setScore(0);

					player.setScoreboard(fakekills.getScoreboard());
				}

				if(actualgametime[0] >= dominationTime && isDominating) {
					winningBroadcast(dominationTeam.getNumber, "domination");
					stopGame();
				}

			}
		}, 0L, 20L);

		Bukkit.getScheduler().cancelTask(generatorTask);

		generatorTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player player1 : mainworld.getPlayers()) {
				Team team = mainboard.getPlayerTeam(player1);
				assert team != null;
				String teamname = team.getName();

				List<String> genStatuses = new ArrayList<>();
				genStatuses.add(genA.getStatus());
				genStatuses.add(genB.getStatus());
				genStatuses.add(genC.getStatus());
				genStatuses.add(genD.getStatus());

				String lang = MopsFiles.getLanguage(player1);

				for (String genStatus : genStatuses) {
					if(player1.getScoreboardTags().contains("ingame") && !player1.getScoreboardTags().contains("spectator")) {
						if (teamname.contains("red")) {
							if (genStatus.contains("woolbattle.generator.red")) {
								if (!player1.getInventory().contains(Material.RED_WOOL, 512)) {
									ItemStack woolitem = new ItemStack(Material.RED_WOOL, 1);
									ItemMeta woolmeta = woolitem.getItemMeta();
									woolmeta.displayName(getByLang(lang, "woolbattle.redWool"));
									woolitem.setItemMeta(woolmeta);
									player1.getInventory().addItem(woolitem);
								}
							}
						} else if (teamname.contains("yellow")) {
							if (genStatus.contains("woolbattle.generator.yellow")) {
								if (!player1.getInventory().contains(Material.YELLOW_WOOL, 512)) {
									ItemStack woolitem = new ItemStack(Material.YELLOW_WOOL, 1);
									ItemMeta woolmeta = woolitem.getItemMeta();
									woolmeta.displayName(getByLang(lang, "woolbattle.yellowWool"));
									woolitem.setItemMeta(woolmeta);
									player1.getInventory().addItem(woolitem);
								}
							}
						} else if (teamname.contains("green")) {
							if (genStatus.contains("woolbattle.generator.green")) {
								if (!player1.getInventory().contains(Material.LIME_WOOL, 512)) {
									ItemStack woolitem = new ItemStack(Material.LIME_WOOL, 1);
									ItemMeta woolmeta = woolitem.getItemMeta();
									woolmeta.displayName(getByLang(lang, "woolbattle.greenWool"));
									woolitem.setItemMeta(woolmeta);
									player1.getInventory().addItem(woolitem);
								}
							}
						} else if (teamname.contains("blue")) {
							if (genStatus.contains("woolbattle.generator.blue")) {
								if (!player1.getInventory().contains(Material.LIGHT_BLUE_WOOL, 512)) {
									ItemStack woolitem = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
									ItemMeta woolmeta = woolitem.getItemMeta();
									woolmeta.displayName(getByLang(lang, "woolbattle.blueWool"));
									woolitem.setItemMeta(woolmeta);
									player1.getInventory().addItem(woolitem);
								}
							}
						} else if (teamname.contains("orange")) {
							if (genStatus.contains("woolbattle.generator.orange")) {
								if (!player1.getInventory().contains(Material.ORANGE_WOOL, 512)) {
									ItemStack woolitem = new ItemStack(Material.ORANGE_WOOL, 1);
									ItemMeta woolmeta = woolitem.getItemMeta();
									woolmeta.displayName(getByLang(lang, "woolbattle.orangeWool"));
									woolitem.setItemMeta(woolmeta);
									player1.getInventory().addItem(woolitem);
								}
							}
						}
					} else if (teamname.contains("pink")) {
						if (genStatus.contains("woolbattle.generator.pink")) {
							if (!player1.getInventory().contains(Material.MAGENTA_WOOL, 512)) {
								ItemStack woolitem = new ItemStack(Material.MAGENTA_WOOL, 1);
								ItemMeta woolmeta = woolitem.getItemMeta();
								woolmeta.displayName(getByLang(lang, "woolbattle.pinkWool"));
								woolitem.setItemMeta(woolmeta);
								player1.getInventory().addItem(woolitem);
							}
						}
					}
				}
				checkForWoolCap(player1);
				updateLevels(player1);
			}
		}, 0L, 20L);
	}

	public Teams getOppositeTeam(Teams team) {
		if(team == Teams.RED) {
			return Teams.GREEN;
		}
		if(team == Teams.GREEN) {
			return Teams.RED;
		}
		if(team == Teams.YELLOW) {
			return Teams.BLUE;
		}
		if(team == Teams.BLUE) {
			return Teams.YELLOW;
		}
		return Teams.SPECTATOR;
	}

	//fgdfg

	public void every5Ticks() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Team team = mainboard.getPlayerTeam(player);
			assert team != null;
			String teamname = team.getName();
			String lang = MopsFiles.getLanguage(player);

			mainworld = player.getWorld();

			if (player.getLocation().getY() > 142 && player.getLocation().getY() < 150 && !gameactive) {
				player.teleport(new Location(mainworld, 9.5, 257, 9.5));
			}

			if (player.getScoreboardTags().contains("ingame")) {

				combo.putIfAbsent(player, 0);
				Location loc0 = player.getLocation();

				if (loc0.clone().add(0, -1, 0).getBlock().getType() == Material.SLIME_BLOCK) {
					player.setVelocity(new Vector(0, 10, 0));
				}

				if (loc0.getY() > 142 && loc0.getY() < 160 && !player.getScoreboardTags().contains("spectator")) {
					ItemStack woolItem;
					TextComponent woolName;
					Teams mopsTeam = convertToTeam(mainboard.getPlayerTeam(player).getName());

					woolItem = new ItemStack(mopsTeam.getType, 1296);
					woolName = getByLang(lang, mopsTeam.getID + "Wool");

					if(mopsTeam == Teams.SPECTATOR) {
						player.removeScoreboardTag("ingame");
						woolItem = new ItemStack(Material.AIR);
						woolName = Component.empty();
					}

					ItemMeta woolMeta = woolItem.getItemMeta();
					woolMeta.displayName(woolName);
					woolItem.setItemMeta(woolMeta);
					player.getInventory().removeItem(woolItem);

					if(mopsTeam != Teams.SPECTATOR) {
					switch (mopsTeam) {
						case RED -> {
							redkills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.RED + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
						case YELLOW -> {
							yellowkills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.YELLOW + "" + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
						case GREEN -> {
							greenkills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.GREEN + "" + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
						case BLUE -> {
							bluekills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.AQUA + "" + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
						case ORANGE -> {
							orangekills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.GOLD + "" + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
						case PINK -> {
							pinkkills++;
							killCount.put(lastDamager.get(player), killCount.get(lastDamager.get(player)) + 1);
							if (!hardmode) {
								broadcastDeath(player, "woolbattle.gotKilledBy", " " + ChatColor.LIGHT_PURPLE + "" + lastDamager.get(player).getName() + ChatColor.GRAY + ".");
							}
						}
					}
					} else {
						if (!hardmode) {
							broadcastDeath(player, "woolbattle.fellInVoid", "");
						}
					}

					if (!hardmode) {
						ItemStack[] savedInventory = new ItemStack[0];

						if (!player.getScoreboardTags().contains("spectator")) {
							savedInventory = player.getInventory().getContents();
							player.getInventory().clear();
							player.getInventory().remove(woolItem);
						}

						ItemStack[] finalSavedInventory = savedInventory;

						player.addScoreboardTag("spectator");
						player.setAllowFlight(true);
						player.setFlying(true);

						player.setGameMode(GameMode.SPECTATOR);

						Location mid = new Location(player.getWorld(), 9, 270, 9);
						player.teleport(mid);
						lastDamager.put(player, null);

						final int[] untilRespawn = {respawnTime};
						final int[] part = {0};
						int split = (int) Math.round(untilRespawn[0] / 4.0);

						deathTimer.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
							int howMuchPassed = Math.abs(untilRespawn[0] -respawnTime);
							if(howMuchPassed % split == 0) {
								part[0]++;
							}
							if(untilRespawn[0] != 0) {
								switch (part[0]) {
									case 0 -> {
										player.sendTitle(ChatColor.DARK_RED + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
									}
									case 1 -> {
										player.sendTitle(ChatColor.RED + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
									}
									case 2 -> {
										player.sendTitle(ChatColor.GOLD + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5F);
									}
									case 3 -> {
										player.sendTitle(ChatColor.YELLOW + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
									}
									case 4 -> {
										player.sendTitle(ChatColor.GREEN + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
									}
									case 5 -> {
										player.sendTitle(ChatColor.DARK_GREEN + String.valueOf(untilRespawn[0]), "", 1, 10, 10);
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.75F);
									}
								}
							}
							if(untilRespawn[0] == 0) {
								respawnPlayer(player, teamname, finalSavedInventory);
								Bukkit.getScheduler().cancelTask(deathTimer.get(player));
							} else {
								untilRespawn[0]--;
							}
						}, 0L, 20L));

					} else if (!player.getScoreboardTags().contains("spectator")) {
						simulateHardmodeDeath(player);
					}
				}
				if (hardmode && !player.getWorld().getWorldBorder().isInside(player.getLocation()) && !player.getScoreboardTags().contains("spectator")) {
					simulateHardmodeDeath(player);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.8F, 1);
				}
			}

			recountTeamMembers();

			if(redkills >= requiredKills && gameactive) {
				winningBroadcast(1, "win");
				stopGame();
			}
			if(yellowkills >= requiredKills && gameactive) {
				winningBroadcast(2, "win");
				stopGame();
			}
			if(greenkills >= requiredKills && gameactive) {
				winningBroadcast(3, "win");
				stopGame();
			}
			if(bluekills >= requiredKills && gameactive) {
				winningBroadcast(4, "win");
				stopGame();
			}
			if(orangekills >= requiredKills && gameactive) {
				winningBroadcast(5, "win");
				stopGame();
			}
			if(pinkkills >= requiredKills && gameactive) {
				winningBroadcast(6, "win");
				stopGame();
			}


			if(!redTeamPlayers.isEmpty() && yellowTeamPlayers.isEmpty() && greenTeamPlayers.isEmpty() && blueTeamPlayers.isEmpty() && gameactive && !testmode) {
				winningBroadcast(1, "wipeout");
				stopGame();
			}
			if(redTeamPlayers.isEmpty() && !yellowTeamPlayers.isEmpty() && greenTeamPlayers.isEmpty() && blueTeamPlayers.isEmpty() && gameactive && !testmode) {
				winningBroadcast(2, "wipeout");
				stopGame();
			}
			if(redTeamPlayers.isEmpty() && yellowTeamPlayers.isEmpty() && !greenTeamPlayers.isEmpty() && blueTeamPlayers.isEmpty() && gameactive && !testmode) {
				winningBroadcast(3, "wipeout");
				stopGame();
			}
			if(redTeamPlayers.isEmpty() && yellowTeamPlayers.isEmpty() && greenTeamPlayers.isEmpty() && !blueTeamPlayers.isEmpty()  && gameactive && !testmode) {
				winningBroadcast(4, "wipeout");
				stopGame();
			}

		}
		for (Entity entity : mainworld.getEntities()) {
			if (entity.getType() == EntityType.ENDERMITE) {
				entity.remove();
			}
			if(entity instanceof Projectile) {
				if (entity.getLocation().getY() > 143 && entity.getLocation().getY() < 160) {
					entity.remove();
				}
			}

			if(entity.getScoreboardTags().contains("generatorTitle")) {
				ArmorStand stand = (ArmorStand) entity;
				EulerAngle vectorAngle = stand.getHeadPose();
				double angle = ((vectorAngle.getY() + Math.PI + BLOCK_ROTATION_RADIANS) % PI_TIMES_TWO) - Math.PI;
				stand.setHeadPose(vectorAngle.setY(angle));
			}
		}
	}

	public void everyTick() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Team team = mainboard.getPlayerTeam(player);
			assert team != null;
			String teamname = team.getName();

			if(doubleJumpBoots.contains(player.getInventory().getBoots())) {
				if (player.isOnGround() && !player.getAllowFlight()) {
					player.setAllowFlight(true);
				}

				if (player.isFlying() && player.getGameMode().equals(GameMode.SURVIVAL) && !player.getScoreboardTags().contains("spectator")) {
					player.setFlying(false);

					boolean hasItems = woolRemove(16, player, teamname);

					if (hasItems) {
						player.setVelocity((player.getEyeLocation().getDirection().multiply(0.95)).add(new Vector(0, 0.45, 0)));
						player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.2F, 1);
						player.setAllowFlight(false);
					} else {
						player.sendActionBar(getByLang(MopsFiles.getLanguage(player), "woolbattle.notEnoughWool"));
					}
				}
			} else if(!player.getScoreboardTags().contains("spectator")) {
				player.setAllowFlight(false);
			}
			if(stickCooldownTicks.get(player) == 1) {
				stickCooldownTicks.put(player, 0);
			}
		}
	}

	public void everySecond() {
		String oldGenA = genA.getStatus();
		String oldGenB = genB.getStatus();
		String oldGenC = genC.getStatus();
		String oldGenD = genD.getStatus();

		if(!hardmode) {
			genCaptureChecks(genA.getBlocks(), genA.getLongBlocks(), "A");
			genCaptureChecks(genB.getBlocks(), genB.getLongBlocks(), "B");
			genCaptureChecks(genC.getBlocks(), genC.getLongBlocks(), "C");
			genCaptureChecks(genD.getBlocks(), genD.getLongBlocks(), "D");

			if(!genA.getStatus().equals(oldGenA) || !genB.getStatus().equals(oldGenB) || !genC.getStatus().equals(oldGenC) || !genD.getStatus().equals(oldGenD)) {
				onCapture(oldGenA, oldGenB, oldGenC, oldGenD, genA.getStatus(), genB.getStatus(), genC.getStatus(), genD.getStatus());
			}
		}

		for(Player player : Bukkit.getOnlinePlayers()) {
			if(slimeCooldownSeconds.get(player) != 0) {
				slimeCooldownSeconds.put(player, slimeCooldownSeconds.get(player)-1);
			}
		}

//			String serverName = MopsUtils.getPath(this).replace("\\plugins", "").replace("D:\\servers\\MopsNetwork\\", "");
//
//			try {
//				String serverID = serverName.replace("woolbattle", "");
//				int line = 1;
//				try {
//					line = Integer.parseInt(serverID+1);
//				} catch (NumberFormatException ignored) { }
//
//				List<String> text = Arrays.asList(MopsUtils.readFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8)).split("\n"));
//				text.set(line, serverName + " " + System.currentTimeMillis() + " " + Bukkit.getOnlinePlayers().size());
//
//				MopsUtils.writeFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8), MopsUtils.combineStrings(text, "\n"));
//			} catch (IOException ignored) { }
	}

	public Teams convertToTeam(String string) {
		try {
			if (string.toLowerCase(Locale.ROOT).contains("red")) {
				return Teams.RED;
			} else if (string.toLowerCase(Locale.ROOT).contains("yellow")) {
				return Teams.YELLOW;
			} else if (string.toLowerCase(Locale.ROOT).contains("green")) {
				return Teams.GREEN;
			} else if (string.toLowerCase(Locale.ROOT).contains("blue")) {
				return Teams.BLUE;
			} else if (string.toLowerCase(Locale.ROOT).contains("orange")) {
				return Teams.ORANGE;
			} else if (string.toLowerCase(Locale.ROOT).contains("pink")) {
				return Teams.PINK;
			}
		} catch (Exception ignored) { }
		return Teams.SPECTATOR;
	}










	@Override
	public TextComponent getByLang(String lang, String string) {
		return translator.getTranslation(lang, string.replaceFirst("woolbattle.", "")).decoration(TextDecoration.ITALIC, false);
	}
	@Override
	public TextComponent getByLang(String lang, String string, Map<String, String> formatV) {
		return translator.getTranslation(lang, string.replaceFirst("woolbattle.", ""), formatV).decoration(TextDecoration.ITALIC, false);

	}
	public Title genTitle(@NotNull String lang, @Nullable String id, @Nullable String id2nd, int i, int j, int k) {
		return new MopsUtils(this).createTitle(lang, id, id2nd, i, j, k);
	}

	public String getStringByLang(String lang, String string, Map<String, String> formatValues) {
		String processedString = LegacyComponentSerializer.legacyAmpersand().serialize(getByLang(lang, string, formatValues)).replaceAll("&", "§");
		processedString = MopsUtils.convertColorCodes(processedString);
		return processedString;
	}
	public String getStringByLang(String lang, String string) {
		String processedString = getStringByLang(lang, string, Map.of("", ""));
		processedString = MopsUtils.convertColorCodes(processedString);
		return processedString;
	}
}