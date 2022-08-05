package me.ellectronchik.betterarmorstand;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Handler implements Listener {



    private Betterarmorstand plugin;
    Map<Integer, ArmorStand> Stands = new HashMap();
    Map<Integer, ItemStack> Buttons = new HashMap();
    Handler(Betterarmorstand plugin) {
        this.plugin = plugin;
        //Invisibility button
        ItemStack inv = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta invM = inv.getItemMeta();
        invM.setDisplayName(plugin.getConfig().getString("item_names.invisibility_button"));
        inv.setItemMeta(invM);
        Buttons.put(1, inv);
        //Visibility button
        ItemStack vis = new ItemStack(Material.POTION);
        ItemMeta visM = vis.getItemMeta();
        visM.setDisplayName(plugin.getConfig().getString("item_names.visibility_button"));
        vis.setItemMeta(visM);
        Buttons.put(2, vis);
        //Name-changing button
        ItemStack name = new ItemStack(Material.NAME_TAG);
        ItemMeta nameM = name.getItemMeta();
        nameM.setDisplayName(plugin.getConfig().getString("item_names.name_button"));
        name.setItemMeta(nameM);
        Buttons.put(3, name);
    }

    @EventHandler
    public void onPlaceStand(EntitySpawnEvent event) {
        if(event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = ((ArmorStand) event.getEntity());
            armorStand.setArms(plugin.getConfig().getBoolean("conditions.hasArms"));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().equals(plugin.standGUI)) {
            if(event.getCurrentItem() == null) return;
            event.setCancelled(true);
            // Invisibility/visibility changing
            if(event.getCurrentItem().getItemMeta().getDisplayName()
                    .equals(plugin.getConfig().getString("item_names.invisibility_button"))) {
                Stands.get(1).setVisible(false);
                plugin.standGUI.setItem(8, Buttons.get(2));
            }
            else if(event.getCurrentItem().getItemMeta().getDisplayName()
                    .equals(plugin.getConfig().getString("item_names.visibility_button"))) {
                Stands.get(1).setVisible(true);
                plugin.standGUI.setItem(8, Buttons.get(1));
            }
            //name changing
            if(event.getCurrentItem().getItemMeta().getDisplayName()
                    .equals(plugin.getConfig().getString("item_names.name_button"))) {
                new AnvilGUI.Builder()
                        .onComplete(((player, s) -> {
                            Stands.get(1).setCustomName(s);
                            return AnvilGUI.Response.close();
                        }));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getInventory().equals(plugin.standGUI)) {
            Stands.clear();
        }
    }


    @EventHandler
    public ArmorStand onPlayerInteractStand(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = ((ArmorStand) event.getRightClicked());
            Player player = event.getPlayer();
            if((player.getInventory().getItemInMainHand() == null ||
                    player.getInventory().getItemInMainHand().getType() == Material.AIR) && player.isSneaking()) {
                (player).openInventory(plugin.standGUI);
                Stands.put(1, armorStand);
                if (armorStand.isInvisible()) {
                    plugin.standGUI.setItem(8, Buttons.get(2));
                }
                if (armorStand.isVisible()){
                    plugin.standGUI.setItem(8, Buttons.get(1));
                }
                plugin.standGUI.setItem(4,Buttons.get(3));


            }
            return armorStand;
        }
        return null;
    }
}
