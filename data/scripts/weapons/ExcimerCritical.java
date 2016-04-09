package data.scripts.weapons;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import java.awt.Color;

public class ExcimerCritical implements BeamEffectPlugin {

    private static final Color EXPLOSION_COLOR = new Color(170, 240, 209,255);
    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "els_kinetic_crit";

	private IntervalUtil fireInterval = new IntervalUtil(0.2f, 0.3f);

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        if (beam.getDamageTarget() instanceof ShipAPI &&
            beam.getBrightness() > 1.0) {

            fireInterval.advance(beam.getDamage().getDpsDuration());

            if (fireInterval.intervalElapsed()) {
                ShipAPI ship = (ShipAPI) beam.getDamageTarget();

                if (ship.getShield() != null &&
                    ship.getShield().isWithinArc(beam.getTo())) {

                    Vector2f dir = Vector2f.sub(beam.getTo(),
                                                beam.getFrom(),
                                                new Vector2f());
                    if (dir.lengthSquared() > 0) dir.normalise();
                    dir.scale(5f);
                    Vector2f point = Vector2f.sub(beam.getTo(),
                                                  dir,
                                                  new Vector2f());
                    // apply the extra damage to the target
                    engine.applyDamage(ship, point,   // where to apply damage
                                       beam.getDamage().getDamage(),
                                       DamageType.ENERGY, // damage type
                                       0,    // amount of EMP damage (none)
                                       false, // does this bypass shields? (no)
                                       false, // does this deal soft flux? (no)
                                       beam.getSource());

                    // do visual effects
                    engine.spawnExplosion(point, ship.getVelocity(),
                                          EXPLOSION_COLOR,
                                          100f,
                                          2f   // how long the explosion lingers for
                                          );
                    // play a sound
                    Global.getSoundPlayer()
                          .playSound(SFX, 1.1f, 0.5f,
                                     ship.getLocation(),
                                     ship.getVelocity());
                }
            }


        }
    }

}
