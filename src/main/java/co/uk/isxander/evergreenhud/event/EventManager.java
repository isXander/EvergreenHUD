/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.event;

import co.uk.isxander.xanderlib.event.PacketEvent;
import co.uk.isxander.xanderlib.utils.Constants;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager implements Constants {

    private static EventManager instance;

    public static EventManager getInstance() {
        if (instance == null)
            instance = new EventManager();
        return instance;
    }

    private final List<Listenable> listenables = new CopyOnWriteArrayList<>();

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void addListener(Listenable listener) {
        this.listenables.add(listener);
    }
    
    public void removeListener(Listenable listener) {
        this.listenables.remove(listener);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onClientTick(event);
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onRenderTick(event);
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onRenderGameOverlay(event);
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onAttackEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onLivingUpdate(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onLivingHurt(event);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Incoming event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onPacketReceive(event);
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Outgoing event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onPacketSend(event);
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        for (Listenable listenable : listenables) {
            if (listenable.canReceiveEvents()) {
                listenable.onBlockPlaced(event);
            }
        }
    }

}
