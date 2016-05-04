package data.scripts.weapons

import java.awt.Color

import com.fs.starfarer.api.combat._
import org.lazywizard.lazylib.MathUtils.{
  getRandomNumberInRange => randomNumBetween
, getPointOnCircumference => pointOnCircumference
}
import org.lwjgl.util.vector.Vector2f

/**
  * Created by hawk on 5/4/16.
  */
trait SparksOnHitEffect
extends OnHitEffectPlugin {

  protected[this] def sparkChance: Float
  protected[this] def sparkColor: Color

  protected[this] def particleSize: Float
  protected[this] def particleBrightness: Float
  protected[this] def particleDuration: Float
  protected[this] def particleCount: Int

  protected[this] def coneAngle: Float
  protected[this] def minVelocity: Float
  protected[this] def maxVelocity: Float

  private[this] val half_angle = coneAngle/2

  override def onHit( projectile: DamagingProjectileAPI
                    , target: CombatEntityAPI
                    , point: Vector2f
                    , shieldHit: Boolean
                    , engine: CombatEngineAPI): Unit
    = target match {
        case ship: ShipAPI if !shieldHit && Math.random <= sparkChance =>
          val speed: Float = projectile.getVelocity.length
          val facing: Float = projectile.getFacing
          for { _ <- 0 until particleCount
                angle = randomNumBetween( facing - half_angle
                                        , facing + half_angle)
                velocity = randomNumBetween( speed * -minVelocity
                                           , speed * -maxVelocity )
                vector = pointOnCircumference(null, angle, velocity)
              } {
            engine addHitParticle ( point
                                  , vector
                                  , particleSize
                                  , particleBrightness
                                  , particleDuration
                                  , sparkColor )
          }
        case _ => {}
      }

}
