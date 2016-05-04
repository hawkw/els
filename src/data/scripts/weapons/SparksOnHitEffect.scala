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

  @inline private[this] final def randomAngle(facing: Float): Float
    = randomNumBetween( facing - half_angle, facing + half_angle)

  @inline private[this] final def randomVelocity(speed: Float): Float
    = randomNumBetween(speed * -minVelocity, speed * -maxVelocity)

  override def onHit( projectile: DamagingProjectileAPI
                    , target: CombatEntityAPI
                    , point: Vector2f
                    , shieldHit: Boolean
                    , engine: CombatEngineAPI): Unit
    = target match {
        case ship: ShipAPI if !shieldHit && Math.random <= sparkChance =>
          val speed = projectile.getVelocity.length
          val facing = projectile.getFacing
          for { _ <- 0 until particleCount
                vector = pointOnCircumference( null
                                             , randomAngle(facing)
                                             , randomVelocity(speed))
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
