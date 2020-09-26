package commoble.potionofbees;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Registrator<T extends IForgeRegistryEntry<T>>
{
	public IForgeRegistry<T> registry;
	
	public Registrator(IForgeRegistry<T> registry)
	{
		this.registry = registry;
	}
	
	public T register(String registryKey, T entry)
	{
		ResourceLocation loc = new ResourceLocation(PotionOfBeesMod.MODID, registryKey);
		return this.register(loc, entry);
	}
	
	public T register(ResourceLocation location, T entry)
	{
		entry.setRegistryName(location);
		this.registry.register(entry);
		return entry;
	}
}
