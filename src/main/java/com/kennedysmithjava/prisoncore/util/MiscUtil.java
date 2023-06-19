package com.kennedysmithjava.prisoncore.util;
import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MiscUtil {

    public static HashSet<String> substanceChars = new HashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z"));

    public static BlockWrapper EAST_LADDER = new BlockWrapper(Material.LADDER, BlockFace.WEST);
    public static BlockWrapper WEST_LADDER = new BlockWrapper(Material.LADDER, BlockFace.EAST);
    public static BlockWrapper SOUTH_LADDER = new BlockWrapper(Material.LADDER, BlockFace.NORTH);
    public static BlockWrapper NORTH_LADDER = new BlockWrapper(Material.LADDER, BlockFace.SOUTH);

    //SORTS DESC
    public static <X extends Comparable<X>>  Map<X, Integer> sortByValue(Map<X, Integer> unsortMap) {
        List<Map.Entry<X, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
        ? o2.getKey().compareTo(o1.getKey())
        : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    public static String getComparisonString(String str)
    {
        String ret = "";

        str = ChatColor.stripColor(str);
        str = str.toLowerCase();

        for (char c : str.toCharArray())
        {
            if (substanceChars.contains(String.valueOf(c)))
            {
                ret += c;
            }
        }
        return ret.toLowerCase();
    }

    public static void blockFill(Location min, Location max, Material material){
        blockFill(min, max, new BlockWrapper(material));
    }

    public static void blockFill(Location min, Location max, BlockWrapper blockWrapper){
        boolean hasBlockData = blockWrapper.hasBlockData();
        Material material = blockWrapper.getMaterial();
        String blockData = blockWrapper.getBlockDataString();
        BlockData data = hasBlockData ? Bukkit.createBlockData(material).merge(Bukkit.createBlockData(blockData)) : null;

        int minX = Math.min(min.getBlockX(), max.getBlockX());
        int minY = Math.min(min.getBlockY(), max.getBlockY());
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ());

        int maxX = Math.max(min.getBlockX(), max.getBlockX());
        int maxY = Math.max(min.getBlockY(), max.getBlockY());
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

        World world = min.getWorld();
        for (int x = minX; x <= maxX; x++){
            for (int z = minZ; z <= maxZ; z++){
                for (int y = minY; y <= maxY; y++){
                    Block b = world.getBlockAt(x, y, z);
                    b.setType(material, false);
                    if(hasBlockData){
                        b.setBlockData(data);
                    }
                }
            }
        }

    }

    public static String getRomanNumeral(int arabicNumber) {

        if (arabicNumber > 0 && arabicNumber < 4000) {

            final LinkedHashMap<Integer, String> numberLimits =
                    new LinkedHashMap<>();

            numberLimits.put(1, "I");
            numberLimits.put(4, "IV");
            numberLimits.put(5, "V");
            numberLimits.put(9, "IX");
            numberLimits.put(10, "X");
            numberLimits.put(40, "XL");
            numberLimits.put(50, "L");
            numberLimits.put(90, "XC");
            numberLimits.put(100, "C");
            numberLimits.put(400, "CD");
            numberLimits.put(500, "D");
            numberLimits.put(900, "CM");
            numberLimits.put(1000, "M");

            StringBuilder romanNumeral = new StringBuilder();

            while (arabicNumber > 0) {
                int highestFound = 0;
                for (Map.Entry<Integer, String> current : numberLimits.entrySet()){
                    if (current.getKey() <= arabicNumber) {
                        highestFound = current.getKey();
                    }
                }
                romanNumeral.append(numberLimits.get(highestFound));
                arabicNumber -= highestFound;
            }

            return romanNumeral.toString();

        } else {
            throw new UnsupportedOperationException(arabicNumber
                    + " is not a valid Roman numeral.");
        }
    }

    public static int yawToBlockData(int yaw){
        return switch (yaw) {
            case 0 -> 3;
            case 90 -> 4;
            case 270 -> 5;
            default -> 2;
        };
    }

    public static int yawToPortalData(int yaw){
        return switch (yaw) {
            case 90, 270 -> 2;
            default -> 1;
        };
    }

    public static BlockWrapper getLadder(BlockFace direction){
        return switch (direction) {
            case SOUTH -> SOUTH_LADDER;
            case EAST -> EAST_LADDER;
            case WEST -> WEST_LADDER;
            default -> NORTH_LADDER;
        };
    }

    public static void givePlayerItem(Player player, ItemStack item, int amount) {
        if(player.getInventory().firstEmpty() == - 1) {
            for(int i = 0; i < amount; i++) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        } else {
            for(int i = 0; i < amount; i++) {
                player.getInventory().addItem(item);
            }
        }
    }

    /**
     * Picks a random element from the given list.
     *
     * @param list The list of elements to choose from.
     * @param <T>  The type of elements in the list.
     * @return A randomly selected element from the list, or null if the list is empty.
     */
    public static <T> T pickRandomElement(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    public static boolean parseBoolean(ItemStack item, String identifier) {
        if(!item.hasItemMeta())
            return false;
        if(!item.getItemMeta().hasLore())
            return false;
        ItemMeta itemMeta = item.getItemMeta();
        String stripped_id = stripColor(ChatColor.translateAlternateColorCodes('&', identifier));
        for(int i = 0; i < itemMeta.getLore().size(); i++) {
            if(stripColor(itemMeta.getLore().get(i)).contains(stripped_id)) {
                if(stripColor(itemMeta.getLore().get(i)).contains("Yes")) {
                    return true;
                } else if(stripColor(itemMeta.getLore().get(i)).contains("No")) {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isOutsideOfBorder(final Location loc) {
        WorldBorder wb = loc.getWorld().getWorldBorder();
        double x = loc.getX();
        double z = loc.getZ();
        double size = wb.getSize() / 2.0;
        return x >= size || -x >= size || z >= size || -z >= size;
    }


    public static String formatTime(int secs) {
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "");
        String fHours = (hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "");
        String fMinutes = (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "");
        String fSeconds = (seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : "") : "");

        return new StringBuilder().append(fDays).append(fHours).append(fMinutes).append(fSeconds).toString();
    }

    public static String getNum(String input) {
        return input.replaceAll("[^0-9]", "");
    }


    public static String stripColor(String input) {
        return ChatColor.stripColor(input);
    }

    public static void increaseEnchant(ItemStack item, String identifier) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) {
            return;
        }
        List<String> lore = meta.getLore();
        increaseEnchant(identifier, lore);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    public static void increaseEnchant(ItemStack item, String identifier, int amt) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) {
            return;
        }
        List<String> lore = meta.getLore();
        //Added and changed contains
        for (int index = 0; index < lore.size(); index++) {
            String line = lore.get(index);
            if (stripColor(line).contains(stripColor(Txt.parse(identifier)))) {
                lore.set(index, Txt.parse(identifier + " " + amt));
            }
        }
        //
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void setEnchant(String identifier, List<String> lore, int amt) {
        for (int index = 0; index < lore.size(); index++) {
            String line = lore.get(index);
            if (stripColor(line).startsWith(stripColor(Txt.parse(identifier)))) {
                lore.set(index, Txt.parse(identifier + " " + amt));
            }
        }
    }

    public static void increaseEnchant(String identifier, List<String> lore) {
        for (int index = 0; index < lore.size(); index++) {
            String line = lore.get(index);
            if (stripColor(line).startsWith(stripColor(Txt.parse(identifier)))) {
                int value = Integer.parseInt(getNum(stripColor(line)));
                lore.set(index, Txt.parse(identifier + " " + --value));

            }
        }
    }

    public static boolean hasStringInLore(String s, List<String> lore) {
        for (String line : lore) {
            if (stripColor(line).contains(stripColor(Txt.parse(s)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean areTheSameItems(ItemStack compare, ItemStack compareTo) {
        if(compare == null || compareTo == null)
            return false;
        if(compare.getType() != compareTo.getType())
            return false;
        if(!compare.hasItemMeta() || !compareTo.hasItemMeta())
            return false;
        if(!compare.getItemMeta().hasLore() || !compareTo.getItemMeta().hasLore())
            return false;
        if(compare.getItemMeta().getLore().size() != compareTo.getItemMeta().getLore().size())
            return false;
        if(!stripColor(compare.getItemMeta().getDisplayName()).equalsIgnoreCase(stripColor(compareTo.getItemMeta().getDisplayName())))
            return false;
        int holder = 0;
        for(String n : compare.getItemMeta().getLore()) {
            if(!stripColor(n).equalsIgnoreCase(stripColor(compareTo.getItemMeta().getLore().get(holder++))))
                return false;
        }
        return true;
    }

    public static boolean areTheSameItemsNoLore(ItemStack compare, ItemStack compareTo) {
        if(compare == null || compareTo == null)
            return false;
        if(compare.getType() != compareTo.getType())
            return false;
        if(!compare.hasItemMeta() || !compareTo.hasItemMeta())
            return false;
        if(!compare.getItemMeta().hasLore() || !compareTo.getItemMeta().hasLore())
            return false;
        if(compare.getItemMeta().getLore().size() != compareTo.getItemMeta().getLore().size())
            return false;
        if(!stripColor(compare.getItemMeta().getDisplayName()).equalsIgnoreCase(stripColor(compareTo.getItemMeta().getDisplayName())))
            return false;
        return true;
    }

    public static int getInt(String identifier, List<String> lore) {
        if(identifier.equalsIgnoreCase(""))
            return 0;
        for (String line : lore) {
            if (stripColor(line).contains(stripColor(Txt.parse(identifier)))) {
                int ret = 0;
                try {
                    ret = Integer.parseInt(getNum(stripColor(line)));
                } catch (NumberFormatException ignored) {
                }
                return ret;
            }
        }
        return 0;
    }

    public static void removeOneItemInHandFromPlayer(Player player) {
        if(player.getInventory().getItemInHand().getAmount() == 1) {
            player.getInventory().setItemInHand(null);
        } else {
            player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount()-1);
        }
    }

    public static boolean areSameLocations(Location loc1, Location loc2) {
        if(loc1.getWorld().getName().equalsIgnoreCase(loc2.getWorld().getName())
                && loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ())
            return true;
        return false;
    }

    public boolean areSameLocations(PS ps, PS ps2) {
        if(ps.getWorld().equalsIgnoreCase(ps2.getWorld())
                && ps.getBlockX() == ps2.getBlockX()
                && ps.getBlockY() == ps2.getBlockY()
                && ps.getBlockZ() == ps2.getBlockZ())
            return true;
        return false;
    }

    //Takes in a percent in decimals, ex. 0.01 is 1% 0.15 is 15% and 1.0 is 100%
    public static boolean randomChance(double chance) {
        return Math.random() < chance;
    }

}

