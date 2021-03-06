package com.tangykiwi.kiwiclient.modules;

import com.google.common.eventbus.Subscribe;
import com.tangykiwi.kiwiclient.event.KeyPressEvent;
import com.tangykiwi.kiwiclient.modules.movement.*;
import com.tangykiwi.kiwiclient.modules.render.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    public static ArrayList<Module> moduleList = new ArrayList<Module>();
    public static MinecraftClient mc = MinecraftClient.getInstance();

    public void init() {
        moduleList.add(new Fly());
        moduleList.add(new NoClip());
        moduleList.add(new Speed());
        moduleList.add(new FullBright());
        moduleList.add(new HUD());
        moduleList.add(new ActiveMods());
    }

    public ArrayList<Module> getEnabledMods() {
        ArrayList<Module> enabledMods = new ArrayList<Module>();

        for(Module m : moduleList) {
            if(m.isEnabled()) enabledMods.add(m);
        }

        Collections.sort(enabledMods, new ModuleComparator());
        return enabledMods;
    }

    public Module getModule(Class<? extends Module> c) {
        for (Module m : moduleList) {
            if (m.getClass().equals(c)) {
                return m;
            }
        }

        return null;
    }

    public Module getModuleByName(String name) {
        for (Module m : moduleList) {
            if (name.equalsIgnoreCase(m.getName()))
                return m;
        }
        return null;
    }

    public ArrayList<Module> getModulesInCat(Category cat) {
        return (ArrayList<Module>) moduleList.stream().filter(m -> m.getCategory().equals(cat)).collect(Collectors.toList());
    }

    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module a, Module b) {
            if(mc.textRenderer.getWidth(a.getName()) >
                    mc.textRenderer.getWidth(b.getName()))
                return -1;
            else if(mc.textRenderer.getWidth(a.getName()) <
                    mc.textRenderer.getWidth(b.getName()))
                return 1;
            return 0;
        }
    }

    @Subscribe
    public static void handleKeyPress(KeyPressEvent e) {
        if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_F3))
            return;

        for(Module m : moduleList) {
            if(m.getKeyCode() == e.getKeyCode()) m.toggle();
        }
    }
}
