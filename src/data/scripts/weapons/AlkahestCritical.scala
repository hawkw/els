package data.scripts.weapons

import java.awt.Color

import com.fs.starfarer.api.combat._
import org.dark.graphics.util.AnamorphicFlare
import org.lwjgl.util.vector.Vector2f

/**
  * Created by hawk on 6/28/16.
  */
class AlkahestCritical extends OnHitEffectPlugin {
  private[this] val ArcCoreColor: Color = new Color(255, 100, 235, 255)
  private[this] val ExplosionColor: Color = new Color(255, 100, 235, 215)
  private[this] val FlareFringeColor: Color = new Color(255,100,235,50)
  private[this] val ArcWidth: Float = 20f
  private[this] val ArcRange: Float = 5000f
  private[this] val SFX: String = "tachyon_lance_emp_impact"
  private[this] val CritChance = 0.25

  override def onHit( projectile: DamagingProjectileAPI
                    , target: CombatEntityAPI
                    , point: Vector2f
                    , shieldHit: Boolean
                    , engine: CombatEngineAPI)
    = target match {
      case ship: ShipAPI if Math.random <= CritChance =>

        val emp = if (shieldHit) projectile.getEmpAmount
                  else {
                    engine.spawnExplosion( point
                                         , ship.getVelocity
                                         , ExplosionColor
                                         , 100f
                                         , 2.8f)

                    projectile.getEmpAmount * 2
                  }

        val dam = projectile.getDamageAmount * 0.25f

        val flareAngle = Math.random().asInstanceOf[Float] * 15f - 7.5f

        AnamorphicFlare.createFlare( ship
                                   , point
                                   , engine
                                   , 0.40f
                                   , 0.1f
                                   , flareAngle
                                   , 10f
                                   , 5f
                                   , ExplosionColor
                                   , FlareFringeColor )

        engine.spawnEmpArcPierceShields( projectile.getSource
                                       , point
                                       , ship
                                       , ship
                                       , DamageType.ENERGY
                                       , dam
                                       , emp
                                       , ArcRange
                                       , SFX
                                       , ArcWidth
                                       , ExplosionColor
                                       , ArcCoreColor)
      case _ =>
    }

}
