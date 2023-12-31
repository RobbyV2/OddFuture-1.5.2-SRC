package MEDMEX.Modules.Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;


import net.lax1dude.eaglercraft.EaglerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StatCollector;
import MEDMEX.Client;
import MEDMEX.Modules.Module;
import MEDMEX.Utils.ColorUtil;
import de.Hero.settings.Setting;

public class HUD extends Module{
	int multiplier = 1;
	public Minecraft mc = Minecraft.getMinecraft();
	
	public HUD() {
		super("HUD", EaglerAdapter.KEY_NONE, Category.CLIENT);
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("ArrayList", this, true));
		Client.settingsmanager.rSetting(new Setting("Watermark", this, true));
		Client.settingsmanager.rSetting(new Setting("PotionHUD", this, true));
		Client.settingsmanager.rSetting(new Setting("Coords", this, true));
		ArrayList<String> options = new ArrayList<>();
		options.add("Horizontal");
		options.add("Vertical");
		Client.settingsmanager.rSetting(new Setting("Coords Layout", this, "Horizontal", options));
	}
	
	public static class ModuleComparator implements Comparator<Module> {
		@Override
		public int compare(Module o1, Module o2) {
			if(Minecraft.getMinecraft().fontRenderer.getStringWidth(o1.name + o1.attribute) > Minecraft.getMinecraft().fontRenderer.getStringWidth(o2.name + o2.attribute))
				return -1;
			if(Minecraft.getMinecraft().fontRenderer.getStringWidth(o1.name + o1.attribute) < Minecraft.getMinecraft().fontRenderer.getStringWidth(o2.name + o2.attribute))
				return 1;
			return 0;
		}
		
		
		
		
	}
	
	public void onRenderGUI() {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		FontRenderer fr = mc.fontRenderer;
		
		if(Client.settingsmanager.getSettingByName("Coords").getValBoolean()) {
			int X = (int) mc.thePlayer.posX;
			int Y = (int) mc.thePlayer.posY;
			int Z = (int) mc.thePlayer.posZ;
			
			if(mc.thePlayer.dimension == -1)
				multiplier = 8;
			else
				multiplier = 1;
			
			
			if(Client.settingsmanager.getSettingByName("Coords Layout").getValString().equalsIgnoreCase("Horizontal")){
				mc.fontRenderer.drawStringWithShadow("�aXYZ �r"+X*multiplier+" "+Y+" "+Z*multiplier+" �5[�r"+(X/8)*multiplier+" "+(Z/8)*multiplier+"�5]", sr.getScaledWidth() - fr.getStringWidth("�aXYZ �r"+X*multiplier+" "+Y+" "+Z*multiplier+" �5[�r"+(X/8)*multiplier+" "+(Z/8)*multiplier+"�5]") - 4, sr.getScaledHeight() - 10, -1);
			}
			if(Client.settingsmanager.getSettingByName("Coords Layout").getValString().equalsIgnoreCase("Vertical")){
				mc.fontRenderer.drawStringWithShadow("�aX �r"+X*multiplier+" �5[�r"+(X/8)*multiplier+"�5]", sr.getScaledWidth() - fr.getStringWidth("�aX �r"+X*multiplier+" �5[�r"+(X/8)*multiplier+"�5]") - 4, sr.getScaledHeight() - 30, -1);
				mc.fontRenderer.drawStringWithShadow("�aY �r"+Y, sr.getScaledWidth() - fr.getStringWidth("�aZ �r"+Y) - 4, sr.getScaledHeight() - 20, -1);
				mc.fontRenderer.drawStringWithShadow("�aZ �r"+Z*multiplier+" �5[�r"+(Z/8)*multiplier+"�5]", sr.getScaledWidth() - fr.getStringWidth("�aZ �r"+Z*multiplier+" �5[�r"+(Z/8)*multiplier+"�5]") - 4, sr.getScaledHeight() - 10, -1);
			}
		}	
		
		if(Client.settingsmanager.getSettingByName("Watermark").getValBoolean()) {
			if(!mc.gameSettings.showDebugInfo)
				mc.fontRenderer.drawStringWithShadow("�a"+Client.name, 4, 4, -1);
			
		}
		
		
		if(Client.settingsmanager.getSettingByName("ArrayList").getValBoolean()) {
			//Collections.sort(Client.modules, new ModuleComparator());
			int count = 0;
			
			for(Module m : Client.modules) {
				
				if(!m.toggled)
					continue;
				if(Client.drawn.contains(m))
					continue;

				if(!GameSettings.showDebugInfo) {
				mc.fontRenderer.drawStringWithShadow(m.name + m.attribute, sr.getScaledWidth() - fr.getStringWidth(m.name + m.attribute) - 4, 4 + count * (9), ColorUtil.astolfoColorsDraw(1000, -count*1000));
				}
				count++;
				
				
			}
		}
		
		
		if(Client.settingsmanager.getSettingByName("PotionHUD").getValBoolean()) {
			Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
			int count = 0;
			for(PotionEffect e : effects) {
				String eName =  StatCollector.translateToLocal(e.getEffectName());
				String duration = Potion.getDurationString(e);
				int amp = e.getAmplifier() + 1;
				
				String toDraw = "�5" +  eName + "�r �3" + amp + " �9" + duration;
				
				if(!GameSettings.showDebugInfo) {
					if(Client.settingsmanager.getSettingByName("Coords Layout").getValString().equalsIgnoreCase("Horizontal")){
						mc.fontRenderer.drawStringWithShadow(toDraw, sr.getScaledWidth() - fr.getStringWidth(toDraw) - 4, sr.getScaledHeight() - 20 - (count * 10), -1);
					}else {
						mc.fontRenderer.drawStringWithShadow(toDraw, sr.getScaledWidth() - fr.getStringWidth(toDraw) - 4, sr.getScaledHeight() - 40 - (count * 10), -1);
					}
				}
				
				
				
				count++;
			}
		}
		
		
		
		
	}
	
}
