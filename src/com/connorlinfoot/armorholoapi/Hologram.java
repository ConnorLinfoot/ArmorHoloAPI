package com.connorlinfoot.armorholoapi;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

public class Hologram {
    String name;
    Location location;
    List<String> text;
    UUID entityID = null;

    public Hologram(String name, Location location, List<String> text) {
        this.name = name;
        this.location = location;
        this.text = text;
    }

    public boolean create() {
        if (this.name == null || this.location == null || this.text == null) return false;

        /* Change horse to Armor Stand on 1.8 API */
        EntityType entityType = EntityType.HORSE;
        Entity entity = this.location.getWorld().spawnEntity(location, entityType);
        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.setCustomNameVisible(true);
        livingEntity.setCustomName(this.text.get(0)); // Will add multi line support soon <3
        // livingEntity.setVisible(false); // Hide the Armor Stand
        this.entityID = livingEntity.getUniqueId();

        return true;
    }

    public void addText(String text) {
        this.text.add(text);
    }

    public void removeText(int index) {
        this.text.remove(index);
    }

    public void delete() {
        for( Entity entity : location.getWorld().getEntities() ){
            if( entity.getUniqueId().equals(this.entityID) ){
                if( !entity.isDead() ) entity.remove();
                break;
            }
        }
    }

}
