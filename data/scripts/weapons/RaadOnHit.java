package data.scripts.weapons;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

public class RaadOnHit implements OnHitEffectPlugin {

    private static final Color CORE_COLOR = new Color(248,255,85,255);
    private static final Color FRINGE_COLOR = new Color(93,253,91,255);
    private static final float ARC_WIDTH = 20f;
    private static final float ARC_RANGE = 100000f;
    private static final String SFX = "tachyon_lance_emp_impact";

	public void onHit(DamagingProjectileAPI projectile,
                      CombatEntityAPI target,
					  Vector2f point,
                      boolean shieldHit,
                      CombatEngineAPI engine) {
		if (shieldHit && target instanceof ShipAPI) {

			float emp = projectile.getEmpAmount();
			float dam = projectile.getDamageAmount() * 0.25f;

			engine.spawnEmpArcPierceShields(projectile.getSource(),
                                            point, target, target,
            							    DamageType.ENERGY,
            							    dam,
            							    emp, // emp
            							    ARC_RANGE,
            							    SFX,
            							    ARC_WIDTH,
            							    CORE_COLOR, FRINGE_COLOR
            							    );

		} else if (target instanceof ShipAPI) {
            // if we hit hull, the hit will do the EMP damage,
            // but spawn a decorative arc for 0 damage anyway.
            engine.spawnEmpArc(projectile.getSource(),
                               point, target, target,
							   DamageType.ENERGY,
							   0,
							   0, // emp
							   ARC_RANGE,
							   SFX,
							   ARC_WIDTH,
                               CORE_COLOR, FRINGE_COLOR
							   );
        }
	}
}
