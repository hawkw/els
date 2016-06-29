package data.shipsystems.scripts

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI
import com.fs.starfarer.api.combat.{MutableShipStatsAPI, ShipAPI}
import com.fs.starfarer.api.plugins.ShipSystemStatsScript
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.{State, StatusData}
import org.lazywizard.lazylib.MathUtils.{getRandomNumberInRange,
getRandomPointOnCircumference}

import java.awt.Color

import scala.collection.JavaConverters._
/**
  * Created by hawk on 6/29/16.
  */
class AMInjectorStats
extends ShipSystemStatsScript {
  import AMInjectorStats._

  override def apply(stats: MutableShipStatsAPI, id: String,
                     state: State, effectLevel: Float): Unit
    = if (state == State.OUT) stats.getMaxSpeed.unmodify(id)
      else {
        stats.getMaxSpeed.modifyPercent(id, speedBonus(effectLevel))
        stats.getAcceleration.modifyPercent(id, accelBonus(effectLevel))
//        stats.getMaxTurnRate.modifyPercent(id, turnBonus(effectLevel))
        stats.getEngineDamageTakenMult
             .modifyPercent(id, engDamage(effectLevel))
        stats.getEngineMalfunctionChance
             .modifyPercent(id, malfChance(effectLevel))
        val combat = Global.getCombatEngine
        stats.getEntity match {
          case ship: ShipAPI if !combat.isPaused =>
            for {
              engine: ShipEngineAPI <- ship.getEngineController
                                           .getShipEngines
                                           .asScala
                if engine.isActive && !engine.isSystemActivated
              _ <- 0 until Math.round(ParticleCountFactor * effectLevel) }
            {
              val dist = getRandomNumberInRange(ParticleDistMin,
                                                ParticleDistMax)
              val size = getRandomNumberInRange(ParticleSizeMin,
                                                ParticleSizeMax)
              val speed = dist / ParticleDuration
              val velocity = getRandomPointOnCircumference(ship.getVelocity,
                                                           speed)
              combat.addHitParticle( engine.getLocation
                                   , velocity
                                   , size
                                   , 1f
                                   , ParticleDuration
                                   , ParticleColor )
            }
          case _ => {}
        }

      }

  override def unapply(stats: MutableShipStatsAPI, id: String): Unit = {
    stats.getMaxSpeed.unmodify(id)
    stats.getAcceleration.unmodify(id)
//    stats.getMaxTurnRate.unmodify(id)
    stats.getEngineDamageTakenMult.unmodify(id)
    stats.getEngineMalfunctionChance.unmodify(id)
  }

  override def getStatusData(index: Int,
                             state: State,
                             effectLevel: Float): StatusData
    = index match {
        case 0 =>
          new StatusData(
            s"maximum speed +${Math.round(speedBonus(effectLevel))}%", false)
        case 1 =>
          new StatusData(
            s"acceleration +${Math.round(accelBonus(effectLevel))}%", false)
//        case 2 =>
//          new StatusData(s"turn rate +${Math.round(turnBonus(effectLevel))}%",
//                         false)
        case 2 =>
          new StatusData(
            s"engine damage +${Math.round(engDamage(effectLevel))}%", false)
        case 3 =>
          new StatusData(
            s"engine malfunction chance +${Math.round(malfChance(effectLevel))
            }%", false)
        case _ => null
      }
}

object AMInjectorStats {
  @inline private def speedBonus(effectLevel: Float): Float
    = MaxSpeedBonus * effectLevel

  @inline private def accelBonus(effectLevel: Float): Float
    = AccelBonus * effectLevel

  @inline private def turnBonus(effectLevel: Float): Float
   = TurnBonus * effectLevel

  @inline private def engDamage(effectLevel: Float): Float
   = ExtraEngineDamage * effectLevel

  @inline private def malfChance(effectLevel: Float): Float
   = MalfunctionChance * effectLevel

  private[this] val MaxSpeedBonus = 200f
  private[this] val AccelBonus = 300f
  private[this] val TurnBonus = 100f
  private[this] val ExtraEngineDamage = 100f
  private[this] val MalfunctionChance = 50f

  private val ParticleCountFactor = 15f
  private val ParticleDistMin = 5f
  private val ParticleDistMax = 20f
  private val ParticleDuration = 0.1f
  private val ParticleSizeMin = 15f
  private val ParticleSizeMax = 20f
  private val ParticleColor = new Color(255,100,235,155)
}