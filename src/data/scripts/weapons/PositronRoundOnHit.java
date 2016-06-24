package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
/**
 * A simple on-hit effect
 * @author Eliza Weisman
 */
public class PositronRoundOnHit implements OnHitEffectPlugin {

    private static final Color EXPLOSION_COLOR = new Color(255,100,235,215);

    @Override
    public void onHit(DamagingProjectileAPI projectile,
                      CombatEntityAPI target,
                      Vector2f point,
                      boolean shieldHit,
                      CombatEngineAPI engine) {

        if (!shieldHit) {
            // do visual effects
            engine.spawnExplosion(point, target.getVelocity(),
                                  EXPLOSION_COLOR, // color of the explosion
                                  120f, // sets the size of the explosion
                                  2.4f   // how long the explosion lingers for
                                  );
        }
    }

}
