package data.scripts.plugins

import java.util
import java.awt.Color

import com.fs.starfarer.api.combat.{CombatEngineAPI => Engine, CombatEntityAPI => Entity, DamagingProjectileAPI => Projectile, _}
import com.fs.starfarer.api.input.InputEventAPI
import org.lazywizard.lazylib.combat.CombatUtils.{getEntitiesWithinRange => entitiesInRange}
import org.lazywizard.lazylib.MathUtils.{getPointOnCircumference => pointOnCircumference, getRandomNumberInRange => randomNumBetween}
import org.lwjgl.util.vector.Vector2f

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import data.util.Vector._
import org.lazywizard.lazylib.{CollisionUtils, VectorUtils}

import scala.collection.generic.FilterMonadic

/**
  * Created by Eliza on 5/23/16.
  */
class ELSOverpenetrationPlugin
extends EveryFrameCombatPlugin {

  private[this] val SIROCCO_HIT_GLOW_RADIUS = 60f
  private[this] val SIROCCO_HIT_GLOW_COLOR = new Color(255,145,130,125)
  private[this] val VEC_ZERO = new Vector2f

  var engine: Engine = null

  @inline private[this] def siroccoPenetration(amount: Float)
                                              (shot: Projectile): Unit = {

    val owner = shot.getOwner
    val loc: Vector2f = shot.getLocation

    val targets: Seq[Entity]
     = entitiesInRange(shot.getLocation, 20f) - shot.getSource

    val validTargets
      = targets.withFilter { _.getCollisionClass != CollisionClass.NONE }
               .withFilter {
                // objects we don't care about colliding with
                case _: Projectile
//                   | _: MissileAPI
                   | _: BattleObjectiveAPI => false
                // skip collisions with drones and fighters owned by the same
                // player
//                case ship: ShipAPI if ship.getOwner == owner =>
//                  !ship.isFighter || !ship.isDrone
                case _ => true
              }



    def _collision(i: FilterMonadic[Entity, _]): Option[( Vector2f
                                                        , Entity
                                                        , Boolean)] = {
      i foreach {
        case ship: ShipAPI if ship.getShield != null && ship.getShield.isOn
                              && ship.getShield.isWithinArc(loc)
                              && Math.random <= 0.25 =>
          val shield = ship.getShield
          val shieldLoc = shield.getLocation
          val point
            = pointOnCircumference( shieldLoc
                                  , shield.getRadius
                                  , VectorUtils.getAngle(shieldLoc, loc)
                                  )
            return Some((point, ship, true))
        case ship: Entity if Math.random <= 0.25 =>
          val pLoc: Vector2f = new Vector2f(shot.getVelocity)
          pLoc.scale(-amount)
          Vector2f.add(pLoc, loc, pLoc)

          Option(CollisionUtils getCollisionPoint(pLoc, loc, ship)) orElse {
            if (CollisionUtils isPointWithinBounds(loc, ship)) Some(loc)
            else None
          } foreach { point: Vector2f =>
            return Some((point, ship, false))
          }
        case _ => {}
      }
      None
    }

    _collision(validTargets) foreach {
      case ((point, ship, true)) =>
        shot setCollisionClass CollisionClass.PROJECTILE_FF
      case ((point, ship, false)) =>
        shot setCollisionClass CollisionClass.NONE
        // fake hit effects as though hitting a shield
        engine.addHitParticle( point
                             , VEC_ZERO
                             , SIROCCO_HIT_GLOW_RADIUS, 1f, 1.5f
                             , SIROCCO_HIT_GLOW_COLOR)

        // Calculate projectile speed
        val speed = shot.getVelocity.length

        // Damage per frame is based on how long it would take
        // the projectile to travel through the entity
        val modifier = 1.0f / ((ship.getCollisionRadius * 2f) / speed)
        val damage = (shot.getDamageAmount * amount) * modifier
        val emp = (shot.getEmpAmount * amount) * modifier

        engine.applyDamage( ship
                          , point
                          , damage
                          , shot.getDamageType
                          , emp
                          , false
                          , false
                          , shot.getSource
                          )

    }

  }
  override def advance(amount: Float,
                       events: util.List[InputEventAPI]): Unit = {
    // if the game is paused, do nothing
    if (engine == null || engine.isPaused) return

    val siroccoPenetrationThisFrame = siroccoPenetration(amount) _
    val projectiles // get projectiles from the engine
      = engine.getProjectiles
             // .withFilter { !_.isFading } // ignore proj. currently fading out
              .withFilter { _.getProjectileSpecId != null }

    projectiles.withFilter { _.getProjectileSpecId == "els_sirocco_shot" }
               .foreach { siroccoPenetrationThisFrame }

  }

  override def renderInUICoords(viewport: ViewportAPI): Unit = {}

  override def renderInWorldCoords(viewport: ViewportAPI): Unit = {}

  override def init(engine: Engine): Unit = {
    this.engine = engine
  }
}
