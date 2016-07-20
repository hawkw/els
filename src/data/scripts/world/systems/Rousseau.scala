package data.scripts.world
package systems

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.SectorAPI
import com.fs.starfarer.api.impl.campaign.ids.Terrain
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams
import com.fs.starfarer.api.impl.campaign.ids.Conditions._
import com.fs.starfarer.api.impl.campaign.ids.Submarkets._

import java.awt.Color

import me.hawkweisman.elizalib.scalaAPIs.WorldgenUtil.RichPlanet
import me.hawkweisman.elizalib.scalaAPIs.graphics.SpriteUtil._

/**
  * Created by hawk on 7/12/16.
  */
object Rousseau {

  def generate(sector: SectorAPI): Unit = {

    val s = Global.getSettings
    val system = sector createStarSystem "Rousseau"
    val hyper = Global.getSector.getHyperspace
    system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg")

    // create the star and generate the hyperspace anchor for this system
    val star = system.initStar("rousseau", // unique id for this star
                               "star_white", // id in planets.json
                                 750f,		// radius (in pixels at default zoom)
                                 1100, // corona radius, from star edge
                                 5f, // solar wind burn level
                                 0.6f, // flare probability
                                 2f); // cr loss mult

    // Rousseau's Trojans --------------------------------------------------
    val trojansL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
                                      new AsteroidFieldParams(
                                        400f, // min radius
                                        600f, // max radius
                                        16, // min asteroid count
                                        24, // max asteroid count
                                        4f, // min asteroid radius
                                        16f, // max asteroid radius
                                        "Rousseau L4 Trojans"))
    val trojansL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
                                      new AsteroidFieldParams(
                                        400f, // min radius
                                        600f, // max radius
                                        16, // min asteroid count
                                        24, // max asteroid count
                                        4f, // min asteroid radius
                                        16f, // max asteroid radius
                                        "Rousseau L5 Asteroids"))
    trojansL4.setCircularOrbit(star, 230 + 60, 9500, 450)
    trojansL5.setCircularOrbit(star, 230 - 60, 9500, 450)

    system.addAsteroidBelt(star, 150, 3100, 128, 60, 80, Terrain
      .ASTEROID_BELT, "The Inner Belt")

    // the inner system (Sophie)
    val sophie = system.addPlanet("planet_sophie", star, "Sophie",
                                "irradiated", 60, 100, 1500, 200)
    val heloise = system.addPlanet("planet_heloise", star, "Heloise",
                                   "barren", 90, 75, 3000, 250)

    val sophieSpec = sophie.getSpec
    sophieSpec.setPlanetColor(new Color(220,245,255,255))
    sophieSpec.setAtmosphereColor(new Color(150,120,100,250))
    sophieSpec.setCloudColor(new Color(150,120,120,150))
    sophie.applySpecChanges()

    /*
		 * addPlanet() parameters:
		 * 1. Unique id for this planet (or null to have it be autogenerated)
		 * 2. What the planet orbits (orbit is always circular)
		 * 3. Name
		 * 4. Planet type id in planets.json
		 * 5. Starting angle in orbit, i.e. 0 = to the right of the star
		 * 6. Planet radius, pixels at default zoom
		 * 7. Orbit radius, pixels at default zoom
		 * 8. Days it takes to complete an orbit. 1 day = 10 seconds.
		 */
    //== the Brumaire system ==================================================

    // Brumaire itse;f --------------------------------------------------------
    val brumaire = system.addPlanet("planet_brumaire",
                                    star,
                                    "Brumaire",
                                    "gas_giant",
                                    0,
                                    400, // planet radius
                                    6800, // orbital radius
                                    350 // orbital period
                                     )

    // brumaire visual spec
    brumaire.setSpec ( planetColor = Some(new Color(70,155,205,255))
                     , atmosphereColor = Some(new Color(110,120,150,150))
                     , cloudColor = Some(new Color(190,210,255,200))
                     , iconColor = Some(new Color(70,155,205,255))
                     , atmosphereThickness = Some(0.6f)
                     , glowTexture = Some(("hab_glows", "aurorae"))
                     , glowColor = Some(new Color(220,130,225,62))
                     , reverseLightForGlow = Some(true)
                     )
//    magec1.getSpec().setPlanetColor(new Color(50,100,255,255));
//    magec1.getSpec().setAtmosphereColor(new Color(120,130,100,150));
//    magec1.getSpec().setCloudColor(new Color(195,230,255,200));
//    magec1.getSpec().setIconColor(new Color(120,130,100,255));
//
//    brumaireSpec.setPlanetColor(new Color(70,235,255,255))
//    brumaireSpec.setAtmosphereColor(new Color(100,220,255,150))
//    brumaireSpec.setCloudColor(new Color(200,230,250,200))

//    brumaireSpec.setGlowTexture(s.getSpriteName("hab_glows", "banded"))
//    brumaireSpec.setGlowColor(new Color(0,205,255,62))
//    brumaireSpec.setGlowColor(new Color(160,205,200,62))


    // Brumaire's rings & moons -----------------------------------------------
    system.addRingBand(brumaire, "misc", "rings1", 256f, 2, Color.white, 256f,
                       700, 33f, Terrain.RING, null)
    system.addRingBand(brumaire, "misc", "rings1", 256f, 3, Color.white, 256f,
                       875, 33f, Terrain.RING, null)

    system.addRingBand(brumaire, "misc", "rings1", 256f, 1, Color.white, 256f,
                       1630, 90f, Terrain.RING, "The Fog Band")
    system.addRingBand(brumaire, "misc", "rings1", 256f, 2, Color.white, 256f,
                       1720, 33f, Terrain.RING, "The Fog Band")

    // Brumaire's two smaller moons
    val fraternite = system.addPlanet("moon_fraternite",
                                      brumaire, "Fraternité",
                                      "water", 30, 85, 1310, 50)
    fraternite.setSpec( glowTexture = Some(("hab_glows", "aurorae"))
                      , glowColor = Some(new Color(220,130,225,62))
                      , reverseLightForGlow = Some(true)
                      , atmosphereThickness = Some(0.4f))

    val egalite = system.addPlanet("moon_egalite",
                                    brumaire, "Égalité",
                                   "rocky_ice", 45, 45, 1430, 65)


    // Liberte & its' station -------------------------------------------------
    val liberte = system.addPlanet("planet_liberte",
                                   brumaire,
                                   "Liberté",
                                  "terran",
                                   0,
                                   120f,
                                   1050f,
                                   42f)

    // add Liberte city lights
    liberte.setSpec( glowTexture = Some(("hab_glows", "sindria"))
                   , glowColor = Some(new Color(255,255,255,255))
                   , reverseLightForGlow = Some(true)
                   )

    liberte.setInteractionImage("illustrations", "city_from_above")

    val liberteStation = system.addCustomEntity("station_liberte",
                                                "Al Qasbah Orbital",
                                                "station_side03",
                                                "directory")
    liberteStation.setCircularOrbitPointingDown(
      liberte,
      45,
      300,
      50)
    liberteStation.setInteractionImage("illustrations", "terran_orbit")
    liberteStation.setCustomDescriptionId("station_liberte")

    liberte addMarket ( "directory", "Liberté", 7, 0.3f
                      , Seq( URBANIZED_POLITY, REGIONAL_CAPITAL
                           , ORBITAL_STATION, MILITARY_BASE
                           , HEADQUARTERS, ORGANICS_COMPLEX
                           , TERRAN, POPULATION_7)
                      , Seq( GENERIC_MILITARY, SUBMARKET_BLACK, SUBMARKET_OPEN)
                      , connectedEntities = liberteStation)



    // Brumaire's Trojans --------------------------------------------------
    val brumaireL4Trojans = system.addTerrain(Terrain.ASTEROID_FIELD,
                                      new AsteroidFieldParams(
                                        200f, // min radius
                                        400f, // max radius
                                        16, // min asteroid count
                                        24, // max asteroid count
                                        4f, // min asteroid radius
                                        16f, // max asteroid radius
                                        "Brumaire L4 Trojans"))
    val brumaireL5Trojans = system.addTerrain(Terrain.ASTEROID_FIELD,
                                      new AsteroidFieldParams(
                                        200f, // min radius
                                        400f, // max radius
                                        16, // min asteroid count
                                        24, // max asteroid count
                                        4f, // min asteroid radius
                                        16f, // max asteroid radius
                                        "Brumaire L4 Asteroids"))
    brumaireL4Trojans.setCircularOrbit(brumaire, 230 + 60, 3100, 450)
    brumaireL5Trojans.setCircularOrbit(brumaire, 230 - 60, 3100, 450)

    val jumpPoint = Global.getFactory.createJumpPoint("liberte_jump_point",
                                                      "Liberté Crossing")
    jumpPoint.setCircularOrbit(brumaire, 230 + 60, 3100, 450)
    jumpPoint.setRelatedPlanet(liberte)
    jumpPoint.setStandardWormholeToHyperspaceVisual()
    system.addEntity(jumpPoint)

//
//    system.addRingBand(brumaire, "misc", "rings1", 256f, 3, Color.white, 256f,
//                       700,
//                       220f, null, null)
//    system.addRingBand(brumaire, "misc", "rings2", 256f, 2, Color.white, 256f,
//                       800, 226f, null, null)

    system.autogenerateHyperspaceJumpPoints(true, true)
  }

}
