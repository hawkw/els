package data.missions.els_holdtheline;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "ISS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Long Range Picket Group K21");
		api.setFleetTagline(FleetSide.ENEMY, "Luddic Path Raiding Fleet");

		// These show up as items in the bulleted list under
		// "Tactical Objectives" on the mission detail screen
	    api.addBriefingItem("Survival is no longer a tactical objective.");
		api.addBriefingItem("Hold the line.");

		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		api.addToFleet( FleetSide.PLAYER
                      , "els_damascus_std"
                      , FleetMemberType.SHIP
                      , "ASV Hamza ibn al-Qalanisi "
                      , true);

		api.addToFleet( FleetSide.PLAYER
                      , "els_adept_std"
                      , FleetMemberType.SHIP
                      , "ASV Colonel Tricaud"
                      , false);

		// Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "hound_d_pirates_Standard", FleetMemberType.SHIP, "Eternal Purity", false);
		api.addToFleet(FleetSide.ENEMY, "hound_d_pirates_Standard", FleetMemberType.SHIP, "Purging Flame", false);
		api.addToFleet(FleetSide.ENEMY, "lasher_luddic_path_Raider", FleetMemberType.SHIP, "Terrible Swift Sword", false);
		api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, "ISS Nothing Personal", false);
		api.addToFleet(FleetSide.ENEMY, "cerberus_luddic_path_Attack", FleetMemberType.SHIP, "Holy Crusade", false);

		api.defeatOnShipLoss("");

		// Set up the map.
		float width = 20000f;
		float height = 14000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);

		float minX = -width/2;
		float minY = -height/2;

		for (int i = 0; i < 25; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 1000f + (float) Math.random() * 1000f;
			api.addNebula(x, y, radius);
		}

		api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.4f, 2000);
		api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.5f, 2000);
		api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.6f, 2000);

		api.addObjective(minX + width * 0.4f + 1000, minY + height * 0.4f, "nav_buoy");

		api.addAsteroidField(minX, minY + height * 0.5f, 0, height,
							20f, 70f, 50);

		//api.addPlanet(0, 0, 350f, "barren", 200f, true);
	}

}
