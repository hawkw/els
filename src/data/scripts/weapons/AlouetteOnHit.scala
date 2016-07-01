package data.scripts.weapons

import java.awt.Color

import com.fs.starfarer.api.combat._
import org.lwjgl.util.vector.Vector2f

/**
  * Created by hawk on 7/1/16.
  */
class AlouetteOnHit
extends OnHitEffectPlugin {

  import AlouetteOnHit._

  override def onHit(projectile: DamagingProjectileAPI,
                     target: CombatEntityAPI,
                     point: Vector2f,
                     shieldHit: Boolean,
                     engine: CombatEngineAPI): Unit
    = target match {
        // if we hit a ship and didn't hit shields
        case ship: ShipAPI if !shieldHit =>
          val chance = Math.random()
          if (chance < FirstArcChance) {
            val emp = projectile.getEmpAmount
            val dam = projectile.getDamageAmount * 0.25f

            engine.spawnEmpArc(projectile.getSource,
                               point, target, target,
                               DamageType.ENERGY,
                               dam,
                               emp, // emp
                               MaxArcRange, // max range
                               "tachyon_lance_emp_impact",
                               20f, // thickness
                               FringeColor,
                               White)

            // Random chance to spawn a second arc
            if (chance < SecondArcChance)
              engine.spawnEmpArc(projectile.getSource,
                                 point, target, target,
                                 DamageType.ENERGY,
                                 dam,
                                 emp, // emp
                                 MaxArcRange, // max range
                                 "tachyon_lance_emp_impact",
                                 20f, // thickness
                                 FringeColor,
                                 White)
          }

        case _ =>
      }
}
object AlouetteOnHit {
  private val White = new Color(255,255,255,255)
  private val FringeColor = new Color(100,165,255,215)
  private val MaxArcRange = 10000f
  private val FirstArcChance = 0.75
  private val SecondArcChance = 0.5
}