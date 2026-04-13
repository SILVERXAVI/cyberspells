package cyberspells.items;

import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import cyberspells.config.CyberSpellsConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public abstract class CyberRuneItem extends Item implements ICyberware, RuneHolder {
    protected final String partName;

    public CyberRuneItem(Properties properties, String partName) {
        super(properties);
        this.partName = partName;
    }

    public abstract String getPartName();

    public int getMaxSlots() {
        if (partName.contains("arm"))
            return CyberSpellsConfig.ARM_SLOTS.get();
        if (partName.contains("leg"))
            return CyberSpellsConfig.LEG_SLOTS.get();
        if (partName.contains("heart"))
            return CyberSpellsConfig.HEART_SLOTS.get();
        return 3;
    }

    @Override
    public int getEssenceCost(ItemStack stack) {
        if (partName.contains("arm"))
            return CyberSpellsConfig.ARM_ESSENCE.get().intValue();
        if (partName.contains("leg"))
            return CyberSpellsConfig.LEG_ESSENCE.get().intValue();
        if (partName.contains("heart"))
            return CyberSpellsConfig.HEART_ESSENCE.get().intValue();
        return 10;
    }

    @Override
    public List<String> getRunes(ItemStack stack) {
        List<String> runes = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Runes", 9)) {
            ListTag list = tag.getList("Runes", 8);
            for (int i = 0; i < list.size(); i++) {
                runes.add(list.getString(i));
            }
        }
        return runes;
    }

    public void addRune(ItemStack stack, String runeId) {
        List<String> runes = getRunes(stack);
        if (runes.size() < getMaxSlots()) {
            runes.add(runeId);
            saveRunes(stack, runes);
        }
    }

    public void clearRunes(ItemStack stack) {
        stack.getOrCreateTag().remove("Runes");
    }

    protected void saveRunes(ItemStack stack, List<String> runes) {
        ListTag list = new ListTag();
        for (String rune : runes) {
            list.add(StringTag.valueOf(rune));
        }
        stack.getOrCreateTag().put("Runes", list);
    }
}
