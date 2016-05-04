package data.scripts.weapons

import java.awt.Color

import com.fs.starfarer.api.combat._
import com.fs.starfarer.api.util.IntervalUtil

import data.util.VectorUtil.Vector2fOps

import org.lazywizard.lazylib.MathUtils.{
  getRandomNumberInRange => randomNumBetween
, getPointOnCircumference => pointOnCircumference
}
/**
  * Created by hawk on 5/4/16.
  */
class HMaserBeamEffect extends BeamEffectPlugin {

  private[this] val ParticleDuration: Float = 0.6f
  private[this] val fireInterval: IntervalUtil = new IntervalUtil(0.25f, 1.75f)

  override def advance(amount: Float, engine: CombatEngineAPI, beam: BeamAPI)
    = beam.getDamageTarget match {
        case ship: ShipAPI if beam.getBrightness >= 1f =>
          fireInterval.advance(beam.getDamage.getDpsDuration)

          if (ship.getShield == null ||
              !ship.getShield.isWithinArc(beam.getTo)) {
            val (to, from) = (beam.getTo, beam.getFrom)
            val dir = to - from
            if (dir.lengthSquared > 0) dir.normalise()
            dir scale 5f

            val point = to - dir
            val angleTo = point ° dir

            val pSpeed = randomNumBetween(20f, 100f)
            val pAngle = randomNumBetween(angleTo + 20, angleTo - 20)
            val pColor = new Color(250, randomNumBetween(128,220), 172)

            engine addHitParticle ( point
                                  , pointOnCircumference(null, pSpeed, pAngle)
                                  , randomNumBetween(5f,9f)
                                  , beam.getBrightness
                                  , ParticleDuration
                                  , pColor )

            if (fireInterval.intervalElapsed)
              engine applyDamage ( ship
                                 , point
                                 , beam.getDamage.getDamage
                                 , DamageType.ENERGY
                                 , 0f
                                 , false
                                 , false
                                 , beam getSource)
          }
        case _ => {}
      }

}
