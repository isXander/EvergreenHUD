/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.settings.impl.StringSetting;
import com.evergreenclient.hudmod.utils.ElementData;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElementCombo extends Element {

    private long sentAttackTime;
    private long lastHitTime;
    private int lastAttackId;
    private int currentCombo;
    private int sentAttack;

    public BooleanSetting alwaysShowCount;
    public IntegerSetting secondsComboDiscard;
    public StringSetting noHitString;

    @Override
    public void initialise() {
        addSettings(alwaysShowCount = new BooleanSetting("Persistent Count", "Determines if the No Hit String shows.", false));
        addSettings(secondsComboDiscard = new IntegerSetting("Discard Time", "How many seconds until the combo is set to 0.", 3, 1, 10, " secs"));
        addSettings(noHitString = new StringSetting("No Hit Message", "What message is shown when no combo is in progress.", "None"));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Combo Counter", "Shows the amount of hits you get before you are attacked.");
    }

    @Override
    protected String getValue() {
        if (!alwaysShowCount.get() && currentCombo == 0) {
            return noHitString.get();
        }
        return Integer.toString(currentCombo);
    }

    @Override
    public String getDisplayTitle() {
        return "Combo";
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - lastHitTime >= secondsComboDiscard.get() * 1000L) {
            currentCombo = 0;
        }
    }

    @Override
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.isCanceled())
            return;
        if (event.entity != mc.thePlayer) {
            return;
        }
        this.sentAttack = event.target.getEntityId();
        this.sentAttackTime = System.currentTimeMillis();
    }

    @Override
    public void onPacketReceive(Packet<?> packet) {
        if (packet instanceof S19PacketEntityStatus) {
            S19PacketEntityStatus p = (S19PacketEntityStatus) packet;

            if (p.getOpCode() != 2) {
                return;
            }

            Entity target = p.getEntity(mc.theWorld);
            if (target == null) {
                return;
            }

            if (this.sentAttack != -1 && target.getEntityId() == this.sentAttack) {
                this.sentAttack = -1;
                if (System.currentTimeMillis() - this.sentAttackTime > 2000L) {
                    this.sentAttackTime = 0L;
                    this.currentCombo = 0;
                    return;
                }
                if (this.lastAttackId == target.getEntityId()) {
                    this.currentCombo++;
                } else {
                    this.currentCombo = 1;
                }
                this.lastHitTime = System.currentTimeMillis();
                this.lastAttackId = target.getEntityId();
            } else if (target.getEntityId() == mc.thePlayer.getEntityId()) {
                this.currentCombo = 0;
            }
        }
    }


}
