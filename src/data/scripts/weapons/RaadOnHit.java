package data.scripts.weapons;

import java.awt.Color;

import com.fs.starfarer.api.combat.*;
import org.lwjgl.util.vector.Vector2f;

public class RaadOnHit implements OnHitEffectPlugin {

    private static final Color CORE_COLOR = new Color(255,255,255,255);
    private static final Color FRINGE_COLOR = new Color(255,100,235,215);
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
            							    CORE_COLOR,
											FRINGE_COLOR
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
                               CORE_COLOR,
					           FRINGE_COLOR
							   );
        }
	}
}
