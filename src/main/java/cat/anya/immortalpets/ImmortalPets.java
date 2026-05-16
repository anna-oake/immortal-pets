package cat.anya.immortalpets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;

public class ImmortalPets implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(this::onEntityDamageOrDeath);
        ServerLivingEntityEvents.ALLOW_DEATH.register(this::onEntityDamageOrDeath);
        ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);
    }

    private boolean onEntityDamageOrDeath(LivingEntity entity, DamageSource source, float amount) {
        if (isEligiblePet(entity)) {
            makeImmortal((TamableAnimal) entity);
            return false;
        }

        return true;
    }

    private void onEntityLoad(Entity entity, ServerLevel level) {
        if (isEligiblePet(entity)) {
            makeImmortal((TamableAnimal) entity);
        }
    }

    private boolean isEligiblePet(Entity entity) {
        if (!(entity instanceof TamableAnimal tamableAnimal)) {
            return false;
        }

        if (!(entity instanceof Cat || entity instanceof Wolf || entity instanceof Parrot)) {
            return false;
        }

        return tamableAnimal.isTame() && tamableAnimal.getOwnerReference() != null;
    }

    private void makeImmortal(TamableAnimal pet) {
        pet.setInvulnerable(true);
        pet.setHealth(pet.getMaxHealth());
    }
}
