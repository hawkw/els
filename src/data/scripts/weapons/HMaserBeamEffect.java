package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;

import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class HMaserBeamEffect implements BeamEffectPlugin {

    // how long the particles last (i'm assuming this is in seconds)
    private static final float PARTICLE_DURATION = 0.6f;

	private IntervalUtil fireInterval = new IntervalUtil(0.25f, 1.75f);

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {

        if (beam.getDamageTarget() instanceof ShipAPI &&
            beam.getBrightness() >= 1f) {

            fireInterval.advance(beam.getDamage().getDpsDuration());
            ShipAPI ship = (ShipAPI) beam.getDamageTarget();

            if (!(ship.getShield() != null &&
                ship.getShield().isWithinArc(beam.getTo()))) {

                Vector2f dir = Vector2f.sub(beam.getTo(),
                                            beam.getFrom(),
                                            new Vector2f());
                if (dir.lengthSquared() > 0) dir.normalise();
                dir.scale(5f);
                Vector2f point = Vector2f.sub(beam.getTo(),
                                              dir,
                                              new Vector2f());

                float angleTo = VectorUtils.getAngle(point, beam.getFrom());

                float particleSpeed
                    = MathUtils.getRandomNumberInRange(20f, 100f);

                float particleAngle
                    = MathUtils.getRandomNumberInRange(angleTo + 20,
                                                       angleTo - 20);
                Vector2f particleVector
                    = MathUtils.getPointOnCircumference(null,
                                                        particleSpeed,
                                                        particleAngle);
                Color particleColor
                    = new Color(250,
                                MathUtils.getRandomNumberInRange(128, 220),
                                172);
                engine.addHitParticle(point,
                                      particleVector,
                                      MathUtils.getRandomNumberInRange(5f,9f),
                                      beam.getBrightness(),
                                      PARTICLE_DURATION,
                                      particleColor );

                if (fireInterval.intervalElapsed())
                    engine.applyDamage(ship, point,   // where to apply damage
                                       beam.getDamage().getDamage(),
                                       DamageType.ENERGY, // damage type
                                       0f,    // amount of EMP damage (none)
                                       false, false,
                                       beam.getSource());

            }
        }
    }
}
