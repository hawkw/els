package data.scripts.weapons

import java.awt.Color

import com.fs.starfarer.api.combat._
import org.lwjgl.util.vector.Vector2f

/**
  * Created by hawk on 7/12/16.
  */
class PositronBlasterOnHit
extends OnHitEffectPlugin {

  import PositronBlasterOnHit._

  override def onHit(projectile: DamagingProjectileAPI,
                     target: CombatEntityAPI,
                     point: Vector2f,
                     shieldHit: Boolean,
                     engine: CombatEngineAPI): Unit
    = target match {
        case ship: ShipAPI if !shieldHit =>
//          engine.spawnExplosion(point, Zero, CoreColor, 240f, 0.6f)
//          engine.spawnExplosion(point, Zero, FringeColor, 120f, 1.6f)
          engine.spawnExplosion(point, Zero, CoreColor, 240f, 1.0f)
          engine.spawnExplosion(point, Zero, FringeColor, 120f, 0.6f)
        case _ => {}
      }
}

object PositronBlasterOnHit {
  private val CoreColor = new Color(255,75,100,25)
  private val FringeColor = new Color(255,100,235,50)
  private val Zero = new Vector2f
}